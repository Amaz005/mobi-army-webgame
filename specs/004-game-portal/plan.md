# Implementation Plan: Web Game Portal

**Branch**: `004-game-portal` | **Date**: 2026-11-22 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/004-game-portal/spec.md`

## Summary

Put a game portal at the site root: a static home page that renders game cards from an
operator-edited `portal/games.json`, one shared game page per game at `/<id>/` (tagline, players,
"Cách chơi", "Trò chơi khác"; structure modelled on dautri.com, research D12), and MobiArmy (the
existing 002 web client, moved to `/mobiarmy/client/` and framed by the play page `/mobiarmy/choi/`, research D14) as the only game at launch. Add one site-wide account: a small JSON API inside the
canonical game server (JDK built-in HTTP server, internal port, proxied by nginx at `/api/`)
handles registration, login, logout and password change against the same `account` table the game
uses, plus read-only live info (online count from the server's in-memory clients, top-10 ranking).
Passwords move from plaintext to salted PBKDF2 hashes for every account, via a migration script and
a one-shot hashing tool run after a backup. The game protocol and client bytecode are unchanged.

## Technical Context

**Language/Version**: Java 17 (server); plain HTML/CSS/JS for the portal page (no build step)

**Primary Dependencies**: none new. `com.sun.net.httpserver` (JDK `jdk.httpserver`), `javax.crypto`
PBKDF2, Jackson (already a dependency) for JSON; nginx 1.27 (existing)

**Storage**: PostgreSQL `account` table (existing; column widened + unique index); sessions and
throttles in server memory; catalog in a static JSON file

**Testing**: JUnit 5 in `mobiarmy-java/src/test/java` (pure units + API handlers exercised
in-process on an ephemeral port); manual/curl checks in quickstart.md; 002 regression checks

**Target Platform**: Docker Compose stack (Linux containers); browsers per 002 FR-009

**Project Type**: web service + static web front end added to an existing game server stack

**Performance Goals**: portal cards < 3 s first visit (SC-002); online count ≤ 60 s stale (SC-004);
login ≤ ~0.5 s server CPU (PBKDF2 600k)

**Constraints**: no change to the MobiArmy wire protocol or client bytecode (constitution I); nothing
loaded from outside sites (FR-012); web and server failures independent (research D10); Vietnamese
UTF-8 text

**Scale/Scope**: tens to low hundreds of concurrent players; 1 game at launch; 7 API endpoints;
4 portal views (home, game page, login/register, account)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Check | Status |
|-----------|-------|--------|
| I. Client protocol compatibility | No message format, command ID or resource changes. `User.login` changes only how the password is compared; replies unchanged. Client bytecode untouched; only the 002 page shell gets a link. No resource-version bump needed (no `res/`/`data/` change). | PASS |
| II. Server-authoritative state | Portal can only create accounts with schema defaults (FR-020) and change the caller's own password after checking the current one. Online count and ranking come from server state; read-only. No admin action over HTTP. | PASS |
| III. Persistent data integrity | Parameterized SQL only. Schema change = ordered migration `sql/migrations/004-portal-accounts.sql` + updated `dbarmy.sql`. Bulk re-hash and duplicate rename run only after a documented `pg_dump` (quickstart §1), in one transaction. Unique index closes the registration race. | PASS |
| IV. Secrets & config hygiene | Plaintext passwords migrated to salted slow hashes (required work, done here). Password removed from the existing login log line. Tokens never logged. New tunables (`portal-port`, `portal-session-idle-minutes`, `portal-secure-cookie`, `WEB_API_RATE`) documented with defaults. No secrets added to files. | PASS |
| V. Verifiable changes & observability | `PasswordHash`, validation, throttle, sessions, ranking are pure units with tests; API handlers tested in-process. Registration, password change and lockouts are logged (no secrets). Verified with the real web client + a phone client per quickstart. | PASS |
| VI. Single canonical codebase / YAGNI | All server code in `mobiarmy-java/`. No new service, framework or dependency; the HTTP server is part of the JDK. The portal page is static files. | PASS — see note |

Note on VI: an HTTP listener in the game server is a new *interface* of the existing codebase, not
a new service; the alternative (a separate backend) would duplicate account logic and is rejected in
research D1.

**Post-design re-check (after Phase 1)**: unchanged — all PASS. The design adds no table, no
service, no dependency. Same-origin storage between games is a recorded limitation, not a
violation (research D7).

## Project Structure

### Documentation (this feature)

```text
specs/004-game-portal/
├── plan.md              # This file
├── research.md          # Phase 0: decisions D1–D12
├── data-model.md        # Phase 1
├── quickstart.md        # Phase 1: validation guide
├── contracts/
│   ├── portal-api.md    # /api/* JSON endpoints
│   ├── game-catalog.md  # portal/games.json format
│   └── web-endpoints.md # URL layout (updates 002's contract)
├── checklists/requirements.md
└── tasks.md             # Phase 2 (/speckit-tasks — not created here)
```

### Source Code (repository root)

```text
mobiarmy-java/
├── army2.conf                                  # + portal-port, portal-session-idle-minutes, portal-secure-cookie
├── dbarmy.sql                                  # password VARCHAR(255), unique index, a→a2 for id 2, hashed seeds
├── sql/migrations/004-portal-accounts.sql      # NEW: widen, rename duplicates, unique index
└── src/
    ├── main/java/com/mobiarmy/
    │   ├── server/
    │   │   ├── PasswordHash.java               # NEW: PBKDF2 hash/verify, legacy detection
    │   │   ├── HashPasswords.java              # NEW: one-shot migration tool (main)
    │   │   ├── Registration.java               # hash on create; portal rules; unique-violation = taken
    │   │   ├── User.java                       # login: fetch by username, verify hash, re-hash legacy
    │   │   ├── ClientEntry.java                # drop password from log line
    │   │   ├── GameServer.java / ServerInit    # start the portal API; startup warning for unhashed rows
    │   │   └── ServerManager.java              # read new config keys; online-count helper
    │   └── portal/                             # NEW package
    │       ├── PortalServer.java               # HttpServer, routing, JSON, Origin/Content-Type checks
    │       ├── PortalSessions.java             # in-memory sessions + expiry
    │       ├── LoginThrottle.java              # per-name lockout, per-IP registration cap
    │       └── Ranking.java                    # top-10 query + 60 s cache
    └── test/java/com/mobiarmy/
        ├── server/PasswordHashTest.java, RegistrationTest.java (extended)
        └── portal/PortalSessionsTest.java, LoginThrottleTest.java, RankingTest.java, PortalServerTest.java

portal/                                         # NEW: static portal, bind-mounted read-only into web
├── index.html, portal.css, portal.js           # home, login/register, account views; Vietnamese
├── game.html                                   # shared game page for every /<id>/ (FR-001a)
├── games.json                                  # catalog: MobiArmy only
└── games/mobiarmy/card.png                     # card picture (from Army165local/ art, copied at build or committed)

web-client/src/main/webapp/index.html           # + "← Cổng game" link (page shell only)
web-client/Dockerfile                           # client output copied to /usr/share/nginx/html/mobiarmy/client/
docker/web/nginx.local.conf.template            # / → portal, /<id>/ → game.html, /<id>/choi/ → play.html, /mobiarmy/client/, redirects, /api/ proxy + limits
docker/web/nginx.public.conf.template           # same, public mode
docker-compose.yml                              # mount ./portal; WEB_API_RATE; PORTAL_SECURE_COOKIE to server
.env.example, README.md, CLAUDE.md              # new keys, URLs, accounts, migration steps
```

**Structure Decision**: Extend the existing stack in place. Server-side portal code goes into a new
`com.mobiarmy.portal` package in the canonical module; shared account logic (`PasswordHash`,
`Registration`) stays in `server/` so the game and the portal use one implementation. The portal
front end is a new top-level `portal/` directory of static files served by the existing nginx.

## Complexity Tracking

No constitution violations to justify.
