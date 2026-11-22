# Implementation Plan: Mouse Control for MobiArmy in the Browser

**Branch**: `005-mouse-control` | **Date**: 2026-11-22 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/005-mouse-control/spec.md`

## Summary

Make the browser build of MobiArmy usable with a mouse without changing the game or the server.
A small **click translator** in the web MIDP layer turns mouse actions on the game canvas into the
phone keys the game already understands:
- clicks on the bottom bar become the soft keys;
- clicks on a list row "seek" the game's own highlight bar to that row with up/down, then press OK;
- the wheel moves the selection; right click is "back".

The existing on-screen keypad becomes a desktop **control pad**, toggled from the portal play page. It
covers every screen without row clicking, matches included. Row clicking is generic: it follows the
highlight bar the game paints (research.md › D2), so there are no per-screen tables and no bytecode
patches.

## Technical Context

**Language/Version**: Java 11 source compiled to JavaScript by TeaVM 0.12 (web-client); plain JS/CSS
for `keypad.js`, `play.css` and the portal play page.

**Primary Dependencies**: none new. The existing web MIDP layer (`javax.microedition.lcdui.Canvas` /
`Graphics`, `web.Js`, `web.EventQueue`) and `keypad.js`.

**Storage**: browser `localStorage` only, for per-viewer preferences (data-model.md).

**Testing**: JUnit 5 in `web-client/src/test/java` for the pure translator logic (`web.MouseControl`);
manual and browser checks in quickstart.md; 002 control checks as regression.

**Target Platform**: desktop browsers from 002 FR-009 (Chrome, Edge, Firefox); touch devices unchanged.

**Project Type**: client-side change to the web build of an existing game.

**Performance Goals**: the click translator adds no measurable frame time; at most one injected key per
frame; seeking a 10-row list takes under 1 s.

**Constraints**:
- Original client bytecode untouched; no change to the wire protocol or the server (constitution I).
- Keyboard and touch keypad behave exactly as before (FR-002).
- Pointer events must still reach the game for its own touch UI.

**Scale/Scope**:
- Row clicking on the 6 vertical-list screens of FR-010.
- Soft keys on all screens.
- Control pad for the rest, including matches.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Check | Status |
|-----------|-------|--------|
| I. Client protocol compatibility | Only key presses the game already handles are injected, in the browser. No message, command ID or resource changes; the server is untouched; no client bytecode patch. | PASS |
| II. Server-authoritative state | The server is untouched and sees the same inputs a phone client could send. | PASS |
| III. Persistent data integrity | No database or server data involved. | PASS (n/a) |
| IV. Secrets & configuration hygiene | No secrets. The two `localStorage` preference keys are documented in data-model.md. | PASS |
| V. Verifiable changes & observability | The translator's logic (bar detection, soft-strip mapping, seek state machine, throttles) is a pure class with JUnit tests. Behaviour is verified in a real browser per quickstart.md. | PASS |
| VI. Single canonical codebase / YAGNI | Changes stay in `web-client/` and `portal/`. No new framework or dependency. A generic approach replaces per-screen adapters; grid screens are deferred (D6). | PASS |

**Post-design re-check**: unchanged, all PASS. The design adds one Java class, small hooks in existing
MIDP classes, a `keypad.js` API, and a play-page toggle.

## Project Structure

### Documentation (this feature)

```text
specs/005-mouse-control/
├── plan.md              # This file
├── research.md          # Phase 0: client facts, decisions D1–D8
├── data-model.md        # Phase 1: selection bar, click, seek state machine, preferences
├── quickstart.md        # Phase 1: validation guide
├── contracts/
│   └── mouse-mapping.md # mouse → key mapping, control pad, JS hooks
├── checklists/requirements.md
└── tasks.md             # Phase 2 (/speckit-tasks — not created here)
```

### Source Code (repository root)

```text
web-client/
├── src/main/java/web/
│   ├── MouseControl.java               # NEW, pure logic: soft-strip thirds, bar candidate filter,
│   │                                   #   seek state machine (per-frame step), wheel throttle, double-click guard
│   └── Js.java                         # listenPointer: right button, wheel, contextmenu, pointerleave/blur/visibility hooks
├── src/main/java/javax/microedition/lcdui/
│   ├── Graphics.java                   # fillRect → report candidate selection bars (screen coordinates)
│   └── Canvas.java                     # end of frame → MouseControl.frameEnd(); pointer release → MouseControl.click()
├── src/main/webapp/
│   ├── keypad.js                       # window.mobiarmyKeypadShow / Shown, localStorage "mobiarmy.keypad", mouse-friendly on desktop
│   └── play.css                        # pad beside the game on wide screens
└── src/test/java/web/MouseControlTest.java   # NEW

portal/
├── play.html                           # "Bàn phím ảo" toggle in the bar; mouse section in the key help panel; one-time hint
├── portal.js                           # toggle wiring (calls into the frame), hint
└── portal.css                          # hint styles

README.md, specs/002-web-client/contracts/key-mapping.md   # point to contracts/mouse-mapping.md
```

**Structure Decision**: All logic goes into the existing web MIDP layer. Decisions live in one pure,
tested class (`web.MouseControl`), and `Graphics`/`Canvas`/`Js` only feed it events. The control pad
reuses `keypad.js`; the portal only adds the toggle and the hints.

## Complexity Tracking

No constitution violations to justify.
