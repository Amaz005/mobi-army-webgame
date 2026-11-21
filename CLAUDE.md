# Project: MobiArmy Server Emulator

A Java server for the MobiArmy (Army 1.6.5) J2ME turn-based artillery game. The client is
fixed; the server must speak its protocol exactly. Governing rules live in
`.specify/memory/constitution.md` — read it before non-trivial work.

## Repository Layout
| Path | Role |
|------|------|
| `mobiarmy-java/` | **Canonical server.** All new work goes here. |
| `archive/mobiarmy/`, `archive/src/` | Older server attempts — reference only, do not develop in them. |
| `Client/SOURCE_ARMYI/` | Client source (Maven) — the source of truth for the wire protocol. |
| `Army165local/` | Decompiled/obfuscated J2ME client classes (`coreLG.TerrainMidlet`) — reference only. |
| `Army165sv.jar` | Prebuilt legacy server jar — reference only. |
| `mobiarmy.sql`, `mobiarmy-java/dbarmy.sql` | MySQL schema + seed dumps. |
| `data/`, `player_data/`, `*.log` | Runtime data and logs — never hand-edit or commit. |

Inside `mobiarmy-java/src/main/java/com/mobiarmy/`:
- `server/` — `GameServer` (entry point, TCP accept loop), `ServerInit`, `ServerManager`
  (config + global state), `MessageHandler` (command-ID dispatch), `User`, `Room`, `ClientEntry`
- `network/` — `Message`, `ISession`, `IMessageHandler` (framing / session contracts)
- `fight/` — fight engine: `FightManager`, `Bullet`/`BulletManager`, `Boss`/`SuperBoss`, maps
- `data/` — static game data (`ItemData`, `MapData`, `NVData`, …) and `SqlConnectionManager`

## Tech Stack
- Java 17, Maven (`mobiarmy-java/pom.xml`); deps: mysql-connector-java, sqlite-jdbc, Jackson
- Raw TCP socket server, default port **19149**, one thread per client (cached thread pool)
- DB: MySQL (`ServerInit` → `SqlConnectionManager.useMySql`), settings from `mysql-*` keys in
  `army2.conf`; password from env `MYSQL_PASSWORD` (overrides `mysql-password`). Startup fails
  fast if MySQL is unreachable. `useLocalDump` (throwaway SQLite rebuilt from `dbarmy.sql`) still
  exists but is unused.
- Only `exp` and the `online` flag are written back to the DB today; xu, luong, items and
  character changes are not persisted yet.
- Config: `mobiarmy-java/army2.conf` (port, room counts, limits, resource versions, DB settings)

## Commands (run from `mobiarmy-java/` — paths like `army2.conf`, `dbarmy.sql`, `res/` are relative)
```
mvn compile                                                        # build
mvn compile exec:java -Dexec.mainClass=com.mobiarmy.server.GameServer   # run server
mvn package                                                        # jar (no shade plugin yet: deps not bundled)
```
There are no automated tests yet. Adding tests for new logic is required (constitution V).

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
