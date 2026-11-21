# Tasks: Web Icon

**Input**: Design documents from `/specs/003-web-icon/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/icon-endpoints.md, quickstart.md

**Tests**: Included — constitution V requires automated tests for new logic (the icon generator).

**Organization**: Grouped by user story so each can be delivered and checked on its own.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies on incomplete tasks)
- **[Story]**: US1 = tab/bookmark icon (P1), US2 = phone home-screen icon (P2)

## Path Conventions

Existing `web-client/` Maven module and `docker/web/` nginx templates (plan.md › Project Structure).
Build/test in the Maven image: `docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/web-client maven:3.9-eclipse-temurin-17 mvn -B package`.

---

## Phase 1: Setup (Shared Infrastructure)

No setup needed: the module, JUnit, exec-maven-plugin and nginx image already exist.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: The build step that produces every icon file. Both stories serve its output.

- [X] T001 [P] Write `MakeIconsTest` in `web-client/src/test/java/web/build/MakeIconsTest.java`: integer nearest-neighbour scaling maps each source pixel to an exact N×N block (data-model.md › Icon set, rule "every output pixel equals one source pixel"); padding places the ×5 art at offset 10 on a 180×180 canvas filled with the background colour and composites transparent pixels over it (data-model.md › Icon set.`apple-touch-icon.png`); the ICO wrapper has a valid 6-byte header, one 16-byte directory entry (32×32, PNG payload size, offset 22) followed by the unchanged PNG bytes (data-model.md › Icon set.`favicon.ico`); a source that isn't 32×32 is rejected (data-model.md › Game icon.width/height). Tests fail first.
- [X] T002 Implement `web-client/src/main/java/web/build/MakeIcons.java`: `main(sourcePng, outDir)` reads the source (data-model.md › Game icon.source), writes `favicon.ico`, `icon-192.png` (×6), `icon-512.png` (×16) and `apple-touch-icon.png` (×5 + 10 px margin, opaque `#63C5F2`) per data-model.md › Icon set; pure static helpers for scale/pad/ico so T001 passes. Build-time only (javax.imageio), never referenced from `web.WebMain`.
- [X] T003 Add exec-maven-plugin execution `make-icons` to `web-client/pom.xml` (phase `process-resources`, declared after `copy-webapp`'s plugin so output lands in `${webapp.out}`), arguments `${client.res}/icon.png` and `${webapp.out}`; run `mvn -B package` and confirm the four files appear in `web-client/target/webapp/` and all tests pass.

**Checkpoint**: The build produces the icon set; nothing is served or declared yet.

---

## Phase 3: User Story 1 - Recognise the game tab (Priority: P1) 🎯 MVP

**Goal**: Desktop/Android browser tabs, bookmarks and history show the MobiArmy icon; `/favicon.ico` is never a 404.

**Independent Test**: quickstart.md V2 for `favicon.ico` and `icon-192.png` (200, right type, `no-cache`), then V4: open the page, the tab shows the icon and the nginx log has no icon 404s.

### Implementation for User Story 1

- [X] T004 [US1] Add `<link rel="icon" href="favicon.ico" sizes="32x32">` and `<link rel="icon" href="icon-192.png" type="image/png" sizes="192x192">` to `<head>` in `web-client/src/main/webapp/index.html` (contracts/icon-endpoints.md › Page declarations).
- [X] T005 [P] [US1] Allow `/favicon.ico` and `/icon-192.png` in `docker/web/nginx.local.conf.template` with `Cache-Control: no-cache` (contracts/icon-endpoints.md rows 1–2), keeping every other unknown path 404.
- [X] T006 [P] [US1] Same change in `docker/web/nginx.public.conf.template`.
- [X] T007 [US1] Rebuild and restart web (`docker compose build web && docker compose up -d web`); run quickstart.md V2 (these two paths, plus 304 on `If-None-Match`) and V4 in headless Chrome; record results in this file.

**Checkpoint**: US1 complete — the tab icon works locally and through the tunnel.

---

## Phase 4: User Story 2 - Add the game to a phone's home screen (Priority: P2)

**Goal**: "Add to Home Screen" on Android Chrome and iPhone Safari gives a sharp MobiArmy icon named "MobiArmy".

**Independent Test**: quickstart.md V2 for `icon-512.png`, `apple-touch-icon.png`, `manifest.webmanifest`; V3 (sharp pixels, sky-blue iPhone background); V5 on a phone (or Chrome's manifest view in an emulated Pixel).

### Implementation for User Story 2

- [X] T008 [P] [US2] Create `web-client/src/main/webapp/manifest.webmanifest` with the fields in data-model.md › Web manifest (`name`, `short_name`, `start_url`, `display`, `background_color`, `theme_color`, `icons`).
- [X] T009 [US2] Add `<link rel="apple-touch-icon" href="apple-touch-icon.png">` and `<link rel="manifest" href="manifest.webmanifest">` to `web-client/src/main/webapp/index.html` (contracts/icon-endpoints.md › Page declarations).
- [X] T010 [US2] In `docker/web/nginx.local.conf.template` allow `/icon-512.png` and `/apple-touch-icon.png` (`no-cache`) and add `location = /manifest.webmanifest` with `default_type application/manifest+json` and `no-cache` (research.md R6; contracts/icon-endpoints.md rows 3–5).
- [X] T011 [US2] Same change in `docker/web/nginx.public.conf.template`.
- [X] T012 [P] [US2] Extend the off-site check in `web-client/Dockerfile` to also scan `target/webapp/manifest.webmanifest` (FR-006).
- [X] T013 [US2] Rebuild web; run quickstart.md V2 (three paths, content types), V3 (view `icon-512.png` and `apple-touch-icon.png`), and V5 via headless Chrome with Pixel emulation (manifest parsed: name "MobiArmy", 192/512 icons); record results; note that a real iPhone check is manual.

**Checkpoint**: US1 and US2 both work.

---

## Phase 5: Polish & Cross-Cutting Concerns

- [X] T014 [P] Note the icon build step in `CLAUDE.md` (web build notes: `web.build.MakeIcons` generates icons from `Army165local/icon.png`; nothing committed).
- [X] T015 Run the full quickstart.md (V1–V6), including V2/V4 against `https://game.amaz005.dev` through the tunnel and the V6 load-time comparison (< 0.1 s); record results in this file.

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 2 (T001 → T002 → T003) blocks both stories: the files must exist before they can be served.
- Phase 3 (US1) and Phase 4 (US2) depend only on Phase 2.
- Phase 5 after the stories you ship.

### User Story Dependencies

- **US1**: needs Phase 2 only.
- **US2**: needs Phase 2 only. It edits the same `index.html` and nginx templates as US1, so if both are done by one person, do US1 first to avoid conflicting edits.

### Within Each User Story

- Page and nginx edits before the rebuild/verify task (T007, T013).

### Parallel Opportunities

- T001 can be written while reading the design (before T002).
- T005 ∥ T006 (two different templates); T008 ∥ T012.

---

## Parallel Example: User Story 1

```text
T005 nginx.local.conf.template icon paths
T006 nginx.public.conf.template icon paths
```

## Parallel Example: User Story 2

```text
T008 manifest.webmanifest
T012 Dockerfile off-site check
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Phase 2: generator + tests + build wiring.
2. Phase 3: declare and serve the tab icon; verify.
3. Stop and check the tab icon in a browser (and through the tunnel).

### Incremental Delivery

1. Phase 2 → US1 (tab icon, the visible gap today) → deploy.
2. US2 (home-screen icon + manifest) → deploy.
3. Polish.

### Parallel Team Strategy

Too small to split: one person, in task order.

---

## Notes

- `Army165local/icon.png` is read, never modified.
- Generated icons live only in `target/` and the image; never commit them.
- Commit after each phase.

## Verification Results (2026-11-22)

- **V1 (T001–T003)**: `mvn -B package` — 26 tests pass (5 new in `MakeIconsTest`); build generates
  `favicon.ico` (ICO, 32×32 PNG), `icon-192.png`, `icon-512.png`, `apple-touch-icon.png` (180×180,
  sky-blue margin); pixels inspected: sharp, no blur (V3).
- **V2 (T007, T013)**: all five paths 200 with `image/x-icon` / `image/png` /
  `application/manifest+json` and `Cache-Control: no-cache`; `If-None-Match` → 304; unknown
  `/nope.png` still 404; page declares all four links. Docker build's off-site check passes
  (pattern now also catches JSON `"src": "http…"`).
- **V4/V5 (headless Chrome, local and `https://game.amaz005.dev`)**: desktop and emulated Pixel 5
  fetch `/favicon.ico` 200; Chrome parses the manifest with 0 errors, name "MobiArmy", icons
  192/512; zero icon 404s in the nginx log. Real-phone home-screen checks (Android, iPhone) not
  done — manual.
- **V6 (T015)**: median of 11 first-visit loads, icons allowed vs blocked: local −23 ms, tunnel
  +49 ms (< 100 ms, SC-004).
