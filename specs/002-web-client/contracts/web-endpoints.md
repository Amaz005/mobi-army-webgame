# Contract: Web Endpoints

> **Superseded for the URL layout by `specs/004-game-portal/contracts/web-endpoints.md`**: the portal
> now serves `/`, and this client lives at `/mobiarmy/client/`, framed by the play page `/mobiarmy/choi/`
> (old paths redirect). `/game` is unchanged.

What browsers (and anyone on the internet, in public mode) can reach. The game protocol is not
defined here; it passes through `/game` unchanged.

Base URL: local `http://localhost:${WEB_PORT:-8080}`; public `https://${WEB_DOMAIN}` (with `WEB_TLS_PORT=443`). Game resources are embedded in `client.js`, so there is no `/res/` path.

| Path | Method | Response | Notes |
|------|--------|----------|-------|
| `/` | GET | `index.html` (200) | Page shell; shows an "unsupported browser" message if the required browser features are missing |
| `/client.js`, `/play.css`, `/keypad.js` | GET | static (200) | `Cache-Control: no-cache` (revalidated, so updates show at once) |
| `/game` | GET + `Upgrade: websocket` | 101 Switching Protocols | Binary frames only. Byte stream to and from `server:19149`, unmodified |
| `/game` (over limit) | GET | 429 (too many attempts) or 503 (too many open sessions) | Per visitor IP, see data-model.md › Settings |
| any other path | GET | 404 | |
| `http://…` in public mode | any | 301 → `https://…` | Except `/.well-known/acme-challenge/` (served from `docker/web/acme/`) |

## `/game` WebSocket rules

- Subprotocol: `binary`. Text frames close the connection (code 1003).
- One WebSocket = one TCP connection to `server:19149` = one player session.
- The destination is fixed in the relay; no query string, header or path can change it.
- Closing either side closes the other within 5 seconds.
- Idle timeout: none added beyond the game server's own behavior.

## Not exposed

- Database, the relay's port `6080`, and nginx status pages are never published.
- In public mode, `19149` stays published for phone clients (unchanged from 001).
