package com.mobiarmy.server;

import com.mobiarmy.data.SqlConnectionManager;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Account lookups and password checks shared by the game login and the web portal, so both accept
 * exactly the same credentials (specs/004-game-portal FR-016).
 */
public final class Accounts {
    public record Row(int id, String username, String password) {}

    private Accounts() {}

    /**
     * Finds an account by login name: exact match first, then case-insensitive, so accounts created
     * in-game with capitals still log in on the portal (which lowercases what users type).
     */
    public static Row find(String username) throws Exception {
        Row exact = query("SELECT id, username, password FROM account WHERE username = ? ORDER BY id LIMIT 1", username);
        if (exact != null) return exact;
        return query("SELECT id, username, password FROM account WHERE lower(username) = lower(?) ORDER BY id LIMIT 1", username);
    }

    public static Row findById(int id) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, username, password FROM account WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new Row(rs.getInt(1), rs.getString(2), rs.getString(3)) : null;
            }
        }
    }

    private static Row query(String sql, String username) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new Row(rs.getInt(1), rs.getString(2), rs.getString(3)) : null;
            }
        }
    }

    /** True if {@code typed} matches the stored password (hashed, or legacy plaintext). */
    public static boolean checkPassword(String stored, String typed) {
        if (stored == null || typed == null) return false;
        if (PasswordHash.isLegacy(stored)) {
            return MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8), typed.getBytes(StandardCharsets.UTF_8));
        }
        return PasswordHash.verify(typed, stored);
    }

    public static void updatePassword(int id, String hash) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE account SET password = ? WHERE id = ?")) {
            stmt.setString(1, hash);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    /**
     * After a successful login with a legacy plaintext password, stores a hash instead. Best effort:
     * before the 004 migration widens the column the UPDATE fails, and the login must still succeed.
     */
    public static void rehashIfLegacy(int id, String stored, String typed) {
        if (!PasswordHash.isLegacy(stored)) return;
        try {
            updatePassword(id, PasswordHash.hash(typed));
        } catch (Exception e) {
            System.err.println("Could not re-hash the password of account " + id
                    + " (run sql/migrations/004-portal-accounts.sql): " + e.getMessage());
        }
    }

    /** Number of accounts whose password is still stored in plaintext. */
    public static int countLegacy() throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM account");
             ResultSet rs = stmt.executeQuery()) {
            int n = 0;
            while (rs.next()) {
                if (PasswordHash.isLegacy(rs.getString(1))) n++;
            }
            return n;
        }
    }
}
