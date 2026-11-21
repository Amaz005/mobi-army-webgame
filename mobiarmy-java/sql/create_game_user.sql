-- Creates a least-privilege MySQL account for the game server (constitution Principle IV).
-- Run once as an admin user:   mysql -u root -p < mobiarmy-java/sql/create_game_user.sql
--
-- Before running, replace:
--   CHANGE_ME_STRONG_PASSWORD  -> a long random password (then set it as MYSQL_PASSWORD for the server)
--   'localhost'                -> the game server's IP if MySQL runs on a different machine
--                                 (e.g. 'army'@'203.0.113.10'); never use '%'.
-- Then in army2.conf set:  mysql-user: army   (leave mysql-password empty)

CREATE USER IF NOT EXISTS 'army'@'localhost' IDENTIFIED BY 'CHANGE_ME_STRONG_PASSWORD';

-- Row-level access only: no DROP/ALTER/CREATE, no GRANT, no access to other databases.
GRANT SELECT, INSERT, UPDATE, DELETE ON `dbarmy`.* TO 'army'@'localhost';

FLUSH PRIVILEGES;
