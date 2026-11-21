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

**Ports:** only the game port `19149` is published (set `GAME_PORT` in `.env` if it's taken). The
database is reachable only inside the stack.

**Connect a client:** point a MobiArmy client at `127.0.0.1:19149` (or `<this machine's IP>:<GAME_PORT>`).
Seeded test account: `nguoiduatin` / `3`.

The full command and settings contract is in
[specs/001-docker-compose-stack/contracts/stack-interface.md](specs/001-docker-compose-stack/contracts/stack-interface.md).

## Adding the web client

The browser client is planned as feature `002-web-client`. It will add a `web` service (plus any
browser-to-game-server gateway it needs) to `docker-compose.yml` that depends on `server`, without
changing the `db` or `server` services. The same `docker compose up -d --build` will then start it too.
