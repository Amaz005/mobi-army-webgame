# Contract: Stack Operator Interface

This feature doesn't add or change the game protocol. Its interface is the set of commands,
settings and ports a developer relies on. Changing anything here is a breaking change for
contributors and must update this file and the README.

All commands run from the repository root.

## Commands

| Intent | Command | Guarantees |
|--------|---------|-----------|
| Start (first run or later) | `docker compose up -d --build` | Builds the server from source, starts `db` then `server`; returns when the containers have started. First run seeds the DB. |
| Watch until ready | `docker compose logs -f server` | Shows `Server started on port 19149` when it is accepting players. |
| Status | `docker compose ps` | `db` shows `healthy`; `server` shows `running`. |
| Logs (all) | `docker compose logs -f` | Merged logs of every service. |
| Stop | `docker compose down` | Stops and removes the containers. **Keeps data.** |
| Apply `army2.conf` edits | `docker compose restart server` | No rebuild; new settings are read on start. |
| Reset all data | `docker compose down -v` | **Deletes the `pgdata` volume.** The next start seeds a fresh DB. This is the only command that removes data. |
| SQL shell (admin) | `docker compose exec db psql -U postgres -d dbarmy` | DB access without publishing its port. |

## Settings file

- Location: `.env` in the repository root. Never committed.
- Template: `.env.example` (committed). Every variable has a placeholder and a description.
- Required: `POSTGRES_PASSWORD`, `DB_PASSWORD`. If either is missing or empty, `docker compose up`
  exits before starting anything, with `required variable <NAME> is missing a value: <hint>`.
- Optional: `GAME_PORT` (default `19149`).

See [data-model.md](../data-model.md#settings) for the full table.

## Network surface

| Port (host) | Service | Protocol | Audience |
|-------------|---------|----------|----------|
| `${GAME_PORT:-19149}` | `server` | MobiArmy TCP protocol (unchanged) | Game clients |

No other host ports are published. The database is reachable only inside the Compose network
as `db:5432`.

## Server startup signals (log lines)

| Line | Meaning |
|------|---------|
| `[ServerInit] Connected to PostgreSQL db:5432/dbarmy` | DB reachable with the game role |
| `Server started on port 19149` | Ready for players |
| `Cannot connect to PostgreSQL ...` | DB unreachable or credentials wrong; the container restarts |
