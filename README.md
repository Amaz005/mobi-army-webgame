# MobiArmy Server

Java server for the MobiArmy (Army 1.6.5) game, with a PostgreSQL database. Contributor notes are in
[CLAUDE.md](CLAUDE.md); project rules are in [.specify/memory/constitution.md](.specify/memory/constitution.md).

## Run it (Docker)

**Prerequisite:** Docker Desktop (Windows/macOS) or Docker Engine with Compose v2 (Linux). You don't need
Java, Maven or a database.

1. Copy `.env.example` to `.env` and set both passwords.
2. From the repo root: `docker compose up -d --build`

The first run builds the server and seeds the database (a few minutes). Later starts take under a minute.
The server is ready when `docker compose logs -f server` shows `Server started on port 19149`.

| Task | Command |
|------|---------|
| Start / rebuild after code changes | `docker compose up -d --build` |
| Follow logs (all / server only) | `docker compose logs -f` / `docker compose logs -f server` |
| Status | `docker compose ps` |
| Stop (keeps data) | `docker compose down` |
| Apply edits to `mobiarmy-java/army2.conf` | `docker compose restart server` |
| SQL shell | `docker compose exec db psql -U postgres -d dbarmy` |
| ⚠️ **Reset: deletes all game data** | `docker compose down -v` |

The database is seeded from `mobiarmy-java/dbarmy.sql` **only on first start** (or after a reset).
Later changes to that file are not applied automatically. Apply them by hand in the SQL shell, or reset.

**Ports:** the game port `19149` (phone clients) and the web client (`8080`, plus `8443` in public
mode) are published; set `GAME_PORT` / `WEB_PORT` / `WEB_TLS_PORT` in `.env` if they're taken. The
database and the relay are reachable only inside the stack.

Seeded test accounts: `nguoiduatin` / `3`, and `a` / `1`.

**New accounts:** on the login screen, type a username (letters and digits, up to 20) and a password
(letters and digits, up to 50), then **Menu → Đăng ký** and retype the password. When the server
says the name is free, press **OK**, then **Đăng nhập**: that login creates the account with the
password you typed. (The original client sends only the username when registering; the password went
in a paid SMS, so the server picks it up from the login instead. Finish within about 30 seconds,
before the connection times out, or just register again.)

The full command and settings contract is in
[specs/001-docker-compose-stack/contracts/stack-interface.md](specs/001-docker-compose-stack/contracts/stack-interface.md).

## Play in a browser

Open **http://localhost:8080** (or `http://<this machine's IP>:8080` from another device on the LAN).
Nothing to install: the page runs the original MobiArmy client and connects to this server.

- **Supported browsers:** current Chrome, Edge and Firefox on a computer; Chrome on Android. Other
  browsers get a message instead of a blank page.
- **Keys:** arrows to move/aim, **Enter** to fire/OK (hold to charge power), **F1** and **F2**/**Esc**
  for the left and right soft keys. Type usernames, passwords and chat directly; **Backspace** deletes.
  Full table: [contracts/key-mapping.md](specs/002-web-client/contracts/key-mapping.md).
- **Touch screens** get an on-screen keypad automatically (`?keypad=1` forces it on, `?keypad=0` off).
- Background music is off in the browser; sound effects play after the first key press or tap.

**Phone clients** still connect to `<host>:19149` directly, and can play alongside browser players.

### Public internet mode

1. Point a domain at the host and open ports 80, 443 and 19149.
2. Put the certificate files in `docker/web/certs/` (`fullchain.pem`, `privkey.pem`; never committed).
3. In `.env`: `WEB_MODE=public`, `WEB_DOMAIN=<your domain>`, `WEB_PORT=80`, `WEB_TLS_PORT=443`.
4. `docker compose up -d --build`, then open `https://<your domain>`.

Each visitor IP may hold `WEB_MAX_CONN_PER_IP` (default 4) game connections and open
`WEB_CONN_RATE` (default 10 per minute) new ones; beyond that they get HTTP 503/429. If the domain or
certificates are missing, only the web service refuses to start; the game server keeps running.

How the browser client works (original bytecode compiled to JavaScript with TeaVM, plus a thin MIDP
layer and a WebSocket relay): [specs/002-web-client/plan.md](specs/002-web-client/plan.md).
