# Project: MobiArmy Server Emulator

A Java server for the MobiArmy (Army 1.6.5) J2ME turn-based artillery game. The client is
fixed; the server must speak its protocol exactly. Governing rules live in
`.specify/memory/constitution.md` — read it before non-trivial work.

## Repository Layout
| Path | Role |
|------|------|
| `mobiarmy-java/` | **Canonical server.** All new work goes here. |
| `archive/mobiarmy/`, `archive/src/` | Older server attempts — reference only, do not develop in them. |
| `Client/SOURCE_ARMYI/` | Decompiled client source — handy for reading the protocol, but **lossy** (JD-Core dropped code, e.g. `model/y.java`); confirm against bytecode. |
| `Army165local/` | **Original** J2ME client bytecode + resources (`coreLG.TerrainMidlet`) — never edit; the web client compiles it. |
| `web-client/` | Browser build of the original client: TeaVM + thin MIDP web layer (`javax/microedition/*`, `web/*`). |
| `portal/` | Game portal (spec 004): static home page, shared game page `game.html`, `games.json` catalog; bind-mounted read-only into `web` (edit, then `docker compose restart web`). |
| `docker/relay/`, `docker/web/` | WebSocket→TCP relay (fixed target `server:19149`) and nginx configs (local/public). Optional `cloudflared` compose service (profile `tunnel`); nginx trusts `CF-Connecting-IP` only from its fixed IP 172.29.252.2. |
| `Army165sv.jar` | Prebuilt legacy server jar — reference only. |
| `mobiarmy-java/dbarmy.sql` | **Canonical** PostgreSQL schema + seed data (also loads into SQLite). |
| `mobiarmy.sql` | Old MySQL dump (different schema) — reference only. |
| `mobiarmy-java/sql/` | DB admin scripts (e.g. `create_game_user.sql`) and ordered migrations in `sql/migrations/` (run after a `pg_dump`). |
| `data/`, `player_data/`, `*.log` | Runtime data and logs — never hand-edit or commit. |

Inside `mobiarmy-java/src/main/java/com/mobiarmy/`:
- `server/` — `GameServer` (entry point, TCP accept loop), `ServerInit`, `ServerManager`
  (config + global state), `MessageHandler` (command-ID dispatch), `User`, `Room`, `ClientEntry`
- `network/` — `Message`, `ISession`, `IMessageHandler` (framing / session contracts)
- `fight/` — fight engine: `FightManager`, `Bullet`/`BulletManager`, `Boss`/`SuperBoss`, maps
- `data/` — static game data (`ItemData`, `MapData`, `NVData`, …) and `SqlConnectionManager`
- `portal/` — web portal JSON API on the JDK HTTP server (`PortalServer`, internal port `portal-port` 8081,
  nginx proxies `/api/`), `PortalTokens` (JWT HS256 sessions, secret `PORTAL_JWT_SECRET`), `LoginThrottle`, `Ranking`. Accounts: `server/Accounts`,
  `Registration`, `PasswordHash` (PBKDF2; game login and portal share them), `HashPasswords` (migration tool)

## Tech Stack
- Java 17, Maven (`mobiarmy-java/pom.xml`); deps: postgresql (JDBC), sqlite-jdbc, Jackson
- Raw TCP socket server, default port **19149**, one thread per client (cached thread pool)
- DB: PostgreSQL (`ServerInit` → `SqlConnectionManager.usePostgres`), settings from `db-*` keys
  in `army2.conf` (`db-host` is `host:port`); password from env `DB_PASSWORD` (overrides
  `db-password`). Startup fails fast if the DB is unreachable. `useLocalDump` (throwaway SQLite
  rebuilt from `dbarmy.sql`) still exists but is unused by the server; tests use it (`TestDb.fresh()`).
- SQL must stay portable: no backtick quoting, unquoted identifiers (PostgreSQL lowercases them),
  flags as SMALLINT 0/1.
- Only `exp`, the `online` flag, the selected character (`nhanvat`, 1-based), passwords
  (hashes, from the portal or re-hashing) and character purchases
  (`nvBuy` + the session's current `xu`, written together) are written back to the DB today; other
  xu changes, luong and items are not persisted yet.
- Config: `mobiarmy-java/army2.conf` (port, room counts, limits, resource versions, DB settings)

## Commands
Primary: Docker, from the repo root (needs `.env`, copied from `.env.example`; see `README.md`):
```
docker compose up -d --build     # build + start db, server, relay, web (first run seeds the DB); browser: http://localhost:8080
docker compose logs -f server    # wait for "Server started on port 19149"
docker compose restart server    # apply army2.conf edits (mounted read-only, no rebuild)
docker compose down              # stop, keeps data
docker compose down -v           # RESET: deletes all game data
docker compose exec db psql -U postgres -d dbarmy   # SQL shell; DB also on 127.0.0.1:${DB_PORT:-5432} for local tools
```
Inside Docker the server gets `DB_HOST`/`DB_USER`/`DB_DATABASE`/`DB_PASSWORD` from the environment,
which overrides `army2.conf`. Container scripts must stay LF (`.gitattributes`).

Web client (`web-client/`, no local JDK needed — build in the Maven image):
```
docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/web-client maven:3.9-eclipse-temurin-17 mvn -B package
#   add -Dteavm.minify=false -Dteavm.opt=SIMPLE for a readable client.js when debugging
```
- The MIDP layer implements only what the original bytecode calls; check with `javap -c` on
  `Army165local/*.class` before adding APIs. Pure logic lives in `web/*` with JUnit tests.
- In `@JSBody` scripts, `$`-prefix every local and inner-function parameter (TeaVM's minifier renames
  parameters but not locals, which caused real collisions).
- `web.build.FixGameAction` patches the original client's self-recursive `el.getGameAction` at build time.
- `web.build.AutoLoginPatch` patches the login screen `coreLG.dl.d()` at build time: with a portal ticket
  (`web.AutoLogin`, fetched by the page shell) it answers the sound question, skips the intro and logs in.
- `web.build.MakeIcons` generates `favicon.ico` and the home-screen icons from `Army165local/icon.png`
  at build time (integer nearest-neighbour scaling); generated icons are never committed. New
  web paths must be added to the nginx allowlist in both `docker/web/nginx.*.conf.template`.

Without Docker (run from `mobiarmy-java/`, because paths like `army2.conf` and `res/` are relative; needs
JDK 17, Maven and a reachable PostgreSQL):
```
mvn compile                                                        # build
mvn compile exec:java -Dexec.mainClass=com.mobiarmy.server.GameServer   # run server
mvn package                                                        # jar (no shade plugin yet: deps not bundled)
```
Unit tests (JUnit 5, `src/test/java`): `mvn test`, or in the Maven image as for the web client
(`-w /w/mobiarmy-java ... mvn -B test`). Coverage is thin; adding tests for new logic is required
(constitution V). The server Dockerfile builds with `-DskipTests`.

## Critical Rules
1. **Protocol first.** Before changing any message read/write, find the matching handler in
   `Client/SOURCE_ARMYI/` and match byte order, types and field order exactly. Command IDs
   (e.g. `-27` handshake) are defined by the client, not by us.
2. **Server is authoritative.** Never trust client values for xu/luong, exp, items, damage or
   admin rights — validate server-side.
3. **Bump resource versions** (`iconversion2`, `valuesversion2`, `equipVersion2`,
   `levelCVersion2`, `playerVersion2` in `army2.conf`) whenever files under `res/` or `data/`
   change, or clients keep stale caches.
4. **No secrets in files you write.** `army2.conf` currently holds a plaintext DB password —
   don't copy it anywhere, don't print it in logs or responses; move secrets to env/`.env`
   when touching that code.
5. **Data safety.** Parameterized SQL only; multi-balance changes in one transaction; schema
   changes as a migration script plus an updated dump.
6. Player-facing strings are Vietnamese — keep UTF-8 end to end.

## Starting Development
1. Check for a feature spec under `specs/{NNN}-{name}/` (`spec.md`, `plan.md`, `tasks.md`).
   Spec Kit flow: `/speckit-specify` → `/speckit-plan` → `/speckit-tasks` → `/speckit-implement`.
2. Locate the relevant command ID in `MessageHandler` and its client counterpart.
3. Check existing models/managers (`User`, `Room`, `FightManager`, `data/*`) before adding new
   ones — avoid duplicates, and don't port from `archive/mobiarmy/` without checking it still applies.
4. Verify against a real client connecting to port 19149 for any protocol-visible change.

## Coding Behavior Guidelines
Bias toward caution over speed; for trivial tasks use judgment.

### 1. Think Before Coding
Don't assume. Don't hide confusion. Surface tradeoffs.
- State assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them — don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If the client's expected behavior is unclear, read the client source before guessing.

### 2. Simplicity First
Minimum code that solves the problem. Nothing speculative.
- No features beyond what was asked.
- No abstractions for single-use code; no new frameworks.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios (but malformed client packets are *not*
  impossible — log and drop them).
- If 200 lines could be 50, rewrite it. Ask: "Would a senior engineer call this overcomplicated?"

### 3. Surgical Changes
Touch only what you must. Clean up only your own mess.
- Don't "improve" adjacent code, comments, or formatting; don't refactor what isn't broken.
- Match existing style (including Vietnamese identifiers/comments), even if you'd do it differently.
- Unrelated dead code: mention it, don't delete it.
- Remove imports/variables/functions that YOUR changes made unused.
- Test: every changed line traces directly to the user's request.

### 4. Goal-Driven Execution
Define success criteria. Loop until verified.
- "Add validation" → write tests for invalid inputs, then make them pass.
- "Fix the bug" → write a test (or packet replay) that reproduces it, then make it pass.
- "Refactor X" → behavior identical before and after, verified with a client.
- For multi-step tasks, state a brief plan: `1. [Step] → verify: [check]`.
