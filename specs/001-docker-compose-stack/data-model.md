# Data Model: One-Command Local Stack

This feature adds no database tables. Its "data" is the stack's components, the persistent
volume, and the settings that wire them together. The game schema itself stays as defined in
`mobiarmy-java/dbarmy.sql`.

## Components (Compose services)

| Service | Built from / image | Published ports | Depends on | Restart |
|---------|-------------------|-----------------|------------|---------|
| `db` | `postgres:16-alpine` | none (internal `5432`) | — | `unless-stopped` |
| `server` | `mobiarmy-java/Dockerfile` | `${GAME_PORT:-19149}` → `19149` | `db` healthy | `unless-stopped` |
| `web` *(added by `002-web-client`)* | — | TBD in 002 | `server` | — |

## Persistent volume

| Volume | Mounted at | Holds | Lifecycle |
|--------|-----------|-------|-----------|
| `pgdata` | `db:/var/lib/postgresql/data` | All game data (accounts, items, maps, characters) | Created on first `up`; kept by `down`, rebuilds and reboots; deleted **only** by `down -v` (reset) |

**State transitions of `pgdata`:**

```text
(absent) --first `up`--> initializing (01-dbarmy.sql, 02-game-user.sh run once)
initializing --init succeeds--> ready (healthcheck passes; server starts)
initializing --init fails--> broken (db exits; fix the script, then reset)
ready --stop/start, rebuild, reboot--> ready (no re-seed)
ready --reset (`down -v`)--> (absent)
```

## Settings

### `.env` (repo root, git-ignored; template `.env.example` committed)

| Variable | Required | Used by | Meaning | Validation |
|----------|----------|---------|---------|------------|
| `POSTGRES_PASSWORD` | yes | `db` | Password of the DB admin role `postgres` (init only) | `${…:?}`: start refused if empty |
| `DB_PASSWORD` | yes | `db` init, `server` | Password of the least-privilege game role `army` | `${…:?}`: start refused if empty |
| `GAME_PORT` | no (19149) | `server` | Host port players connect to | Must be free on the host |

### `server` environment (set in compose, overrides `army2.conf`)

| Variable | Value | Overrides config key |
|----------|-------|---------------------|
| `DB_HOST` | `db:5432` | `db-host` |
| `DB_USER` | `army` | `db-user` |
| `DB_DATABASE` | `dbarmy` | `db-database` |
| `DB_PASSWORD` | from `.env` | `db-password` |

Precedence: environment variable (if set) > `army2.conf` value > built-in default. Without
Docker nothing changes: if the variables are unset, `army2.conf` is used as before.

### `army2.conf`

Bind-mounted read-only at `/app/army2.conf`. All game settings (rooms, limits, resource
versions) stay there (FR-009). Editing it and running `docker compose restart server` applies
changes.

## DB roles

| Role | Created by | Privileges | Used by |
|------|-----------|-----------|---------|
| `postgres` | image, from `POSTGRES_PASSWORD` | superuser | init scripts, manual admin only |
| `army` | `02-game-user.sh` | `CONNECT` on `dbarmy`; `SELECT, INSERT, UPDATE, DELETE` on all tables in `public` | game server (FR-006) |
