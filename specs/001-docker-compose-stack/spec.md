# Feature Specification: One-Command Local Stack

**Feature Branch**: `001-docker-compose-stack`

**Created**: 2026-11-21

**Status**: Draft

**Input**: User description: "setup docker compose for this project so we can run with just one command (include: server, client, db, cache)"

## Clarifications

### Session 2026-11-21

- Q: How should the bundled client be delivered? → A: Switch to a web client (played in the
  browser) instead of the J2ME client. The web client does not exist yet and is built in its own
  feature (`002-web-client`); this feature reserves its place in the stack (see US3).
- Q: What should the cache hold? → A: Leave the cache out until a feature needs it.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Start the game server with one command (Priority: P1)

A developer clones the repository onto a machine that has only the container runtime
installed (no Java, no build tool, no database). They copy the example settings file, fill in
passwords, and run one command. The database and game server start in the right order, the
database is prepared with the game's tables and starter data automatically, and the server
accepts player connections.

**Why this priority**: This is the core ask. Without it, every contributor must install and
configure a JDK, Maven and a database by hand, which is exactly what blocked building and
running the server so far.

**Independent Test**: On a clean machine, clone, copy settings, run the start command, then
connect an existing game client to the server's game port and log in with a seeded test
account.

**Acceptance Scenarios**:

1. **Given** a fresh clone and a filled-in settings file, **When** the developer runs the start
   command, **Then** all components report healthy and the server accepts connections on the
   game port without any further manual step.
2. **Given** the stack was never started before, **When** it starts for the first time,
   **Then** the database contains the game's tables and seed data (accounts, items, maps,
   characters).
3. **Given** the stack is running, **When** the developer runs the stop command, **Then** every
   component shuts down cleanly.

---

### User Story 2 - Player progress survives restarts (Priority: P1)

A tester logs in, plays a match, gains experience, then the whole stack is stopped and started
again (or the server is rebuilt after a code change). When they log back in, their progress is
still there.

**Why this priority**: Persisting progress was the reason for moving to a real database; a
one-command setup that loses data on restart would undo that work.

**Independent Test**: Log in, gain experience, stop and start the stack, rebuild the server,
log in again, and confirm the experience value is unchanged.

**Acceptance Scenarios**:

1. **Given** a player gained experience, **When** the stack is stopped and started again,
   **Then** the player's experience is unchanged.
2. **Given** existing player data, **When** the server is rebuilt after a code change,
   **Then** the database is not re-seeded and no player data is lost.
3. **Given** the developer wants a clean slate, **When** they run the explicit reset command,
   **Then** all stored data is removed and the next start seeds a fresh database.

---

### User Story 3 - Web client joins the same one-command start (Priority: P3)

Once the web client exists (feature `002-web-client`), it starts with the same single command
as the server and database, is opened in a normal web browser at a documented local address,
and is already pointed at the bundled server.

**Why this priority**: It completes the "server + client + db in one command" goal, but it
depends on a client that is not built yet, so this feature only has to make room for it.

**Independent Test**: After `002-web-client` lands, run the start command, open the
documented address in a browser, and log in with a seeded account without editing any server
address.

**Acceptance Scenarios**:

1. **Given** the web client feature is merged, **When** the developer runs the same start
   command, **Then** the web client starts too and is reachable at the documented address.
2. **Given** the web client is not built yet, **When** the developer runs the start command,
   **Then** the server and database still start normally (the missing client does not block
   them).

---

### Edge Cases

- The game port (19149), or another port the stack publishes, is already in use on the host:
  startup fails with a message naming the port and how to change it.
- The settings file is missing or a required password is empty: the stack refuses to start
  and says which setting is missing, and it never falls back to a built-in default password.
- The database is still starting up when the server starts: the server waits and retries
  instead of crashing, and gives up with a clear error only after a set timeout.
- The seed data file changes after the database was already created: the change is NOT
  applied automatically (to protect player data); the docs explain how to apply it or reset.
- The container runtime is not running, or there is no internet for the first build: the start
  command fails with a readable error rather than leaving half-started components.
- The machine reboots: data is kept, and the stack resumes with the same start command.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The stack MUST start the game server and database with a single command run from
  the repository root.
- **FR-002**: The only host prerequisite MUST be the container runtime; the server MUST be
  built from the repository source as part of startup, with no JDK, build tool or database
  installed on the host.
- **FR-003**: On first start, the database MUST be created and loaded from the repository's
  canonical schema and seed file; on every later start it MUST NOT be re-seeded or wiped.
- **FR-004**: Database data MUST persist across stack stop/start, host reboot, and rebuilding
  the server.
- **FR-005**: The server MUST start only after the database is ready to accept connections,
  and MUST wait for it instead of crash-looping.
- **FR-006**: The server MUST connect to the database with the least-privilege game account,
  not an administrative account.
- **FR-007**: All passwords MUST come from a local settings file that is excluded from version
  control; a committed example file MUST list every setting with a placeholder and a
  description. No real secret may be committed.
- **FR-008**: Only the game port MUST be published to the host by default; the database MUST
  be reachable only from inside the stack.
- **FR-009**: The game server's settings (rooms, limits, resource versions) MUST stay editable
  in the existing config file and take effect after restarting the server, with no rebuild.
- **FR-010**: There MUST be one command each to stop the stack, view logs from all components
  together, and reset all stored data; reset MUST NOT happen as a side effect of any other
  command.
- **FR-011**: The stack MUST work on Windows (with a desktop container runtime) and on Linux.
- **FR-012**: Project documentation MUST describe setup, start, stop, logs, reset and how to
  connect a client, in no more than one page.
- **FR-013**: The stack definition MUST allow the web client from `002-web-client` to be added
  as one more component started by the same command, without changing how the server and
  database are started.

### Key Entities

- **Game server component**: built from `mobiarmy-java/`, exposes the game port, reads game
  settings from the config file and DB credentials from the settings file.
- **Database component**: holds accounts, items, maps and characters; initialized once from
  the canonical seed file; its data is the asset that must survive restarts.
- **Web client component** (added by `002-web-client`): browser-based game client, served
  at a local address and pointed at the bundled server.
- **Settings file**: local, uncommitted values for passwords and ports, with a committed
  example.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A developer with only the container runtime installed goes from a fresh clone to
  a server accepting a seeded test login in under 15 minutes on the first run, following only
  the docs.
- **SC-002**: Later starts (no code change) bring the stack to "accepting players" in under 60
  seconds.
- **SC-003**: Over 10 consecutive stop/start cycles, plus one server rebuild, 100% of player
  experience gained before the restarts is still there afterwards.
- **SC-004**: The number of manual steps between clone and running stack is at most 2 (copy and
  fill the settings file, run the start command).
- **SC-005**: A scan of the repository finds no real passwords in committed files.
- **SC-006**: From another machine on the network, only the documented ports respond; the
  database does not.

## Assumptions

- The stack is for local development and testing. Production deployment (TLS, backups,
  monitoring, public hosting) is out of scope.
- The user asked specifically for Docker Compose as the container tool; the plan will use it.
- The database is PostgreSQL, the canonical server is `mobiarmy-java/`, and the schema/seed
  file is `mobiarmy-java/dbarmy.sql`, as already decided in the constitution and recent changes.
- Seeded accounts are test data (confirmed by the project owner) and may be loaded as-is.
- The J2ME client (`Client/`, `Army165local/`) is not part of the stack; it stays usable from
  outside by pointing it at the published game port, which is how US1 is tested until the web
  client exists.
- Building the web client, including any browser-to-game-server connection layer it needs,
  belongs to `002-web-client`, not this feature.
- A cache is out of scope until a feature needs one.
- Saving gold, items and characters (beyond experience) is a separate feature and out of
  scope here.
- First-run build needs internet access to download base images and dependencies.
