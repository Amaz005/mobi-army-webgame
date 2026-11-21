# Feature Specification: Play the Existing Client in a Web Browser

**Feature Branch**: `002-web-client`

**Created**: 2026-11-21

**Status**: Draft

**Input**: User description: "start 002-web-client" (the browser client reserved by
`001-docker-compose-stack`, US3 / FR-013)

## Clarifications

### Session 2026-11-21

- Q: Who can reach the web client? → A: Public internet players (HTTPS, per-visitor connection limits; see FR-012).
- Q: How is the client delivered? → A: Try running the existing client inside the browser first; fall back to streaming it from the server if that proof fails (FR-002, FR-013, FR-014). A from-scratch rewrite was requested and declined as out of scope (FR-002).

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Open the game in a browser and log in (Priority: P1)

A player opens a web address in an ordinary browser. The project's existing game client loads,
with nothing to install, and is already pointed at the project's own game server. The player
logs in with an existing account and reaches the lobby.

**Why this priority**: Today the only client is a phone (J2ME) app that needs an emulator, a
Java install and a manual change of the server address. Removing all of that is the whole point
of the feature.

**Independent Test**: With the stack running, open the documented address in a fresh browser
profile, log in as a seeded account, and confirm the lobby appears without any install or
address change.

**Acceptance Scenarios**:

1. **Given** the stack is running, **When** a player opens the documented address, **Then** the
   game's login screen appears with no download, plugin or install prompt.
2. **Given** the login screen, **When** the player enters a valid account, **Then** they reach
   the lobby of the project's server (not any outside server).
3. **Given** the login screen, **When** the player enters a wrong password, **Then** the game
   shows its normal "wrong account" message and stays usable.

---

### User Story 2 - Play a full match from the browser (Priority: P1)

Two players, each in their own browser, join the same room, play a match to the end (move,
aim, shoot, use items) and see the results. Experience earned is saved by the server, as with
the phone client.

**Why this priority**: Logging in alone isn't playing. A match exercises the real-time part of
the connection, and it's also what closes the client checks left open in 001 (T013, T016).

**Independent Test**: Two browser windows (or two machines) log in as two seeded accounts,
join one room, finish a match; both see the result, and a later login shows the saved
experience.

**Acceptance Scenarios**:

1. **Given** two logged-in browser players, **When** one creates a room and the other joins,
   **Then** both see each other in the room.
2. **Given** a match in progress, **When** a player shoots, **Then** the other player sees the
   shot and its effect within the time in SC-003.
3. **Given** a finished match, **When** the player logs in again later, **Then** their
   experience includes what the match awarded.

---

### User Story 3 - Started by the same one command (Priority: P2)

The web client is part of the stack from `001-docker-compose-stack`: the same start command
brings it up with the server and database, and stopping the stack stops it too.

**Why this priority**: It completes the "server + client + db in one command" goal, but the
client is still useful even if it had to be started separately.

**Independent Test**: From a fresh clone, run the 001 start command and open the documented
address; it works with no extra step.

**Acceptance Scenarios**:

1. **Given** a fresh clone with the settings file filled in, **When** the developer runs the one
   start command, **Then** the web client is reachable at the documented address once the
   server is ready.
2. **Given** the web client fails to start, **When** the stack starts, **Then** the game server
   and database still start and serve phone clients normally.

---

### User Story 4 - Phone controls work with keyboard, mouse and touch (Priority: P3)

The game was built for phone keypads. In the browser, a player can control it fully with a
computer keyboard (documented key mapping), and on a touch screen through an on-screen keypad.

**Why this priority**: Without this the game is technically running but hard to play; it
matters less than connecting and playing at all.

**Independent Test**: Complete US2's match using only the keyboard, then once using only the
on-screen keypad on a touch device.

**Acceptance Scenarios**:

1. **Given** the game in a desktop browser, **When** the player uses the documented keys,
   **Then** every phone key the game needs (arrows, fire/OK, both soft keys, number keys) is
   reachable.
2. **Given** a touch-screen device, **When** the player taps the on-screen keypad, **Then** it
   acts exactly like the phone keys.

---

### Edge Cases

- The game server is down or restarting: the player sees a clear "can't reach server" message
  (the game's own, or one shown around it), and can retry without reloading the whole page.
- The connection drops mid-match: the game behaves as the phone client does on a lost
  connection; the server releases the player's slot.
- The same account logs in from two browsers: the server's existing rules apply; the browser
  client adds no new behavior.
- Many tabs open at once: each tab is a separate player connection; limits come from the
  server's existing `max-clients` setting.
- The browser can't run the client (very old or unsupported browser): a plain message lists
  the supported browsers instead of a blank page.
- The browser connection path is only ever used to reach the project's game server; asking it
  to connect anywhere else is refused.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: Players MUST be able to play using only a supported web browser, with no
  download, plugin, emulator or language runtime installed on their device.
- **FR-002**: The browser client MUST be the project's existing game client (the same game,
  screens and rules). It MUST NOT be a rewrite or re-creation of the game. Primary delivery:
  the client runs inside the player's browser. Fallback delivery (only if FR-013's gate fails):
  the client runs on the server and its screen is streamed to the browser.
- **FR-003**: The client MUST connect to the project's own game server automatically; it MUST
  NOT contact the original publisher's server-address service or any outside game server.
- **FR-004**: The game server's protocol MUST stay unchanged, and phone clients MUST keep
  working alongside browser players at the same time (constitution I).
- **FR-005**: The browser-to-server connection path MUST only forward to the configured game
  server, and MUST NOT be usable to reach other hosts or ports.
- **FR-006**: Everything the browser loads MUST be served by the stack itself. No outside
  website is required at play time, apart from the browser itself.
- **FR-007**: The web client MUST start and stop with the `001-docker-compose-stack` start and
  stop commands, and its failure MUST NOT stop the server or database (001 FR-013).
- **FR-008**: The full set of phone keys the game uses MUST be available from a keyboard (with a
  documented mapping) and from an on-screen keypad on touch devices.
- **FR-009**: The client MUST work in the current versions of Chrome, Edge and Firefox on
  desktop, and Chrome on Android. Other browsers MAY work, and unsupported ones get a clear
  message.
- **FR-010**: Each browser tab MUST act as an independent player connection, counted against the
  server's existing client limit.
- **FR-011**: The README MUST document the web address, the supported browsers and the key
  mapping.
- **FR-012**: The web client MUST be reachable by players over the public internet, served
  over HTTPS, and the browser connection path MUST limit connections per visitor so it cannot
  be used to flood the game server.
- **FR-013**: Before the rest of the feature is built, a time-boxed proof MUST show whether the
  in-browser delivery can load the client, log in and play one match (US1 + US2) while meeting
  SC-002 and SC-003. If it can't within the time box, the streamed fallback is used, and the
  decision and its evidence are recorded in the plan.
- **FR-014**: If the streamed fallback is used, the number of simultaneous browser players
  MUST be capped at a configured limit. Visitors beyond it MUST see a "server full, try later"
  message rather than a degraded game.

### Key Entities

- **Web client**: the existing game client, made playable in a browser; loads its game
  resources from the stack; has a fixed target: the project's game server.
- **Browser connection path**: the piece that lets a browser talk to the game server, which
  browsers can't reach directly; passes game messages through unchanged; has a single fixed
  destination.
- **Player session**: one browser tab connected to the server; behaves exactly like one phone
  client.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A new player goes from opening the address to the lobby in under 2 minutes, with
  zero install steps.
- **SC-002**: On first visit over a local network, the login screen appears in under 20 seconds;
  on later visits, in under 5 seconds.
- **SC-003**: On a local network, another player sees a shot or move within 0.5 seconds in 95%
  of actions during a match.
- **SC-004**: Two browser players and one phone client can finish the same match together with no
  disconnects in 5 out of 5 attempts.
- **SC-005**: A match finished in the browser saves experience 100% of the time (checked with a
  later login).
- **SC-006**: Attempts to use the browser connection path to reach any destination other than
  the game server fail 100% of the time.

## Assumptions

- The client delivered is the one already in the repo (`Client/SOURCE_ARMYI/` or
  `Army165local/`), including its own graphics and sounds. No new game art, rules or screens are
  made.
- The project owner has the right to run and distribute this client to their players; this
  feature makes it available to public internet players (FR-012).
- The game server, its protocol and the database from 001 are reused unchanged.
- Account registration in the browser is out of scope; players use accounts that already exist.
- Saving gold, items and characters is still a separate feature.
- Phone-only features that have no browser meaning (SMS sending, phone calls) may be disabled
  or show "not supported".
- Under the streamed fallback, only a small number of players can play at once (each costs
  server resources); FR-014 caps it. The in-browser delivery has no such per-player cost.
- Public hosting (a domain, its certificate, and a host machine) is provided by the project
  owner; this feature makes the stack ready for it but doesn't buy or run the host.
