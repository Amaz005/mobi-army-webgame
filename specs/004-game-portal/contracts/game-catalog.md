# Contract: Game Catalog (`portal/games.json`)

The one operator-editable list of games (FR-003). Served as `/games.json`; read by the home page
and by the shared game page (`/<id>/`, FR-001a). Edit, then `docker compose restart web`.

```json
[
  {
    "id": "mobiarmy",
    "name": "MobiArmy",
    "description": "Bắn súng theo lượt, đấu cùng bạn bè.",
    "tagline": "Miễn phí, chơi ngay trên trình duyệt, không cần tải app.",
    "players": "2–8 người",
    "image": "/icon-192.png",
    "playUrl": "/mobiarmy/client/",
    "howToPlay": [
      { "title": "Cách chơi", "text": "Chọn phòng, vào trận, căn góc và lực bắn để hạ đối thủ." },
      { "title": "Điều khiển", "text": "Phím mũi tên để di chuyển và căn góc, Enter để bắn, F1/F2 là hai phím mềm." }
    ],
    "order": 1,
    "status": "visible",
    "live": "mobiarmy"
  }
]
```

| Field | Required | Rule |
|-------|----------|------|
| `id` | yes | `^[a-z0-9-]{1,32}$`, unique, not a reserved path (`api`, `game`, `games`); it is the game page address `/<id>/`, the asset folder `portal/games/<id>/`, and the prefix the game must use for its `localStorage` keys |
| `name` | yes | Card and page heading; page title becomes "Chơi <name> online miễn phí" |
| `description` | yes | Card text, ≤ 160 characters shown |
| `tagline` | no | One line under the heading on the game page |
| `players` | no | Shown on card and game page (free text, e.g. "2–8 người") |
| `image` | no | Same-site path (e.g. `/games/<id>/card.png`, or `/icon-192.png` as MobiArmy uses); missing or failing to load → placeholder |
| `playUrl` | yes | Same-site path starting with `/` (no `//`, no scheme): the game itself. "Chơi ngay" opens the play page `/<id>/choi/`, which shows `playUrl` in a same-site frame inside the portal layout (sidebar, "Rời game" bar) |
| `howToPlay` | no | List of `{title, text}` sections shown on the game page; `text` may contain blank-line paragraphs |
| `order` | no | Ascending; ties by `name` |
| `status` | no | `visible` (default); `maintenance` — card and game page show "Đang bảo trì", no "Chơi ngay"; `hidden` — no card, game page shows "Không tìm thấy trò chơi" |
| `live` | no | Live-info source; only `"mobiarmy"` exists now (online count + ranking) |

Rules:
- All text is plain text: rendered with `textContent`, never as HTML.
- An entry missing a required field or breaking a rule is skipped (reported in the browser
  console); the others still render (FR-005).
- "Trò chơi khác" on a game page = the other visible entries, in catalog order.
- Unknown fields are ignored. Unknown `/<id>/` → "Không tìm thấy trò chơi" with a link home.
- File fails to load or parse → "Chưa có trò chơi" + a short note; no blank page.
- Adding a game: put its files under `portal/games/<id>/` (served at `/games/<id>/…`, so
  `playUrl` is `/games/<id>/…`) or at its own path added to the nginx allowlist; add the entry;
  restart `web`. Each new game is its own feature (FR-014). The MobiArmy wording above is a draft
  for the operator to adjust.
