# MobiArmy Server Constitution

## Core Principles

### I. Client Protocol Compatibility (NON-NEGOTIABLE)

The server exists to serve the existing MobiArmy clients (the J2ME client in `Army165local/`
and the source client in `Client/SOURCE_ARMYI/`). Therefore:

- Every message the server sends or accepts MUST match the byte layout, command IDs, field
  order, and encoding the client expects. Changes to wire format MUST be verified against the
  client source before merge.
- Server code MUST NOT require client modifications unless a spec explicitly scopes a client
  change and both sides ship together.
- Whenever server-delivered resources change (icon, values, equipment, level, player packs),
  the matching version key in configuration (e.g. `iconversion2`, `valuesversion2`) MUST be
  incremented so clients refresh their caches.
- Unknown or malformed client messages MUST be logged and dropped; they MUST NOT crash the
  session thread or the server.

Rationale: the client is effectively frozen; a protocol mismatch silently breaks every player.

### II. Server-Authoritative Game State

The client is untrusted input. The server MUST be the single source of truth for:

- Currency (`xu`, `luong`), experience, levels, inventory, equipment, and owned characters.
- Fight outcomes: turn order, bullet trajectories, damage, and win/loss resolution.
- Room membership, matchmaking, and admin privileges.

Any client-supplied value that affects these MUST be validated (range, ownership, timing)
before being applied. Admin-only actions MUST check privilege server-side on every request.

Rationale: trusting the client enables cheating and economy corruption that cannot be undone.

### III. Persistent Data Integrity

- Every change that touches more than one balance or item (purchases, trades, rewards) MUST
  execute atomically (single transaction); partial application is a defect.
- Player state MUST be persisted on logout, disconnect, and server shutdown; an abrupt
  disconnect MUST NOT lose committed progress.
- Database queries MUST use parameterized statements; string-concatenated SQL is forbidden.
- Schema changes MUST be delivered as reviewed, ordered SQL migration scripts and reflected in
  the canonical schema dump. Ad-hoc manual edits to production data are forbidden.
- Any destructive data operation (drop, truncate, bulk update) MUST be preceded by a backup.

Rationale: player accounts and purchased currency are the project's most valuable, least
recoverable asset.

### IV. Secrets & Configuration Hygiene

- Credentials (DB passwords, API keys, admin secrets) MUST NOT appear in source code,
  committed config files, logs, or SQL dumps. They MUST be supplied via environment variables
  or an untracked `.env` file, with a committed `.env.example` documenting required keys.
- Player passwords MUST be stored as salted, slow hashes (e.g. bcrypt/argon2); plaintext or
  reversible storage is forbidden for new code, and migrating existing rows is required work.
- Gameplay tunables (room counts, limits, map IDs) belong in configuration, not hard-coded
  constants, and every key MUST have a documented default.

Rationale: a leaked DB credential or password table compromises every account at once.

### V. Verifiable Changes & Observability

- New game logic (damage, rewards, economy, matchmaking) MUST be written as units testable
  without a live socket or database, and MUST ship with automated tests.
- Protocol handlers SHOULD be covered by tests that replay captured or hand-built packets and
  assert the encoded response.
- A change is not complete until the server builds via Maven and is exercised against a real
  client (or a scripted client) for the affected flow.
- The server MUST log session lifecycle (connect, login, disconnect), errors with stack traces,
  and economy-affecting actions with player ID, before/after values. Debug-level logging MUST
  be switchable via config and off by default in production.

Rationale: this is a reverse-engineered system; without tests and logs, regressions are
invisible until players report them.

### VI. Single Canonical Server Codebase

- Exactly one server module is canonical: `mobiarmy-java/`. New features and fixes MUST land
  there.
- Other server trees (`archive/mobiarmy/`, `archive/src/`) are reference-only; code MAY be ported from
  them but they MUST NOT receive new development. They SHOULD be removed once
  their useful logic is migrated.
- Decompiled client artifacts (`Army165local/`, `Client/`) are reference material and MUST NOT
  be treated as server source.
- Start simple (YAGNI): no new frameworks, services, or abstraction layers without a spec that
  justifies them in the plan's complexity section.

Rationale: parallel, divergent server implementations double the maintenance cost and make it
unclear which behavior is correct.

## Technical Constraints

- Language/runtime: Java 17; build with Maven (`mobiarmy-java/pom.xml`). Dependencies MUST be
  declared in the POM with pinned versions; no committed loose JARs for libraries.
- Persistence: MySQL is the production database; SQLite is permitted only for local
  development and tests. Code MUST access storage through a single data-access layer so both
  backends behave identically.
- Networking: raw TCP socket server compatible with the MobiArmy client protocol. Each client
  session MUST be isolated so one failing session cannot block or kill others; shared state
  (rooms, fights, online users) MUST be thread-safe.
- Capacity targets are set by config (`max-clients`, `max-player`, room counts); the server
  MUST enforce them and degrade gracefully (reject with a client-visible message) when full.
- Text MUST be handled as UTF-8 end to end (source, DB, wire, where the client supports it),
  since player-facing strings are Vietnamese.

## Development Workflow & Quality Gates

- Work follows the Spec Kit flow: `/speckit-specify` → `/speckit-plan` → `/speckit-tasks` →
  `/speckit-implement`. Plans MUST include a Constitution Check against every principle above.
- Before merge, each change MUST pass these gates:
  1. `mvn package` succeeds for the canonical module.
  2. Automated tests pass; new logic includes tests (Principle V).
  3. Protocol changes are cross-checked against client source (Principle I).
  4. No secrets in the diff (Principle IV).
  5. Schema changes include a migration script (Principle III).
- Generated logs (`*.log`), build output (`target/`), local databases, and player data files
  MUST NOT be committed.
- Any deviation from a principle MUST be recorded in the plan's Complexity Tracking table with
  justification and the simpler alternative that was rejected.

## Governance

- This constitution supersedes conflicting conventions, comments, or legacy code patterns.
  Where legacy code violates it, new or modified code MUST comply; untouched legacy code is
  remediated through planned work, not silently ignored.
- Amendments are made via `/speckit-constitution`, with a Sync Impact Report describing the
  change, and MUST be approved by the project maintainer before taking effect.
- Versioning follows semantic versioning:
  - MAJOR: removal or incompatible redefinition of a principle or governance rule.
  - MINOR: new principle or section, or materially expanded guidance.
  - PATCH: clarifications and wording fixes with no change in meaning.
- Compliance is reviewed at plan time (Constitution Check), at task generation, and during code
  review. Reviewers MUST reject changes that violate a NON-NEGOTIABLE principle.

**Version**: 1.0.1 | **Ratified**: 2026-11-21 | **Last Amended**: 2026-11-21
