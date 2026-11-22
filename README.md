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
relay is reachable only inside the stack.

**Database from this machine:** PostgreSQL is published on `127.0.0.1:5432` only (not the LAN or
internet; change with `DB_PORT` in `.env`). Connect with any client (psql, DBeaver, pgAdmin…) to
host `localhost`, database `dbarmy`, user `army` with `DB_PASSWORD` (the game's own role), or
`postgres` with `POSTGRES_PASSWORD` (admin).

Seeded test accounts: `a` / `1`, `a2` / `1` (superadmin) and `nguoiduatin` / `3` (superadmin).
Passwords are stored only as salted PBKDF2 hashes.

**Upgrading a stack created before the game portal (spec 004)** — fresh stacks skip this. Login names
become unique (duplicates are renamed `<name><id>`; the script lists them) and plaintext passwords are
hashed. Until you do this, portal registration fails and the server logs a warning at startup.
```
docker compose exec -T db pg_dump -U postgres -d dbarmy -Fc > before-004.dump    # backup first; never commit it
docker compose exec -T db psql -U postgres -d dbarmy -v ON_ERROR_STOP=1 < mobiarmy-java/sql/migrations/004-portal-accounts.sql
docker compose up -d --build server
docker compose exec server java -cp "classes:lib/*" com.mobiarmy.server.HashPasswords
```

**New accounts:** register on the portal (see below), or in the game: on the login screen, type a username (letters and digits, up to 20) and a password
(letters and digits, up to 50), then **Menu → Đăng ký** and retype the password. When the server
says the name is free, press **OK**, then **Đăng nhập**: that login creates the account with the
password you typed. (The original client sends only the username when registering; the password went
in a paid SMS, so the server picks it up from the login instead. Finish within about 30 seconds,
before the connection times out, or just register again.)

The full command and settings contract is in
[specs/001-docker-compose-stack/contracts/stack-interface.md](specs/001-docker-compose-stack/contracts/stack-interface.md).

## Play in a browser

Open **http://localhost:8080** (or `http://<this machine's IP>:8080` from another device on the LAN).
That's the **game portal** (spec 004): game cards, a page per game, and one account for every game.

- `/` home page · `/mobiarmy/` MobiArmy page (how to play, ranking, online count) ·
  `/mobiarmy/choi/` play page (the game inside the portal, with "Rời game", key help and full screen) ·
  `/mobiarmy/client/` the bare game client. Old links (`/index.html`, …) redirect to the play page.
- **Đăng ký / Đăng nhập** in the top right: login name 3–20 lowercase letters or digits, password
  6–50 letters or digits. The same name and password log in to MobiArmy (browser and phone).
  Existing MobiArmy accounts log in to the portal as they are. Forgotten passwords are reset by the
  operator (no e-mail is collected).
- Per-visitor limit on the portal API: `WEB_API_RATE` (default 30 requests/minute; login and
  registration also lock after repeated failures). When players use HTTPS (public mode or the
  tunnel), set `PORTAL_SECURE_COOKIE=true` in `.env`.

**Add a game to the portal:** put its files in `portal/games/<id>/` (served at `/games/<id>/…`), add an
entry to `portal/games.json`, then `docker compose restart web`. Set `"status"` to `maintenance` or
`hidden` to take a game offline. Fields and rules:
[contracts/game-catalog.md](specs/004-game-portal/contracts/game-catalog.md).

The MobiArmy page runs the original client and connects to this server; nothing to install.

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

### Cloudflare Tunnel (instead of public mode)

Cloudflare serves HTTPS and forwards browser traffic through an outbound tunnel, so you don't
need certificates or open web ports. It doesn't carry the raw TCP port 19149: phone clients need
19149 opened directly, pointing at the host's real IP (DNS record not proxied).

1. Install `cloudflared` (Windows: `winget install Cloudflare.cloudflared`), with your domain on Cloudflare.
2. Create the tunnel and its hostname:
   ```
   cloudflared tunnel login
   cloudflared tunnel create mobiarmy
   cloudflared tunnel route dns mobiarmy game.example.com
   cloudflared tunnel token mobiarmy        # copy the output
   ```
3. In the Cloudflare dashboard (Zero Trust › Networks › Tunnels › mobiarmy › Public hostname), send
   `game.example.com` to service `http://web:8080`.
4. In `.env`: keep `WEB_MODE=local`, then set `COMPOSE_PROFILES=tunnel`,
   `CLOUDFLARE_TUNNEL_TOKEN=<token>` and, to make the site reachable only through Cloudflare,
   `WEB_PORT=127.0.0.1:8080`.
5. `docker compose up -d --build`, then open `https://game.example.com`.

Per-visitor limits still work: nginx takes the visitor IP from Cloudflare's `CF-Connecting-IP`
header, but only for requests coming from the `cloudflared` container (fixed address 172.29.252.2 on
the compose network `tunnel`, subnet 172.29.252.0/29; change both in `docker-compose.yml` and
`docker/web/nginx.local.conf.template` if that subnet is taken on your host).

How the browser client works (original bytecode compiled to JavaScript with TeaVM, plus a thin MIDP
layer and a WebSocket relay): [specs/002-web-client/plan.md](specs/002-web-client/plan.md).
