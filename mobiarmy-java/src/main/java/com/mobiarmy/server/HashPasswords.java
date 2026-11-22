package com.mobiarmy.server;

import com.mobiarmy.data.SqlConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * One-shot migration (specs/004-game-portal research D3): replaces every plaintext password with a
 * PBKDF2 hash, in one transaction. Run after a pg_dump backup and after
 * sql/migrations/004-portal-accounts.sql, from the server's working directory:
 * {@code java -cp "classes:lib/*" com.mobiarmy.server.HashPasswords}. Prints only a count.
 */
public final class HashPasswords {
    private HashPasswords() {}

    public static void main(String[] args) throws Exception {
        ServerManager.loadConfigFile();
        SqlConnectionManager.configure(ServerManager.dbHost, ServerManager.dbDatabase, ServerManager.dbUser, ServerManager.dbPass);
        SqlConnectionManager.usePostgres();
        System.out.println("hashed " + run() + " accounts");
    }

    /** Hashes all legacy rows on the current connection target; returns how many were changed. */
    static int run() throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection()) {
            conn.setAutoCommit(false);
            try {
                List<Integer> ids = new ArrayList<>();
                List<String> plain = new ArrayList<>();
                try (PreparedStatement sel = conn.prepareStatement("SELECT id, password FROM account ORDER BY id");
                     ResultSet rs = sel.executeQuery()) {
                    while (rs.next()) {
                        String p = rs.getString(2);
                        if (PasswordHash.isLegacy(p)) {
                            ids.add(rs.getInt(1));
                            plain.add(p == null ? "" : p);
                        }
                    }
                }
                try (PreparedStatement upd = conn.prepareStatement("UPDATE account SET password = ? WHERE id = ?")) {
                    for (int i = 0; i < ids.size(); i++) {
                        upd.setString(1, PasswordHash.hash(plain.get(i)));
                        upd.setInt(2, ids.get(i));
                        upd.addBatch();
                    }
                    upd.executeBatch();
                }
                conn.commit();
                return ids.size();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
