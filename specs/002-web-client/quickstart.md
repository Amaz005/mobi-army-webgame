# Quickstart & Validation: Web Client

Endpoints: [contracts/web-endpoints.md](contracts/web-endpoints.md) · keys:
[contracts/key-mapping.md](contracts/key-mapping.md) · settings:
[data-model.md](data-model.md#settings-env-extends-001s-table).

## Prerequisites

- The 001 stack works (`docker compose up -d --build`, `.env` filled in).
- Chrome, Edge or Firefox (desktop); an Android phone with Chrome for the touch checks.
- Public-mode checks only: a domain pointing at the host, certificate files in
  `docker/web/certs/`, ports 80/443 open.

## Run (local mode)

`docker compose up -d --build`, then open `http://localhost:8080`.

## Proof gate (FR-013), run first

| # | Check | Pass |
|---|-------|------|
| G1 (P1) | `docker compose build web` | TeaVM build succeeds |
| G2 (P2) | Open the page | Login screen with correct images, no console errors about missing resources |
| G3 (P3) | Log in as `nguoiduatin` / `3` | Lobby appears; `docker compose logs server` shows `Client connected` |
| G4 (P4) | First-visit load (cache disabled) and a 2-player match | Login screen < 20 s; ≥ 95% of actions seen by the other player within 0.5 s |

All pass within 5 working days → continue with A. Any fail → record evidence in `plan.md`,
switch to the streamed fallback (research.md R8).

## Validation scenarios

| # | Covers | Steps | Expected |
|---|--------|-------|----------|
| W1 | US1, FR-001, SC-001 | Fresh browser profile → open the page → log in | Lobby in < 2 min, zero installs |
| W2 | US1 scenario 3 | Log in with a wrong password | Game's normal error message; can retry |
| W3 | FR-003, FR-006 | DevTools → Network during load and login | Requests go only to the page's own origin; nothing to teamobi.com or any other host |
| W4 | US2, SC-003, SC-005 | Two browsers, two accounts, one room, finish a match; log in again | Both see the result; exp increased and kept |
| W5 | FR-004, SC-004 | Two browser players + one phone client (J2ME, port 19149) in one match, 5 times | All 5 finish, no disconnects |
| W6 | US3, FR-007 | `docker compose down` then `up -d --build` on a fresh clone | Web client comes up with the same command |
| W7 | US3 scenario 2 | Break the web build (e.g. bad config), `up -d` | `db` and `server` still run; phone client still works |
| W8 | FR-005, SC-006 | Try to reach anything except the game through `/game` (different path/query/header; text frame) | Always the game server, or closed; no other destination |
| W9 | FR-012 limits | Open `WEB_MAX_CONN_PER_IP` + 1 game tabs from one IP; reconnect rapidly | Extra tab refused (503); rapid reconnects rate-limited (429) |
| W10 | FR-008, US4 | Play W4's match using keyboard only; again with the on-screen keypad on Android | Every needed key works; page doesn't scroll |
| W11 | FR-009 | W1 on Chrome, Edge, Firefox desktop and Chrome Android; open in an unsupported browser | Works on all four; clear message on the unsupported one |
| W12 | Edge: server down | `docker compose stop server`, try to log in, start it, retry | Clear error, retry works without reloading |
| W13 | R10 | Log in, reload the page | Saved username/options still there |
| W14 | FR-012 public | `WEB_MODE=public`, `WEB_DOMAIN` set, certs present; open `http://<domain>` | 301 → HTTPS; valid certificate; game plays |
| W15 | Public exposure | From outside: scan the host | Only 80, 443, 19149 open |
| W16 | FR-006 bundle | Bundle check from data-model.md › Client bundle | No off-site URLs |
