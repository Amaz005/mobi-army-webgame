# Quickstart: Validate Mouse Control (spec 005)

Prerequisites: the stack from 004 running (`docker compose up -d --build`); a desktop browser with a
mouse; a portal account. Open `/mobiarmy/choi/` while logged in on the portal (auto-login takes you to
the main menu).

## 1. Lists and soft keys (US1, FR-003–FR-006, SC-001, SC-002)

1. Main menu: click "CỬA HÀNG ITEM" → the highlight walks down to it and the shop opens. Leave the shop
   by clicking the right part of the bottom bar.
2. Main menu → "BẮT ĐẦU": the room list opens. Click a row near the bottom → it is selected and
   opened. Use the wheel to scroll the list.
3. With a dialog showing ("OK" at the bottom), click the label → the dialog closes.
4. Right-click on the game → acts as "Thoát" / back; no browser menu appears.
5. Time a first-time player: main menu → room → back with the mouse only, in under 1 minute (SC-001).
6. Go through every screen in FR-010 and tick each visible row and soft label against a list
   (SC-002).

## 2. Control pad (US3, FR-007, SC-005)

1. In the play page bar, turn on "Bàn phím ảo" → the pad appears beside the game, and the game doesn't
   restart.
2. Reload the page → the pad is still on. Turn it off → it stays off after a reload.
3. Start a match (two accounts). With the mouse on the pad only, move, aim, open the item bar, and hold
   fire to build power, then release. Repeat 5 times (SC-005).

## 3. Safety and coexistence (FR-002, FR-008, edge cases, SC-003, SC-006)

1. Hold a pad arrow and move the pointer off the game, or switch tabs → the character stops
   (10 of 10, SC-006).
2. Double-click a menu row → it opens once.
3. Open a text field with the on-screen text keyboard → clicking its keys types. Clicks near its
   bottom row don't also trigger the soft keys (research D5).
4. Re-run the 002 keyboard and touch-keypad checks (SC-003), and the 004 play-page checks (focus,
   "Rời game", full screen).
5. Full screen and phone width: repeat §1 steps 1–2 (FR-009, SC-004).

## 4. Tests

```
docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/web-client maven:3.9-eclipse-temurin-17 mvn -B package
```

This runs the pure-logic tests for the click translator (`web.MouseControl`: soft-strip thirds, bar
detection, the seek state machine with its stop conditions, wheel throttle) and builds `client.js`.

## Validation log

**2026-11-22**: main stack, Chrome on desktop, logged in on the portal (auto-login to the main menu).

| Check | What happened |
|-------|---------------|
| §1.1 | Clicking "CẤU HÌNH" walked the highlight down six rows and opened Cấu hình. Clicking "OK" on the bottom line returned to the menu. |
| §1.2 | Clicking "BẮT ĐẦU" walked up six rows and opened it. "Đến khu vực" opened, then "Phòng 3" in a list split by section headers. |
| §1.2, scrolling list | Clicking "Khu vực 11" in the scrolling area list entered area 11. The first build entered area 16: scroll animation pauses were taken as "settled", fixed by research D2 (4 unchanged frames, scroll tracking). |
| Pop-up menu (waiting room "Menu") | "Rời trận chiến" by click works: the bar colour is `0xF0AA01`, width relative to the menu box. |
| §1.3, §1.4 | Wheel down moved the selection. Two fast notches count once (80 ms throttle). Right click went back to the main menu with no browser menu. |
| Keyboard | Arrows still move the selection (SC-003, partial). |
| §2.1, §2.2 | 🎮 shows the pad beside the game without restarting it. The ▼ pad button moves the selection. On/off survives a reload. |
| FR-012 | The one-time hint shows on desktop, and "Đã hiểu" hides it for good. |

Not run yet:
- §2.3: a match turn with the pad (SC-005, needs two accounts in a match).
- §3.1: held keys released on blur (SC-006).
- §3.2: double click.
- §3.3: the game's text keyboard next to the soft strip (research D5).
- §3.4: the full 002 and 004 regression.
- §3.5: full screen and phone width.
