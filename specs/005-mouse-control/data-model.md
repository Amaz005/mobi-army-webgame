# Data Model: Mouse Control (spec 005)

Everything below lives in the browser; nothing is stored on the server or sent to it beyond the
normal key input (FR-011).

## Selection bar (per frame)

The highlighted row of a vertical list, as painted in the last complete frame (research.md › D2).

| Field | Rule |
|-------|------|
| `x`, `y`, `w`, `h` | Screen pixels after translation; `w ≥ 0.8 × canvas width`, `12 ≤ h ≤ 60` |
| `color` | `0xFFFF36` or `0xFFD200` |

At most one per frame. If a frame paints several, the last one wins. None if no qualifying `fillRect`
was painted.

## Click

| Field | Rule |
|-------|------|
| `pressX/Y`, `releaseX/Y` | Canvas pixels |
| `button` | left = click; right = right soft key (research D3) |
| `isClick` | left button, press-to-release distance ≤ 6 px |

## Seek (state machine, at most one at a time)

```
idle ──click above soft strip, bar present, click outside bar──▶ stepping(dir, steps=0)
idle ──click inside bar──▶ send FIRE ──▶ idle
stepping ──bar still moving──▶ waiting
waiting ──bar unchanged 2 frames──▶ (bar contains click? send FIRE ▶ idle : send dir, steps+1 ▶ stepping)
stepping/waiting ──bar gone | screen changed | wrapped | passed click | steps=30 | real key | new click | blur──▶ idle (no FIRE)
```

`dir` is DOWN (-2) when the click is below the bar and UP (-1) when it is above.

## Soft strip

The bottom `SOFT_H` px of the canvas (`SOFT_H` = the game's soft-bar height, measured once; research
D1). Thirds map to `-6` (left soft key), `-5` (fire) and `-7` (right soft key).

## Preferences (`localStorage`, per browser)

| Key | Values | Set by |
|-----|--------|--------|
| `mobiarmy.keypad` | `on` / `off` / absent (automatic: touch devices only) | Control pad toggle (D4) |
| `portal.mouseHintSeen` | `1` | Closing the mouse hint on the play page (D8) |

Storage access is wrapped in try/catch; without storage the defaults apply.
