# Research: Play the Existing Client in a Web Browser

**Feature**: `002-web-client` | **Date**: 2026-11-21

> **Updated by the proof gate (2026-11-21):** R1–R3 assumed compiling the decompiled source and
> preloading PNGs. The build now compiles the original bytecode and decodes PNGs in Java, with all
> resources embedded. See plan.md › "Proof Gate Result" for the evidence and the full list of changes.

Repo facts marked *(verified)* were checked in this session. External facts come from the
sources at the end; recheck them at implementation time.

## R1. What a playable client is made of

- **Finding** *(verified)*: `Client/SOURCE_ARMYI/` holds the decompiled Java source, but its built
  jar has **no resources**. Images, sounds, fonts and maps exist only in `Army165local/`
  (`arrow.png`, `fire.wav`, `0.mid`, `font/`, `gui/`, `item/`, `map/`, `player/`, …), and the
  code loads them by path (`Image.createImage("/arrow.png")`,
  `getResourceAsStream("/fire.wav")`).
- **Decision**: The web build compiles the source from `Client/SOURCE_ARMYI/` and bundles the
  non-class files from `Army165local/`. Both stay untouched (reference-only, constitution VI).

## R2. Pointing the client at our server without editing it

- **Finding** *(verified)*: The connection is made in `coreLG/dl.java:166` as
  `"socket://" + TerrainMidlet.d + ":" + TerrainMidlet.c`, both `public static`
  (`TerrainMidlet.java:25-27`). The teamobi.com lookup (`model/x.java:48`) only stores a
  `TLIP` record, which nothing uses to connect.
- **Decision**: (a) The web launcher sets `TerrainMidlet.d`/`c` before starting the MIDlet.
  (b) In the browser, the socket layer ignores the requested host anyway and always connects to
  the relay at the page's own origin, which gives FR-003 and FR-005 even if (a) were bypassed.
  (c) `HttpConnection` is refused with `ConnectionNotFoundException`, so the teamobi.com lookup
  fails quietly (FR-003, FR-006).
- **Alternative rejected**: Editing the decompiled source (it would diverge from reference,
  and constitution VI says it is reference-only).

## R3. In-browser runtime for the client (primary delivery, "A")

- **Finding** *(verified)*: The client uses a small part of MIDP. In `lcdui` it uses one
  `Canvas`, `Graphics` (`setColor` 92, `drawImage` 68, `fillRect` 67, `drawRegion` 55,
  `setClip` 37, `translate` 29, `drawLine` 17, `drawRect` 9, `fillArc` 5, plus single uses of
  `fillTriangle`, `fillRoundRect`, `drawRGB`, `getRGB`, `createRGBImage`, clip getters) and
  `Image.createImage` (21). It has **no `drawString`** (text uses its own bitmap font). Beyond
  that: `rms.RecordStore` (open/add/set/get), `io.SocketConnection`, `io.HttpConnection`,
  `media.Player` (wav + one midi), `wireless.messaging` (SMS), `MIDlet.platformRequest`.
  It has 9 classes with threads.
- **Decision**: Compile the client to JavaScript with **TeaVM** (open source, Apache-2.0) and
  write a **thin MIDP web layer** (`web-client/src/main/java/javax/microedition/...`) that covers
  exactly the APIs above. Graphics go to an HTML canvas, RecordStore to `localStorage`,
  SocketConnection to a WebSocket, and `Player` to Web Audio. Everything is served by the stack
  itself.
- **Rationale**: The API surface is small and fully known, and TeaVM supports Java threads in its
  JavaScript output by turning blocking calls into async ones (needed for the socket's blocking
  `read`). The output is plain static files: no licence fees, no CDN, fast to load (SC-002).
- **Alternatives considered**:
  - *freej2me-web on CheerpJ*: runs whole J2ME jars in the browser today, but CheerpJ is
    proprietary (free tier only for individuals/non-commercial FOSS, with attribution and loading
    from its CDN, which conflicts with FR-006), its raw-socket support goes through a Tailscale
    VPN rather than a plain WebSocket, and the author notes it can run slowly. It's a good quick
    smoke test, not a production path.
  - *PluotSorbet / j2me.js* (Mozilla): JavaScript J2ME VM, unmaintained for years, built for
    Firefox OS sockets. Too risky.
  - *Rewriting the client*: out of scope (spec FR-002).

## R4. Unknowns for the proof gate (FR-013)

The proof must settle these before the rest is built. The time box is **5 working days**.

| # | Question | Pass condition |
|---|----------|---------------|
| P1 | Does TeaVM compile the decompiled client (obfuscated names, Java 8 bytecode) with no unsupported JDK classes? | Build succeeds; any missing class is covered by the web layer |
| P2 | Do `Class.getResourceAsStream` / `Image.createImage(path)` find bundled resources? | Login screen renders with correct images (TeaVM resource supplier, or a preloaded resource map in the web layer) |
| P3 | Does a blocking socket `read` work through the relay while the game keeps drawing? | Login succeeds against the real server (US1) |
| P4 | Is the game fast enough? | SC-002 (login screen < 20 s first visit) and SC-003 (≤ 0.5 s for 95% of actions) in a 2-player match |

**If any of P1–P4 fails within the time box, switch to the streamed fallback (R8)** and record
the evidence in `plan.md`.

## R5. Browser connection path (relay)

- **Decision**: **websockify** in its own `relay` service, started with a single **fixed
  target** `server:19149`. It converts binary WebSocket frames to a raw TCP byte stream and back,
  without parsing them (FR-004 protocol untouched).
- **Rationale**: Mature, single-purpose, and the target is fixed at the command line, so a
  browser can't pick any other destination (FR-005, SC-006).
- **Alternatives**: WebSocket support inside the game server (touches server networking, mixes
  concerns); writing a custom relay (more code to secure).

## R6. Public HTTPS front and per-visitor limits (FR-012)

- **Decision**: An **nginx** `web` service serves the static client files and proxies `/game`
  (WebSocket upgrade) to `relay:6080`. Per-visitor limits use built-in `limit_conn` (max
  simultaneous game connections per IP, default 4) and `limit_req` (connection attempts per
  IP, default 10/min burst 5). Visitors over the limit get HTTP 429/503.
  - **Local mode** (`WEB_MODE=local`, default): plain HTTP on `${WEB_PORT:-8080}`; browsers
    allow WebSockets to `localhost` without TLS.
  - **Public mode** (`WEB_MODE=public`): HTTPS on 443, HTTP 80 redirects to it (and serves ACME
    challenges); certificate files are provided by the owner in `docker/web/certs/` (git-ignored).
  The mode chooses which nginx config file is mounted
  (`./docker/web/nginx.${WEB_MODE}.conf.template`, filled in by the nginx image's envsubst), so neither config needs if/else logic.
- **Rationale**: Limits and TLS are built into nginx; no extra modules. Keeping certificate
  issuance with the owner avoids running ACME automation the project can't test locally.
- **Alternatives**: Caddy (automatic HTTPS, but per-IP rate limiting needs a third-party
  plugin); limits inside websockify (not supported).

## R7. Controls (FR-008)

- **Decision** (revised 2026-11-21 for direct typing: letters and Space now type text): Keyboard arrows → up/down/left/right; Enter → fire (OK); `F1` → left
  soft key; `F2`/Esc → right soft key; Backspace → delete; any printable character → typed as itself. On touch devices, an
  on-screen keypad below the canvas sends the same key codes. Full table in
  [contracts/key-mapping.md](contracts/key-mapping.md).
- **Rationale**: F1/F2 match other J2ME emulators (freej2me-web); Q/W were dropped as soft keys
  because the client accepts typed characters (QWERTY-phone mode), so letters must reach text fields.

## R8. Streamed fallback ("B"), only if the proof gate fails

- **Decision (outline)**: A fixed pool of `STREAM_SLOTS` (default 4) slot containers. Each runs
  a virtual display, the existing desktop client (MicroEmulator, already in `Client/`'s POM)
  pointed at `server:19149`, and a browser-viewable remote-display server. nginx hands each
  visitor a free slot, and visitors beyond the cap get a "server full" page (FR-014). Slots reset
  when the visitor disconnects.
- **Rationale**: It reuses the desktop client that already runs. A fixed pool keeps the cap
  simple and avoids starting containers on demand.
- **Detail**: This is designed in full only if the gate fails; its tasks are added then.

## R9. Sound and phone-only features

- **Decision**: `.wav` effects play through Web Audio, starting on the first user gesture
  (browser autoplay rules). The single `.mid` background track is **muted** in v1, since
  browsers have no built-in MIDI synth. `platformRequest` (`tel:`, URLs) and SMS show "not
  supported" or no-op (spec assumption).

## R10. RecordStore persistence

- **Decision**: Each RecordStore becomes a `localStorage` entry keyed
  `mobiarmy.rms.<storeName>`, holding its records base64-encoded. It's per browser, as on a
  phone. Saved login names/settings survive reloads; clearing site data resets them.

## Sources

- [freej2me-web](https://gh.r-mj.com/zb3/freej2me-web), CheerpJ-based J2ME emulator port (licence/CDN/speed notes, key defaults)
- [CheerpJ networking guide](https://cheerpj.com/docs/guides/Networking) (raw sockets via Tailscale)
- [CheerpJ licensing](https://cheerpj.com/licensing/) (Community tier conditions)
- [PluotSorbet](https://wiki.mozilla.org/PluotSorbet) / [j2me.js](https://github.com/mbebenita/j2me.js) (unmaintained JS J2ME VM)
