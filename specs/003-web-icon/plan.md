# Implementation Plan: Web Icon

**Branch**: `003-web-icon` | **Date**: 2026-11-22 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `/specs/003-web-icon/spec.md`

## Summary

Give the browser game an icon: tab/bookmark icon, `/favicon.ico`, and phone home-screen icons
named "MobiArmy". All sizes are generated at build time from the original 32×32
`Army165local/icon.png` by integer nearest-neighbour scaling (sharp pixel art), declared in
`index.html` plus a minimal web manifest, and served by nginx with revalidating caching.
Details: [research.md](research.md).

## Technical Context

**Language/Version**: Java 17 build JVM (web-client module compiled with `--release 11`); HTML/JSON
for the page shell

**Primary Dependencies**: `javax.imageio` (JDK, build time only); existing exec-maven-plugin 3.5.0;
nginx 1.27-alpine

**Storage**: N/A (generated files in the image)

**Testing**: JUnit 5.10 in `web-client` (existing); curl + headless Chrome per [quickstart.md](quickstart.md)

**Target Platform**: Chrome/Edge/Firefox desktop, Chrome Android, iPhone Safari (home-screen icon)

**Project Type**: web page shell + build step of the existing `web-client` module

**Performance Goals**: icons add < 0.1 s to first load (SC-004); total icon payload a few KB

**Constraints**: no off-site loads (FR-006); `Army165local/` never modified; nginx allowlist keeps
all other paths 404

**Scale/Scope**: 4 generated images + 1 static manifest; ~5 files changed

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status |
|-----------|--------|
| I. Client protocol compatibility | Pass — no protocol change; the game client and server are untouched. |
| II. Server-authoritative state | N/A — no game state. |
| III. Persistent data integrity | N/A — no database change. |
| IV. Secrets & configuration hygiene | Pass — no secrets; no new configuration. |
| V. Verifiable changes | Pass — icon generation is pure logic with JUnit tests; endpoints and browsers verified per quickstart. |
| VI. Single canonical server codebase | Pass — changes stay in `web-client/` and `docker/web/`. |

Re-check after Phase 1 design: unchanged, all pass. No violations → Complexity Tracking empty.

## Project Structure

### Documentation (this feature)

```text
specs/003-web-icon/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/icon-endpoints.md
└── tasks.md             # /speckit-tasks
```

### Source Code (repository root)

```text
web-client/
├── pom.xml                                   # + exec execution "make-icons" (process-resources, after copy-webapp)
├── src/main/java/web/build/MakeIcons.java    # new: reads icon.png, writes the icon set (scale, pad, ICO)
├── src/test/java/web/build/MakeIconsTest.java# new: scaling exact, padding/background, ICO header
├── src/main/webapp/index.html                # + <link> icon/apple-touch-icon/manifest
└── src/main/webapp/manifest.webmanifest      # new, static
web-client/Dockerfile                         # off-site check also scans manifest.webmanifest
docker/web/nginx.local.conf.template          # allowlist icon paths; manifest content type
docker/web/nginx.public.conf.template         # same
```

**Structure Decision**: Everything lives in the existing `web-client` module and the nginx
templates; the build step follows the `web.build.FixGameAction` pattern (exec-maven-plugin,
build-time only, not part of `client.js`).

## Complexity Tracking

No constitution violations.
