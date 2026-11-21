# Contract: Key Mapping

Browser input → key codes delivered to the game's `Canvas.keyPressed/keyReleased`. The README links
to this table (FR-011). Implemented in `web-client/src/main/java/web/KeyMap.java` (tested by `KeyMapTest`).

## Physical keyboard

| Action | Code sent | Keyboard |
|--------|-----------|----------|
| Up / Down / Left / Right | `-1` / `-2` / `-3` / `-4` | Arrow keys |
| Fire / OK | `-5` | Enter (also numpad Enter) |
| Left soft key | `-6` | F1 |
| Right soft key | `-7` | F2, Esc |
| Delete in text fields | `8` | Backspace |
| Typed text | the character's code | Any printable character: letters, digits, symbols, space, IME/Vietnamese input |

Digits (`48`–`57`), `*` (`42`) and `#` (`35`) have the same codes as the phone keypad, so outside text
fields they still act as keypad keys (e.g. 2/4/6/8 as directions).

Rules:
- Ctrl/Cmd combinations, Tab, other function keys and keys during IME composition pass through to the
  browser; mapped keys call `preventDefault()` so the page doesn't scroll.
- Text entry: the client's text field types characters literally once it has seen a letter key
  (QWERTY-phone mode, page-wide). If the first typed character is not a letter (e.g. a password
  `3`), the web layer first sends `A` + Backspace so the digit isn't multi-tapped into `d`.
- Holding a key sends `keyRepeated` at the browser's repeat rate.

## On-screen keypad (touch devices)

| Button | Code |
|--------|------|
| ▲ ▼ ◀ ▶ | `-1` `-2` `-3` `-4` |
| ● | `-5` |
| top-left / top-right | `-6` / `-7` |
| 0–9, `*`, `#` | `48`–`57`, `42`, `35` (phone-style multi-tap in text fields) |

Holding a button repeats after 400 ms, every 120 ms.
