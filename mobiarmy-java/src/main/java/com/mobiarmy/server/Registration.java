package com.mobiarmy.server;

import com.mobiarmy.data.SqlConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Account registration. The client's Register action (command 35) sends only the username; in the
 * original game the password went in a paid SMS. So registration is two steps on one connection:
 * command 35 reserves the name, then the next login with that name creates the account with the
 * password the player typed (see ClientEntry). The web portal registers in one step
 * (specs/004-game-portal), with stricter minimum lengths.
 */
public final class Registration {
    /** Same rule as login (letters/digits), and fits account.username VARCHAR(20). */
    private static final Pattern USERNAME = Pattern.compile("^[a-z0-9]{1,20}$");
    /** Same rule as login, at most 50 characters. */
    private static final Pattern PASSWORD = Pattern.compile("^[a-zA-Z0-9]{1,50}$");
    /** Portal: data-model.md › Account.username (3–20, research D5). */
    private static final Pattern PORTAL_USERNAME = Pattern.compile("^[a-z0-9]{3,20}$");
    /** Portal: data-model.md › Account.password (6–50, research D5). */
    private static final Pattern PORTAL_PASSWORD = Pattern.compile("^[a-zA-Z0-9]{6,50}$");

    private Registration() {}

    /** The client lowercases and trims the name before sending it. */
    public static String normalize(String username) {
        return username == null ? "" : username.trim().toLowerCase();
    }

    static boolean isValidUsername(String username) {
        return USERNAME.matcher(username).matches();
    }

    static boolean isValidPassword(String password) {
        return password != null && PASSWORD.matcher(password).matches();
    }

    public static boolean isValidPortalUsername(String username) {
        return username != null && PORTAL_USERNAME.matcher(username).matches();
    }

    public static boolean isValidPortalPassword(String password) {
        return password != null && PORTAL_PASSWORD.matcher(password).matches();
    }

    /** Login names are unique regardless of case. */
    static boolean exists(String username) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM account WHERE lower(username) = lower(?) LIMIT 1")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Inserts the account with the schema's default values and a hashed password. Returns false if
     * the name is taken (also when a simultaneous registration wins the unique index).
     * account.id has no sequence, so the next id is taken in the same statement.
     */
    public static boolean create(String username, String password) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO account (id, username, password) "
                     + "SELECT (SELECT COALESCE(MAX(id), 0) + 1 FROM account), ?, ? "
                     + "WHERE NOT EXISTS (SELECT 1 FROM account WHERE lower(username) = lower(?))")) {
            stmt.setString(1, username);
            stmt.setString(2, PasswordHash.hash(password));
            stmt.setString(3, username);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            if (isUniqueViolation(e)) return false;
            throw e;
        }
    }

    /** PostgreSQL SQLSTATE 23505; SQLite reports constraint failures as error code 19. */
    static boolean isUniqueViolation(SQLException e) {
        return "23505".equals(e.getSQLState()) || e.getErrorCode() == 19
                || (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed"));
    }
}
