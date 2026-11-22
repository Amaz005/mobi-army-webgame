# Contract: Portal API

Served by the game server (`portal-port`, internal), reached by browsers only through nginx at
`/api/`. All bodies are UTF-8 JSON. Error bodies are `{"error": "<code>", "message": "<Vietnamese
text>"}`. No response ever includes a password, hash, session token or any account column other
than those listed (FR-010, SC-009). The one exception is `/api/mobiarmy/ticket`: it returns a
one-time game ticket, only to the logged-in owner, never cached (`no-store`).

State-changing requests (`POST`) MUST send `Content-Type: application/json` and an `Origin`
matching the host; otherwise `403 {"error":"forbidden"}`.

Session cookie: `portal_session=<JWT>; HttpOnly; SameSite=Strict; Path=/api` (+ `Secure` in
public mode). Any authenticated response may carry a renewed cookie (sliding idle timeout); logins
survive server restarts (data-model.md › Portal session, research D16).

| Method | Path | Auth | Request | Success | Errors |
|--------|------|------|---------|---------|--------|
| POST | `/api/register` | — | `{"username","password"}` | `201 {"username"}` + sets cookie | `400 invalid_username` / `invalid_password` (message states the rule), `409 username_taken`, `429 too_many` |
| POST | `/api/login` | — | `{"username","password"}` | `200 {"username"}` + sets cookie | `401 bad_credentials` (always same body, whether or not the name exists), `429 too_many` |
| POST | `/api/logout` | cookie | `{}` | `204`, cookie cleared | — (idempotent) |
| GET | `/api/me` | cookie | — | `200 {"username"}` | `401 not_logged_in` |
| POST | `/api/password` | cookie | `{"currentPassword","newPassword"}` | `204`; other sessions of the account end | `401 not_logged_in`, `401 bad_credentials`, `400 invalid_password`, `429 too_many` |
| GET | `/api/mobiarmy/status` | — | — | `200 {"online": 3, "takenAt": "2026-11-22T10:00:00Z"}` | — |
| GET | `/api/mobiarmy/ranking` | — | — | `200 {"entries":[{"rank":1,"name":"a","level":12,"exp":8000}], "takenAt": "…"}` | — |
| POST | `/api/mobiarmy/ticket` | cookie | `{}` | `200 {"username","ticket"}`: one-time game ticket (24 letters/digits, 2 min), accepted once as the password of that account's game login (research D15) | `401 not_logged_in` |

Rules:
- `username` is trimmed and lowercased by the server. Lookup tries the exact name first, then a
  case-insensitive match, so older accounts with capitals (created in-game) still log in. Names are
  unique regardless of case.
- Validation rules: data-model.md › Account. Messages, e.g.
  - `invalid_username`: "Tên đăng nhập 3–20 ký tự, chỉ gồm chữ thường a–z và số."
  - `invalid_password`: "Mật khẩu 6–50 ký tự, chỉ gồm chữ và số."
  - `username_taken`: "Tên đăng nhập đã có người dùng."
  - `bad_credentials`: "Sai tài khoản hoặc mật khẩu."
  - `too_many`: "Bạn thử quá nhiều lần, vui lòng đợi rồi thử lại."
- Registration logs the user in (sets the cookie).
- A successful portal login with a legacy plaintext password re-hashes it (research D3).
- `/api/password`: 5 wrong current passwords for one account in 15 min lock password changes for that
  account for 15 min (`429 too_many`).
- `/api/mobiarmy/status` has no nginx per-IP limit (answers come from a 10 s cache); all other
  `/api/*` paths share `WEB_API_RATE`. nginx answers `429` and `502` itself with the same JSON shape
  (`too_many`, `server_offline`).
- Unknown paths → `404 {"error":"not_found"}`; wrong method → `405`.
- Malformed JSON or oversized body (> 4 KB) → `400 {"error":"bad_request"}`; never a stack trace.

When the game server is down, nginx answers `/api/*` with `502`/`504`; the portal treats that as
"server offline".
