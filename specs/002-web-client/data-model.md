# Data Model: Play the Existing Client in a Web Browser

No database tables are added; accounts and progress stay in the 001 database. This feature's
data is the client bundle, the browser-side storage, the per-visitor connection state and the
stack settings.

## Components (added to `docker-compose.yml`)

| Service | Built from / image | Published ports | Depends on | Restart |
|---------|-------------------|-----------------|------------|---------|
| `relay` | `docker/relay/Dockerfile` (websockify, pinned) | none (internal `6080`) | `server` | `unless-stopped` |
| `web` | `web-client/Dockerfile` (TeaVM build → nginx) | `${WEB_PORT:-8080}`→8080 (HTTP), `${WEB_TLS_PORT:-8443}`→8443 (HTTPS, public mode) | `relay` | `unless-stopped` |
| `db`, `server` | unchanged from 001 | unchanged (`19149` for phone clients) | — | — |

`web` and `relay` failing never stops `db` or `server` (FR-007, 001 FR-013): they depend on
`server`, never the reverse.

## Client bundle (output of the `web` build)

| Item | Source | Notes |
|------|--------|-------|
| `index.html`, `play.css`, `keypad.js` | `web-client/src/main/webapp/` | Page shell, canvas, on-screen keypad, unsupported-browser message |
| `client.js` | TeaVM output of the **original** client bytecode (`Army165local/*.class`, `coreLG/*.class`) + `web-client/src/main/java` | Game code + MIDP web layer + launcher `web.WebMain`; all game resources (~135 KB) embedded via TeaVM `ResourceSupplier` |

**Validation rule:** the page shell (`index.html`, `play.css`, `keypad.js`) MUST NOT load anything off-site (FR-006); the image build fails
if those files contain an off-site `src`/`href`/`url()`/`import`. `client.js` contains two inert URL strings (the client's teamobi.com lookup, refused by `Connector`, and a TeaVM doc link in an error message); runtime traffic is verified by quickstart W3.

## Player session (one browser tab)

Fields: `visitorIp` (from nginx), `wsConnection`, `tcpConnection` (relay → `server:19149`).

```text
page-loaded --client starts, opens socket://…--> connecting
connecting --relay accepts, TCP to server ok--> connected        (server logs "Client connected")
connecting --over per-IP limit (429/503)--> refused             (game shows its connect-error screen)
connecting --server down--> failed                              (game's own error; retry from login)
connected --tab closed / network lost--> closed                 (relay closes TCP; server frees the slot)
connected --server restarts--> closed
```

**Validation rules:**
- Destination is always `server:19149`; the browser can't choose it (research.md R5, FR-005).
- At most `WEB_MAX_CONN_PER_IP` sessions per `visitorIp` at once; at most
  `WEB_CONN_RATE` new sessions per minute per `visitorIp` (FR-012).
- Sessions also count against the server's existing `max-clients` (FR-010).

## Browser storage (RecordStore → `localStorage`)

| Key | Value | Lifecycle |
|-----|-------|-----------|
| `mobiarmy.rms.<storeName>` | JSON array of base64 records (record id = index + 1) | Per browser profile; survives reloads; cleared with site data |

Records hold only what the phone stores locally (saved username/options, `TLIP`). Passwords
stored by the game this way are the client's existing behavior and are not changed.

## Settings (`.env`, extends 001's table)

| Variable | Required | Default | Used by | Meaning |
|----------|----------|---------|---------|---------|
| `WEB_MODE` | no | `local` | `web` | `local` = HTTP game page; `public` = HTTPS page + HTTP redirect/ACME |
| `WEB_PORT` | no | `8080` | `web` | Host port for HTTP (public mode: set `80`) |
| `WEB_TLS_PORT` | no | `8443` | `web` | Host port for HTTPS (public mode: set `443`) |
| `WEB_DOMAIN` | public mode only | — | `web` | Domain the certificate is for; `${WEB_DOMAIN:?}` is enforced in public mode |
| `WEB_MAX_CONN_PER_IP` | no | `4` | `web` (nginx `limit_conn`) | Max simultaneous game sessions per visitor IP |
| `WEB_CONN_RATE` | no | `10r/m` | `web` (nginx `limit_req`) | New game sessions allowed per visitor IP |
| `STREAM_SLOTS` | fallback only | `4` | stream slots | Max simultaneous streamed players (FR-014) |

Certificate files (public mode): `docker/web/certs/fullchain.pem` and `privkey.pem`, git-ignored,
provided by the owner.

## Key mapping

Defined in [contracts/key-mapping.md](contracts/key-mapping.md); the web layer converts
browser keys and keypad taps to MIDP key codes (`KEY_NUM0`–`KEY_NUM9`, `KEY_STAR`, `KEY_POUND`,
arrows, fire, soft keys −6/−7).
