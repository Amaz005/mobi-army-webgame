# Implementation Plan: Play the Existing Client in a Web Browser

**Branch**: `002-web-client` | **Date**: 2026-11-21 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/002-web-client/spec.md`

## Summary

Make the project's existing MobiArmy client playable in a browser without rewriting it. The
client's original MIDlet bytecode (`Army165local/`; see "Proof Gate Result" for why not the decompiled source) is compiled to JavaScript with TeaVM,
together with a thin web implementation of the ~25 MIDP APIs it actually uses (canvas
graphics, RecordStore → `localStorage`, SocketConnection → WebSocket, Web Audio). The
original resources come from `Army165local/`. The browser's socket goes to a fixed-target
websockify relay, which forwards raw bytes to `server:19149`, so the game protocol and phone
clients are unaffected. An nginx front serves the static client, terminates HTTPS in public
mode and limits game connections per visitor IP. Two services (`web`, `relay`) join the 001
compose file. A 5-day proof gate (FR-013) validates the in-browser path first; if it fails,
the streamed fallback (research.md R8) replaces it.

## Technical Context

**Language/Version**: Java 8 bytecode level for the client source (as its POM), compiled by
TeaVM 0.10.x (pinned at implementation) to JavaScript; HTML/CSS/vanilla JS for the page shell
and keypad; nginx config; Compose YAML

**Primary Dependencies**: TeaVM (Maven plugin + classlib), websockify (pinned, Python image),
`nginx:1.27-alpine`; build image `maven:3.9-eclipse-temurin-17`

**Storage**: Browser `localStorage` for RecordStore (data-model.md); game data stays in the 001
PostgreSQL

**Testing**: JUnit 5 unit tests for the web layer's pure logic (RecordStore encoding, key
mapping, socket stream buffering) run on the JVM during the `web` image build; end-to-end
checks in [quickstart.md](quickstart.md) (gate G1–G4, W1–W16)

**Target Platform**: Current Chrome, Edge, Firefox (desktop) and Chrome (Android); Linux
containers via Docker

**Project Type**: Web front end (static bundle) + relay, added to the existing game-server stack

**Performance Goals**: Login screen < 20 s first visit / < 5 s cached (SC-002); ≤ 0.5 s action
visibility for 95% of actions on LAN (SC-003)

**Constraints**: Game protocol unchanged (constitution I); no off-site requests (FR-006);
fixed relay destination (FR-005); client source and resources not modified (constitution VI);
per-IP limits and HTTPS in public mode (FR-012)

**Scale/Scope**: About 25 MIDP methods across 12 classes in the web layer, 2 new services, 1
new module `web-client/`; concurrent players bounded by the server's `max-clients` (1000)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle / Gate | Assessment | Status |
|------------------|-----------|--------|
| I. Client Protocol Compatibility | Relay forwards bytes untouched; server unchanged; phone clients keep using 19149 (W5). The client is run with a different server address and a refused HTTP lookup, but this is set by the launcher and the web layer, and both ship together in this spec, as Principle I requires. | PASS |
| II. Server-Authoritative State | No game state moves to the browser; the server still decides everything. | PASS |
| III. Persistent Data Integrity | No schema or server write path changes. | PASS |
| IV. Secrets & Configuration Hygiene | Certificate private keys git-ignored (`docker/web/certs/`); DB and relay never published; new settings documented in `.env.example`. Public mode refuses to start without `WEB_DOMAIN`. | PASS |
| V. Verifiable Changes & Observability | New non-game logic in the web layer gets JUnit tests (Testing above); end-to-end gate and scenarios in quickstart; nginx access logs record visitor IP, path and status for `/game` refusals. | PASS |
| VI. Single Canonical Codebase / YAGNI | No new server tree; decompiled client stays reference-only and untouched (compiled from, not edited). New tools are justified below. The streamed fallback is outlined, not built, unless the gate fails. | PASS (with Complexity Tracking) |
| Tech constraints | PostgreSQL unchanged; versions pinned; UTF-8 for the page. | PASS |
| Workflow gates | Server build unchanged; web build and its tests run in the image build; secret scan extended to `docker/web/certs/`. | PASS |

**Post-design re-check (after Phase 1)**: Still PASS. The design keeps every change outside the
server and the reference client trees, and the only new external surface is the set of
endpoints listed in [contracts/web-endpoints.md](contracts/web-endpoints.md).

## Project Structure

### Documentation (this feature)

```text
specs/002-web-client/
├── plan.md                 # This file
├── research.md             # R1–R10, proof-gate questions P1–P4, sources
├── data-model.md           # services, bundle, session states, storage, settings
├── quickstart.md           # proof gate G1–G4, validation W1–W16
├── contracts/
│   ├── web-endpoints.md    # HTTP/WebSocket surface, limits, status codes
│   └── key-mapping.md      # keyboard/keypad → MIDP key codes
├── checklists/requirements.md
└── tasks.md                # Phase 2 (/speckit-tasks)
```

### Source Code (repository root)

```text
web-client/                              # NEW module: browser build of the existing client
├── pom.xml                              # TeaVM plugin; copies ../Army165local/*.class onto the classpath and
│                                        #   embeds its resources; runs web.build.FixGameAction before TeaVM
├── Dockerfile                           # maven build (incl. JUnit) → nginx:1.27-alpine with the bundle
├── src/main/java/
│   ├── web/WebMain.java                 # launcher: set TerrainMidlet.d/c, start MIDlet, wire canvas + keys
│   └── javax/microedition/              # MIDP web layer (only the APIs in research.md R3)
│       ├── midlet/MIDlet.java
│       ├── lcdui/{Display,Displayable,Canvas,Graphics,Image}.java
│       ├── rms/RecordStore.java         # → localStorage (data-model.md)
│       ├── io/{Connector,SocketConnection,HttpConnection,ConnectionNotFoundException}.java
│       ├── media/{Manager,Player,MediaException,control/VolumeControl}.java
│       └── (wireless.messaging stubs)   # SMS → "not supported"
├── src/main/webapp/{index.html,play.css,keypad.js}
└── src/test/java/...                    # JUnit: RecordStore codec, key mapping, socket buffer
docker/
├── relay/Dockerfile                     # NEW: websockify, fixed target server:19149
└── web/
    ├── nginx.local.conf.template        # NEW: HTTP on 8080, /game proxy, limits (envsubst by nginx image)
    ├── nginx.public.conf.template       # NEW: 80→443 redirect, TLS, /game proxy, limits, ACME path
    ├── 10-check-public.sh               # NEW: public mode refuses to start without WEB_DOMAIN/certs
    └── certs/                           # git-ignored; owner-provided fullchain.pem / privkey.pem
docker-compose.yml                       # CHANGED: + relay, + web (001 services untouched)
.env.example                             # CHANGED: + WEB_MODE, WEB_PORT, WEB_DOMAIN, limits
.gitignore                               # CHANGED: + docker/web/certs/
README.md                                # CHANGED: web address, browsers, key mapping link, public mode
```

**Structure Decision**: A separate `web-client/` module keeps browser concerns out of the
canonical server (`mobiarmy-java/`) and out of the reference client trees, which it only
compiles from and copies from. Relay and nginx config live under `docker/` next to 001's DB
script.

## Complexity Tracking

| Addition | Why Needed | Simpler Alternative Rejected Because |
|----------|------------|-------------------------------------|
| TeaVM (new build tool) | Runs the existing Java client in the browser without rewriting it (FR-002) | CheerpJ: proprietary licence, CDN load conflicts with FR-006, sockets only via Tailscale. Rewriting: out of scope |
| MIDP web layer (~25 methods) | TeaVM provides the JDK but not J2ME APIs | A full J2ME emulator is much larger than the surface this client uses |
| websockify `relay` service | Browsers can't open raw TCP; the server speaks raw TCP (FR-004) | WebSocket support inside the server would change server networking for a client concern |
| nginx `web` service | Static hosting + HTTPS + per-IP limits in one well-known component (FR-012) | Caddy needs a third-party plugin for per-IP rate limits |

## Proof Gate Result (FR-013), 2026-11-21

**Decision: PASS, keep in-browser delivery (A).** The streamed fallback (R8) is not needed. Evidence
was gathered with headless Chrome (Puppeteer 22.15) against the running stack, in under one day of
the 5-day time box.

| Check | Result | Evidence |
|-------|--------|----------|
| G1: client compiles to JS | ✅ | `mvn package`: 19/19 JUnit tests green, TeaVM 0.12.0 bundle 0.9 MB, no TeaVM warnings |
| G2: resources and graphics | ✅ | Sound prompt, intro animation, title, login form and lobby render pixel-correct (bitmap fonts with Vietnamese diacritics, sprites, transforms) |
| G3: login through the relay | ✅ | Server log `Client: 1 name: a … version: 1.6.5` → `Login Success!`; wrong password shows the game's own error (W2); lobby, area/room list, waiting room work for two browsers at once |
| G4: speed | ✅ load / ⚠️ match | First game frame 3.1 s first visit (no cache), 0.3 s cached (SC-002). Client-measured round trip `PING 42` ms through the relay (SC-003 budget 500 ms). A 2-player match starts and both views stay in sync, but **shots are not resolved**: see below |

**Design changes found during the proof (docs updated to match):**

1. **The original MIDlet bytecode is compiled, not the decompiled source.** `Client/SOURCE_ARMYI` was
   decompiled with errors (e.g. `model/y.java` lost the assignments that read the resource packs), so
   the build uses `Army165local/*.class` directly. `Client/` is no longer an input.
2. **PNG decoding happens in Java** (`web.PngDecoder`), because MIDP's `Image.createImage(byte[])` must
   be synchronous and most images arrive from the server as bytes. All resources (~135 KB) are embedded
   in `client.js` through TeaVM's `ResourceSupplier`; there is no separate `res/` folder.
3. **The original client has a bug**: `coreLG.el.getGameAction(int)` calls itself (infinite recursion,
   present in the original bytecode). A build step (`web.build.FixGameAction`, ASM) rewrites that one
   call to `Canvas.getGameAction` in the compiled copy.
4. **`microedition.platform` must be set**; the client calls `.indexOf` on it.
5. **TeaVM's minifier renames `@JSBody` parameters but not locals**, so all JS locals are `$`-prefixed.
6. **Ports:** the web container always listens on 8080 (HTTP) and 8443 (HTTPS in public mode),
   published as `WEB_PORT` / `WEB_TLS_PORT`.

**Server issue found by the proof (pre-existing, FIXED 2026-11-21):** in a match, the browser client sends the
fire message (command 22, `SendWait_Fire …` in the client log) through the same path that carried login,
room join, ready and start, but `FightManager.shootMessage` returns early without a reply, on either
player's turn. W4/W5/SC-003-in-match/SC-005 can't be completed until that server bug is fixed. It is not
a web-client defect. Cause: nothing ever set `User.State.Fighting`, so `MessageHandler` dropped every in-match command guarded by `fighting()` (shoot, move, items…). Fix: `FightManager.startGame` now sets players to `Fighting` (endGame already resets them). Verified: 32 shots resolved over 16 alternating turns between two browsers.

Other server issues seen: disconnected players stay in rooms as "ghosts" until the server restarts; the
server logs plaintext passwords (`pass: …`, constitution IV); two seeded accounts are both named `a`.
