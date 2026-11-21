package com.mobiarmy.server.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mobiarmy.server.Player;
import com.mobiarmy.server.model.character.CharacterManager;
import com.mobiarmy.server.network.Message;
import com.mobiarmy.server.network.Session;

public class LoginService {

    private Message message;
    private Session session;

    public LoginService(Session session, Message message) {
        this.session = session;
        this.message = message;
    }

    public void login() {
        try {
            if (message == null || message.getData().length < 6) {
                System.err.println("[Login] payload too short (len="
                    + (message == null ? -1 : message.getData().length) + ")");
                PopupService.sendThongBao(session, "Du lieu dang nhap khong hop le!");
                return;
            }

            String username = message.reader().readUTF().trim();
            String password = message.reader().readUTF().trim();
            session.version = message.reader().readUTF().trim();

            System.out.println("[Login] user='" + username + "' pass='" + password + "' ver='" + session.version + "'");

            Player player = loadPlayerFromDB(username, password);
            if (player == null) {
                PopupService.sendThongBao(session, "Sai tên đăng nhập hoặc mật khẩu!");
                return;
            }

            session.player = player;
            player.state = Player.State.Waiting;

            // Send login response (cmd 3)
            Message resp = buildLoginResponse(player, "Đăng nhập thành công!");
            session.push(resp);

            // Send the menu sequence the client expects after login
            sendMenuSequence(session, player);

            System.out.println("[Login] OK: " + player.name + "#" + player.iddb);
        } catch (Exception e) {
            System.err.println("[Login] error: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            try {
                if (session != null) {
                    PopupService.sendThongBao(session, "Loi dang nhap: " + e.getClass().getSimpleName());
                }
            } catch (Exception ignore) {}
        }
    }

    private Player loadPlayerFromDB(String username, String password) {
        if (session.jbdc == null || session.jbdc.getConnection() == null) {
            System.err.println("[Login] DB connection unavailable");
            return null;
        }
        String query = "SELECT * FROM `account` WHERE `username` = ? AND `password` = ?";
        try (PreparedStatement stmt = session.jbdc.getConnection().prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPlayerData(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[Login] SQL error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[Login] loadPlayerFromDB error: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Player extractPlayerData(ResultSet rs) throws SQLException {
        Player p = new Player();
        p.iddb = safeGetInt(rs, "id", 0);
        p.name = safeGetString(rs, "username", "");
        p.xu = safeGetLong(rs, "xu", 0L);
        p.luong = (short) safeGetInt(rs, "luong", 0);
        p.online = safeGetBoolean(rs, "online", false);
        p.itembag = safeGetString(rs, "itembag", "");
        p.superadmin = safeGetBoolean(rs, "superadmin", false);
        p.banbe = safeGetString(rs, "banbe", "");
        p.nvIndex = (byte) safeGetInt(rs, "nhanvat", 0);
        p.exp = safeGetInt(rs, "exp", 0);
        p.killdead = safeGetString(rs, "killdead", "");
        p.thongtin = safeGetString(rs, "thongtin", "");
        p.isActive = safeGetBoolean(rs, "active", true);
        // Extended character data
        p.selectedChar = safeGetInt(rs, "selected_char", 0);
        if (p.selectedChar < 0) p.selectedChar = 0;
        String ownedStr = safeGetString(rs, "owned_mask", null);
        // Owned character bitmask. Default for missing column / legacy rows:
        // 0x1FF (all 9 characters unlocked). The legacy default 0x7 only
        // owned NV 1-3, which made the client hide picker stats such as
        // "Ma sát gió" for King Kong and the other paid characters.
        p.ownedMask = parseIntSafe(ownedStr, 0x1FF);
        if (p.ownedMask == 0) {
            // Treat "0 stored in DB" as "unlock everything" so a fresh
            // account still gets the full picker UI populated.
            p.ownedMask = 0x1FF;
        }
        String levelsStr = safeGetString(rs, "char_levels", null);
        // Default char levels: 1 for NV 1-3 (legacy), 1 for NV 4-9 so that
        // the client does not dim their stat bars.
        p.charLevels = parseIntArray(levelsStr, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        String itemsStr = safeGetString(rs, "item_counts", null);
        p.itemCounts = parseIntArray(itemsStr, new int[]{20, 20, 20, 20, 20, 20, 20, 20, 20});
        return p;
    }

    private boolean safeGetBoolean(ResultSet rs, String col, boolean def) {
        try { return rs.getBoolean(col); }
        catch (SQLException e) { return def; }
    }

    private int safeGetInt(ResultSet rs, String col, int def) {
        try { return rs.getInt(col); }
        catch (SQLException e) { return def; }
    }

    private long safeGetLong(ResultSet rs, String col, long def) {
        try { return rs.getLong(col); }
        catch (SQLException e) { return def; }
    }

    private String safeGetString(ResultSet rs, String col, String def) {
        try { return rs.getString(col); }
        catch (SQLException e) { return def; }
    }

    private int parseIntSafe(String s, int def) {
        if (s == null) return def;
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private int[] parseIntArray(String s, int[] def) {
        if (s == null) return def;
        try {
            s = s.trim();
            if (s.startsWith("[") && s.endsWith("]")) s = s.substring(1, s.length() - 1);
            String[] parts = s.split(",");
            int[] arr = new int[Math.min(parts.length, 9)];
            for (int i = 0; i < arr.length; i++) arr[i] = Integer.parseInt(parts[i].trim());
            return arr;
        } catch (Exception e) { return def; }
    }

    /**
     * Build cmd 3 (login response).
     * Wire format (matches army1 client bb.class case 3 offsets 501..747):
     *   int    playerId        (LE)
     *   int    xu             (LE)
     *   byte   selectedChar
     *   byte   0
     *   int    exp            (LE)
     *   22 x { byte, short }  (char level seed)
     *   9 x { byte, short }    (NV stat seed; first 3 are hardcoded)
     *   UTF   message
     *   UTF   (empty)
     */
    private Message buildLoginResponse(Player player, String msg) {
        Message m = new Message(3);
        try {
            writeIntLE(m.writer(), player.iddb);
            writeIntLE(m.writer(), (int) Math.min(player.xu, Integer.MAX_VALUE));
            m.writer().writeByte(player.selectedChar);
            m.writer().writeByte(0);
            writeIntLE(m.writer(), (int) Math.min(player.exp, Integer.MAX_VALUE));
            // 22 char-level entries (3 bytes each)
            for (int i = 0; i < 22; i++) {
                m.writer().writeByte(20);
                int level = (i < player.charLevels.length) ? player.charLevels[i] : 1000 + i;
                m.writer().writeShort(level);
            }
            // 9 NV stat entries (first 3 are hardcoded by client, skip them)
            // For NV 4-9, send level=1 so the client considers the character
            // unlocked and renders its stats (HP, sát thương, ma sát gió).
            // The exact (byte, short) layout for these slots is:
            //   byte   stat seed (e.g. base damage tier)
            //   short  level / unlock flag (>0 == unlocked)
            for (int i = 0; i < 9; i++) {
                if (i >= 3) {
                    int level = (i < player.charLevels.length) ? player.charLevels[i] : 1;
                    if (level <= 0) level = 1;
                    m.writer().writeByte(0);
                    m.writer().writeShort(level);
                }
            }
            m.writer().writeUTF(msg);
            m.writer().writeUTF("");
        } catch (IOException e) {
            System.err.println("[Login] buildLoginResponse error: " + e.getMessage());
        }
        return m;
    }

    private void writeIntLE(java.io.DataOutputStream ds, int value) throws IOException {
        ds.writeByte(value & 0xFF);
        ds.writeByte((value >> 8) & 0xFF);
        ds.writeByte((value >> 16) & 0xFF);
        ds.writeByte((value >> 24) & 0xFF);
    }

    /**
     * Send the post-login menu sequence the army1 client expects:
     *   1. NVData (cmd 64)  - allocates picker UI objects
     *   2. RoomList (cmd 6) - server→client NV init signal
     *   3. RegionList (cmd 7) - region/area list
     *   4. EntryList (cmd 88) - entry buttons
     *   5. Board76 - board list for picker
     *   6. Data packs (cmd 90, types 0-3)
     */
    private void sendMenuSequence(Session session, Player player) {
        CharacterService.sendNVData(session, player);
        CharacterService.sendRoomList(session);
        CharacterService.sendRegionList(session);
        CharacterService.sendEntryList(session);
        CharacterService.sendBoard76(session);
        // Auto-send data packs
        for (int packType = 0; packType <= 3; packType++) {
            try { Thread.sleep(50); } catch (InterruptedException e) { break; }
            SendPackData.sendPack(session, packType);
        }
    }
}
