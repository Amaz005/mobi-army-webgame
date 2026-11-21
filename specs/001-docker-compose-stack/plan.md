# Implementation Plan: One-Command Local Stack

**Branch**: `001-docker-compose-stack` | **Date**: 2026-11-21 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-docker-compose-stack/spec.md`

## Summary

Run the MobiArmy game server and its PostgreSQL database with one command
(`docker compose up -d --build`), with Docker as the only host prerequisite. The server image
is built from `mobiarmy-java/` source in a multi-stage Dockerfile. The database is seeded
once from `mobiarmy-java/dbarmy.sql` through Postgres's init-on-empty-volume mechanism, and
the server connects as the least-privilege `army` role. Secrets come from a git-ignored
`.env`, and only the game port is published. Code changes are limited to environment
overrides for the DB host, user and name, so the same `army2.conf` works inside and outside
Docker. The web client and its browser gateway are left to `002-web-client`; the cache is out
of scope.

## Technical Context

**Language/Version**: Java 17 (server); POSIX sh (one DB init script); Compose spec YAML

**Primary Dependencies**: Docker Engine / Desktop ≥ 24 with Compose v2; images
`maven:3.9-eclipse-temurin-17`, `eclipse-temurin:17-jre`, `postgres:16-alpine`; existing
`org.postgresql:postgresql` 42.7.4

**Storage**: PostgreSQL 16 in named volume `pgdata`

**Testing**: Scripted end-to-end checks in [quickstart.md](quickstart.md) (V1–V14); the
server build (`mvn package`) runs inside the image build. No unit-test harness exists yet.
This feature adds no game logic, so no unit tests are added (constitution V applies to game
logic).

**Target Platform**: Linux containers on Windows (Docker Desktop, verified 28.3.0) and Linux
hosts

**Project Type**: Single backend service plus its database (TCP game server)

**Performance Goals**: Warm start to "accepting players" < 60 s (SC-002); first run < 15 min
(SC-001)

**Constraints**: Only the game port is published; no secrets in git; DB never re-seeded
implicitly; LF line endings for container scripts

**Scale/Scope**: Local dev/test stack, 2 services, 1 volume, about 8 new or changed files

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle / Gate | Assessment | Status |
|------------------|-----------|--------|
| I. Client Protocol Compatibility | No wire-format change; port 19149 kept as the container and default host port. | PASS |
| II. Server-Authoritative State | Untouched. | PASS |
| III. Persistent Data Integrity | Seed runs only on an empty volume; data survives stop, rebuild and reboot; the only destructive command (`down -v`) is explicit and documented with a warning. Seed changes are not auto-applied, which protects player data. | PASS |
| IV. Secrets & Configuration Hygiene | Passwords only in git-ignored `.env`; `${VAR:?}` forbids empty or default passwords; server uses the least-privilege `army` role; DB not exposed. Plaintext player passwords are pre-existing and not made worse. | PASS |
| V. Verifiable Changes & Observability | `mvn package` gate runs in the image build; end-to-end validation in quickstart; startup log lines documented in the contract. | PASS |
| VI. Single Canonical Codebase / YAGNI | Builds only `mobiarmy-java/`; no placeholder web service, no cache. | PASS |
| Tech constraints: PostgreSQL in prod, pinned deps, UTF-8 | Postgres 16; image tags pinned to a major/minor line; `-Dfile.encoding=UTF-8`. | PASS |
| Workflow gates (build, tests, protocol, secrets, migrations) | Build ✔; no new game logic; no protocol change; secret scan in V14; no schema change. | PASS |

**Post-design re-check (after Phase 1)**: Still PASS. The design adds one small code change
(env overrides in `ServerManager.loadConfigFile`), which is configuration, not game logic,
and keeps `army2.conf` behavior unchanged when the variables are unset.

## Project Structure

### Documentation (this feature)

```text
specs/001-docker-compose-stack/
├── plan.md              # This file
├── research.md          # Phase 0: decisions R1–R11
├── data-model.md        # Phase 1: services, volume, settings, roles
├── quickstart.md        # Phase 1: validation scenarios V1–V14
├── contracts/
│   └── stack-interface.md   # Phase 1: commands, settings file, ports, log signals
├── checklists/
│   └── requirements.md
└── tasks.md             # Phase 2 (/speckit-tasks, not created here)
```

### Source Code (repository root)

```text
docker-compose.yml               # NEW: services db + server, volume pgdata
.env.example                     # NEW: POSTGRES_PASSWORD, DB_PASSWORD, GAME_PORT (placeholders)
.gitattributes                   # NEW: *.sh text eol=lf
docker/
└── db/
    └── 02-game-user.sh          # NEW: create role army with $DB_PASSWORD + grants (runs once)
mobiarmy-java/
├── Dockerfile                   # NEW: multi-stage build (maven → temurin JRE), WORKDIR /app
├── .dockerignore                # NEW: keep target/, logs, *.sqlite, .env out of the build context
├── army2.conf                   # UNCHANGED content; bind-mounted read-only
├── dbarmy.sql                   # UNCHANGED; mounted as initdb.d/01-dbarmy.sql
├── sql/create_game_user.sql     # UNCHANGED; reference for non-Docker setups
└── src/main/java/com/mobiarmy/server/
    └── ServerManager.java       # CHANGED: DB_HOST / DB_USER / DB_DATABASE env overrides
README.md                        # NEW (root): one-page run guide (FR-012)
CLAUDE.md                        # CHANGED: Commands section gains the Docker commands
```

**Structure Decision**: Compose file and shared Docker assets live at the repo root, because
the build context and `.env` resolve from there and "one command from the repo root" is the
requirement. The server's Dockerfile sits next to its POM in `mobiarmy-java/`, with that
folder as its build context. `002-web-client` will add its own service to the same compose
file.

## Complexity Tracking

No constitution violations; nothing to justify.
