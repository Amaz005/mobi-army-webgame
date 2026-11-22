---

description: "Task list for 005-mouse-control"
---

# Tasks: Mouse Control for MobiArmy in the Browser

**Input**: Design documents from `specs/005-mouse-control/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/mouse-mapping.md,
quickstart.md

**Tests**: Included for the click translator's pure logic (constitution V). UI behaviour is verified
with quickstart.md in a real browser.

**Organization**:
- **US1 (P1)**: click lists, soft keys, wheel and right click.
- **US3 (P2)**: desktop control pad.
- **US2** (mouse play on the battlefield) is deferred by the spec and has no tasks here.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies on incomplete tasks)
- Paths are from the repository root. `wc/` = `web-client/src/main/`, `wt/` = `web-client/src/test/java/`.

Build and test: `docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/web-client maven:3.9-eclipse-temurin-17 mvn -B clean package`

---

## Phase 1: Setup

**Purpose**: Pin down the client facts the translator depends on.

- [X] T001 Measure the game's soft-bar height `ds.ab` (font height + 6) in the running client. Record it as `SOFT_H` in `specs/005-mouse-control/research.md` › D1, where it defines data-model.md › Soft strip.
  - Method: log `el.e`, plus the `y` of the soft-label `drawImage` calls, once from the web `Graphics`; remove the log afterwards.
- [X] T002 [P] Use `javap -c` on `Army165local/coreLG/{ej,f,ci,et,dd,el}.class` and `Army165local/bp.class` (or wherever `model.bp` lives in the bytecode) to confirm the highlight `setColor` constants `0xFFFF36` and `0xFFD200`, and the bar widths and heights. Note any difference in `specs/005-mouse-control/research.md` › D2 (data-model.md › Selection bar.color, .w, .h).

---

## Phase 2: Foundational (blocking prerequisites)

**Purpose**: Key injection and the release-safety mechanism, used by both US1 and US3.

**⚠️ CRITICAL**: US1 and US3 can't be finished until this phase is done.

- [X] T003 Add a single key-injection entry point for the translator to `wc/java/javax/microedition/lcdui/Canvas.java`: `injectKey(int code, boolean pressed)`, delivered on the game thread like real keys.
  - Read how `Js.exposeKeypad` → `EventQueue` delivers `MIDP:<code>` today in `wc/java/web/Js.java` and `wc/java/web/EventQueue.java`, and reuse that path. No new queue.
  - Track which codes are currently held through this path.
- [X] T004 Implement release-safety (spec FR-008, research D7). On canvas `pointerleave`, window `blur` and `document.visibilitychange` → hidden:
  - release every key held through `injectKey`, then cancel any seek (MouseControl hook added in T006);
  - expose `window.mobiarmyReleaseAll()` so `keypad.js` can call it.

  Files: `wc/java/web/Js.java`, `wc/java/javax/microedition/lcdui/Canvas.java`.

**Checkpoint**: Keys injected from the browser console with `mobiarmyKeypad('pressed', -2)` still work, and switching tabs releases held keys.

---

## Phase 3: User Story 1 - Use menus and dialogs with the mouse (Priority: P1) 🎯 MVP

**Goal**: With the mouse alone the player can:
- click rows of the vertical-list screens in FR-010;
- click the soft-key labels and dialog buttons on every screen;
- scroll with the wheel;
- go back with a right click.

**Independent Test**: quickstart.md §1. Go from the main menu into a room list and a room, and back, with the mouse only, in under 1 minute.

### Tests for User Story 1 ⚠️ (write first, must fail)

- [X] T005 [US1] Write `wt/web/MouseControlTest.java`, pure JUnit with no TeaVM natives, against contracts/mouse-mapping.md and data-model.md. Cover:
  - **Soft strip** (data-model.md › Soft strip): clicks in each third map to -6 / -5 / -7; clicks above the strip map to nothing.
  - **Bar filter** (data-model.md › Selection bar.w, .h, .color): too narrow, too short, too tall or the wrong colour is rejected; the last qualifying bar of a frame wins; a frame without one gives "none".
  - **Click detection** (data-model.md › Click.isClick): a drag of more than 6 px is not a click.
  - **Seek**:
    - a click inside the bar sends fire;
    - a click below sends down, one per stable frame, then fire once the bar contains the click y;
    - it stops without firing on a wrap, on overshoot past the click, when the bar disappears, at 30 steps (data-model.md › Seek), on a real key, or on a new click.
  - **Wheel**: at most one key per 80 ms.
  - **Double click**: a click within 300 ms of one still being handled is ignored.
  - **Right click**: maps to -7.

### Implementation for User Story 1

- [X] T006 [US1] Implement `wc/java/web/MouseControl.java`, the pure logic for:
  - data-model.md › Selection bar, Click and Seek;
  - research D1 (soft strip, `SOFT_H` from T001), D2 (seek), D3 (wheel, right click) and D7 (cancel and double click).

  Its API:
  - `barCandidate(x, y, w, h, color)`
  - `frameEnd()`, which returns the key to inject this frame or none
  - `click(pressX, pressY, releaseX, releaseY)`
  - `rightClick()`, `wheel(dir, nowMs)`, `realKey()`, `cancel()`

  T005 must pass.
- [X] T007 [US1] In `wc/java/javax/microedition/lcdui/Graphics.java`, report every `fillRect` to `MouseControl.barCandidate` in screen coordinates (add `tx`, `ty`) with the current colour. Keep the cost to one comparison when the colour is not a highlight colour.
- [X] T008 [US1] Wire the translator in `wc/java/javax/microedition/lcdui/Canvas.java`:
  - at the end of each `paintNow()`, call `MouseControl.frameEnd()`; inject the returned key as a press followed by a release on the next frame, via `injectKey` from T003;
  - real key presses call `MouseControl.realKey()`.
- [X] T009 [US1] Extend the `Js.listenPointer` script in `wc/java/web/Js.java` (keep `$`-prefixed locals, per CLAUDE.md):
  - **Left button**: keep forwarding press, drag and release to the game (research D5), and also report press and release positions to `MouseControl.click`.
  - **Right button**: call `MouseControl.rightClick`, don't forward it to the game, and `preventDefault` the `contextmenu` event on the canvas.
  - **Wheel**: `wheel` on the canvas calls `MouseControl.wheel` with `{passive:false}` and `preventDefault`.
  - **Touch**: touch events are unchanged.
- [ ] T010 [US1] Coexistence check (research D5). Open the game's on-screen text keyboard (`model.h`) and click its bottom row.
  - If the soft strip also fires, make `MouseControl` ignore the strip on frames that paint the keyboard's background `fillRect` (record its colour and size in research D5).
  - Cover this with a case in `wt/web/MouseControlTest.java`.
- [X] T011 [US1] Build (`mvn -B clean package`), run `docker compose up -d --build web`, and run quickstart.md §1 (SC-001, SC-002). Record the per-screen results for every screen in spec FR-010 in `specs/005-mouse-control/quickstart.md` › a new "Validation log".

**Checkpoint**: MVP. The main menu, lists, dialogs and soft keys work with the mouse; the keyboard is unchanged.

---

## Phase 4: User Story 3 - Mouse-friendly control pad on desktop (Priority: P2)

**Goal**: A clickable pad (arrows, OK with hold, both soft keys) can be turned on from the play page. It works beside the game, covers grid screens and matches, and is remembered per browser.

**Independent Test**: quickstart.md §2. Turn the pad on without the game restarting; it survives a reload; play a match turn with the pad.

- [X] T012 [P] [US3] Change `wc/webapp/keypad.js`:
  - add `window.mobiarmyKeypadShow(on)` and `window.mobiarmyKeypadShown()`, which show or hide the existing pad live (no reload);
  - read and write the preference data-model.md › Preferences `mobiarmy.keypad`, with `localStorage` wrapped in try/catch. `?keypad=` in the URL still wins; with no stored value it stays automatic (touch only);
  - call `window.mobiarmyReleaseAll()` (T004) when hiding the pad.
- [X] T013 [P] [US3] Add a wide-screen layout to `wc/webapp/play.css`: when the pad is shown and the viewport is landscape and wider than about 700 px, place the pad to the right of the canvas, with the canvas sized to the remaining space. Narrow screens keep the current layout (pad below).
- [X] T014 [US3] In `portal/play.html`, add a "Bàn phím ảo" toggle button (`id="btn-pad"`, `aria-pressed`) to the bar's tools, next to the key help and full-screen buttons.
- [X] T015 [US3] Wire `btn-pad` in `portal/portal.js` › `playPage`:
  - on click, call `frame.contentWindow.mobiarmyKeypadShow(!shown)` (same origin) and update `aria-pressed`;
  - read `mobiarmyKeypadShown()` when the frame loads;
  - give focus back to the game.

  Style the pressed state in `portal/portal.css`.
- [ ] T016 [US3] Rebuild web, then run quickstart.md §2 (SC-005, five match turns with the pad) and record the results in `specs/005-mouse-control/quickstart.md` › Validation log.

**Checkpoint**: Every screen, matches included, can be played with the mouse.

---

## Phase 5: Polish & Cross-Cutting Concerns

- [X] T017 [P] FR-012, research D8:
  - add a one-time mouse hint to the play page (`portal/play.html`, `portal/portal.js`, `portal/portal.css`). It can be dismissed, and is remembered as data-model.md › Preferences `portal.mouseHintSeen`;
  - add a "Chuột" section to the key help panel, listing the mappings from contracts/mouse-mapping.md.
- [X] T018 [P] Documentation:
  - `README.md`: a mouse controls section linking to `specs/005-mouse-control/contracts/mouse-mapping.md`;
  - `specs/002-web-client/contracts/key-mapping.md`: a one-line pointer to the mouse mapping;
  - `CLAUDE.md`: `web.MouseControl` (click translator, follows the game's highlight bar) and the `keypad.js` show/hide API.
- [ ] T019 Run regression and safety checks per quickstart.md §3: held keys released (SC-006, 10 of 10), double click, the text keyboard, the 002 keyboard and touch-keypad checks (SC-003), and the 004 play-page checks; then full screen and phone width (FR-009, SC-004). Record the results in `specs/005-mouse-control/quickstart.md` › Validation log.
- [X] T020 Make sure the server is unaffected: run `mvn -B test` for `mobiarmy-java` (no server changes are expected; the suite must still pass), and `git diff --stat` shows no files under `mobiarmy-java/` or `Army165local/`.

---

## Dependencies & Execution Order

- **Setup**: T001 and T002 run in parallel; T001 feeds T006 (`SOFT_H`).
- **Foundational**: T003 → T004. Blocks T008 (US1) and T012 (US3).
- **US1**: T005 → T006 → T007, T008 → T009 → T010 → T011.
- **US3**: depends only on T003–T004.
  - T012 ∥ T013, then T014 → T015 → T016.
  - It can run in parallel with US1 (different files).
- **Polish**: T017 and T018 in parallel after US1 and US3; then T019 → T020.

### Shared files (no [P] between them)

- `Canvas.java`: T003 → T004 → T008
- `Js.java`: T004 → T009
- `MouseControlTest.java`: T005 → T010
- `portal/portal.js`: T015 → T017

### Parallel opportunities

- T001 ∥ T002
- T012 ∥ T013 (US3), and US3 as a whole ∥ US1 after Phase 2
- T017 ∥ T018

---

## Parallel Example: User Story 3

```bash
Task: "keypad.js show/hide API and preference in web-client/src/main/webapp/keypad.js"
Task: "Wide-screen pad layout in web-client/src/main/webapp/play.css"
```

---

## Implementation Strategy

### MVP First

1. Phase 1 → Phase 2 → Phase 3 (US1): menus, lists, dialogs and soft keys work with the mouse. Ship it.
2. **STOP and VALIDATE** with quickstart.md §1.

### Incremental Delivery

3. US3: the control pad, which reaches grid screens and matches with the mouse.
4. Polish: hint, help panel, docs and the full regression run.

### Notes

- Out of scope, as follow-ups: US2 (mouse play on the battlefield) and direct clicking on the grid screens (research D6).
- Never edit `Army165local/` or the server; all changes stay in `web-client/` and `portal/`.

## Implementation notes (2026-11-22)

- **Open**:
  - T010: the game's text keyboard next to the soft strip.
  - T016: SC-005, a match turn with the pad; the pad itself works.
  - T019: the full regression and safety run.

  See quickstart.md › Validation log.
- **Deviations**:
  - T001: `SOFT_H` is not a measured constant; it is read at runtime from the client's own `coreLG.ds.ab`
    (`Display.setSoftBarHeight` in `WebMain`), which is 19 px today.
  - T003: there is no `Canvas.injectKey`. Translated keys are delivered by `Display` directly, on the
    Java thread that is running: the event thread for clicks, the painting thread for seek steps.
    Posting to `EventQueue` from a running Java thread raises TeaVM's "Another thread is running".
  - T006: research D2 changed during testing:
    - a third colour, `0xF0AA01`, for pop-up menus, with width measured against the clip;
    - 4 unchanged frames before the bar counts as settled;
    - scroll tracking (`pitch − movement`).

    Covered by 2 extra tests (16 in total).
  - T013: the pad sits beside the game at ≥ 700 px landscape (240 px wide).
