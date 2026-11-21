# Research: One-Command Local Stack

**Feature**: `001-docker-compose-stack` | **Date**: 2026-11-21

Facts checked in the repo and on the dev machine (Docker 28.3.0, Compose v2.38.1, Linux engine,
`core.autocrlf=true`) are marked *(verified)*.

## R1. Building the server without a JDK on the host

- **Decision**: Multi-stage `mobiarmy-java/Dockerfile`. Stage 1 `maven:3.9-eclipse-temurin-17`
  runs `mvn -B package -DskipTests dependency:copy-dependencies -DoutputDirectory=target/lib`.
  Stage 2 `eclipse-temurin:17-jre` copies `target/classes` + `target/lib` and runs
  `java -cp "classes:lib/*" com.mobiarmy.server.GameServer`.
- **Rationale**: The POM has no shade/assembly plugin, so `mvn package` makes a jar without its
  dependencies *(verified)*. `copy-dependencies` fixes that without editing the POM. Copying
  `pom.xml` first and running `dependency:go-offline` puts the dependency download in its own
  cached layer, so code-only rebuilds are fast (SC-002).
- **Alternatives**: Add maven-shade to the POM (changes the build for everyone, not needed
  yet). Build on the host and copy the jar in (breaks FR-002).

## R2. Files the server reads at runtime

- **Decision**: Working directory `/app` holds `res/` (614 KB) and `data/army1/` (144 KB),
  copied into the image. `army2.conf` is bind-mounted read-only from
  `mobiarmy-java/army2.conf`.
- **Rationale**: The server loads `army2.conf`, `res/...` and `data/army1/*.pack` by relative
  path from the working directory *(verified: `ServerManager:133,503-506`, `ServerInit:66`,
  `MapData:97`, `MapEntry:16-29`)*. Mounting only the config gives FR-009 (edit + restart, no
  rebuild). Resources change with code, so they belong in the image.
- **Alternatives**: Mount all of `mobiarmy-java/` (leaks the host's `target/` and SQLite files
  into the container, slower on Windows).

## R3. Pointing the server at the database container

- **Decision**: Add environment overrides `DB_HOST`, `DB_USER`, `DB_DATABASE` in
  `ServerManager.loadConfigFile`, same pattern as the existing `DB_PASSWORD`. Compose sets
  `DB_HOST=db:5432`.
- **Rationale**: `army2.conf` says `db-host: localhost:5432`, which is right outside Docker
  and wrong inside it. One config file for both modes, with env vars winning, keeps the
  file usable for both (FR-009) and keeps secrets out of it (FR-007).
- **Alternatives**: A second `army2.docker.conf` (two files drift apart). Rewriting the file
  in an entrypoint script (hidden behavior, harder to debug).

## R4. Database init exactly once (FR-003, FR-004)

- **Decision**: `postgres:16-alpine` with a named volume `pgdata`. Mount init files into
  `/docker-entrypoint-initdb.d/`:
  1. `01-dbarmy.sql` ← `mobiarmy-java/dbarmy.sql` (schema + seed)
  2. `02-game-user.sh` ← new `docker/db/02-game-user.sh`, which creates role `army` with
     `$DB_PASSWORD` and the grants from `mobiarmy-java/sql/create_game_user.sql`.
- **Rationale**: The official image runs `initdb.d` scripts **only when the data directory is
  empty**, which is exactly "seed on first start, never again". The password has to come
  from an env var, so the role creation is a short shell wrapper around `psql`; the `.sql` file
  stays the reference for manual (non-Docker) setups.
- **Alternatives**: Seed from the server at startup (needs "already seeded?" logic, and the
  server would need admin rights, which breaks FR-006).

## R5. Start order and waiting (FR-005)

- **Decision**: `db` healthcheck `pg_isready -h 127.0.0.1 -U $POSTGRES_USER -d dbarmy`
  (interval 2s, retries 30). The server uses `depends_on: db: condition: service_healthy` and
  `restart: unless-stopped`.
- **Rationale**: During first-run init the image runs a temporary server on the Unix socket
  only. Checking over TCP (`-h 127.0.0.1`) stays "unhealthy" until init has finished and the real
  server listens, so the game server never sees a half-seeded DB. `restart: unless-stopped` is
  needed because on a DB connection failure `GameServer` catches the exception and `main`
  returns with exit code 0 *(verified: `GameServer.java` catch + `finally stop()`)*, which
  `on-failure` would not restart. It also covers "resume after reboot".
- **Alternatives**: A wait-for-it script in the server image (duplicates the healthcheck).

## R6. Secrets and the settings file (FR-007)

- **Decision**: Compose reads the repo-root `.env` (already git-ignored, with `!.env.example`
  allowed *(verified: `.gitignore`)*). Required variables use `${VAR:?message}` so
  `docker compose up` refuses to start and names the missing one. Variables:
  `POSTGRES_PASSWORD` (DB admin) and `DB_PASSWORD` (game role), plus optional `GAME_PORT`
  (default 19149).
- **Rationale**: `${VAR:?}` is built into Compose, so no extra script is needed; it also
  rules out a built-in default password.
- **Alternatives**: Docker secrets files (more setup, no real benefit locally).

## R7. Network exposure (FR-008, SC-006)

- **Decision**: Publish only `${GAME_PORT:-19149}:19149` on the server. `db` has no `ports:`
  entry; the server reaches it on the default Compose network as `db:5432`.
- **Rationale**: This is the smallest exposure that satisfies the spec. Developers who need SQL
  access use `docker compose exec db psql ...`, which the contract documents.

## R8. Windows line endings

- **Decision**: Add `.gitattributes` with `*.sh text eol=lf`.
- **Rationale**: `core.autocrlf=true` on this machine *(verified)*. A CRLF shell script
  fails inside the Linux container with `bad interpreter` or `$'\r': command not found`. This
  is required for FR-011.

## R9. Text encoding

- **Decision**: Run the JVM with `-Dfile.encoding=UTF-8`. The DB is created UTF8 (image
  default).
- **Rationale**: Java 17's default charset follows the locale. Without it, the Vietnamese
  strings in `army2.conf` and the DB can garble (constitution: UTF-8 end to end).

## R10. Clean shutdown

- **Decision**: Exec-form `ENTRYPOINT ["java", ...]` so the JVM is PID 1 and receives SIGTERM;
  `stop_grace_period: 15s`.
- **Rationale**: Gives the JVM time to close sockets. Saving player state on shutdown is not
  done today and is out of scope (persistence feature).

## R11. Web client slot (FR-013)

- **Decision**: No placeholder service. The compose file keeps server and DB self-contained,
  so `002-web-client` adds one `web` service (and any browser connection gateway) without
  touching them.
- **Rationale**: An empty placeholder would be speculative (constitution VI, YAGNI) and would
  fail US3 scenario 2 if it couldn't start.
