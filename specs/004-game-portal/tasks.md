---

description: "Task list for 004-game-portal"
---

# Tasks: Web Game Portal

**Input**: Design documents from `specs/004-game-portal/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**Tests**: Included. Constitution V requires automated tests for new server logic (password
hashing, validation, sessions, throttling, ranking, API handlers). Portal pages and nginx are
verified with the quickstart.md checks.

**Organization**: Tasks are grouped by user story (spec.md US1–US4) so each can be built and
tested on its own.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies on incomplete tasks)
- **[Story]**: US1 portal + game page + MobiArmy move, US2 one account, US3 catalog
  management, US4 live info
- Paths are from the repository root. Server code is `mobiarmy-java/src/main/java/com/mobiarmy/`
  (abbreviated `…/` below), tests `mobiarmy-java/src/test/java/com/mobiarmy/` (`…test/`).

Build and test in Docker (no local JDK needed):
`docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/mobiarmy-java maven:3.9-eclipse-temurin-17 mvn -B test`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Confirm the runtime assumption and add configuration.

- [X] T001 Verify that `jdk.httpserver` is in the server's runtime image: `docker run --rm eclipse-temurin:17-jre java --list-modules | grep jdk.httpserver`. Record the result under research.md › D1 "To verify" in `specs/004-game-portal/research.md`. If it is missing, stop and revisit D1 before Phase 2.
- [X] T002 [P] Add `portal-port: 8081`, `portal-session-idle-minutes: 30`, `portal-secure-cookie: false` with Vietnamese comments to `mobiarmy-java/army2.conf`. Read them in `…/server/ServerManager.java` with the existing `configInt`/`configBool` helpers, and let env `PORTAL_SECURE_COOKIE` override `portal-secure-cookie` the way `DB_PASSWORD` overrides `db-password` (data-model.md › Configuration)
- [X] T003 [P] Create the static portal skeleton: empty `portal/index.html`, `portal/game.html`, `portal/portal.css`, `portal/portal.js` and `portal/games.json` (`[]`). Add `portal/games/mobiarmy/card.png`, copied from an image in `Army165local/` (the source stays untouched) and scaled with integer nearest-neighbour steps.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: The portal API listener and its nginx route, used by US2 and US4.

**⚠️ CRITICAL**: US2 and US4 can't start until this phase is done. US1 and US3 only need Phase 1.

- [X] T004 Write `…test/portal/PortalServerTest.java`. It starts `PortalServer` on an ephemeral port in-process and checks the generic rules in contracts/portal-api.md:
  - unknown path → `404 {"error":"not_found"}`
  - wrong method → `405`
  - POST without `Content-Type: application/json`, or with a mismatched `Origin` → `403 {"error":"forbidden"}`
  - malformed JSON or a body over 4 KB → `400 {"error":"bad_request"}`, with no stack trace in the body

  The test must fail before T005.
- [X] T005 Implement `…/portal/PortalServer.java`:
  - `com.sun.net.httpserver.HttpServer` on `portal-port`, with a small executor
  - a route table and JSON read/write via the existing Jackson dependency
  - UTF-8 everywhere
  - the Origin/Content-Type check, the 4 KB body limit and the error body shape from contracts/portal-api.md
  - visitor IP taken from the `X-Real-IP` header

  T004 must pass.
- [X] T006 Start `PortalServer` after DB init in `…/server/GameServer.java`, and stop it on shutdown. Log `Portal API started on port <n>`. If the listener fails, log the error but keep the game server running (spec: phone clients unaffected).
- [X] T007 [P] Add the `/api/` route to `docker/web/nginx.local.conf.template` and `docker/web/nginx.public.conf.template`:
  - `resolver 127.0.0.11 valid=10s;` with `set $portal_api http://server:8081; proxy_pass $portal_api;` (research.md › D10)
  - `limit_req_zone … zone=portal_api rate=${WEB_API_RATE}`, applied with `burst=10 nodelay` and `limit_req_status 429`
  - `proxy_set_header X-Real-IP $remote_addr` and `Host`
  - a JSON 502/504 error body
- [X] T008 [P] In `docker-compose.yml`: pass `WEB_API_RATE: ${WEB_API_RATE:-30r/m}` to `web`; pass `PORTAL_SECURE_COOKIE` to `server` (`true` when `WEB_MODE=public`, documented); add `expose: ["8081"]` without publishing the port. Document `WEB_API_RATE` and `PORTAL_SECURE_COOKIE` in `.env.example`.

**Checkpoint**: `curl http://localhost:8080/api/nope` → `404 {"error":"not_found"}`. With the server stopped, `/api/nope` → 502 and nginx keeps running.

---

## Phase 3: User Story 1 - Pick a game from the portal and play it (Priority: P1) 🎯 MVP

**Goal**: `/` shows the portal, `/mobiarmy/` the MobiArmy game page, and `/mobiarmy/choi/` the unchanged 002 client. Old links redirect.

**Independent Test**: quickstart.md §2. In a fresh profile, go `/` → card → `/mobiarmy/` → "Chơi ngay" → log in `a`/`1` → lobby, then "← Cổng game" back to `/`. Old `/index.html` redirects. All 002 checks still pass (SC-005).

- [X] T009 [US1] In `web-client/Dockerfile`, copy the TeaVM output to `/usr/share/nginx/html/mobiarmy/choi/` instead of the html root. Keep the FR-006 off-site check.
- [X] T010 [P] [US1] Add a small "← Cổng game" link to `/` in `web-client/src/main/webapp/index.html` and style it in `web-client/src/main/webapp/play.css`. Neither the canvas size nor the keypad may change, and the game bytecode stays untouched.
- [X] T011 [US1] In `docker-compose.yml`, mount `./portal` read-only at `/usr/share/nginx/portal` in the `web` service.
- [X] T012 [US1] Rewrite the static locations in both `docker/web/nginx.local.conf.template` and `docker/web/nginx.public.conf.template` to match contracts/web-endpoints.md:
  - `/` → `index.html`, and `portal.css`, `portal.js`, `game.html`, `games.json` from the portal root, all with `Cache-Control: no-cache`
  - `/games/<id>/…` from the portal root
  - `^/[a-z0-9-]+/$` → `game.html`, except reserved `api`, `game`, `games`; `^/[a-z0-9-]+$` → 301 to the same path with a trailing slash
  - `/mobiarmy/choi/` and its allowlisted files from the client root
  - icons served at `/` as well
  - 301s from `/index.html`, `/client.js`, `/play.css`, `/keypad.js`, `/manifest.webmanifest` to `/mobiarmy/choi/<same>`
  - the `/game` WebSocket unchanged, and everything else 404
- [X] T013 [US1] Write the home page `portal/index.html` (lang `vi`, title "Cổng game", links to `portal.css`/`portal.js`, a cards container, a status line), plus mobile-first CSS in `portal/portal.css`. The CSS must work at phone width with no horizontal scroll and load nothing from other sites (FR-012, FR-013).
- [X] T014 [US1] Fill `portal/games.json` with the MobiArmy entry from contracts/game-catalog.md (`playUrl` `/mobiarmy/choi/`, `live` `mobiarmy`). It must follow data-model.md › Game catalog entry.id, .name, .description, .playUrl, .status.
- [X] T015 [US1] Write the home-page code in `portal/portal.js`:
  - fetch `/games.json`
  - render one card per `visible` entry, in `order` then `name`, linking to `/<id>/`, using only `textContent`
  - redirect to `/mobiarmy/choi/` when `matchMedia('(display-mode: standalone)')` matches (research.md › D7)
- [X] T016 [US1] Write the game page `portal/game.html`. Its code goes in the shared `portal/portal.js`, one function per page. It reads the id from `location.pathname`, finds the entry and renders:
  - name, tagline, players, image
  - "Chơi ngay" → `playUrl`
  - each `howToPlay` section
  - "Trò chơi khác" (the other visible entries)
  - `document.title` = "Chơi <name> online miễn phí"

  Unknown id → "Không tìm thấy trò chơi" with a link home (contracts/game-catalog.md; data-model.md › Game catalog entry.tagline, .players, .howToPlay).
- [ ] T017 [US1] Rebuild with `docker compose up -d --build web` and run quickstart.md §2 steps 1–4. Re-run `specs/002-web-client/quickstart.md` from `/mobiarmy/choi/`, and record the results in `specs/004-game-portal/quickstart.md` (SC-001, SC-002, SC-005).

**Checkpoint**: The portal and MobiArmy are playable through the new URLs, with no account or API features yet.

---

## Phase 4: User Story 2 - One portal account for every game (Priority: P1)

**Goal**: Register or log in on the portal; the same credentials work in MobiArmy. All passwords are hashed.

**Independent Test**: quickstart.md §1 and §3. Register `testuser1`/`abc123` on the portal, then log in to MobiArmy with it. An old account logs in to the portal. A password change applies to both. Login failures give one generic message. Rate limits hold. No secrets appear in responses or logs (SC-006–SC-009).

### Tests for User Story 2 ⚠️ (write first, must fail)

- [X] T018 [P] [US2] Write `…test/server/PasswordHashTest.java`:
  - hash → verify true; wrong password → false
  - two hashes of the same password differ (salt)
  - the format matches data-model.md › Password hash (`pbkdf2_sha256$600000$…$…`, length ≤ 255 per data-model.md › Account.password)
  - legacy detection is true for a non-prefixed value; a tampered hash → false
  - iterations are parsed from the stored value
- [X] T019 [P] [US2] Extend `…test/server/RegistrationTest.java` with the portal rules (data-model.md › Account.username, Account.password):
  - accepts `testuser1`/`abc123`; rejects `ab`, a 21-character name, `Test_1`, and a 5-character or non-alphanumeric password
  - normalises `" TestUser1 "` → `testuser1`
  - the in-game rules stay unchanged
  - `create` stores a hash, never the plain password
- [X] T020 [P] [US2] Write `…test/portal/PortalSessionsTest.java` with an injectable clock:
  - create → lookup → logout
  - idle expiry after `portal-session-idle-minutes`, and the 7-day absolute expiry
  - "end other sessions of account" keeps the current one
  - the token is 32 random bytes, base64url (data-model.md › Portal session.token, .lastUsedAt)
- [X] T021 [P] [US2] Write `…test/portal/LoginThrottleTest.java` with an injectable clock:
  - 5 failures in 15 min lock that name for 15 min, other names stay unaffected, and the lock expires
  - the 6th registration from one IP within the hour is refused (data-model.md › Login throttle.login name, .visitor IP)
- [X] T022 [US2] Extend `…test/portal/PortalServerTest.java` for the account endpoints in contracts/portal-api.md, using a test DB (SQLite via `SqlDumpLoader`, as in the existing tests). Check:
  - register `201` sets the cookie (`HttpOnly; SameSite=Strict; Path=/api`); a duplicate → `409 username_taken`; an invalid name or password → `400` with the documented message
  - login, `/api/me`, logout and `/api/me` → `401`
  - a wrong password and an unknown name give identical status and body
  - password change: a wrong current password → `401 bad_credentials`; the new password works and the old one fails in `User` login as well
  - legacy plaintext login re-hashes the row
  - no response body contains `password`, a hash, or a token outside `Set-Cookie`

### Implementation for User Story 2

- [X] T023 [P] [US2] Implement `…/server/PasswordHash.java` per data-model.md › Password hash and research.md › D2: `hash`, `verify` (constant-time `MessageDigest.isEqual`) and `isLegacy`. Use `PBKDF2WithHmacSHA256`, 600 000 iterations, a 16-byte `SecureRandom` salt and a 32-byte key. T018 must pass.
- [X] T024 [P] [US2] Write the ordered migration `mobiarmy-java/sql/migrations/004-portal-accounts.sql` for PostgreSQL (research.md › D4; data-model.md › Account.username, Account.password):
  - widen `account.password` to `VARCHAR(255)`
  - rename duplicate usernames, keeping the lowest id: `substr(username, 1, 20 - length(CAST(id AS VARCHAR))) || CAST(id AS VARCHAR)`
  - `CREATE UNIQUE INDEX account_username_uq ON account (username)`
  - all of it in one transaction, with a header comment that requires a `pg_dump` backup first
- [X] T025 [US2] Update `…/server/Registration.java`:
  - `create` hashes the password with `PasswordHash`
  - add `isValidPortalUsername`/`isValidPortalPassword` per data-model.md › Account.username, Account.password (portal-only minimums `3` and `6` from research.md › D5; the in-game rules stay as they are)
  - treat a unique-constraint violation on insert as "taken" (returns false)

  T019 must pass.
- [X] T026 [US2] Change `User.login` in `…/server/User.java`:
  - select by `username` only, ordered by `id`
  - verify with `PasswordHash.verify`, or with an exact compare when `isLegacy`
  - on a successful legacy login, `UPDATE account SET password=? WHERE id=?` with a new hash
  - keep every reply message byte-identical (constitution I)

  Extract the credential check into a package-private static `checkPassword(stored, typed)` so the portal reuses it.
- [X] T027 [US2] Remove the plaintext password from the login log line in `…/server/ClientEntry.java`; keep the client id, name and version (research.md › D11).
- [X] T028 [US2] Implement the one-shot tool `…/server/HashPasswords.java` (`main`):
  - load config and env like `ServerInit`
  - in one transaction, hash every row where `PasswordHash.isLegacy(password)` is true
  - print only `hashed N accounts`

  In `ServerInit`, log at startup `WARNING: N accounts still have plaintext passwords; run HashPasswords` when N > 0 (research.md › D3).
- [X] T029 [US2] Update `mobiarmy-java/dbarmy.sql` (data-model.md › Account.username, Account.password):
  - `password VARCHAR(255)`
  - the unique index
  - seed id 2 renamed to `a2`
  - the three seed passwords (`1`, `1`, `3`) replaced with PBKDF2 hashes generated with `PasswordHash`

  The file must still load into SQLite (portable SQL, no backticks).
- [X] T030 [P] [US2] Implement `…/portal/PortalSessions.java` (in-memory, thread-safe) per data-model.md › Portal session.token, .accountId, .lastUsedAt. Include an injectable clock. T020 must pass.
- [X] T031 [P] [US2] Implement `…/portal/LoginThrottle.java` (in-memory, thread-safe) per data-model.md › Login throttle.login name, .visitor IP, with an injectable clock. T021 must pass.
- [X] T032 [US2] Add the account routes to `…/portal/PortalServer.java` (they may live in a new `…/portal/AccountApi.java`): `POST /api/register`, `POST /api/login`, `POST /api/logout`, `GET /api/me`, `POST /api/password`, exactly as in contracts/portal-api.md.
  - Use `Registration` and `checkPassword`, and re-hash legacy passwords on login.
  - The session cookie gets `Secure` when `portal-secure-cookie` is on. A password change ends the account's other sessions.
  - Log registrations (account id and name), password changes (account id) and lockouts (name and IP). Never log passwords or tokens.

  T022 must pass.
- [X] T033 [US2] Add the account UI to `portal/index.html` and `portal/portal.js`:
  - header showing either "Đăng nhập · Đăng ký" or the logged-in `username` + "Đăng xuất" (via `GET /api/me`)
  - login and register forms that show the API's Vietnamese messages and state the name/password rules
  - an "Đổi mật khẩu" form (current + new)
  - "Quên mật khẩu? Liên hệ quản trị viên." under login
  - a note that MobiArmy uses the same name and password
  - requests made with `Content-Type: application/json` and `credentials: 'same-origin'`

  Style the forms in `portal/portal.css`.
- [ ] T034 [US2] Run quickstart.md §1 on a copy of an existing stack and §3 on a fresh stack. Confirm SC-006 to SC-009, and that a phone/J2ME client still logs in with `nguoiduatin`/`3`.

**Checkpoint**: US1 + US2 together form the MVP. New players can register and play.

---

## Phase 5: User Story 3 - Add a new game to the catalog (Priority: P2)

**Goal**: Games are added, hidden or put under maintenance by editing `portal/games.json`, and a broken entry never breaks the page.

**Independent Test**: quickstart.md §4 with a placeholder `demo` game.

- [X] T035 [US3] Validate catalog entries in `portal/portal.js` per contracts/game-catalog.md and data-model.md › Game catalog entry.id, .name, .description, .playUrl, .status:
  - skip entries with a missing or invalid required field, an id matching `api`/`game`/`games`, or a `playUrl` that is not a same-site path (`console.warn`)
  - ignore unknown fields
  - a failed or unparseable `games.json` → "Chưa có trò chơi" + a short note
  - a missing or failing image → a CSS placeholder
- [X] T036 [US3] Handle `status` in `portal/portal.js`:
  - `maintenance`: the card and game page show "Đang bảo trì" with no "Chơi ngay"
  - `hidden`: no card, and the game page shows "Không tìm thấy trò chơi"
  - "Trò chơi khác" lists only visible entries
- [X] T037 [US3] Document "Thêm trò chơi mới" in `README.md`: files go in `portal/games/<id>/`, add the entry, set the status, run `docker compose restart web`. Link contracts/game-catalog.md.
- [ ] T038 [US3] Run quickstart.md §4 with a throwaway `portal/games/demo/`, measure SC-003 (< 15 min), then remove the demo files and entry so nothing extra gets committed.

**Checkpoint**: The catalog can be operated without touching portal code.

---

## Phase 6: User Story 4 - See live MobiArmy information on the portal (Priority: P3)

**Goal**: The online count shows on the MobiArmy card, the top 10 ranking on `/mobiarmy/`, and an offline state when the server is down.

**Independent Test**: quickstart.md §5.

- [X] T039 [P] [US4] Write `…test/portal/RankingTest.java` against a test DB seeded with accounts of known exp, including a superadmin:
  - top 10 ordered by `exp DESC, id ASC`, superadmins excluded
  - `level` from `User.levelFromExpValue`
  - entries carry only `rank`, `name`, `level`, `exp` (data-model.md › Ranking entry.rank, .name, .level, .exp)
  - the cache holds for 60 s (injectable clock)

  The test must fail before T040.
- [X] T040 [US4] Implement `…/portal/Ranking.java`: the query from data-model.md › Ranking entry (parameterless, read-only), plus a 60 s cache. T039 must pass.
- [X] T041 [US4] Add `ServerManager.onlineCount()` to `…/server/ServerManager.java`: the count of `clients` with `login == true` (data-model.md › Live game status.online). Then add `GET /api/mobiarmy/status` and `GET /api/mobiarmy/ranking` to `…/portal/PortalServer.java` per contracts/portal-api.md, and cover both, including `takenAt`, in `…test/portal/PortalServerTest.java`.
- [X] T042 [P] [US4] Cache `/api/mobiarmy/status` for 10 s in both nginx templates (`proxy_cache_path` + `proxy_cache_valid 200 10s`), in `docker/web/nginx.local.conf.template` and `docker/web/nginx.public.conf.template`.
- [X] T043 [US4] Add live info to `portal/portal.js` and `portal/portal.css`:
  - for entries with `live: "mobiarmy"`, show "N người đang chơi" on the card and game page
  - refresh every 30 s while `document.visibilityState === 'visible'`
  - on 502/504 or a network error, show "Máy chủ tạm dừng" with no number
  - cards render before the live data arrives
  - the `/mobiarmy/` game page shows a "Bảng xếp hạng" table (hạng, tên, cấp, kinh nghiệm)
- [X] T044 [US4] Run quickstart.md §5 (SC-004, offline state).

**Checkpoint**: All four stories work.

---

## Phase 7: Polish & Cross-Cutting Concerns

- [X] T045 [P] Update `README.md`:
  - new URLs (`/`, `/mobiarmy/`, `/mobiarmy/choi/`)
  - seeded accounts `a`/`1`, `a2`/`1`, `nguoiduatin`/`3`
  - portal registration and the name/password rules
  - the upgrade steps for existing stacks (backup → migration → `HashPasswords`, from quickstart.md §1)
  - `WEB_API_RATE` and `PORTAL_SECURE_COOKIE`
- [X] T046 [P] Update `CLAUDE.md`:
  - a `portal/` row in Repository Layout, and the `portal/` package in the source list
  - the portal API on internal port 8081
  - passwords are PBKDF2 hashes (`PasswordHash`), and new web paths must be added to the nginx allowlist in both templates
  - the persisted fields now include `password` writes
- [X] T047 [P] Add a note at the top of `specs/002-web-client/contracts/web-endpoints.md` that 004's contracts/web-endpoints.md supersedes its URL layout.
- [X] T048 Check that the portal loads nothing from another site: `grep -nE "(src|href)=[\"']?https?://|url\(['\"]?https?://" portal/*.html portal/*.css portal/*.js` must find nothing (FR-012). Add the same grep to `docker/web/10-check-public.sh` so the web container refuses to start in public mode if it finds a match.
- [ ] T049 Run the full server test suite (`mvn -B test` in the Maven image) and `docker compose up -d --build`, then go through quickstart.md end to end in local mode, and the redirects and `Secure` cookie in public mode. Run `/security-review` on the branch, and confirm `docker compose logs server` contains no password or token values.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: none. T001 gates Phase 2.
- **Foundational (Phase 2)**: depends on T001 and T002. It blocks US2 and US4.
- **US1 (Phase 3)**: depends on T003 only. It can run in parallel with Phase 2.
- **US2 (Phase 4)**: depends on Phase 2. Its UI tasks (T033) also need T013/T015 from US1.
- **US3 (Phase 5)**: depends on US1 (T015, T016).
- **US4 (Phase 6)**: depends on Phase 2. Its UI task T043 needs T015/T016.
- **Polish (Phase 7)**: after the stories you ship.

### Within US2

T018–T022 (tests) → T023, T024 → T025 → T026 → T027, T028 → T029 (needs `PasswordHash` to
generate seed hashes) → T030, T031 → T032 → T033 → T034.

### Shared files (no [P] between them)

- `portal/portal.js`: T015 → T016 → T033 → T035 → T036 → T043
- nginx templates: T007 → T012 → T042
- `docker-compose.yml`: T008 → T011
- `…/portal/PortalServer.java`: T005 → T032 → T041

### Parallel Opportunities

- Phase 1: T002 ∥ T003 (after T001, or alongside it)
- Phase 2: T007 ∥ T008 while T004/T005 are in progress
- US1 ∥ Phase 2 (different files, except that `docker-compose.yml` T008 must come before T011)
- US2 tests T018 ∥ T019 ∥ T020 ∥ T021; implementation T023 ∥ T024, then T030 ∥ T031
- US4: T039 ∥ T042

---

## Parallel Example: User Story 2

```bash
# Tests first, together:
Task: "PasswordHashTest in mobiarmy-java/src/test/java/com/mobiarmy/server/PasswordHashTest.java"
Task: "Portal rules in mobiarmy-java/src/test/java/com/mobiarmy/server/RegistrationTest.java"
Task: "PortalSessionsTest in mobiarmy-java/src/test/java/com/mobiarmy/portal/PortalSessionsTest.java"
Task: "LoginThrottleTest in mobiarmy-java/src/test/java/com/mobiarmy/portal/LoginThrottleTest.java"

# Then independent units:
Task: "PasswordHash in mobiarmy-java/src/main/java/com/mobiarmy/server/PasswordHash.java"
Task: "Migration in mobiarmy-java/sql/migrations/004-portal-accounts.sql"
```

---

## Implementation Strategy

### MVP First

1. Phase 1 → Phase 3 (US1): the portal is live, and MobiArmy is reachable at its new URLs. This is shippable on its own.
2. Phase 2 → Phase 4 (US2): accounts and password hashing. This is the real MVP, because new players can sign up.
3. **STOP and VALIDATE** with quickstart.md §1–§3, and with a phone client.

### Incremental Delivery

4. US3 (catalog robustness) → validate §4.
5. US4 (live info) → validate §5.
6. Polish → docs, the off-site check, the full quickstart and a security review.

### Notes

- On an existing stack, the migration in US2 renames duplicate accounts and rewrites passwords. Take the backup first (constitution III).
- Commit after each task or logical group; never commit `before-004.dump`, `.env` or logs.

## Implementation notes (2026-11-22)

- **Open**: T017 (full 002 match, keypad and phone regression), T034 (phone/J2ME login), T038 (demo game
  catalog run), T049 (public mode + `Secure` cookie, `/security-review`). See quickstart.md › Validation log.
- **Deviations** (documents updated to match):
  - T003: no `portal/games/mobiarmy/card.png`. The card uses the build-generated `/icon-192.png`, since
    generated icons are never committed, and the catalog `image` rule became "same-site path".
  - T015: no installed-app redirect. The 002 manifest is `display: "browser"`, so old home-screen
    shortcuts open the portal (research D7, spec FR-007).
  - T023–T032: shared account code lives in a new `server/Accounts.java`, used by both `User.login` and
    `PortalServer`, instead of `User.checkPassword`. DB tests use a new `TestDb` helper (SQLite from
    `dbarmy.sql`) — analysis U1.
  - Analysis fixes: name lookup is exact then case-insensitive, and uniqueness ignores case (I1).
    Re-hash on login is best-effort, so an un-migrated DB still logs in (I2). Password changes are
    throttled per account (U2). The game page header links home (U3).
  - T007/T042: `/api/mobiarmy/status` has no per-IP limit (it is cached for 10 s and many players can share
    one IP); every other `/api/` path is limited by `WEB_API_RATE`.
