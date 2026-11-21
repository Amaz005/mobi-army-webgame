# Quickstart & Validation: One-Command Local Stack

Proves the feature end to end. Commands are in [contracts/stack-interface.md](contracts/stack-interface.md);
settings are in [data-model.md](data-model.md#settings).

## Prerequisites

- Docker Desktop (Windows/macOS) or Docker Engine + Compose v2 (Linux). Nothing else.
- Internet access for the first build.
- For login checks: a MobiArmy client pointed at `127.0.0.1:19149` (the J2ME client in
  `Client/`, until `002-web-client` exists).

## Setup (the only 2 manual steps, SC-004)

1. Copy `.env.example` to `.env` and set `POSTGRES_PASSWORD` and `DB_PASSWORD`.
2. Run `docker compose up -d --build`.

## Validation scenarios

| # | Covers | Steps | Expected |
|---|--------|-------|----------|
| V1 | US1, FR-001/002/003, SC-001 | On a fresh clone: setup steps above, then `docker compose logs -f server` | `Connected to PostgreSQL db:5432/dbarmy`, then `Server started on port 19149`; time it from clone (< 15 min) |
| V2 | FR-003 | `docker compose exec db psql -U postgres -d dbarmy -c "select count(*) from account; select count(*) from map;"` | `3` and `61` |
| V3 | FR-006 | `docker compose exec db psql -U postgres -d dbarmy -c "\du army"` | `army` exists with no superuser/createdb/createrole attributes |
| V4 | US1 | Connect a client and log in as `nguoiduatin` / `3` | Reaches the lobby |
| V5 | US2, FR-004, SC-003 | Log in, finish a match (exp rises). `docker compose down`, then `up -d`; log in again | Same exp. Repeat 10 times, plus once with `--build` after touching a `.java` file |
| V6 | FR-003 (no re-seed) | Before V5: `update account set exp=12345 where id=3`; after restart, `select exp from account where id=3` | `12345` (not reset to the seed value) |
| V7 | FR-010 reset | `docker compose down -v`, then `up -d --build`, then repeat V6's select | Back to the seed value `500000` |
| V8 | FR-007 | Rename `.env`, then `docker compose up -d` | Exits immediately naming `POSTGRES_PASSWORD`; nothing starts |
| V9 | FR-008, SC-006 | From another machine: `nc -zv <host-ip> 19149` and `nc -zv <host-ip> 5432` | 19149 open, 5432 refused |
| V10 | FR-009 | Change `n-room-0` in `army2.conf`, then `docker compose restart server` | Server log or lobby shows the new room count, with no rebuild |
| V11 | SC-002 | With images built: `docker compose down; time docker compose up -d` plus wait for the "Server started" log line | < 60 s |
| V12 | Edge: port in use | Start anything on 19149, then `docker compose up -d` | Error names port 19149; set `GAME_PORT` in `.env` to fix |
| V13 | FR-011 | Run V1 on Windows (Docker Desktop) and on Linux | Both pass; `02-game-user.sh` runs (no `\r` errors) |
| V14 | SC-005 | `git grep -nIE "PASSWORD=.+"` (excluding `.env.example` placeholders) | No real secrets |

## Cleanup

`docker compose down -v` removes containers and data. `docker image prune` reclaims build
layers.
