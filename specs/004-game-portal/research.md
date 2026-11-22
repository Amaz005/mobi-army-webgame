# Research: Web Game Portal

Findings from the current code (2026-11-22) and the decisions they lead to.

## Current state that shapes the design

- `account` table (`mobiarmy-java/dbarmy.sql`): `username VARCHAR(20)`, `password VARCHAR(50)`,
  **plaintext**, **no unique constraint**. The seed has two rows named `a` (ids 1 and 2);
  `User.login` picks one of them arbitrarily (`LIMIT 1`, no `ORDER BY`).
- `User.login` checks `WHERE username=? AND password=?`. `ClientEntry` logs the typed password in
  plain text (`"... pass: " + pass`).
- In-game registration (`Registration`, command 35 + next login): username `^[a-z0-9]{1,20}$`
  (lowercased), password `^[a-zA-Z0-9]{1,50}$`. The login handler accepts only `^[a-zA-Z0-9]+$`
  for both fields, so these are the characters the client can actually log in with.
- The web client (`web-client/src/main/webapp/`) uses only relative asset paths and builds its
  WebSocket URL as `location.host + '/game'`, so it works from any sub-path. Its manifest has
  `start_url: "."`. Its RecordStore lives in `localStorage`.
- nginx (`docker/web/nginx.*.conf.template`) serves the client at `/` with a strict allowlist,
  `/game` → `relay:6080`, everything else 404. Per-IP limits already exist for `/game`.
- Online players: `ServerManager.clients` (in-memory, `ClientEntry.login == true`). The DB
  `online` flag is not reliable (stays `1` after a crash).
- Level: `User.levelFromExpValue(exp)` (package-private, pure).

## Decisions

### D1. Portal backend lives in the game server, on the JDK's built-in HTTP server

- **Decision**: Add a small HTTP JSON API to `mobiarmy-java` using `com.sun.net.httpserver`
  (module `jdk.httpserver`, part of the JDK/JRE; no new Maven dependency). It listens on an
  internal port (`portal-port`, default `8081`), reachable only on the compose network; nginx
  proxies `/api/` to it.
- **Rationale**: Accounts, password checks, the online count and level calculation already live
  in this server. Sharing them guarantees portal and game accept exactly the same credentials
  (FR-016) and read the online count from the game's own state (FR-008). Constitution VI: one
  canonical codebase, no new framework.
- **Alternatives rejected**: a separate service (Node/Spring) — duplicates the account and
  password logic, adds a service and a stack, violates VI; nginx reading the DB directly — not
  possible without another component; PostgREST — new service, exposes tables.
- **Consequence**: when the game server is down, portal login/registration and live info are down
  too (the static portal still loads; FR-005, SC-002). Accepted: without the server nobody can
  play anyway.
- **To verify at implementation**: `jdk.httpserver` is present in the `eclipse-temurin:17-jre`
  runtime image (`java --list-modules`).

### D2. Password hashing: PBKDF2-HMAC-SHA256 from the JDK

- **Decision**: `PBKDF2WithHmacSHA256`, 600 000 iterations, 16-byte random salt, 32-byte key.
  Stored as `pbkdf2_sha256$<iterations>$<salt b64>$<hash b64>` (~95 chars). Comparison with
  `MessageDigest.isEqual`. One shared `PasswordHash` class is used by `User.login`,
  `Registration` and the portal API.
- **Rationale**: Salted and slow (constitution IV), in the JDK (no dependency), iteration count
  per current OWASP guidance for PBKDF2-SHA256. Iterations are stored per hash, so they can be
  raised later.
- **Alternatives rejected**: bcrypt/argon2 — need a new library (allowed, but no benefit worth a
  dependency here); pgcrypto `crypt()` — Postgres-only, breaks the SQLite path.
- **Cost**: ~0.2–0.5 s CPU per login. Fine at this scale; rate limits (D6) stop it being used to
  burn CPU.

### D3. Migrating existing plaintext passwords

- **Decision**:
  1. SQL migration `mobiarmy-java/sql/migrations/004-portal-accounts.sql` widens
     `password` to `VARCHAR(255)`, renames duplicate usernames, adds a unique index.
  2. A one-shot Java tool `com.mobiarmy.server.HashPasswords` hashes every row whose password is
     not yet in `pbkdf2_sha256$…` form, in one transaction, and prints only the count.
  3. The operator runs both after a `pg_dump` backup (constitution III), as documented in
     quickstart.md and README.
  4. `dbarmy.sql` is updated so fresh installs start with hashed seed passwords and the new
     column size/index — no plaintext ever on a new stack.
  5. Until step 2 has run, login still accepts legacy plaintext rows, and the server logs a
     warning with the number of unhashed accounts at startup. Once a legacy row logs in
     successfully it is re-hashed immediately (so a forgotten step 2 still converges).
- **Rationale**: no player has to reset (FR-018); destructive bulk update is explicit and preceded
  by a backup instead of silently running at startup.
- **Alternatives rejected**: auto-migrate at every startup — a bulk update without a backup step;
  lazy re-hash only — plaintext would stay forever for inactive accounts (fails SC-007).

### D4. Duplicate usernames and uniqueness

- **Decision**: Keep the lowest `id` for each duplicated name; rename the others to
  `substr(username, 1, 20 - length(id)) || id` (e.g. seed id 2 → `a2`). Then
  `CREATE UNIQUE INDEX account_username_uq ON account (username)`. Registration (both paths)
  keeps the `INSERT … WHERE NOT EXISTS` and also treats a unique-violation as "name taken", so
  simultaneous registrations of one name give exactly one success (spec edge case).
- **Seed effect**: README's test accounts become `a`/`1` (id 1), `a2`/`1` (id 2, superadmin),
  `nguoiduatin`/`3`.

### D5. Login name and password rules

- **Decision**: Login name `^[a-z0-9]{3,20}$` on the portal (the portal lowercases input first);
  password `^[a-zA-Z0-9]{6,50}$`. The in-game registration path keeps its current rules
  (unchanged behavior for phone players). Existing short names/passwords keep working for login
  everywhere.
- **Rationale**: Only letters and digits pass the game's login check, and the name must fit
  `VARCHAR(20)`, so every portal account can log in to MobiArmy (FR-015). Minimum lengths stop
  trivially guessable new accounts on a public site.

### D6. Portal sessions, CSRF and rate limits

- **Session**: random 32-byte token in an `HttpOnly; SameSite=Strict; Path=/api` cookie
  (`Secure` in public mode). Sessions are kept in server memory (map token → account id, last
  use); idle timeout `portal-session-idle-minutes` (default 30), absolute max 7 days. A server
  restart logs everyone out of the portal — acceptable, game sessions are unaffected.
- **CSRF**: `SameSite=Strict`, plus state-changing endpoints require `Content-Type:
  application/json` and an `Origin` equal to the request host.
- **Rate limits** (two layers):
  - nginx `limit_req` zone `portal_api` per visitor IP on `/api/` (default `WEB_API_RATE=30r/m`,
    burst 10) — reuses the `CF-Connecting-IP` handling already in place.
  - In the server, per login name: after 5 failed portal logins in 15 minutes, further attempts
    for that name are refused for 15 minutes (in memory). Registrations per IP: at most 5 per
    hour (in memory, keyed by `X-Real-IP` that nginx sets).
- **Generic errors**: login failure always returns the same body and status (FR-019).
- **Password change**: requires the current password; does not drop an open game session (spec
  edge case). Other portal sessions of that account are ended.

### D7. URL layout: portal at `/`, MobiArmy page at `/mobiarmy/`, game at `/mobiarmy/choi/`

- **Decision**: `/` serves the portal; the existing web client moves to `/mobiarmy/choi/` (no code
  change needed: relative assets, WebSocket still `/game`). Old game URLs (`/index.html`,
  `/client.js`, `/play.css`, `/keypad.js`, `/manifest.webmanifest`) answer `301` to
  `/mobiarmy/choi/…`. `/mobiarmy/` is the MobiArmy game page (D12). The MobiArmy page shell gets a small "← Cổng game" link to `/` (FR-006); the
  game bytecode is untouched.
- **FR-007 interpretation**: the old direct address *was* `/`, which now is the portal itself
  (MobiArmy one click away). The 002 manifest uses `display: "browser"`, so a home-screen shortcut is a
  plain bookmark of its `start_url`: shortcuts made before 004 open `/` and land on the portal, one tap
  from the game (they can't be told apart from a normal visit, so no redirect is attempted). New
  shortcuts use the manifest re-served under `/mobiarmy/choi/` and open the game directly. spec.md
  FR-007 is reworded to match.
- **Storage**: the web client's `localStorage` keys are on the same origin as the portal; the
  portal itself keeps only `portal.welcomeSeen` (welcome dialog closed once) and its session cookie is `HttpOnly` (and scoped
  to `/api`), so the game can't read it. Future games MUST namespace storage keys under their
  game id (catalog contract); full origin isolation for untrusted games is out of scope (launch
  ships MobiArmy only, FR-014).

### D8. Game catalog

- **Decision**: a static `portal/games.json` (array of entries, see contracts/game-catalog.md)
  plus per-game assets under `portal/games/<id>/`. The `portal/` directory is bind-mounted
  read-only into the web container, so adding/hiding a game is: edit files → `docker compose
  restart web` (SC-003). The portal page renders the cards client-side from the JSON with plain
  JS (no framework, no outside site — FR-012).
- **Rationale**: no database or admin UI needed (spec assumption); one editable place (FR-003).
- **Failure handling**: entries with missing required fields are skipped; a missing picture
  shows a placeholder; if `games.json` itself fails to parse, the page shows "Chưa có trò chơi"
  plus an error note (FR-005).

### D9. Live information

- **Online count**: `GET /api/mobiarmy/status` → number of `ServerManager.clients` with
  `login == true`, computed per request (cheap); nginx caches it for 10 s
  (`proxy_cache_valid 10s`) so a busy home page doesn't hit the server per view. Portal refreshes
  every 30 s while visible → within SC-004's 60 s.
- **Ranking**: `GET /api/mobiarmy/ranking` → top 10 non-superadmin accounts by `exp DESC, id ASC`,
  with name, level (`levelFromExpValue`) and exp; cached in the server for 60 s. The in-game name
  shown to other players *is* the account's `username` (message 87), so showing it reveals
  nothing beyond what any opponent in a room sees; no other column is selected.
- **Offline**: if `/api/` is unreachable nginx returns 502/504; the portal shows "Máy chủ tạm
  dừng" (US4 scenario 2).

### D10. nginx must start even when the game server is down

- **Decision**: proxy `/api/` via a variable (`set $portal_api http://server:8081;
  proxy_pass $portal_api;`) with `resolver 127.0.0.11 valid=10s;` so nginx resolves `server` per
  request instead of failing at startup when the container is absent (keeps 002 FR-007 / 001
  FR-013: web failure and server failure are independent).

### D11. Logging

- Remove the plaintext password from the `ClientEntry` login log line (constitution IV).
- Log portal registrations (account id, name), password changes (account id), login lockouts
  (name, IP) — never passwords or tokens.

### D12. Per-game pages (layout reference: dautri.com)

- **Input**: the user pointed to https://dautri.com/ as a reference. Only its text could be read
  (the page renders with JavaScript): a plain list of games on the home page, and one page per game
  at a short path (`/caro`, `/domino`) with title "Chơi <game> online miễn phí", a tagline with the
  number of players ("2 người, miễn phí, không cần tải app"), "Cách chơi" sections and a
  "Trò chơi khác" list. Its visual style, account UI and rankings were not visible.
- **Decision**: copy that *structure*, not the site: home page of cards → game page
  `/<id>/` → "Chơi ngay" → `playUrl`. One shared `portal/game.html` serves every `/<id>/` (nginx
  maps any single-segment path to it), and fills it from the catalog entry (`tagline`, `players`,
  `howToPlay`, other visible games). Adding a game stays a catalog edit (FR-003, FR-001a).
  The MobiArmy card shows online count and the ranking sits on the MobiArmy game page.
- **Not copied**: name, logo, colours, texts, art. Its room/invite-link features don't apply:
  MobiArmy rooms live inside the fixed client.
- **Alternatives rejected**: one static HTML file per game — duplicate markup per game, and
  adding a game would need page code; server-side rendering — no server-side templating in the
  stack, and the catalog is small enough to render in the browser.

### D13. Visual layout (user screenshot of dautri.com, 2026-11-22)

- **Adopted** (layout patterns only):
  - left sidebar with brand, menu ("Trò chơi", "Hướng dẫn", "Bảng xếp hạng") and an account box at
    the bottom; on phones it becomes a top bar with a horizontal menu
  - a large centred headline and a highlighted banner into the featured game
  - a first-visit welcome dialog that lists what an account gives, with "Tạo tài khoản",
    "Đăng nhập" and "Xem trước, để sau"
  - login, register and password forms as dialogs (`#dang-nhap`, `#dang-ky`, `#tai-khoan`)
  - an online line at the bottom of the home page; a dark theme with purple buttons and gold accents
- **Not copied**: the name, logo, mascot, wording, the community banner, social links, avatars and
  profile features. The benefits listed are our own: one account, saved level and experience,
  ranking, PC and phone.
- **Storage**: the welcome dialog shows once per browser, remembered in `localStorage` under
  `portal.welcomeSeen`. Access is wrapped in try/catch, so the page works without storage.

### D14. Play page inside the portal (user screenshot of an in-game screen, 2026-11-22)

- **Adopted**: while playing, the portal sidebar stays on the left and the game sits in the middle.
  A bottom bar holds the game name, a red **"Rời game"** button (back to `/<id>/`), and tools:
  key help panel and full screen. The online count sits in the top-right corner. On phones the
  sidebar is hidden so the game gets the screen.
- **How**: `"Chơi ngay"` → `/<id>/choi/` (portal `play.html`), which loads the catalog `playUrl` in a
  same-site `<iframe>`. The MobiArmy client moved to `/mobiarmy/client/`; it is unchanged except that
  its own "← Cổng game" link hides itself when framed. The play page keeps keyboard focus in the game,
  on load and after clicks on the bar or empty space, so keys reach the client without clicking it
  first. Query strings are passed through, e.g. `?keypad=1`.
- **Not done**: the reference shows the room roster and the player list beside the board. For MobiArmy
  those live inside the fixed client and its protocol, so the portal can't show them (constitution I).
- **Home-screen installs** made from the client manifest (`/mobiarmy/client/`) open the bare client
  in full screen, which suits phones.

### D15. Skip the in-game login when already logged in on the portal (user request, 2026-11-22)

- **Problem**: the portal never knows the real password (it stores only hashes), and the client and
  its protocol are fixed.
- **Decision**: a one-time **game ticket**.
  1. **Issue**: `POST /api/mobiarmy/ticket` with a portal session returns `{username, ticket}`. The
     ticket is 24 letters/digits, valid 2 minutes, bound to the account, in server memory
     (`server/GameTickets`).
  2. **Fetch**: the client page shell `/mobiarmy/client/` asks for a ticket before starting the game
     (waits at most 3 s) and leaves it in `window.mobiarmyAutoLogin`.
  3. **Use**: a build-time patch `web.build.AutoLoginPatch` (ASM, like `FixGameAction`, original
     bytecode untouched) rewrites the login screen `coreLG.dl.d()`. While a ticket is pending it:
     - answers "Bật âm thanh?" with "Bật", keeping the saved volume;
     - fast-forwards the intro, like "Qua nhanh";
     - then fills the name and ticket fields and calls `g()`, the "Đăng nhập" action.
  4. **Accept**: the normal login message (command 1) carries the ticket as the password.
     `Accounts.checkLogin` accepts the password **or** a valid ticket for that account, and consumes
     the ticket.
  5. **Don't remember it**: the client remembers the last password in RMS `caropass`; the web layer's
     `RecordStore` refuses to store the ticket there (`web.AutoLogin.isTicket`).
- **Properties**:
  - The wire protocol is unchanged (constitution I).
  - A ticket never becomes the stored password and never triggers a re-hash.
  - Not logged in on the portal → 401 → the usual login screen.
  - After logging out in-game the ticket is used up, so the player can log in by hand as another
    account.
  - Phone clients are unaffected.
- **Alternatives rejected**:
  - Sending the real password to the client: the portal doesn't have it.
  - Simulating key presses on a timer: the login screen's state can't be seen from outside, so this
    is fragile.
  - A new protocol command: needs a client change.

### D16. Portal sessions as JWT (user request, 2026-11-22)

- **Problem**: sessions in server memory (D6) ended on every server restart or deploy.
- **Decision**: the session cookie holds a signed JWT, HS256 with `PORTAL_JWT_SECRET` from `.env`
  (`portal/PortalTokens`). It is built on the JDK's HMAC and Jackson, so no new dependency. The
  header must match exactly, so `alg: none` and algorithm switching are rejected.
- **Timeouts**:
  - Idle: `exp` = issue time + `portal-session-idle-minutes`. A token older than 5 minutes is
    re-issued in a response cookie, so active players stay logged in.
  - Absolute: 7 days after the login (`auth`).
- **Revocation without server state**:
  - Password change: `pv`, an HMAC fingerprint of the stored password hash, is checked against the
    database on every use. Changing the password therefore ends every other session, and the current
    session gets a new cookie in the response.
  - Log-out: the cookie is cleared and the `jti` is denied in memory until `exp`.
- **Missing or short secret**: a random key is used for this run only, with a startup warning, so the
  old behaviour returns. The secret is never logged or committed (constitution IV).
- **Unchanged**: game tickets (D15) and login throttles stay in memory. They are short-lived, so a
  restart only costs an in-flight ticket.
- **Alternatives rejected**:
  - Storing sessions in PostgreSQL: a new table and migration, plus writes on every request.
  - A JWT library (jjwt, nimbus): a new dependency for about 150 lines of code.
