# Data Model: Web Game Portal

## Account (table `account`, existing — changed)

The single account for the portal and MobiArmy (FR-016). Only the changes are listed; all other
columns stay as in `mobiarmy-java/dbarmy.sql`.

| Column | Before | After | Notes |
|--------|--------|-------|-------|
| `username` | `VARCHAR(20) NOT NULL` | same + **unique index** `account_username_uq` | Duplicates renamed first (research D4) |
| `password` | `VARCHAR(50) NOT NULL`, plaintext | `VARCHAR(255) NOT NULL`, `pbkdf2_sha256$<iter>$<salt>$<hash>` | Legacy plaintext accepted only until migrated (research D3) |

Validation (portal registration, research D5):
- `username`: trimmed, lowercased, `^[a-z0-9]{3,20}$`, unique.
- `password`: `^[a-zA-Z0-9]{6,50}$` before hashing.
- New rows get the schema defaults for every other column (xu, nvBuy, itembag, nhanvat, exp…);
  the API accepts no other fields (FR-020).

Read by the portal: `id`, `username`, `password` (verify only), `exp`, `superadmin` (ranking
filter). Written by the portal: insert (registration), `password` (change / re-hash).

Migration: `mobiarmy-java/sql/migrations/004-portal-accounts.sql` + `HashPasswords` tool;
`dbarmy.sql` updated with the new column size, index, renamed duplicate and hashed seed passwords.

## Password hash (value format)

`pbkdf2_sha256$600000$<base64 16-byte salt>$<base64 32-byte derived key>`

- Anything not starting with `pbkdf2_sha256$` is a legacy plaintext value.
- Verify: parse iterations + salt from the stored value, derive, compare in constant time.

## Portal session (server memory only)

| Field | Type | Notes |
|-------|------|-------|
| token | 32 random bytes, base64url | Cookie `portal_session`; never logged |
| accountId | int | |
| createdAt, lastUsedAt | instant | Expire after `portal-session-idle-minutes` idle (default 30) or 7 days total |

States: `active` → (`logout` \| idle timeout \| 7-day limit \| password change on another session
\| server restart) → gone.

## Login throttle (server memory only)

| Key | Rule |
|-----|------|
| login name | 5 failed portal logins in 15 min → refuse that name for 15 min |
| visitor IP (`X-Real-IP`) | max 5 successful registrations per hour |
| account (`#<id>`) | 5 wrong current passwords on password change in 15 min → refuse password changes for 15 min |

Plus the nginx per-IP request limit on `/api/` except the cached status endpoint (research D6).

## Game catalog entry (`portal/games.json`)

See contracts/game-catalog.md. Fields: `id`, `name`, `description`, `tagline`, `players`, `image`, `playUrl`, `howToPlay[]`, `order`,
`status` (`visible` \| `maintenance` \| `hidden`), optional `live` (`mobiarmy` only for now).

## Live game status (computed, not stored)

| Field | Source |
|-------|--------|
| `online` | count of `ServerManager.clients` with `login == true` |
| `takenAt` | server time |

"Offline" is not a field: it is the portal's view when `/api/mobiarmy/status` is unreachable.

## Ranking entry (computed, cached 60 s in server memory)

| Field | Source |
|-------|--------|
| `rank` | position (1-based) |
| `name` | `account.username` (= in-game name) |
| `level` | `User.levelFromExpValue(exp)` |
| `exp` | `account.exp` |

Query: `SELECT username, exp FROM account WHERE superadmin = 0 ORDER BY exp DESC, id ASC LIMIT 10`.

## Configuration (new keys)

| Where | Key | Default | Meaning |
|-------|-----|---------|---------|
| `army2.conf` | `portal-port` | `8081` | Internal HTTP port of the portal API (not published) |
| `army2.conf` | `portal-session-idle-minutes` | `30` | Portal session idle timeout |
| `army2.conf` | `portal-secure-cookie` | `false` | Add `Secure` to the cookie; compose sets it `true` in public mode via env `PORTAL_SECURE_COOKIE` |
| `.env` | `WEB_API_RATE` | `30r/m` | nginx per-IP request rate for `/api/` |
