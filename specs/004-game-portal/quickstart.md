# Quickstart: Validate the Web Game Portal

Prerequisites: Docker, `.env` from `.env.example` (as in 001/002). Commands run from the repo root.
Base URL `http://localhost:8080` (local mode).

## 0. Fresh stack

```
docker compose down -v            # throwaway data only!
docker compose up -d --build
docker compose logs -f server     # wait for "Server started on port 19149" and the portal API line
```

## 1. Existing stack: migrate accounts (research D3, D4)

```
docker compose exec -T db pg_dump -U postgres -d dbarmy -Fc > before-004.dump      # backup first (keep it off git)
docker compose exec -T db psql -U postgres -d dbarmy -v ON_ERROR_STOP=1 < mobiarmy-java/sql/migrations/004-portal-accounts.sql
docker compose up -d --build server
docker compose exec server java -cp "classes:lib/*" com.mobiarmy.server.HashPasswords
```
Expected: the tool prints `hashed N accounts` (no names, no passwords). Then
`SELECT count(*) FROM account WHERE password NOT LIKE 'pbkdf2_sha256$%';` → `0` (SC-007), and
`SELECT username, count(*) FROM account GROUP BY username HAVING count(*) > 1;` → no rows.
Seeded accounts `a`/`1`, `a2`/`1`, `nguoiduatin`/`3` still log in to MobiArmy.

## 2. US1 — portal and MobiArmy

1. Open `/` in a fresh browser profile → portal with a MobiArmy card (SC-002: cards < 3 s, also
   with `docker compose stop server`).
2. Card → `/mobiarmy/` game page (tagline, players, "Cách chơi", "Trò chơi khác" empty, title "Chơi MobiArmy online miễn phí") → "Chơi ngay" → `/mobiarmy/choi/` play page (sidebar, game framed, "Rời game" bar; keys reach the game without clicking it) → log in as `a`/`1` → lobby. "← Cổng game" → back to `/`.
3. Open `/index.html` and `/manifest.webmanifest` → redirected to `/mobiarmy/choi/` and `/mobiarmy/client/…` (FR-007); `/nosuchgame/` → "Không tìm thấy trò chơi".
4. Re-run 002's quickstart checks (match, saved exp, keyboard, keypad) from `/mobiarmy/choi/` and `/mobiarmy/client/` (SC-005).

## 3. US2 — one account

1. Portal → "Đăng ký": `testuser1` / `abc123` → logged in, name shown.
2. `/mobiarmy/choi/` → log in `testuser1` / `abc123` → lobby (SC-006: whole flow < 3 min).
3. Register `testuser1` again → "Tên đăng nhập đã có người dùng."; `ab` → name rule message.
4. Log out; log in on the portal as `nguoiduatin` / `3` (pre-existing account) → works.
5. Wrong password for `nguoiduatin` and for `nosuchuser` → identical message and status.
6. Change `testuser1`'s password to `xyz789` → old password fails in MobiArmy, new one works.
7. `curl` checks (contracts/portal-api.md): POST without `Origin` → 403; 40 rapid
   `POST /api/login` from one IP → 429 after the limit while a second browser still logs in
   (SC-008); 6 wrong passwords for one name → `too_many` for that name.
8. Inspect every `/api/*` response body: no password, hash, token or other account column
   (SC-009). `docker compose logs server | grep -i pass` shows no password values.

## 4. US3 — catalog

1. Copy a placeholder game to `portal/games/demo/index.html`, add a `demo` entry (contracts/
   game-catalog.md), `docker compose restart web` → card appears, `/demo/` game page lists MobiArmy under "Trò chơi khác" and MobiArmy's page lists demo, "Chơi ngay" opens it (SC-003 < 15 min).
2. Set `"status": "maintenance"` → "Đang bảo trì", no play button; `"hidden"` → gone.
3. Remove `demo`'s `name` → only that card is skipped; break the JSON → "Chưa có trò chơi".

## 5. US4 — live info

1. Log in two accounts in MobiArmy → card shows `2` within 60 s (SC-004).
2. Ranking on `/mobiarmy/` lists top 10 by level/exp, superadmins excluded, names only.
3. `docker compose stop server` → card shows "Máy chủ tạm dừng"; portal still loads;
   `docker compose start server` → count returns.

## 6. Server tests

```
docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/mobiarmy-java maven:3.9-eclipse-temurin-17 mvn -B test
```
Covers PasswordHash, validation rules, throttling, session expiry, ranking/level mapping and API
handlers (in-process, no socket to 19149 needed).

## Validation log

**2026-11-22**: isolated stack `-p mobiarmy004test` with a fresh DB and the tunnel off, local mode.
- §0: server logs `Portal API started on port 8081`, with no plaintext warning on the fresh seed.
- §1: the old `dbarmy.sql` (git HEAD) was loaded into a scratch database with extra rows `A`/`Bob`. The
  migration renamed `a`→`a2` and `A`→`A10`, and created the index. `HashPasswords` printed `hashed 5 accounts`,
  leaving 0 plaintext.
- §2: `/` cards, `/mobiarmy/` game page (title, how-to-play, ranking, "Sắp có thêm trò chơi"),
  "Chơi ngay" → `/mobiarmy/choi/` original client with the "← Cổng game" link. Old `/index.html`,
  `/client.js` and `/manifest.webmanifest` return 301 to `/mobiarmy/choi/…`; `/nosuchgame/` shows
  "Không tìm thấy trò chơi". Not re-run: the full 002 match, keypad and phone checks.
- §3: registered `testuser1` on the portal, then logged in to the MobiArmy client in Chrome → main menu. Duplicate
  → 409, wrong password and unknown name gave identical 401s, missing or foreign `Origin` → 403, rapid `/api/me`
  → 429 after the burst. Server log has no password values. The portal login form, header and logout work.
- §5: online count went 0 → 1 after the game login. With the server stopped, `/` still returns 200 and
  `/api/*` returns 502 `server_offline`; restarting `web` while the server is down works.
- Not run yet: §4 (demo game), public mode / `Secure` cookie, phone (J2ME) client, `/security-review`.
