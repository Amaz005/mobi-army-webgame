---

description: "Task list for One-Command Local Stack"
---

# Tasks: One-Command Local Stack

**Input**: Design documents from `specs/001-docker-compose-stack/`

**Prerequisites**: [plan.md](plan.md), [spec.md](spec.md), [research.md](research.md),
[data-model.md](data-model.md), [contracts/stack-interface.md](contracts/stack-interface.md),
[quickstart.md](quickstart.md)

**Tests**: No automated test tasks; the spec does not request them and the feature adds no
game logic (plan › Technical Context › Testing). Each story ends with tasks that run the
matching [quickstart.md](quickstart.md) validation scenarios (V1–V14) and record the results.

**Organization**: Tasks are grouped by user story so each story can be built and checked on
its own.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Paths are relative to the repository root

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Repo hygiene files that every later task relies on

- [X] T001 [P] Create `.gitattributes` at the repo root with `*.sh text eol=lf` so container scripts keep LF endings under `core.autocrlf=true` (research.md R8)
- [X] T002 [P] Create `mobiarmy-java/.dockerignore` excluding `target/`, `*.log`, `*.sqlite*`, `cache/`, `.env`, `.env.*` so the build context stays small and secret-free (plan › Source Code)
- [X] T003 [P] Create `.env.example` at the repo root with a placeholder and a one-line description for each variable: `POSTGRES_PASSWORD` (data-model.md `.env.POSTGRES_PASSWORD`), `DB_PASSWORD` (data-model.md `.env.DB_PASSWORD`), `GAME_PORT` commented out with its default (data-model.md `.env.GAME_PORT`); confirm `.env` is still ignored and `.env.example` is not (`git check-ignore -v .env .env.example`)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: A server image that can be pointed at the containerized DB. Nothing can start
without it.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Add env-var overrides in `ServerManager.loadConfigFile` in `mobiarmy-java/src/main/java/com/mobiarmy/server/ServerManager.java`: `dbHost` from `DB_HOST`, `dbUser` from `DB_USER`, `dbDatabase` from `DB_DATABASE`, using the same `System.getenv(...) != null ? ... : configStr(...)` pattern already used for `DB_PASSWORD`. Follow the precedence in data-model.md › server environment (`DB_HOST`, `DB_USER`, `DB_DATABASE`, `DB_PASSWORD`); no other changes (research.md R3)
- [X] T005 [P] Create `mobiarmy-java/Dockerfile`, multi-stage (research.md R1, R2, R9, R10). Build stage `maven:3.9-eclipse-temurin-17`: copy `pom.xml`, run `mvn -B dependency:go-offline`, copy `src/`, run `mvn -B package -DskipTests dependency:copy-dependencies -DoutputDirectory=target/lib`. Runtime stage `eclipse-temurin:17-jre`: `WORKDIR /app`, copy `target/classes` → `/app/classes`, `target/lib` → `/app/lib`, `res/` → `/app/res`, `data/` → `/app/data`; `EXPOSE 19149`; exec-form `ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-cp","classes:lib/*","com.mobiarmy.server.GameServer"]`
- [X] T006 Verify the image builds: `docker build -t mobiarmy-server mobiarmy-java` succeeds (this is the constitution's `mvn package` gate, now running inside Docker); fix any compile errors from T004 or from the earlier PostgreSQL switch

**Checkpoint**: Server image builds; user story work can begin

---

## Phase 3: User Story 1 - Start the game server with one command (Priority: P1) 🎯 MVP

**Goal**: `docker compose up -d --build` from a fresh clone starts a seeded PostgreSQL and a
game server that accepts players on port 19149.

**Independent Test**: quickstart.md V1–V4 and V8 pass on a fresh clone.

### Implementation for User Story 1

- [X] T007 [P] [US1] Create `docker/db/02-game-user.sh` (`#!/bin/sh`, `set -eu`): run `psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB"` with a heredoc that creates role `army` LOGIN with password `$DB_PASSWORD` and grants exactly the privileges in data-model.md `DB roles.army` (mirror `mobiarmy-java/sql/create_game_user.sql`, including `ALTER DEFAULT PRIVILEGES`). Pass the password safely (`psql -v pw="$DB_PASSWORD"` and `:'pw'`), never by interpolating it into SQL
- [X] T008 [US1] Create `docker-compose.yml` at the repo root with service `db` per data-model.md › Components and research.md R4/R5/R7: image `postgres:16-alpine`; env `POSTGRES_DB=dbarmy`, `POSTGRES_PASSWORD=${POSTGRES_PASSWORD:?set POSTGRES_PASSWORD in .env}` (data-model.md `.env.POSTGRES_PASSWORD`), `DB_PASSWORD=${DB_PASSWORD:?set DB_PASSWORD in .env}` (data-model.md `.env.DB_PASSWORD`); volumes `pgdata:/var/lib/postgresql/data` (data-model.md `pgdata`), `./mobiarmy-java/dbarmy.sql:/docker-entrypoint-initdb.d/01-dbarmy.sql:ro`, `./docker/db/02-game-user.sh:/docker-entrypoint-initdb.d/02-game-user.sh:ro`; healthcheck `pg_isready -h 127.0.0.1 -U postgres -d dbarmy` (interval 2s, timeout 3s, retries 30); `restart: unless-stopped`; **no `ports:`** (FR-008); top-level `volumes: pgdata: {}`
- [X] T009 [US1] Add service `server` to `docker-compose.yml` per data-model.md › Components, data-model.md › server environment and research.md R2/R3/R5/R7/R10: `build: ./mobiarmy-java`; `depends_on: db: condition: service_healthy`; env `DB_HOST=db:5432`, `DB_USER=army`, `DB_DATABASE=dbarmy`, `DB_PASSWORD=${DB_PASSWORD:?set DB_PASSWORD in .env}`; `ports: ["${GAME_PORT:-19149}:19149"]` (data-model.md `.env.GAME_PORT`); volume `./mobiarmy-java/army2.conf:/app/army2.conf:ro`; `restart: unless-stopped`; `stop_grace_period: 15s`
- [X] T010 [US1] Run `docker compose config` to validate the file, then `docker compose up -d --build` with a local `.env`; confirm quickstart.md **V1** (log lines from contracts/stack-interface.md › Server startup signals), **V2** (3 accounts, 61 maps) and **V3** (`army` has no superuser/createdb/createrole). Fix and re-run until all pass
- [X] T011 [US1] Run quickstart.md **V8** (rename `.env`, `docker compose up -d` must fail naming `POSTGRES_PASSWORD`, nothing started), then restore `.env`
- [X] T012 [US1] Run quickstart.md **V10** (edit `n-room-0` in `mobiarmy-java/army2.conf`, `docker compose restart server`, new value takes effect without a rebuild), then revert the edit
- [ ] T013 [US1] Run quickstart.md **V4** (log in as `nguoiduatin`/`3` with the J2ME client pointed at `127.0.0.1:19149`). If no client can run on this machine, record it as "manual, pending" in this file rather than marking it done — **Manual, pending (2026-11-21)**: no game client runnable here; TCP connect to 127.0.0.1:19149 verified (server logged `Client connected`).

**Checkpoint**: One command brings up a seeded DB and a server accepting connections; the MVP
is usable

---

## Phase 4: User Story 2 - Player progress survives restarts (Priority: P1)

**Goal**: Data in `pgdata` survives stop/start, rebuilds and reboots; only the explicit reset
removes it.

**Independent Test**: quickstart.md V6 and V7 pass, and V5 passes when a client is available.

### Implementation for User Story 2

- [X] T014 [US2] Run quickstart.md **V6**: set `exp=12345` for account id 3 via `docker compose exec db psql -U postgres -d dbarmy`, then `docker compose down` + `up -d`, then `touch` a `.java` file and `up -d --build`; `exp` must still be `12345` after both (data-model.md `pgdata` state transitions "ready → ready")
- [X] T015 [US2] Run quickstart.md **V7**: `docker compose down -v`, `up -d --build`; `exp` for id 3 is back to the seed value `500000` (data-model.md `pgdata` state transition "ready → absent")
- [ ] T016 [US2] Run quickstart.md **V5** (gain exp in a real match, 10 stop/start cycles plus one `--build`, exp unchanged; SC-003). If no client is available, record it as "manual, pending" in this file — **Manual, pending (2026-11-21)**: needs a client to play a match; DB-level persistence proven by T014/T015.

**Checkpoint**: US1 and US2 both work; data persistence is proven

---

## Phase 5: User Story 3 - Web client joins the same one-command start (Priority: P3)

**Goal**: Leave a clear, non-blocking place for `002-web-client` to add its service
(FR-013). Per research.md R11, no placeholder service is created.

**Independent Test**: US3 scenario 2: the stack starts normally with no web client present
(already shown by T010). Scenario 1 is checked in `002-web-client`.

### Implementation for User Story 3

- [X] T017 [US3] Add a short "Adding the web client" section to the root `README.md` (created in T018) stating that `002-web-client` adds a `web` service (and any browser gateway) to `docker-compose.yml` that depends on `server`, without changing `db` or `server`

**Checkpoint**: All user stories addressed

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Documentation and the remaining success-criteria checks

- [X] T018 [P] Create the root `README.md`, at most one page (FR-012): prerequisites, the 2 setup steps, then the command table from contracts/stack-interface.md › Commands (start, logs, status, stop, restart after config edits, reset with a ⚠️ data-loss warning, SQL shell), the port table, and how to point a client at `127.0.0.1:${GAME_PORT}`; plus the T017 section. Link to the contract rather than duplicating it
- [X] T019 [P] Update the Commands section of `CLAUDE.md`: add `docker compose up -d --build` / `logs -f` / `down` / `down -v` as the primary way to run, and keep the Maven commands for running without Docker
- [X] T020 Run quickstart.md **V11** (warm start < 60 s, SC-002) and **V12** (port 19149 already taken → clear error; `GAME_PORT` fixes it)
- [ ] T021 Run quickstart.md **V9** (from another machine only 19149 answers; 5432 refused) where a second machine is available; otherwise confirm with `docker compose ps` that `db` publishes no ports and record it as partially verified — **Partially verified (2026-11-21)**: `docker compose ps` shows db with internal `5432/tcp` only; second-machine check pending.
- [ ] T022 Run quickstart.md **V13** on Windows/Docker Desktop (this machine: confirm `02-game-user.sh` ran with no `\r` errors in `docker compose logs db`); record Linux as pending if no Linux host is available — **Partially verified (2026-11-21)**: Windows/Docker Desktop passes (script stored LF, no `` errors, role created); Linux host pending.
- [X] T023 Run quickstart.md **V14** (`git grep -nIE "PASSWORD=.+"` finds only `.env.example` placeholders) and confirm `git status` does not list `.env`
- [X] T024 Mark completed tasks `[x]` in this file and note any "manual, pending" items; leave committing to the user

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: no dependencies; T001–T003 can run in parallel
- **Foundational (Phase 2)**: T004 and T005 can run in parallel; T006 needs both, plus T002
- **US1 (Phase 3)**: needs Phase 2. T007 is parallel with T008; T009 needs T008 (same file); T010 needs T003, T007, T009; T011–T013 need T010
- **US2 (Phase 4)**: needs T010 (a running stack); T014 → T015 in order (T015 wipes the data T014 checks); T016 independent of T015
- **US3 (Phase 5)**: T017 needs T018 (same file)
- **Polish (Phase 6)**: T018/T019 can start after T010; T020–T023 need US1 and US2 done; T024 last

### User Story Dependencies

- **US1 (P1)**: depends only on Foundational
- **US2 (P1)**: builds on the US1 stack (it checks the persistence of that stack's volume), but adds no files, so it can't conflict with US1 work
- **US3 (P3)**: documentation only; independent

### Parallel Opportunities

- T001, T002, T003 (three different new files)
- T004 (Java) with T005 (Dockerfile)
- T007 (init script) with T008 (compose `db` service)
- T018 (README) with T019 (CLAUDE.md)

---

## Parallel Example: User Story 1

```bash
# After Phase 2, these two touch different files:
Task: "Create docker/db/02-game-user.sh (T007)"
Task: "Create docker-compose.yml with service db (T008)"
# Then sequentially: T009 (same compose file) → T010 validation
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Phase 1 Setup → Phase 2 Foundational (image builds)
2. Phase 3 US1 → **STOP and VALIDATE** with T010–T012
3. The stack is usable for anyone with a client

### Incremental Delivery

1. Setup + Foundational → buildable image
2. US1 → one-command start (MVP)
3. US2 → persistence proven (no new files, only verification)
4. US3 + Polish → docs, remaining success criteria

---

## Notes

- Constrained settings are defined once in data-model.md (`.env.*`, server environment, `pgdata`, `DB roles.army`); tasks cite them instead of repeating the rules
- Tasks that need a game client (T013, T016) may stay "manual, pending" on a machine without one; say so instead of marking them done
- Never run `docker compose down -v` outside T015 or a deliberate reset; it deletes all game data
- Don't commit or push as part of these tasks; the user decides
