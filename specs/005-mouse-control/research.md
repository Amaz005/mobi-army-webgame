# Research: Mouse Control (spec 005)

Findings from the original client (decompiled `Client/SOURCE_ARMYI`; class and field names are
confirmed against the bytecode in `Army165local/` at implementation) and from the web layer
(`web-client/`).

## Client facts

- **Screens**: every screen is a subclass of `coreLG.ds` and draws itself on one 240×320 canvas
  (`el.d` × `el.e`). Input reaches the screens only as flags in `el.H[]`:

  | Flag | Key |
  |------|-----|
  | `H[2]` / `H[8]` | up / down |
  | `H[4]` / `H[6]` | left / right |
  | `H[5]` | fire / OK |
  | `H[12]` / `H[13]` | left / right soft key |

  The web layer produces these by sending MIDP key codes; the existing keypad path accepts
  `-1…-7` (contracts/key-mapping.md in 002).
- **Soft keys**: `ds.a(Graphics)` draws the three command labels `ac` / `ad` / `ae` (left / centre /
  right) on the bottom line.
  - Positions: x = 5, `el.g` (centre) and `el.d − 5`.
  - Baseline: `el.e − fontH − 3`. The bar is `ds.ab = fontH + 6` px high.
  - Dialogs (`model.q`) draw their buttons in exactly the same places, so clicking the bottom bar
    can stand for the soft keys everywhere.
- **Selection bars**: vertical lists draw the selected row as a `fillRect` almost the full canvas
  width, in one of two colours.

  | Colour | Screen (class) | Bar height |
  |--------|----------------|------------|
  | `0xFFFF36` | main menu (`ej`) | 22 (rows 20) |
  | `0xFFFF36` | room list (`f`) | `ds.ab` |
  | `0xFFFF36` | in-match menu: Tiếp tục / Dùng item / Rời trận chiến (`ci`) | 36 |
  | `0xFFFF36` | area or list dialog (`et`) | `ds.ab` |
  | `0xFFFF36` | pop-up menu (`el`, `bg`) | 24 |
  | `0xFFD200` | invite friends (`dd`) | 57 |
  | `0xFFD200` | top-up menu (`model.bp`) | 20 |

- **Screens without a selection bar**: item shop (`l`, drawn with images), character select/buy (`fa`,
  carousel with stat bars), waiting-room item slots (`bx`, 26 px grid cells), the loading screen (`dc`)
  and the battlefield.
- **Built-in pointer use**: `el.pointerPressed/Dragged/Released` store the pointer in `el.J/K/L/M`.
  Only the on-screen text keyboard (`model.h`) and the login screen (`dl`, keyboard icon at the top)
  read them.
- **Web layer**:
  - `Js.listenPointer` converts mouse and touch to canvas pixels and calls the Canvas pointer
    methods.
  - `Graphics` knows the translation (`tx/ty`) and the current colour, so it can see every `fillRect`.
  - `Canvas.paintNow()` runs once per frame.
  - `keypad.js` uses pointer events, so it already works with a mouse when shown (`?keypad=1`).

## Decisions

### D1. Soft-key strip (all screens)

- **Decision**: a click (press and release without dragging) whose y is in the bottom `SOFT_H` px of the
  canvas sends the key for its third:
  - left third → left soft key (-6)
  - middle third → fire (-5)
  - right third → right soft key (-7)

  `SOFT_H` = `ds.ab`, measured at implementation (font height + 6; expected 18–20 px) and fixed as a
  constant.
- **Rationale**: every screen and dialog draws its commands there, so one rule covers FR-004 everywhere.
- **Alternative rejected**: per-screen soft-key hit areas. They are not needed, because the positions
  are the same on every screen.

### D2. Selection seek for vertical lists (generic)

> **Updated during implementation (2026-11-22)**:
> - A third highlight colour, `0xF0AA01`, is used by the pop-up menus (`el` / `bg`). Their bar spans the
>   menu box, so the 80 % width rule is measured against the current clip.
> - "Settled" now means 4 unchanged frames, because scroll animations pause.
> - Each step moves the click target by `pitch − bar movement`, so the content scrolled under the bar is
>   followed. The pitch is learned from the first plain move.

- **Decision**: the web `Graphics` remembers the last **selection bar** painted in each frame.
  - A selection bar is a `fillRect` in `0xFFFF36` or `0xFFD200`, at least 80 % of the canvas wide and
    12–60 px high, in screen coordinates.
  - At the end of each frame, that frame's bar becomes the "current bar" (or none).
- **On a click above the soft strip**:
  1. No current bar → nothing extra happens; the click is still passed to the game (D5).
  2. The click is inside the bar → send fire.
  3. Otherwise **seek**: send down if the click is below the bar, up if above, one key per frame.
     After each key, wait until the bar has held still for 2 frames (lists animate their scroll).
     Repeat until the bar contains the click y, then send fire.
- **Stop without firing** when:
  - the bar disappears or the screen changes;
  - the bar jumps the other way (the list wrapped around);
  - the bar passes the click y without containing it (the click was between rows or past the end);
  - 30 steps are reached;
  - any real key or another click arrives.
- **Rationale**:
  - No bytecode patch and no per-screen tables.
  - It uses only actions the game already accepts, so the server sees normal key input (FR-011).
  - It works for long, scrolling lists because it follows the bar instead of computing row positions.
- **Alternatives rejected**:
  - Per-screen click tables (row height and offset for each of ~10 screens): fragile, a lot of work,
    and scrolling offsets are private fields.
  - Patching each screen's bytecode to accept clicks: many patches against obfuscated code.

### D3. Wheel and right click

- **Decision**:
  - Wheel over the canvas: one up or down key per notch, at most one every 80 ms. The page does not
    scroll while the pointer is over the game.
  - Right click: the right soft key (-7). The browser's context menu is suppressed over the canvas, and
    the right button is **not** passed to the game as a pointer.

### D4. Control pad on desktop (US3, FR-007)

- **Decision**: reuse `keypad.js`.
  - It gets `window.mobiarmyKeypadShow(on)`, so the play page can show or hide it live; reloading the
    frame would restart the game.
  - It remembers the choice in `localStorage` as `mobiarmy.keypad`: `on`, `off`, or absent = automatic
    (touch only).
  - The portal play page gets a toggle button "Bàn phím ảo" in its bottom bar.
  - On wide screens the pad sits to the right of the game; otherwise below it, as now.
  - The fire button keeps its press-and-hold behaviour, so a match can be played with the mouse
    (power builds while held).

### D5. Coexistence with the game's own pointer use

- **Decision**: left-button pointer events are still forwarded to the game, so the text keyboard
  (`model.h`) and the login keyboard icon keep working. The click translator acts on **release** and
  only when the press-to-release distance is ≤ 6 canvas px.
- **Risk**: on screens that use both (only the text keyboard), D2 never fires because those screens
  have no selection bar. The soft strip (D1) may also fire under the keyboard's bottom row; this is
  checked in quickstart §3 and, if needed, D1 is skipped while `model.h` is open (detected by its
  background `fillRect`).

### D6. Screens not covered by direct clicking

- **Decision**: the item shop (`l`), character select/buy (`fa`), waiting-room item slots (`bx`) and the
  battlefield use the soft keys (D1) and the control pad (D4).
- **Why**: these screens have no selection bar to follow, so each would need its own adapter. That is
  left as a follow-up feature, together with the deferred US2. spec.md FR-010 is amended to match.

### D7. Safety (FR-008, edge cases)

- **Releasing held keys**: on `pointerleave` of the canvas, `blur` and `visibilitychange`, every key
  that the translator or the pad pressed is released, and any seek in progress is cancelled.
- **Double clicks**: a second click within 300 ms while a seek or fire from the first is still running
  is ignored, so a double click never sends two fires.

### D8. Hint and help (FR-012)

- The play page shows a one-time hint, dismissable and remembered as `portal.mouseHintSeen`:
  "Dùng chuột: bấm vào mục để chọn, bấm dòng chữ dưới cùng để Chọn / Thoát, chuột phải để quay lại.
  Cần thêm nút? Bật **Bàn phím ảo**."
- The key help panel gets a "Chuột" section with the same mappings.
