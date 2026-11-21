# Research: Web Icon

Facts checked before deciding:
- `Army165local/icon.png` is a 32×32 palette PNG with transparency (343 of 1024 pixels transparent,
  `tRNS` chunk present). The JAR manifest declares no `MIDlet-Icon`; this is the game's app icon.
- The page (`web-client/src/main/webapp/index.html`) has no `<link rel="icon">`, and nginx answers
  every path outside an allowlist with 404, so `/favicon.ico` is a 404 today.
- nginx 1.27's `mime.types` maps `png` → `image/png` and `ico` → `image/x-icon`, but has **no**
  entry for `webmanifest`.

## R1 — Where the icon files come from

- **Decision**: Generate every size at build time from `Army165local/icon.png` with a small build
  step (`web.build.MakeIcons`, run by the existing exec-maven-plugin, like `FixGameAction`).
- **Rationale**: One source of truth (FR-002); nothing derived is committed; the same build that
  compiles the client produces the icons, in the Docker image and locally. Fits the existing
  build-step pattern.
- **Alternatives**: Commit pre-made PNG/ICO files — simpler build, but binary copies drift from the
  source and the scaling can't be tested. Hand-drawn larger artwork — out of scope (spec Assumptions).

## R2 — Which files and sizes

- **Decision**:
  | File | Size | Purpose |
  |------|------|---------|
  | `favicon.ico` | 32×32 | Tab/bookmark icon; also the default root URL browsers probe (FR-003) |
  | `icon-192.png` | 192×192 (6×) | Android home screen / manifest, high-DPI tabs |
  | `icon-512.png` | 512×512 (16×) | Android splash/large launcher icon (manifest) |
  | `apple-touch-icon.png` | 180×180 (5× art + padding) | iPhone home screen |
  | `manifest.webmanifest` | — | Name "MobiArmy" + the two Android icons (FR-004) |
- **Rationale**: These are the sizes current Chrome/Edge/Firefox, Android Chrome and iOS Safari
  actually use; integer scale factors keep pixels square (FR-005).
- **Alternatives**: A single large PNG for everything — browsers downscale it with smoothing, so tab
  icons get blurry. Many intermediate sizes (16, 48, 96…) — no supported browser needs them.

## R3 — Keeping pixel art sharp

- **Decision**: Nearest-neighbour scaling by an integer factor only (6×, 16×, 5×). 180 isn't a
  multiple of 32, so the iPhone icon is the 5× art (160 px) centred on a 180 px square.
- **Rationale**: Integer nearest-neighbour is exact: each source pixel becomes an N×N block, so
  there is no blur (FR-005, SC-003).

## R4 — favicon.ico format

- **Decision**: An ICO container holding the original 32×32 PNG bytes unchanged (one directory
  entry, PNG payload).
- **Rationale**: All supported browsers read PNG-in-ICO; embedding the original file is lossless and
  needs no image encoder for the 32 px size.
- **Alternatives**: Serving `icon.png` as `/favicon.ico` — browsers that sniff the type cope, but
  it's mislabelled. BMP-based ICO — more code, same result.

## R5 — Transparency on iPhone

- **Decision**: `apple-touch-icon.png` gets an opaque background in the game's sky blue
  (`#63C5F2`, sampled from the in-game sky). The other files keep transparency.
- **Rationale**: iOS fills transparent home-screen icons with black, which would make the dark
  outline of the art disappear. Android adaptive launchers and browser tabs handle transparency.

## R6 — Serving and caching

- **Decision**: Add the five paths to the nginx allowlist in both `nginx.local.conf.template` and
  `nginx.public.conf.template`, with `Cache-Control: no-cache` (same as the page shell: the browser
  keeps the file but revalidates with the ETag, getting a cheap 304). Serve
  `manifest.webmanifest` with `default_type application/manifest+json` in its own exact-match
  location, because nginx's `mime.types` lacks that type.
- **Rationale**: FR-007 — a changed icon shows up on the next visit, without re-downloading
  unchanged icons. The tunnel passes these responses through unchanged.
- **Alternatives**: Long `max-age` with versioned file names — needs cache-busting names in the
  manifest and HTML for little gain at this size (total ≈ a few KB).

## R7 — Manifest scope

- **Decision**: Minimal manifest: `name`/`short_name` "MobiArmy", `start_url` ".", `display`
  "browser", the 192 and 512 icons, `background_color`/`theme_color` sky blue.
- **Rationale**: Gives Android the name and a sharp icon for "Add to Home Screen" (FR-004) without
  turning the site into an installable app (out of scope: no service worker, no standalone mode).
