# Feature Specification: Web Game Portal

**Feature Branch**: `004-game-portal`

**Created**: 2026-11-22

**Status**: Draft

**Input**: User description: "chuyển đổi sang webgame" — refined in chat: "Trang chủ cổng game. Tôi
muốn tạo 1 trang web với nhiều trò chơi bên trong bao gồm cả trò hiện tại" (a game-portal home
page: one website hosting several games, including the current one).

## Clarifications

### Session 2026-11-22

- Q: Which games ship at launch besides MobiArmy? → A: None. MobiArmy only, with the catalog
  ready for more games later; each new game is its own feature (FR-014).
- Q: Is there a portal account? → A: Yes. One portal account, created on the website, that also
  logs into MobiArmy and future games (FR-015 to FR-020, US2).

## Context

The browser build of the original MobiArmy client already exists (`002-web-client`) and is
served at the stack's web address. This feature puts a **portal** in front of it: a home page
that lists games, lets a visitor pick one and play it in the browser, and shows a little live
information (players online, rankings). MobiArmy is the first game in the catalog; the portal
is built so more games can be added later.

Layout reference (user, 2026-11-22): https://dautri.com/. It is a text-first, Vietnamese game
index. Each game has its own page at a short address (e.g. `/caro`) with a title, a one-line
tagline ("2 người, miễn phí, không cần tải app"), the number of players, a "Cách chơi" section and
a "Trò chơi khác" list. This portal follows that structure only; it uses none of that site's
name, branding, text or art. The visual layout (sidebar, welcome dialog, dialog forms) follows a
screenshot the user shared of the same site (research D13).

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Pick a game from the portal and play it (Priority: P1)

A visitor opens the website and lands on the portal home page instead of going straight into
MobiArmy. They see the games on offer as cards (name, picture, short description). Clicking
MobiArmy opens its game page: name, tagline, number of players, a "Chơi ngay" button, a
"Cách chơi" section and a "Trò chơi khác" list. "Chơi ngay" opens the game exactly as it runs
today, and they can log in and play.

**Why this priority**: This is the portal itself. Without it there is just one game at one
address. It is also the smallest slice that delivers value and keeps today's game reachable.

**Independent Test**: With the stack running, open the site's root address in a fresh browser
profile; the portal page appears; clicking the MobiArmy card opens its game page; "Chơi ngay"
reaches the MobiArmy login screen; log in as a seeded account and reach the lobby.

**Acceptance Scenarios**:

1. **Given** the stack is running, **When** a visitor opens the site's root address, **Then** the
   portal home page appears with one card per available game, MobiArmy included.
2. **Given** the portal page, **When** the visitor chooses MobiArmy, **Then** its game page shows
   the name, tagline, number of players, "Chơi ngay", "Cách chơi" and "Trò chơi khác", and the
   browser tab title reads like "Chơi MobiArmy online miễn phí".
3. **Given** the MobiArmy game page, **When** the visitor presses "Chơi ngay", **Then** the
   MobiArmy client loads and behaves as in `002-web-client` (login, lobby, matches, controls).
4. **Given** a player is in a MobiArmy game, **When** they use the portal's "back to portal"
   link, **Then** they return to the home page (their game connection ends, as when closing
   the game).
5. **Given** someone saved an old MobiArmy link (e.g. `/index.html`), **When** they open it, **Then**
   they reach MobiArmy (a link or shortcut to the bare site root now opens the portal).

---

### User Story 2 - One portal account for every game (Priority: P1)

A new visitor creates an account on the portal (login name + password). They can log in to the
portal with it, and the same login name and password work on the MobiArmy login screen right
away. Existing MobiArmy players log in to the portal with the MobiArmy account they already
have. A logged-in user can change their password from the portal, and the change applies in
MobiArmy too.

**Why this priority**: Today nobody can register from the browser, so new players can't play at
all without help from the operator. One account for the whole site is also what makes it one
portal rather than a list of links.

**Independent Test**: Register a new account on the portal, log out and back in, then open
MobiArmy and log in with the same login name and password and reach the lobby. Log in to the
portal with a seeded MobiArmy account. Change the password on the portal; the old one no longer
works in MobiArmy and the new one does.

**Acceptance Scenarios**:

1. **Given** a visitor on the portal, **When** they register with a free login name and a valid
   password, **Then** the account is created, they are logged in to the portal, and the same
   credentials log in to MobiArmy.
2. **Given** a login name that is already taken (by a portal or MobiArmy account), **When** a
   visitor tries to register it, **Then** registration is refused with a clear message and no
   account is changed.
3. **Given** an existing MobiArmy account, **When** its owner logs in to the portal with the same
   credentials, **Then** they are logged in, with no separate sign-up.
4. **Given** a wrong password, **When** someone tries to log in to the portal, **Then** they get
   one generic "sai tài khoản hoặc mật khẩu" message that doesn't reveal whether the login name
   exists.
5. **Given** a logged-in user, **When** they change their password (entering the current one),
   **Then** the new password works on the portal and in MobiArmy and the old one stops working
   in both.
6. **Given** a logged-in portal user, **When** they view the home page, **Then** they see their
   login name and a log-out action.

---

### User Story 3 - Add a new game to the catalog (Priority: P2)

The site operator adds another game to the portal by providing its files and a catalog entry
(name, picture, description, address). After a restart or reload of the web part of the stack,
the new game appears on the home page and can be played. A game can also be hidden (e.g. under
maintenance) without deleting it.

**Why this priority**: The user wants "many games"; the portal is only worth having if adding
a game is a routine step, not a redesign. It comes after US1 because one game already proves
the page works.

**Independent Test**: Add a second, small placeholder game to the catalog, restart the web
service, and confirm it appears and opens; mark it hidden and confirm it disappears from the
home page while MobiArmy is unaffected.

**Acceptance Scenarios**:

1. **Given** a new game's files and catalog entry, **When** the operator applies them and
   restarts the web service, **Then** the game appears on the home page in the configured order.
2. **Given** a game marked hidden or "under maintenance", **When** a visitor opens the home page,
   **Then** that game is not playable from it (hidden, or shown as "Đang bảo trì" without a play
   button).
3. **Given** a broken catalog entry (missing picture, missing game files), **When** the home page
   loads, **Then** the other games still show and work; the broken one is skipped or shown with
   a placeholder.

---

### User Story 4 - See live MobiArmy information on the portal (Priority: P3)

On the home page, the MobiArmy card shows how many players are online now, and a ranking page
or panel lists the top players (by experience/level). If the game server is down, the card says
so instead of showing a wrong number.

**Why this priority**: It makes the portal feel alive and draws players in, but the portal is
fully usable without it.

**Independent Test**: Log in two accounts in MobiArmy; the home page shows 2 players online
within the refresh time in SC-004. Stop the game server; the card shows it as offline. The
ranking lists seeded accounts in order of experience.

**Acceptance Scenarios**:

1. **Given** N players are logged in to MobiArmy, **When** a visitor views the home page,
   **Then** the MobiArmy card shows N players online (within SC-004's delay).
2. **Given** the game server is stopped, **When** a visitor views the home page, **Then** the card
   shows "Máy chủ tạm dừng" (or similar) and no player count, and the page itself still works.
3. **Given** players with different experience, **When** a visitor opens the ranking, **Then** the
   top players are listed by level/experience with their in-game names only.

---

### Edge Cases

- The portal is up but the MobiArmy server is down: the home page still loads; MobiArmy shows as
  offline (US4) and the game itself shows its own "can't reach server" message as today.
- A visitor on a phone: the home page is readable and usable at phone width; games open as
  they already do on that device.
- The catalog lists no visible games: the home page shows a plain "Chưa có trò chơi" message,
  not a blank or broken page.
- Live info takes too long to fetch: the home page shows the game cards first and fills in
  numbers when ready, or leaves them out.
- A game loaded from the portal must not be able to read the portal's login session (it is kept
  out of reach of page scripts). Games share one site, so each keeps its saved data under its own
  name prefix; full isolation of untrusted games is out of scope (only MobiArmy ships).
- The ranking must never show passwords, account login names if they differ from in-game names,
  or any other private account data.
- Someone scripts thousands of registrations or password guesses: the portal limits attempts per
  visitor and per account, and the game keeps working for everyone else.
- A login name the portal would accept but the MobiArmy client can't type or display (length,
  characters): registration refuses it, so every portal account can actually log in to MobiArmy.
- A user changes their password while logged in to MobiArmy: the current game session isn't
  dropped; the new password applies from the next MobiArmy login.
- Two visitors register the same free login name at the same moment: exactly one succeeds.
- A user forgets their password: there is no self-service reset in this feature (no e-mail or
  phone is collected); the portal says to contact the operator.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The site's root address MUST show a portal home page listing every visible game in
  the catalog, each with name, picture and short description, linking to that game's page.
- **FR-001a**: Each visible game MUST have its own page at a short address (`/<game-id>/`) that
  shows the name, a tagline, the number of players, a "Chơi ngay" action, a "Cách chơi"
  (how-to-play) section and a "Trò chơi khác" list of the other visible games (empty at launch),
  and sets a descriptive browser title. All of it MUST come from the catalog entry, so a new game
  needs no new page code.
- **FR-002**: MobiArmy MUST be in the catalog and MUST play exactly as in `002-web-client`; the
  portal MUST NOT change the game client, the game server's protocol, or how phone clients
  connect (constitution I).
- **FR-003**: The catalog of games MUST be defined in one operator-editable place (configuration
  plus each game's files), so adding, reordering, hiding or removing a game needs no change to
  the portal page itself.
- **FR-004**: Each catalog entry MUST support a status: visible, under maintenance (shown, not
  playable), or hidden.
- **FR-005**: A broken or missing game MUST NOT stop the home page or the other games from
  working.
- **FR-006**: Every game page MUST offer a way back to the portal home page.
- **FR-007**: The site root becomes the portal (MobiArmy one click away). Every other previous
  MobiArmy address (its page and files) MUST keep reaching MobiArmy, by redirect if it moves.
  Home-screen shortcuts made before this feature point at the root and open the portal.
- **FR-008**: The MobiArmy card MUST show the number of players currently online, or an
  "offline" state when the game server can't be reached. The figure MUST come from the game
  server's own state, not from the browser.
- **FR-009**: The portal MUST offer a MobiArmy ranking of top players (at least the top 10) by
  level/experience, showing only in-game name and the ranked values.
- **FR-010**: Live information (online count, ranking) MUST be read-only and MUST NOT expose
  account credentials, login names or other private data, nor allow any change to game data.
- **FR-011**: Portal text MUST be in Vietnamese, UTF-8 end to end.
- **FR-012**: The portal MUST be served by the existing stack under the same start/stop commands,
  over the same local and public (HTTPS) setups as `002-web-client`, with everything it loads
  served by the stack itself (no outside website needed at play time).
- **FR-013**: The home page MUST work in the browsers supported by `002-web-client` (current
  Chrome, Edge, Firefox on desktop; Chrome on Android) and be usable at phone width.
- **FR-014**: At launch the catalog MUST contain MobiArmy only. Adding further games is done
  through FR-003 and each new game is a separate feature; this feature ships no other game.
- **FR-015**: Visitors MUST be able to register a portal account on the website with a login name
  and password. The rules for login names and passwords MUST be ones the MobiArmy client can
  type and accept, so every portal account can log in to MobiArmy.
- **FR-016**: A portal account and a MobiArmy account MUST be the same account: one login name,
  one password, usable on the portal and on the MobiArmy login screen (browser and phone
  clients alike). Existing MobiArmy accounts MUST work on the portal without signing up again.
  Login names MUST be unique across the whole site.
- **FR-017**: Logged-in users MUST be able to log out and to change their password (by entering
  the current one). The change MUST apply to the portal and to MobiArmy.
- **FR-018**: Passwords MUST be stored only as salted, slow hashes, for new and existing accounts
  alike (constitution IV); existing plaintext passwords MUST be migrated without players having
  to reset them.
- **FR-019**: Login failures MUST give one generic message that doesn't reveal whether the login
  name exists. Registration and login attempts MUST be rate-limited per visitor and per account.
  The portal session MUST end on log-out and after a period of inactivity.
- **FR-020**: New accounts MUST start with the same in-game starting state as accounts created
  any other way today (starting character, currency, experience); the portal MUST NOT let anyone
  set those values.

### Key Entities

- **Game catalog entry**: one game on the portal — id (its page address), name, picture, short
  description, tagline, number of players, how-to-play sections, play address, display order,
  status (visible / maintenance / hidden), optional live-info source.
- **Game**: the playable files behind an entry; MobiArmy's is the existing web client; each game
  is self-contained and isolated from the others.
- **Account**: one per player for the whole site — login name (unique), password (hashed only),
  creation time; is the player's MobiArmy account; future games link their player data to it.
- **Portal session**: a logged-in browser on the portal — belongs to one account, ends on log-out
  or inactivity; separate from a MobiArmy game connection.
- **Live game status**: per game, a read-only snapshot — reachable or not, players online, and
  the time it was taken.
- **Ranking entry**: in-game name, level, experience, position; derived from the game's own data,
  never edited from the portal.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A first-time visitor goes from opening the root address to the MobiArmy lobby in
  under 2 minutes, with no install step (same bar as `002-web-client` SC-001).
- **SC-002**: The portal home page shows its game cards in under 3 seconds on a local network,
  first visit, even when the game server is down.
- **SC-003**: The operator adds a new game to the catalog and has it playable from the home page,
  with its own game page, in under 15 minutes, without editing the portal page itself.
- **SC-004**: The online count on the home page matches the server's actual count within 60
  seconds, in 5 out of 5 checks.
- **SC-005**: 100% of the existing MobiArmy acceptance checks from `002-web-client` (login, full
  match, saved experience, keyboard/touch controls) still pass when the game is opened from the
  portal.
- **SC-006**: A new visitor registers on the portal and reaches the MobiArmy lobby with that
  account in under 3 minutes.
- **SC-007**: 100% of accounts (seeded, registered on the portal, or created before this feature)
  log in to both the portal and MobiArmy with the same credentials, and none has a password
  stored in readable form.
- **SC-008**: When one visitor sends registration or login attempts faster than the configured
  limit, every attempt above the limit is refused, and other players keep registering, logging
  in and playing normally.
- **SC-009**: No portal page or live-info response ever contains a password, another user's login
  name or other private account field (verified by inspecting every response type).

## Assumptions

- "Webgame" means a website with a game catalog (portal), not a rewrite of MobiArmy; MobiArmy
  stays the original client running in the browser as built in `002-web-client`. A from-scratch
  recreation of MobiArmy is out of scope.
- Any game added to the catalog is one the project owner has the right to host; the portal does
  not provide games itself.
- The game server, its protocol, the database and the relay from 001/002 are reused; reading the
  online count and ranking is allowed to need a small read-only addition on the server side or
  the database, but no change to what phone/browser clients see.
- Ranking uses data the server already persists (level/experience); currency rankings wait until
  xu/luong are fully persisted.
- No comments, chat, news posts, payments or admin web panel in this feature; catalog changes are
  made by the operator in files/configuration.
- Accounts are login name + password only; no e-mail, phone or social login, and so no
  self-service password reset (the operator resets passwords by hand).
- The MobiArmy client can't be changed, so players still type their credentials on its own login
  screen; the portal login doesn't carry over into the game automatically.
- Hashing passwords means the game server's login check changes on the server side only; what
  the client sends and receives stays the same (constitution I).
- Account deletion and profile pages (avatar, display name) are out of scope.
