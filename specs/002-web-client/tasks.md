---

description: "Task list for Play the Existing Client in a Web Browser"
---

# Tasks: Play the Existing Client in a Web Browser

**Input**: Design documents from `specs/002-web-client/`

**Prerequisites**: [plan.md](plan.md), [spec.md](spec.md), [research.md](research.md),
[data-model.md](data-model.md), [contracts/web-endpoints.md](contracts/web-endpoints.md),
[contracts/key-mapping.md](contracts/key-mapping.md), [quickstart.md](quickstart.md)

**Tests**: The spec doesn't request tests, but the plan (Technical Context › Testing, required by
constitution V) asks for JUnit tests of the web layer's pure logic. Those are included and are
written before their implementation. Everything else is validated with the
[quickstart.md](quickstart.md) gate (G1–G4) and scenarios (W1–W16).

**Organization**: Tasks are grouped by user story. **The proof gate (FR-013) ends in T027**: if
it fails, stop, switch to the streamed fallback (research.md R8), and regenerate tasks.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: US1–US4 from spec.md
- Paths are relative to the repository root; `MIDP/` below means
  `web-client/src/main/java/javax/microedition/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Module skeleton and settings

- [X] T001 Create `web-client/pom.xml` per plan › Source Code: Java 8 source/target, TeaVM Maven plugin + classlib (pin the latest 0.10.x and record it), JavaScript target with main class `web.WebMain`; `build-helper-maven-plugin` adding `../Client/SOURCE_ARMYI/SOURCE_ARMYI/src/main/java` as a source root (read-only; never edit it); `maven-resources-plugin` copying every non-`.class` file from `../Army165local/` (excluding `META-INF/`) into the bundle's `res/` (data-model.md › Client bundle `res/**`); JUnit 5 for `src/test/java`. The client POM's MicroEmulator dependencies must NOT be added; the MIDP web layer replaces them
- [X] T002 [P] Add `docker/web/certs/` to `.gitignore` and create `docker/web/acme/.gitkeep` (ACME challenge dir, contracts/web-endpoints.md); confirm with `git check-ignore -v docker/web/certs/privkey.pem`
- [X] T003 [P] Extend `.env.example` with commented entries and one-line descriptions for data-model.md › Settings `WEB_MODE`, `WEB_PORT`, `WEB_DOMAIN`, `WEB_MAX_CONN_PER_IP`, `WEB_CONN_RATE` (defaults as in that table; `STREAM_SLOTS` only if the fallback is chosen)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: The client compiles to JavaScript and is served by a container (proof gate **G1**)

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Create compile-only MIDP API stubs (correct signatures, bodies may throw `UnsupportedOperationException`) for every class the client imports (research.md R3), under `MIDP/`: `midlet/MIDlet`, `lcdui/{Display,Displayable,Canvas,Graphics,Image}`, `rms/RecordStore`, `io/{Connector,Connection,StreamConnection,SocketConnection,HttpConnection,ConnectionNotFoundException}`, `media/{Manager,Player,MediaException}`, `media/control/VolumeControl`, and `wireless/messaging/{MessageConnection,TextMessage,Message}`. Derive the exact members from compile errors in the client source — **Done 2026-11-21 as real implementations, not stubs; API list taken from the ORIGINAL bytecode (javap), not the decompiled source**
- [X] T005 Create `web-client/src/main/java/web/WebMain.java`: set `coreLG.TerrainMidlet.d` to the page host and `coreLG.TerrainMidlet.c = 19149` (research.md R2a), construct `coreLG.TerrainMidlet` and call its start lifecycle through the `MIDlet` stub
- [X] T006 [P] Create `web-client/src/main/webapp/index.html` and `play.css`: one `<canvas id="screen">` sized to the game, `client.js` include, and an "unsupported browser" message shown when the required browser features (canvas 2D, WebSocket, `localStorage`) are missing (FR-009)
- [X] T007 Create `web-client/Dockerfile`: build stage `maven:3.9-eclipse-temurin-17` running `mvn -B package` (runs JUnit + TeaVM) with build context at the repo root (it needs `Client/` and `Army165local/`); fail the build if the bundle breaks the data-model.md › Client bundle validation rule (no off-site URLs); runtime stage `nginx:1.27-alpine` with the bundle in `/usr/share/nginx/html`
- [X] T008 Run proof gate **G1**: `docker build -f web-client/Dockerfile -t mobiarmy-web .` succeeds. Record any TeaVM-unsupported JDK class and how T004/later tasks cover it. **If TeaVM can't compile the client at all, jump to T027 (gate fails)**

**Checkpoint**: The client compiles to JS; real behavior comes next

---

## Phase 3: User Story 1 - Open the game in a browser and log in (Priority: P1) 🎯 MVP

**Goal**: Opening the page shows the real login screen; logging in reaches the lobby on our
server.

**Independent Test**: quickstart.md G2, G3, W1, W2, W3, W13.

### Tests for User Story 1 (write first, must fail before implementation)

- [X] T009 [P] [US1] JUnit test for the RecordStore codec in `web-client/src/test/java/web/RecordStoreCodecTest.java`: round-trips records, keeps 1-based ids, and uses the key format from data-model.md › Browser storage `mobiarmy.rms.<storeName>`
- [X] T010 [P] [US1] JUnit test for the socket byte buffer in `web-client/src/test/java/web/SocketBufferTest.java`: frames arriving in arbitrary chunks are read back in order by `read()`/`read(byte[],int,int)`; EOF after close
- [X] T011 [P] [US1] JUnit test for key translation in `web-client/src/test/java/web/KeyMapTest.java`: every row of contracts/key-mapping.md maps browser `key`/`code` → the listed MIDP code; unmapped keys return "pass through"

### Implementation for User Story 1

- [X] T012 [P] [US1] Implement `MIDP/lcdui/Graphics.java` on the HTML canvas 2D context: every method in research.md R3 (`setColor`, `drawImage` with anchors, `drawRegion` with all 8 transforms, `fillRect`, `drawRect`, `drawLine`, `fillArc`, `fillTriangle`, `fillRoundRect`, `setClip`/clip getters, `translate`/`getTranslateX/Y`, `drawRGB`)
- [X] T013 [P] [US1] Implement `MIDP/lcdui/Image.java`: `createImage(String)` from bundled `res/`, `createImage(int,int)` (mutable, offscreen canvas) + `getGraphics()`, `createImage(Image,…)` if the client uses it, `createRGBImage`, `getRGB`, `getWidth/getHeight`; images must be fully loaded before `createImage` returns (preload or TeaVM async)
- [X] T014 [US1] Make `"".getClass().getResourceAsStream("/…")` return bundled resources (proof question P2): use TeaVM's resource supplier mechanism, or preload `res/` into a map that the launcher installs. Document the choice in `web-client/README.md` (one paragraph) — **Done 2026-11-21 via TeaVM ResourceSupplier (web.build.GameResources); documented in plan.md › Proof Gate Result and CLAUDE.md instead of a separate web-client/README.md**
- [X] T015 [US1] Implement `MIDP/lcdui/{Display,Displayable,Canvas}.java`: `Display.getDisplay/setCurrent`, `Canvas.repaint/serviceRepaints` through `requestAnimationFrame`, `getWidth/getHeight` from the canvas, `setFullScreenMode`, and `keyPressed/keyReleased/keyRepeated` dispatch
- [X] T016 [US1] Implement `web-client/src/main/java/web/KeyMap.java` and wire the browser `keydown`/`keyup` events into `Canvas` per contracts/key-mapping.md (keyboard part; repeat and `preventDefault` rules), making T011 pass
- [X] T017 [P] [US1] Implement `MIDP/rms/RecordStore.java` on `localStorage` (data-model.md › Browser storage `mobiarmy.rms.<storeName>`): `openRecordStore`, `addRecord`, `setRecord`, `getRecord`, `getNumRecords`, `closeRecordStore`, `deleteRecordStore`, making T009 pass
- [X] T018 [P] [US1] Implement `MIDP/io/Connector.java` + `SocketConnection`: any `socket://…` opens a binary WebSocket to `wss|ws://<page origin>/game` and ignores the requested host (data-model.md › Player session destination rule; research.md R2b); blocking `read` suspends via TeaVM async; `openDataInputStream/openDataOutputStream` work; `http://…` throws `ConnectionNotFoundException` (research.md R2c); any other scheme also throws. Makes T010 pass
- [X] T019 [P] [US1] Implement `MIDP/midlet/MIDlet.java` lifecycle (`startApp`/`pauseApp`/`destroyApp`, `notifyDestroyed` shows a "game closed, reload to play" message), `platformRequest` → returns `false`/no-op, and SMS (`wireless/messaging`) → "not supported" (research.md R9)
- [X] T020 [P] [US1] Create `docker/relay/Dockerfile`: a Python slim image with a pinned `websockify`, running `websockify 0.0.0.0:6080 server:19149` (fixed target, contracts/web-endpoints.md › `/game` rules; data-model.md › Player session destination rule)
- [X] T021 [P] [US1] Create `docker/web/nginx.local.conf.template` (nginx image `templates/` envsubst): listen on 8080; serve the bundle with cache headers per contracts/web-endpoints.md; `location /game` WebSocket proxy to `relay:6080`; `limit_conn` with data-model.md › Settings `WEB_MAX_CONN_PER_IP` (503) and `limit_req` with data-model.md › Settings `WEB_CONN_RATE` (429), keyed by visitor IP; 404 for other paths; access log includes status for `/game`
- [X] T022 [US1] Add services `relay` and `web` to `docker-compose.yml` per data-model.md › Components: `relay` (build `docker/relay`, depends_on `server`, no ports); `web` (build context `.`, dockerfile `web-client/Dockerfile`, depends_on `relay`, port `${WEB_PORT:-8080}:8080` per data-model.md › Settings `WEB_PORT`, mounts `./docker/web/nginx.${WEB_MODE:-local}.conf.template` per data-model.md › Settings `WEB_MODE`, env for the limit variables); both `restart: unless-stopped`; `db` and `server` blocks unchanged
- [X] T023 [US1] Run proof gate **G2** and **G3** plus quickstart.md **W1**, **W2**, **W3**, **W13** (`docker compose up -d --build`, open `http://localhost:8080`). Fix and re-run until they pass, or record the failure for T027

**Checkpoint**: A player can log in from the browser (MVP)

---

## Phase 4: User Story 2 - Play a full match from the browser (Priority: P1)

**Goal**: Two browser players finish a match; experience is saved.

**Independent Test**: quickstart.md W4 (and W5 when a phone client is available) + gate G4.

### Implementation for User Story 2

- [X] T024 [P] [US2] Implement `MIDP/media/{Manager,Player,MediaException}.java` and `control/VolumeControl.java`: `.wav` through Web Audio (decoded once, starts after the first user gesture), `audio/midi` → silent player (research.md R9); `realize`/`prefetch`/`start`/`stop`/`setLoopCount`/`getControl` behave without throwing
- [ ] T025 [US2] Run quickstart.md **W4** (two browsers, one room, finish a match, exp kept). Fix rendering, input or timing bugs found during play — **Partial, 2026-11-21: server shot bug fixed (players were never set to Fighting); two browsers now play alternating turns (32 shots resolved). Finishing a match and checking saved exp needs aimed play by a human; scripted shots don't hit**
- [ ] T026 [US2] Run proof gate **G4** (first-visit load with cache disabled < 20 s; ≥ 95% of actions seen within 0.5 s across a 2-player match; method in quickstart.md) and **W5** (2 browser players + 1 phone client; mark "manual, pending" if no phone client can run) — **Partial, 2026-11-21: first frame 3.1 s (no cache) / 0.3 s (cached); client PING 42 ms through the relay. In-match latency and W5 (phone client) pending: shots blocked by the server bug; no phone client here**
- [X] T027 [US2] **Proof gate decision (FR-013)**: add a "Proof gate result" section to `specs/002-web-client/plan.md` with G1–G4 evidence and the date. **Pass** → continue to Phase 5. **Fail** (or 5 working days exceeded) → stop here, record the reason, mark later tasks as not applicable, and run `/speckit-tasks` again for the streamed fallback (research.md R8, data-model.md › Settings `STREAM_SLOTS`)

**Checkpoint**: In-browser delivery proven; US1 + US2 work

---

## Phase 5: User Story 3 - Started by the same one command (Priority: P2)

**Goal**: The web client starts and stops with the 001 commands, and its failure never takes down
the server or database.

**Independent Test**: quickstart.md W6, W7.

### Implementation for User Story 3

- [X] T028 [US3] Run quickstart.md **W6** (fresh clone, one command brings up all four services) and **W7** (deliberately break `web`, e.g. a bad template, then `docker compose up -d`: `db` and `server` stay healthy and a phone client can still connect). Fix `docker-compose.yml` dependencies if `server` waits on `web` or `relay` in any way (data-model.md › Components dependency rule)

**Checkpoint**: One command runs server, web client and database

---

## Phase 6: User Story 4 - Phone controls work with keyboard, mouse and touch (Priority: P3)

**Goal**: Every phone key is reachable from the keyboard (done in T016) and from an on-screen
keypad on touch devices.

**Independent Test**: quickstart.md W10.

### Implementation for User Story 4

- [X] T029 [P] [US4] Create `web-client/src/main/webapp/keypad.js` and its styles in `play.css`: on-screen keypad below the canvas, shown on touch devices, with every button in contracts/key-mapping.md › On-screen keypad; `touchstart`/`touchend` (and mouse for desktop testing) send the same MIDP codes through the same entry point as T016; holding a button repeats
- [ ] T030 [US4] Run quickstart.md **W10**: finish a match keyboard-only on desktop, then keypad-only on Android Chrome; the page never scrolls or zooms during play — **Partial, 2026-11-21: keyboard drove login, menus, rooms and match start; touch keypad (emulated Pixel 5) logged in and reached the lobby with taps only, page never scrolled. Full match blocked by the server shot bug; real Android device pending**

**Checkpoint**: All user stories done

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Public mode, docs, remaining checks

- [X] T031 [P] Create `docker/web/nginx.public.conf.template`: port 80 → 301 to HTTPS except `/.well-known/acme-challenge/` served from `docker/web/acme/`; port 443 with `docker/web/certs/fullchain.pem` + `privkey.pem`, `server_name ${WEB_DOMAIN}` (data-model.md › Settings `WEB_DOMAIN`); same static, `/game` and limit rules as T021. Update `docker-compose.yml` so public mode publishes 80/443 and mounts `certs/` and `acme/` read-only
- [X] T032 [P] Create `docker/web/10-check-public.sh` (copied into the nginx image's `/docker-entrypoint.d/`, LF endings): when `WEB_MODE=public`, exit non-zero with a clear message if `WEB_DOMAIN` is empty or either certificate file is missing (data-model.md › Settings `WEB_DOMAIN`). This only stops `web`, never `server`
- [X] T033 [P] Update `README.md` (FR-011): web address for local and public mode, supported browsers (FR-009), link to contracts/key-mapping.md, public-mode steps (DNS, certificate files, `WEB_MODE=public`, `WEB_DOMAIN`, ports 80/443/19149), and replace the "Adding the web client" section with the real one
- [X] T034 [P] Update `CLAUDE.md`: repository layout row for `web-client/` and `docker/web`, `docker/relay`; note that the web layer implements only the MIDP APIs the client uses and that `Client/`/`Army165local/` stay untouched
- [ ] T035 Run quickstart.md **W8** (relay can't reach anything but the game server), **W9** (per-IP limits: 503 and 429), **W11** (four browsers + unsupported-browser message), **W12** (server down → clear error → retry), **W16** (no off-site URLs in the bundle) — **Partial, 2026-11-21: W8 ✓ (all /game tricks reach only server:19149, /relay 404, text frame → 1003), W9 ✓ (5th session 503, rapid reconnects 429), W12 ✓ (status message during outage, retry works without reload), W16 ✓ (image build check). W11: only Chrome tested here; Edge, Firefox, Android pending**
- [ ] T036 Run quickstart.md **W14** and **W15** where a domain and public host are available; otherwise record them as "manual, pending" with the reason — **Manual, pending: needs a public domain, certificates and host**
- [X] T037 Mark completed tasks `[X]` in this file, note every "manual, pending" item, and leave committing to the user

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: T001 first; T002, T003 in parallel with it
- **Foundational (Phase 2)**: T004 needs T001; T005 needs T004; T006 parallel; T007 needs T001; T008 needs T004–T007
- **US1 (Phase 3)**: tests T009–T011 first (parallel); T012, T013, T017–T021 in parallel (different files); T014 needs T013; T015 needs T012; T016 needs T011 and T015; T022 needs T020, T021; T023 needs all of Phase 3
- **US2 (Phase 4)**: needs T023; T024 parallel with T025 setup; T026 needs T025; **T027 is a hard gate for everything after it**
- **US3 (Phase 5)**: needs T027 pass
- **US4 (Phase 6)**: T029 needs T016 (shared key entry point); T030 needs T029
- **Polish (Phase 7)**: T031, T032 need T022; T033, T034 can start after T027; T035, T036 need T031/T032; T037 last

### User Story Dependencies

- **US1**: only Foundational
- **US2**: needs US1's running client (a match needs a login); adds sound and the gate
- **US3**: verification of US1's compose changes; independent of US2/US4 content
- **US4**: builds on US1's key handling; independent of US2/US3

### Parallel Opportunities

- T002, T003 (with T001)
- T009, T010, T011 (tests, different files)
- T012, T013, T017, T018, T019, T020, T021 (different files)
- T031, T032, T033, T034

---

## Parallel Example: User Story 1

```bash
# Tests first, together:
Task: "RecordStoreCodecTest (T009)"   Task: "SocketBufferTest (T010)"   Task: "KeyMapTest (T011)"
# Then independent web-layer pieces and infrastructure, together:
Task: "Graphics (T012)"  Task: "Image (T013)"  Task: "RecordStore (T017)"  Task: "Connector/SocketConnection (T018)"
Task: "MIDlet + stubs (T019)"  Task: "relay Dockerfile (T020)"  Task: "nginx.local.conf.template (T021)"
# Then sequentially: T014 → T015 → T016 → T022 → T023
```

---

## Implementation Strategy

### MVP First (User Story 1)

1. Phase 1 → Phase 2 (G1: it compiles)
2. Phase 3 → **STOP and VALIDATE** with T023 (G2, G3: login from the browser)

### Gate, then incremental delivery

1. US2 → T027 proof gate decision (in-browser kept, or switch to fallback)
2. US3 → one-command verification
3. US4 → touch keypad
4. Polish → public mode, docs, remaining checks

---

## Notes

- Never edit `Client/` or `Army165local/`; the web build only reads them
- Constrained settings and rules are defined in data-model.md (`Settings.*`, Player session destination and limits, Browser storage key, Client bundle validation); tasks cite them instead of repeating them
- Tasks that need a phone client, an Android device or a public domain may stay "manual, pending"; say so instead of marking them done
- Don't commit or push as part of these tasks; the user decides
