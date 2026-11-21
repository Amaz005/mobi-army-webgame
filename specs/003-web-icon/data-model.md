# Data Model: Web Icon

No stored data. The "entities" are build inputs and outputs.

## Game icon (input)

| Field | Value / rule |
|-------|--------------|
| `source` | `Army165local/icon.png` — never modified (constitution / CLAUDE.md) |
| `width`, `height` | 32 × 32; the build fails if the size differs (the scale factors assume 32) |
| `transparency` | allowed (palette PNG with `tRNS`) |

## Icon set (output, in `web-client/target/webapp/`)

| File | Size | Derived by | Background |
|------|------|-----------|------------|
| `favicon.ico` | 32×32 | ICO wrapper around the source PNG bytes, unchanged | transparent |
| `icon-192.png` | 192×192 | nearest-neighbour ×6 | transparent |
| `icon-512.png` | 512×512 | nearest-neighbour ×16 | transparent |
| `apple-touch-icon.png` | 180×180 | nearest-neighbour ×5 (160×160), centred with 10 px margin | opaque `#63C5F2` |

Rules:
- Every output pixel equals one source pixel (integer nearest-neighbour; FR-005). Margin pixels of
  `apple-touch-icon.png` are the background colour; transparent source pixels are composited over
  the background in that file only.
- Outputs are regenerated on every build; none are committed.

## Web manifest (static, `web-client/src/main/webapp/manifest.webmanifest`)

| Field | Value |
|-------|-------|
| `name`, `short_name` | `MobiArmy` |
| `start_url` | `.` |
| `display` | `browser` |
| `background_color`, `theme_color` | `#63C5F2` |
| `icons` | `icon-192.png` (192x192, image/png), `icon-512.png` (512x512, image/png) |
