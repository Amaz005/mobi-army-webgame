# Contract: Web Endpoints (updates `specs/002-web-client/contracts/web-endpoints.md`)

Base URL as in 002. Everything else not listed → 404.

| Path | Method | Response | Notes |
|------|--------|----------|-------|
| `/` | GET | portal `index.html` | Home page: game cards (+ login/register, account views) |
| `/<id>/` (one segment, `[a-z0-9-]+`) | GET | portal `game.html` | Shared game page, filled from `games.json` (FR-001a); `/<id>` → 301 to `/<id>/` |
| `/portal.css`, `/portal.js`, `/game.html`, `/play.html`, `/games.json` | GET | static, `Cache-Control: no-cache` | From the mounted `portal/` dir |
| `/games/<id>/…` | GET | static | Card pictures and hosted game files |
| `/favicon.ico`, icons | GET | static, `no-cache` | Same icons as 002/003, served at the root for the portal too |
| `/<id>/choi/` | GET | portal `play.html` | Play page: sidebar, the game (`playUrl`) in a frame, bottom bar ("Rời game", key help, full screen); `/<id>/choi` → 301 |
| `/mobiarmy/client/` | GET | MobiArmy page shell (002) | Its "← Cổng game" link hides itself when framed |
| `/mobiarmy/client/client.js`, `play.css`, `keypad.js`, `manifest.webmanifest`, icons | GET | static, `no-cache` | Moved from `/` |
| `/index.html`, `/client.js`, `/play.css`, `/keypad.js`, `/manifest.webmanifest` | GET | `/index.html` → 301 `/mobiarmy/choi/`; the files → 301 `/mobiarmy/client/<same>` | Old links and installed home-screen apps (FR-007) |
| `/game` | WebSocket | unchanged from 002 | |
| `/api/*` | GET/POST | contracts/portal-api.md | Proxied to `server:8081`; per-IP limit `WEB_API_RATE` (burst 10) → 429; `X-Real-IP` set; `/api/mobiarmy/status` cached 10 s |

Reserved first segments (never a game id): `api`, `game`, `games`.

Not exposed: the portal API port `8081` is never published (like the relay's `6080`).
