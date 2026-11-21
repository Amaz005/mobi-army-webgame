-- Creates a least-privilege PostgreSQL role for the game server (constitution Principle IV).
-- Run once as a superuser, AFTER loading dbarmy.sql:
--   psql -U postgres -d dbarmy -f mobiarmy-java/sql/create_game_user.sql
--
-- Before running, replace CHANGE_ME_STRONG_PASSWORD with a long random password,
-- then give it to the server as the DB_PASSWORD environment variable.
-- army2.conf should have:  db-user: army   (leave db-password empty)
--
-- Restrict WHERE the role can connect from in pg_hba.conf, e.g. only the game server's IP:
--   host  dbarmy  army  203.0.113.10/32  scram-sha-256
-- and keep listen_addresses / the firewall closed to everything else.

CREATE ROLE army LOGIN PASSWORD 'CHANGE_ME_STRONG_PASSWORD';

-- Row-level access only: no DDL, no superuser, no access to other databases.
GRANT CONNECT ON DATABASE dbarmy TO army;
GRANT USAGE ON SCHEMA public TO army;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO army;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO army;
