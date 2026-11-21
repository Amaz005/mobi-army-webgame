#!/bin/sh
# Runs once, on first start with an empty data volume (after 01-dbarmy.sql).
# Creates the least-privilege role the game server uses. Mirrors mobiarmy-java/sql/create_game_user.sql.
set -eu

: "${DB_PASSWORD:?DB_PASSWORD must be set}"

psql -v ON_ERROR_STOP=1 -v pw="$DB_PASSWORD" --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<'SQL'
CREATE ROLE army LOGIN PASSWORD :'pw';
GRANT CONNECT ON DATABASE dbarmy TO army;
GRANT USAGE ON SCHEMA public TO army;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO army;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO army;
SQL
