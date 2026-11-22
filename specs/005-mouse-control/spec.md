# Feature Specification: Mouse Control for MobiArmy in the Browser

**Feature Branch**: `005-mouse-control`

**Created**: 2026-11-22

**Status**: Draft

**Input**: User description: "Điều khiển MobiArmy bằng chuột trong trình duyệt: người chơi bấm chuột trực
tiếp vào menu, nút và màn chơi trong game như một web game bình thường, thay vì chỉ dùng bàn phím (bàn
phím vẫn phải hoạt động như hiện tại). Client gốc chỉ nhận phím điện thoại."

## Clarifications

### Session 2026-11-22

- Q: Which parts must support direct mouse clicks at launch? → A: Menus, lists, dialogs and soft keys
  (US1), plus a clickable control pad beside the game (US3) for everything else, matches included.
  Mouse play inside matches (US2) is deferred to a later feature (FR-010).

## Context

MobiArmy in the browser (`002-web-client`, played from the portal play page in `004-game-portal`) is
the original phone game. It is driven by phone keys: arrows, OK/fire and two soft keys. In the browser
those come from the computer keyboard, or from an on-screen keypad on touch devices. On a computer,
clicking inside the game does nothing on almost every screen. Players expect a web game to respond to
the mouse, and some do not know the key mapping. Reported 2026-11-22: "không thể tương tác bằng bàn
phím và chuột" (on checking, the keyboard works; the mouse does not).

This feature lets players use the mouse directly on the game. The keyboard and the touch keypad keep
working as they do today.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Use menus and dialogs with the mouse (Priority: P1)

A player on a computer opens MobiArmy from the portal and plays the out-of-battle parts with the mouse
alone:
- Clicking a menu entry ("Bắt đầu", "Cửa hàng item", "Cấu hình"…) selects it and opens it.
- Clicking the soft-key labels at the bottom of the game screen ("Chọn", "Thoát", "Menu",
  "Đăng nhập", "OK"…) does what that key does.
- Clicking a row in a list, such as rooms, areas, friends or a pop-up menu, selects it (grid
  screens such as the shop and character select use the control pad; FR-010).
- Clicking a text field lets them type in it.

**Why this priority**: This is what players hit first: every session starts in menus, and a game that
ignores clicks there feels broken.

**Independent Test**: With only the mouse (keyboard untouched), go from the main menu into a room list,
pick a room, open "Cấu hình" and come back.

**Acceptance Scenarios**:

1. **Given** the main menu, **When** the player clicks "Cửa hàng item", **Then** the shop opens, as if
   they had moved to it and pressed OK.
2. **Given** any screen with soft-key labels, **When** the player clicks the left, centre or right
   label, **Then** the game does what that soft key or OK does.
3. **Given** a list longer than the screen, **When** the player scrolls the mouse wheel over the game,
   **Then** the selection moves up or down as with the arrow keys.
4. **Given** a dialog with an "OK" button, **When** the player clicks it, **Then** the dialog closes as
   with the OK key.
5. **Given** a text field (login, chat, room password), **When** the player clicks it and types,
   **Then** the text goes into that field.

---

### User Story 2 - Play a match with the mouse (Deferred — not in this feature)

In a match, on their turn, the player:
- clicks or holds on the left and right parts of the ground to walk;
- aims by pointing or dragging;
- holds the mouse button to build power and releases it to fire;
- picks items from the item bar by clicking.

Keyboard play is unchanged.

**Status**: Deferred by the 2026-11-22 clarification; in this feature, matches are played with the
keyboard or the control pad (US3). Kept here to describe the follow-up.

**Why this priority**: Matches are the heart of the game, but they need the most work. Menus by mouse
(US1) already remove most of the "it doesn't respond" frustration.

**Independent Test**: Two players start a match. One plays a full turn with the mouse only: move, aim,
choose an item, fire. The shot lands as it would with the same angle and power from the keyboard.

**Acceptance Scenarios**:

1. **Given** it is the player's turn, **When** they press and hold beside their character, **Then** the
   character walks that way while the button is held, within the game's movement rules.
2. **Given** it is the player's turn, **When** they move the pointer or drag, **Then** the aim angle
   changes accordingly, within the character's allowed angles.
3. **Given** it is the player's turn, **When** they hold the main button on the fire control and
   release it, **Then** power builds while held and the shot fires on release, like holding and
   releasing the fire key.
4. **Given** it is not the player's turn, **When** they click the battlefield, **Then** nothing is sent
   to the game that it would not accept from keys in the same situation.

---

### User Story 3 - Mouse-friendly on-screen controls on desktop (Priority: P2)

On a computer, the player can turn on a clickable control pad next to the game: arrows, OK and both
soft keys. This covers any screen where clicking the game itself isn't supported yet. On touch devices
the existing keypad stays as it is.

**Why this priority**: It makes every screen reachable by mouse, matches included (US2 is deferred),
so it is the second half of this feature.

**Independent Test**: Turn the pad on from the play page and finish the main-menu → room → back flow
using only the pad.

**Acceptance Scenarios**:

1. **Given** the play page on a computer, **When** the player turns on the control pad, **Then** it
   appears beside the game without shrinking the game below a readable size, and stays on for their
   next visit.
2. **Given** the pad is on, **When** the player clicks a pad button, **Then** the game reacts exactly
   as to that key. Holding a button repeats like holding the key.

---

### Edge Cases

- A click that lands on nothing clickable does nothing, and does not trigger something else on that
  screen.
- A click during a loading screen or while the server connection is being made is ignored.
- Mouse and keyboard used together (e.g. aiming with the mouse and firing with the keyboard) both work,
  in any order.
- Right-click inside the game does not open the browser's context menu. It acts as the right soft key
  (usually "back" or "exit").
- A double click acts as two clicks only where two presses would make sense. It never fires twice in a
  match.
- After the game window is resized (window size, full screen), clicks still land on the right element.
- Dragging outside the game area, or the window losing focus while a button is held, releases the
  button, so the character doesn't keep walking and power doesn't keep building.
- Touch devices are not affected unless a touch is the same as a click.
- The portal page around the game (sidebar, bottom bar) works as before.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Players MUST be able to operate MobiArmy in the browser with the mouse on the screens in
  scope (FR-010), without using the keyboard.
- **FR-002**: Keyboard control and the touch keypad MUST keep working exactly as in `002-web-client`.
  Mouse support only adds to them.
- **FR-003**: Clicking a selectable entry (menu entry, list row, tab, item) MUST select it and, where
  the same entry is already selected or the screen opens on selection, activate it. This MUST match
  moving to it with the arrows and pressing OK.
- **FR-004**: Clicking a soft-key label or an on-screen "OK"/"Có"/"Không" button MUST have exactly
  the effect of the matching key.
- **FR-005**: The mouse wheel over the game MUST scroll lists and move the selection like the up and
  down arrows.
- **FR-006**: Right-click inside the game MUST act as the right soft key and MUST NOT open the
  browser's context menu.
- **FR-007**: In matches, the mouse MUST be able to play through the control pad (US3): every key a
  match needs (arrows, fire with hold to build power, both soft keys) is on the pad. Direct mouse
  play on the battlefield (US2) is out of scope for this feature.
- **FR-008**: A held button MUST be released when the pointer leaves the game area, when the window
  loses focus, or when the game is hidden, so no action stays stuck.
- **FR-009**: Clicks MUST land on the intended element at every game size the play page uses (normal,
  full screen, phone width), with an accuracy of at least the element's visible area.
- **FR-010**: Direct clicking MUST cover, at launch:
  - every screen whose entries are a vertical list with a highlighted row: the main menu, the room
    list, the pop-up menus, the in-match menu, invite friends and top-up;
  - soft-key labels and dialog buttons on every screen;
  - the on-screen text keyboard, which already accepts clicks.

  Grid and carousel screens (item shop, character select/buy, waiting-room item slots) and the
  battlefield are reached through the soft keys and the control pad (US3). Direct clicking on them is
  a follow-up (research.md › D6).
- **FR-011**: Mouse support MUST NOT change the game's network messages or the game server. The server
  sees the same inputs a phone client would send (constitution I).
- **FR-012**: The play page MUST tell first-time players they can use the mouse, in a short hint that
  can be dismissed, and the key help panel MUST list the mouse actions.

### Key Entities

- **Clickable area**: a region of the game screen that a click maps to (menu entry, list row,
  soft-key label, button, field, battlefield part). It belongs to one game screen and has the key
  action it stands for.
- **Pointer action**: click, hold, release, drag, wheel or right-click on the game, turned into the key
  presses the game already understands.
- **Control pad (desktop)**: optional clickable arrows, OK and soft keys beside the game. On or off is
  remembered per browser.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A first-time player can go from the MobiArmy main menu into a room and back using only
  the mouse in under 1 minute, without reading any instructions.
- **SC-002**: On the screens in scope, 100% of visible selectable entries, soft-key labels and dialog
  buttons respond to a click (checked screen by screen against a list).
- **SC-003**: 0 regressions in keyboard and touch control: all `002-web-client` control checks still
  pass.
- **SC-004**: Clicks land on the intended element at least 99% of the time at the three play-page sizes
  (normal, full screen, phone width).
- **SC-005**: A full match turn (move, aim, choose an item, fire with power) can be played with the
  mouse on the control pad alone, in 5 of 5 tries.
- **SC-006**: No held action (walking, power) stays active after the pointer leaves the game or the
  window loses focus, in 10 of 10 tries.

## Assumptions

- The game client itself can't be changed at the source level; as in 002 and 004, the browser build
  may adapt it, but the original files stay untouched and the server protocol is unchanged.
- "Direct clicking" means clicking where things are drawn on the game screen. The game draws its own
  menus, so each in-scope screen needs to be known individually; screens not yet covered still work by
  keyboard and by the control pad (US3).
- Phones and tablets keep the existing touch keypad; this feature targets mouse and trackpad on
  computers.
- A click on the currently selected menu entry activates it. A click on another entry selects it and
  activates it right away, like a normal web menu, unless that screen needs a separate confirm.
- Mouse actions are listed in the play page's key help panel (004) and in README.
- The phone (J2ME) client is not affected.
