# Feature Specification: Web Icon

**Feature Branch**: `003-web-icon`

**Created**: 2026-11-22

**Status**: Draft

**Input**: User description: "thêm icon web" (add an icon for the web game page)

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Recognise the game tab (Priority: P1)

A player opens the browser game. The browser tab, the bookmark list and the history show the
MobiArmy icon next to the title, instead of the browser's generic blank-page icon, so the player
can find the game among many open tabs.

**Why this priority**: The tab icon is what every visitor sees, on every browser, from the first
visit. Today the page has no icon at all, so it looks unfinished.

**Independent Test**: Open the game page in a fresh desktop browser; the tab shows the MobiArmy
icon. Bookmark the page; the bookmark shows the same icon.

**Acceptance Scenarios**:

1. **Given** a player who has never visited the site, **When** they open the game page, **Then**
   the browser tab shows the MobiArmy icon within the time the page takes to load.
2. **Given** a player who bookmarks the game page, **When** they look at their bookmarks,
   **Then** the bookmark shows the MobiArmy icon.
3. **Given** the page is served over the public address (through Cloudflare), **When** it is
   opened, **Then** the icon appears the same way as on the local address.

---

### User Story 2 - Add the game to a phone's home screen (Priority: P2)

A player on a phone uses "Add to Home Screen". The shortcut on the home screen shows the MobiArmy
icon, sharp and not blurry, and is named "MobiArmy", so the game can be launched like an app.

**Why this priority**: Touch-screen players are a supported audience (on-screen keypad exists),
and a home-screen shortcut makes returning easy. It matters to fewer people than the tab icon.

**Independent Test**: On an Android phone in Chrome (and on an iPhone in Safari), add the page to
the home screen; the shortcut shows the icon, crisp at the phone's icon size, labelled "MobiArmy".

**Acceptance Scenarios**:

1. **Given** a player on Android Chrome, **When** they add the game to the home screen, **Then**
   the shortcut shows the MobiArmy icon with crisp pixel edges and the label "MobiArmy".
2. **Given** a player on iPhone Safari, **When** they add the game to the home screen, **Then**
   the shortcut shows the MobiArmy icon, not a screenshot of the page.

---

### Edge Cases

- A browser that only looks for an icon at the conventional site-root address (without reading
  the page) still finds it, instead of getting a "not found" response.
- The original artwork is very small (32×32 pixels); larger sizes must be enlarged without
  smoothing, so the pixel art stays sharp rather than blurry.
- Browsers cache icons aggressively: after the icon is changed in a later release, a player who
  reloads normally should eventually see the new one (no permanent stale icon).
- Icons must not be fetched from any other website, consistent with the page's rule of loading
  nothing off-site.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The game page MUST declare an icon that desktop browsers show in the tab, in
  bookmarks and in history.
- **FR-002**: The icon MUST be the original MobiArmy game icon shipped with the game client, so
  the web version is recognisable as the same game.
- **FR-003**: The icon MUST also be available at the site-root icon address that browsers request
  by default, so no request for it returns "not found".
- **FR-004**: The page MUST provide a home-screen icon for phones (Android and iPhone), large
  enough to look sharp at home-screen size, with the shortcut name "MobiArmy".
- **FR-005**: Enlarged versions of the icon MUST keep hard pixel edges (no blurring).
- **FR-006**: All icon files MUST be served by the game's own web server; the page MUST NOT load
  icons from other sites.
- **FR-007**: Icons MUST be served with caching that lets browsers pick up a changed icon after a
  new release, without forcing a re-download on every page view.

### Key Entities

- **Game icon**: the original 32×32 pixel-art image from the game client; the single source for
  every icon size.
- **Icon set**: the sizes the site offers (small for tabs, large for phone home screens), all
  derived from the game icon.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: On current Chrome, Edge and Firefox on a computer, and Chrome on Android, 100% of
  first visits show the MobiArmy icon in the tab (no generic icon).
- **SC-002**: The web server logs zero "not found" responses for icon requests during a normal
  visit.
- **SC-003**: A home-screen shortcut created on Android and on iPhone shows the MobiArmy icon,
  judged sharp (no visible blur) when viewed at normal phone size.
- **SC-004**: Adding the icons increases the first-visit page load time by less than 0.1 seconds.

## Assumptions

- The icon is the original game icon (`icon.png` in the game client files) rather than a newly
  designed logo; redesigning the artwork is out of scope.
- "Web icon" means the browser/home-screen icon of the game page. A full installable app
  (offline play, splash screens, app-store listing) is out of scope.
- The page title stays "MobiArmy".
- Supported browsers are the same as for the web client (spec 002): current Chrome, Edge and
  Firefox on computers, Chrome on Android; iPhone Safari is covered for the home-screen icon only.
- Works the same in local mode, public mode and through the Cloudflare Tunnel, since all three
  serve the same page.
