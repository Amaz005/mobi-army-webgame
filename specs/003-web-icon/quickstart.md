# Quickstart: Web Icon validation

Prerequisites: the stack runs (`docker compose up -d --build`, README). Base URL below is
`http://localhost:8080`; repeat V2 with `https://game.amaz005.dev` when the tunnel is up.

## V1 — Unit tests (build)

```
docker run --rm -v "$PWD":/w -v mobiarmy-m2:/root/.m2 -w /w/web-client maven:3.9-eclipse-temurin-17 mvn -B test
```
Expected: icon tests pass (integer scaling is exact, padding/background correct, ICO header valid).

## V2 — Endpoints

```
for p in favicon.ico icon-192.png icon-512.png apple-touch-icon.png manifest.webmanifest; do
  curl -s -o /dev/null -w "$p %{http_code} %{content_type}\n" -D - http://localhost:8080/$p | grep -i cache-control
done
```
Expected: every path 200 with the Content-Type and `Cache-Control: no-cache` from
[contracts/icon-endpoints.md](contracts/icon-endpoints.md). A repeat with `If-None-Match` → 304.

## V3 — Pixels are sharp

Open `/icon-512.png` and `/apple-touch-icon.png`: hard square pixels, no blur; the iPhone icon has a
sky-blue background and margin, the others are transparent.

## V4 — Browser tab (US1)

Open the base URL in Chrome, Edge and Firefox: the tab shows the soldier icon. Headless check: the
nginx access log shows no 404 for any icon path during the visit (SC-002).

## V5 — Home screen (US2)

- Android Chrome: menu → Add to Home screen → shortcut named "MobiArmy" with the sharp icon.
- iPhone Safari: Share → Add to Home Screen → the icon on a sky-blue background (not a page screenshot).

## V6 — Load time (SC-004)

Compare first-visit load time before/after in headless Chrome; the difference stays under 0.1 s.
