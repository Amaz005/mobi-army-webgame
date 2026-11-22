-- Migration 004 (specs/004-game-portal, research D3/D4): accounts shared by the web portal and the game.
-- PostgreSQL. Fresh stacks don't need it (dbarmy.sql already has these changes).
--
-- BACK UP FIRST (constitution III), from the repo root:
--   docker compose exec -T db pg_dump -U postgres -d dbarmy -Fc > before-004.dump
-- Then:
--   docker compose exec -T db psql -U postgres -d dbarmy -v ON_ERROR_STOP=1 < mobiarmy-java/sql/migrations/004-portal-accounts.sql
--   docker compose exec server java -cp "classes:lib/*" com.mobiarmy.server.HashPasswords
--
-- 1. Room for PBKDF2 hashes (~95 characters).
-- 2. Duplicate login names (compared case-insensitively): the lowest id keeps the name, the others become
--    <name><id> (cut to fit VARCHAR(20)). Such players must use the new name; the list is printed below.
-- 3. Unique index, so two registrations of one name can't both succeed. If a new name from step 2 is
--    already taken, this step fails and everything rolls back: rename that account by hand and rerun.

BEGIN;

ALTER TABLE account ALTER COLUMN password TYPE VARCHAR(255);

-- Accounts that will be renamed (shown so the operator can tell those players).
SELECT a.id, a.username AS old_name,
       substr(a.username, 1, 20 - length(CAST(a.id AS VARCHAR))) || CAST(a.id AS VARCHAR) AS new_name
FROM account a
WHERE EXISTS (SELECT 1 FROM account b WHERE lower(b.username) = lower(a.username) AND b.id < a.id)
ORDER BY a.id;

UPDATE account a
SET username = substr(a.username, 1, 20 - length(CAST(a.id AS VARCHAR))) || CAST(a.id AS VARCHAR)
WHERE EXISTS (SELECT 1 FROM account b WHERE lower(b.username) = lower(a.username) AND b.id < a.id);

CREATE UNIQUE INDEX account_username_uq ON account (username);

COMMIT;
