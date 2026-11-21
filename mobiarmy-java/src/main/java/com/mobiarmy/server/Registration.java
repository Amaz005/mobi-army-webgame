package com.mobiarmy.server;

import com.mobiarmy.data.SqlConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

/**
 * Account registration. The client's Register action (command 35) sends only the username; in the
 * original game the password went in a paid SMS. So registration is two steps on one connection:
 * command 35 reserves the name, then the next login with that name creates the account with the
 * password the player typed (see ClientEntry).
 */
public final class Registration {
    /** Same rule as login (letters/digits), and fits account.username VARCHAR(20). */
    private static final Pattern USERNAME = Pattern.compile("^[a-z0-9]{1,20}$");
    /** Same rule as login, and fits account.password VARCHAR(50). */
    private static final Pattern PASSWORD = Pattern.compile("^[a-zA-Z0-9]{1,50}$");

    private Registration() {}

    /** The client lowercases and trims the name before sending it. */
    static String normalize(String username) {
        return username == null ? "" : username.trim().toLowerCase();
    }

    static boolean isValidUsername(String username) {
        return USERNAME.matcher(username).matches();
    }

    static boolean isValidPassword(String password) {
        return password != null && PASSWORD.matcher(password).matches();
    }

    static boolean exists(String username) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM account WHERE username = ? LIMIT 1")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Inserts the account with the schema's default values. Returns false if the name is taken.
     * account.id has no sequence, so the next id is taken in the same statement.
     */
    static boolean create(String username, String password) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO account (id, username, password) "
                     + "SELECT (SELECT COALESCE(MAX(id), 0) + 1 FROM account), ?, ? "
                     + "WHERE NOT EXISTS (SELECT 1 FROM account WHERE username = ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, username);
            return stmt.executeUpdate() == 1;
        }
    }
}
