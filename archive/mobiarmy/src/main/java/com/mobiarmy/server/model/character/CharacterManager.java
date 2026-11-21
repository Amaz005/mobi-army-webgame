package com.mobiarmy.server.model.character;

import com.mobiarmy.server.Player;
import com.mobiarmy.server.network.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages character (nhanvat) templates: loads from DB or falls back to defaults,
 * and handles ownership queries.
 */
public class CharacterManager {

    private static final Logger LOGGER = Logger.getLogger(CharacterManager.class.getName());

    private static volatile CharacterManager instance;
    private NhanVat[] cache;

    private CharacterManager() {
        loadFromDatabase();
    }

    public static CharacterManager getInstance() {
        if (instance == null) {
            synchronized (CharacterManager.class) {
                if (instance == null) {
                    instance = new CharacterManager();
                }
            }
        }
        return instance;
    }

    /** Returns all character templates. */
    public NhanVat[] getAll() {
        if (cache == null) loadFromDatabase();
        return cache;
    }

    public NhanVat get(int index) {
        NhanVat[] arr = getAll();
        if (index < 0 || index >= arr.length) return null;
        return arr[index];
    }

    public int count() {
        return getAll().length;
    }

    /** Returns true if the character is free (xu=0 && luong=0). */
    public boolean isCharFree(int index) {
        NhanVat nv = get(index);
        return nv != null && nv.isFree();
    }

    /** Returns true if player owns the given 0-based character index. */
    public boolean userOwnsChar(Player player, int index) {
        if (index < 0 || index >= 9) return false;
        if (player.ownedMask != 0) {
            return (player.ownedMask & (1 << index)) != 0;
        }
        if (player.charLevels == null || index >= player.charLevels.length) {
            return false;
        }
        return player.charLevels[index] > 0;
    }

    /** Grant a character to a player and persist the ownedMask. */
    public void grantCharToUser(Session session, Player player, int index) {
        if (index < 0 || index >= 9) return;
        player.grantChar(index);
        if (session != null && session.jbdc != null && session.jbdc.getConnection() != null) {
            String sql = "UPDATE `account` SET `owned_mask` = ? WHERE `id` = ?";
            try (PreparedStatement stmt = session.jbdc.getConnection().prepareStatement(sql)) {
                stmt.setInt(1, player.ownedMask);
                stmt.setInt(2, player.iddb);
                stmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to persist ownedMask for userId=" + player.iddb, e);
            }
        }
    }

    /** Persist player's ownedMask to DB. */
    public void persistOwnedMask(Session session, Player player) {
        if (session == null || session.jbdc == null) return;
        String sql = "UPDATE `account` SET `owned_mask` = ? WHERE `id` = ?";
        try (PreparedStatement stmt = session.jbdc.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, player.ownedMask);
            stmt.setInt(2, player.iddb);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to persist ownedMask for userId=" + player.iddb, e);
        }
    }

    private void loadFromDatabase() {
        try {
            java.sql.Connection conn = com.mobiarmy.server.sql.JBDCManager.getStaticConnection();
            if (conn != null) {
                String sql = "SELECT nhanvat_id, name, xu, luong, ma_sat_gio, goc_min, "
                    + "sat_thuong, sat_thuong_dan, so_dan FROM nhanvat "
                    + "WHERE nhanvat_id <= 9 ORDER BY nhanvat_id";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    ArrayList<NhanVat> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new NhanVat(
                            rs.getInt("nhanvat_id"),
                            rs.getString("name"),
                            rs.getInt("xu"),
                            rs.getInt("luong"),
                            rs.getInt("ma_sat_gio"),
                            rs.getInt("goc_min"),
                            rs.getInt("sat_thuong"),
                            rs.getInt("sat_thuong_dan"),
                            rs.getInt("so_dan")
                        ));
                    }
                    if (!list.isEmpty()) {
                        cache = list.toArray(new NhanVat[0]);
                        LOGGER.info("Loaded " + cache.length + " characters from DB");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not load characters from DB: " + e.getMessage());
        }
        // Fallback: 9 default characters
        cache = new NhanVat[] {
            new NhanVat(1, "Gunner",       0,      0, 80, -45, 280, 28, 1),
            new NhanVat(2, "Miss 6",       0,      0, 50, -45, 290, 14, 2),
            new NhanVat(3, "Electician",    0,      0, 80, -45, 300, 10, 3),
            new NhanVat(4, "King Kong",    40000,  32, 40,  30, 420, 10, 4),
            new NhanVat(5, "Rocketer",     30000,  24, 50,  20, 321, 12, 3),
            new NhanVat(6, "Granos",      20000,  16, 30,  30, 310,  7, 5),
            new NhanVat(7, "Chicky",      100000,  80, 20, -45, 450, 23, 2),
            new NhanVat(8, "Tarzan",       50000,  40, 10,   1, 340, 30, 1),
            new NhanVat(9, "Apache",       70000,  64, 30, -45, 431, 11, 4),
        };
        LOGGER.info("Using fallback " + cache.length + " default characters");
    }
}
