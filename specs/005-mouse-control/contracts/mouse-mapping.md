# Contract: Mouse Mapping (adds to `specs/002-web-client/contracts/key-mapping.md`)

How mouse actions on the game canvas turn into the phone keys the game already understands. Keyboard
and touch mappings from 002 are unchanged. Codes are MIDP key codes as in 002.

## On the game canvas (desktop)

| Mouse action | Where | Result |
|--------------|-------|--------|
| Left click | Bottom soft bar, left third | Left soft key (`-6`) |
| Left click | Bottom soft bar, middle third | Fire/OK (`-5`) |
| Left click | Bottom soft bar, right third | Right soft key (`-7`) |
| Left click | Inside the highlighted row of a list | Fire/OK (`-5`) |
| Left click | Another row of a list | Up (`-1`) or down (`-2`) repeated until that row is highlighted, then fire (`-5`) |
| Left click | Anywhere else | Passed to the game only, as a pointer event (works where the game supports it, e.g. the text keyboard) |
| Right click | Anywhere on the canvas | Right soft key (`-7`); no browser context menu |
| Wheel down / up | Over the canvas | Down (`-2`) / Up (`-1`), one per notch, at most every 80 ms; the page doesn't scroll |
| Drag (more than 6 px) | Anywhere | Passed to the game only; no key |

Rules:
- A seek stops without firing when the list wraps, the row isn't reachable, the screen changes,
  30 steps pass, or the player presses a key or clicks again (research.md › D2).
- Keys pressed by the mouse or the pad are released on pointer leave, window blur and tab hide.
- Screens covered by row clicking (spec FR-010): main menu, room list, pop-up menus, in-match menu,
  invite friends, top-up. Other screens use the soft bar and the control pad.

## Control pad (portal play page)

| Control | Result |
|---------|--------|
| "Bàn phím ảo" toggle in the play page bar | Shows or hides the pad live (no game reload); remembered as `mobiarmy.keypad` |
| Pad buttons | Same codes as the 002 on-screen keypad; holding repeats (fire builds power while held) |

## JavaScript hooks (page ↔ client frame, same origin)

| Name | Defined by | Meaning |
|------|------------|---------|
| `window.mobiarmyKeypadShow(on: boolean)` | `keypad.js` in the client page | Show or hide the pad and remember the choice |
| `window.mobiarmyKeypadShown(): boolean` | `keypad.js` | Current state, so the toggle shows the right label |
