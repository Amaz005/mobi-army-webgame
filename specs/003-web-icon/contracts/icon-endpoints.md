# Contract: Icon Endpoints

Extends [002 web endpoints](../../002-web-client/contracts/web-endpoints.md). Same in local mode,
public mode and through the Cloudflare Tunnel. All are `GET`, served by nginx from the image; no
other new paths are opened (everything else stays 404).

| Path | Status | Content-Type | Cache-Control | Body |
|------|--------|--------------|---------------|------|
| `/favicon.ico` | 200 | `image/x-icon` | `no-cache` | ICO, one 32×32 PNG entry |
| `/icon-192.png` | 200 | `image/png` | `no-cache` | 192×192 PNG |
| `/icon-512.png` | 200 | `image/png` | `no-cache` | 512×512 PNG |
| `/apple-touch-icon.png` | 200 | `image/png` | `no-cache` | 180×180 opaque PNG |
| `/manifest.webmanifest` | 200 | `application/manifest+json` | `no-cache` | JSON per [data-model.md](../data-model.md) |

Conditional requests (`If-None-Match`) for unchanged files return 304.

## Page declarations (`/` → `index.html` `<head>`)

```html
<link rel="icon" href="favicon.ico" sizes="32x32">
<link rel="icon" href="icon-192.png" type="image/png" sizes="192x192">
<link rel="apple-touch-icon" href="apple-touch-icon.png">
<link rel="manifest" href="manifest.webmanifest">
```

All links are relative (same site; FR-006). The Docker build's off-site check also covers
`manifest.webmanifest`.
