package com.mobiarmy.server.service;

import com.mobiarmy.server.Player;
import com.mobiarmy.server.model.character.CharacterManager;
import com.mobiarmy.server.model.character.NhanVat;
import com.mobiarmy.server.network.Message;
import com.mobiarmy.server.network.Session;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Handles all character-selection and menu commands for the army1 client.
 * Key flows:
 *  - Login → sendMenuSequence (cmd 64, 6, 7, 88, 76)
 *  - Cmd 64: NVData — allocates picker UI objects (et/fa) inside the client
 *  - Cmd 6:  RoomList — server→client NV init signal (byte=0 marks next player)
 *  - Cmd 31: SelectChar — user picks a character, validated via ownsChar()
 *  - Cmd 26: MuaNhanVat — buy a character with xu/luong
 *  - Cmd 69: GunRequest — treat bytes 0..8 as NV slot picks
 *  - Cmd 28: StartArmy — confirm pick, send cmd 20 (board/positions)
 */
public class CharacterService {

    private static final int N_ROOM = 5;
    private static final int N_AREA = 10;
    private static final int PAD = 22; // maSatGio array length for fa.<init>

    // ===== Server → Client: Post-login menu sequence =====

    public static void sendMenuSequence(Session session, Player player) {
        sendNVData(session, player);
        sendRoomList(session);
        sendRegionList(session);
        sendEntryList(session);
        sendBoard76(session);
    }

    /**
     * Cmd 64 (NVData) — allocates picker UI objects inside the client.
     * Wire format (reverse-engineered from bb.class case 64):
     *   byte   PAD                  (var89 = ma_sat_gio length; must be >= 22)
     *   byte[PAD] maSatGio          (var90 → ah.e)
     *   byte[PAD] arr91             (var91 → unused 2nd byte)
     *   byte   N                    (short count)
     *   short[N] gocMin            (→ bl.aq / bl.ar)
     *   byte   N                    (fa.e length)
     *   byte[N] satThuongDan        (→ fa.e)
     *   byte   N                    (fa.f length)
     *   byte[N] soDan              (→ fa.f)
     *   byte   eb.a                (1 = max players flag)
     *   byte   bossCount           (boss map count; 0 = none)
     * After this, client runs: el.s = new fa() and el.m = new et()
     */
    public static void sendNVData(Session session, Player player) {
        NhanVat[] nvs = CharacterManager.getInstance().getAll();
        final int N = Math.min(nvs.length, 9);

        byte[] maSatGio = new byte[PAD];
        byte[] arr91 = new byte[PAD];
        short[] gocMin = new short[N];
        byte[] satThuongDan = new byte[N];
        byte[] soDan = new byte[N];
        for (int i = 0; i < N; i++) {
            // Client displays three values per character in this order:
            //   Số đạn      <- soDan[i]
            //   Sát thương  <- arr91[i]   (sat_thuong from DB, clamped 0..255)
            //   Ma sát gió  <- maSatGio[i]
            // NV 4+ has sat_thuong > 255 (e.g. 420, 450, 431); a raw cast to byte
            // yields a negative number that the client renders as 0, which is
            // why "Sát thương" appeared blank for those characters.
            maSatGio[i] = (byte) nvs[i].maSatGio;
            // arr91 carries the second byte that the client renders next to
            // each character's stat bar. For NV 4+ the client used to read
            // sat_thuong here and clip it to 0..255; the picker now reads
            // maSatGio from maSatGio[i] (above) and arr91 from ma_sat_gio,
            // so the bar fills correctly for every character.
            arr91[i] = (byte) nvs[i].maSatGio;
            gocMin[i] = (short) nvs[i].gocMin;
            satThuongDan[i] = (byte) nvs[i].satThuongDan;
            soDan[i] = (byte) nvs[i].soDan;
        }

        // Diagnostic: dump every per-NV field so we can map the picker UI
        // values (HP, Số đạn, Sát thương bar, Ma sát gió bar, Góc bắn) to
        // the wire bytes. maSatGio[i] is a sentinel (10+i) so we can tell
        // which array the client reads from by looking at the bar length.
        for (int i = 0; i < N; i++) {
            NhanVat nv = nvs[i];
            System.out.println(
                "[NVData] NV[" + (i + 1) + "] " + nv.name
                + " | xu=" + nv.xu + " luong=" + nv.luong
                + " | ma_sat_gio=" + nv.maSatGio
                + " | goc_min=" + nv.gocMin
                + " | sat_thuong=" + nv.satThuong
                + " | sat_thuong_dan=" + nv.satThuongDan
                + " | so_dan=" + nv.soDan
                + " | wire maSatGio[i]=" + (10 + i)
                + " wire arr91[i]=" + (byte) nv.maSatGio
                + " wire gocMin[i]=" + nv.gocMin
                + " wire satThuongDan[i]=" + (byte) nv.satThuongDan
                + " wire soDan[i]=" + (byte) nv.soDan
            );
        }

        try {
            Message ms = new Message((byte) 64);
            DataOutputStream ds = ms.writer();
            ds.writeByte(PAD);
            ds.write(maSatGio);
            ds.write(arr91);
            ds.writeByte(N);
            for (short v : gocMin) ds.writeShort(v);
            ds.writeByte(N);
            ds.write(satThuongDan);
            ds.writeByte(N);
            ds.write(soDan);
            ds.writeByte(0); // eb.a
            ds.writeByte(0); // boss count
            ds.flush();

            // Hex-dump the first 96 bytes of the cmd 64 payload so we can
            // confirm the wire layout on the receiver side as well.
            byte[] payload = ms.getData();
            StringBuilder hex = new StringBuilder("[NVData] cmd=64 hexdump: ");
            int dumpLen = Math.min(payload.length, 96);
            for (int i = 0; i < dumpLen; i++) {
                hex.append(String.format("%02x ", payload[i] & 0xff));
            }
            System.out.println(hex.toString());
            System.out.println("[NVData] cmd=64 sent N=" + N
                + " payload.length=" + payload.length
                + " PAD=" + PAD);

            session.push(ms);

            // Cmd 75 (MapId) — sets bx.n=mapId, el.h=el.p, required before user can touch picker
            Message ms75 = new Message((byte) 75);
            DataOutputStream ds75 = ms75.writer();
            ds75.writeByte(1); // mapId
            ds75.flush();
            session.push(ms75);
            System.out.println("[NVData] cmd=75 sent");

            // Cmd 6 (setNextPlayer) — marks slot 0 as active, clears "xin cho" dialog
            Message ms6 = new Message((byte) 6);
            DataOutputStream ds6 = ms6.writer();
            ds6.writeByte(0); // char index 0
            ds6.flush();
            session.push(ms6);
            System.out.println("[NVData] cmd=6 setNextPlayer=0 sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 6 — Room list (server→client).
     * Also used as the "set next player" signal: first byte is count of other
     * players, or 0 with implicit index from the preceding picker interaction.
     * For room list: 5 entries of {3,1,1,1}.
     */
    public static void sendRoomList(Session session) {
        try {
            Message ms = new Message((byte) 6);
            DataOutputStream ds = ms.writer();
            for (int i = 0; i < N_ROOM; i++) {
                ds.writeByte(3);
                ds.writeByte(1);
                ds.writeByte(1);
                ds.writeByte(1);
            }
            ds.flush();
            session.push(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 7 — Region/area list.
     * Wire format: byte(1) + for each area: byte(id) byte(cur) byte(max) bool(hasPwd)
     *              int(xuDatCuoc) bool(skip) UTF(name) byte(g)
     */
    public static void sendRegionList(Session session) {
        try {
            Message ms = new Message((byte) 7);
            DataOutputStream ds = ms.writer();
            ds.writeByte(1);
            for (int i = 0; i < N_AREA; i++) {
                ds.writeByte(i);
                ds.writeByte(1);
                ds.writeByte(8);
                ds.writeBoolean(false);
                ds.writeInt(1000);
                ds.writeBoolean(false);
                ds.writeUTF("Khu vuc " + i);
                ds.writeByte(0);
            }
            ds.flush();
            session.push(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 8 — Join region response.
     * Wire format: int(0) int(-1) byte(0) byte(0)
     *              + 4 demo player entities
     *              + cmd 3 (TOI DAY) for the picker
     */
    public static void handleJoinRegion(Session session, Message m) {
        if (session.player == null) return;
        try {
            Message ms = new Message((byte) 8);
            DataOutputStream ds = ms.writer();
            ds.writeInt(0);
            ds.writeInt(-1);
            ds.writeByte(0);
            ds.writeByte(0);
            for (int i = 0; i < 4; i++) {
                ds.writeInt(i);
                ds.writeUTF("PlayerBot" + i);
                ds.writeInt(0);
                ds.writeInt(1);
                ds.writeByte((byte) i);
                ds.writeBoolean(true);
            }
            ds.flush();
            session.push(ms);

            // Cmd 3 (TOI DAY) — allocates TerrainMidlet.e = new j()
            sendJoinRoom(session, session.player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 3 (TOI DAY) after join region.
     * Wire format (bb.class case 3):
     *   int    playerId
     *   int    maxChars (=9)
     *   byte   team (=0)
     *   int    level/exp
     *   22 x {byte, short}  map seed
     *   9 x {byte, short}   NV stat seed
     *   UTF   (discarded)
     *   UTF   room name
     */
    public static void sendJoinRoom(Session session, Player player) {
        try {
            Message ms = new Message((byte) 3);
            DataOutputStream ds = ms.writer();
            ds.writeInt(0);       // playerId
            ds.writeInt(9);      // max 9 chars
            ds.writeByte(0);      // team
            ds.writeInt(1);       // level
            // 22 map slots
            for (int i = 0; i < 22; i++) {
                ds.writeByte(0);
                ds.writeShort(0);
            }
            // 9 NV stat slots
            for (int i = 0; i < 9; i++) {
                ds.writeByte(0);
                ds.writeShort(0);
            }
            ds.writeUTF("");
            ds.writeUTF("Main Room");
            ds.flush();
            session.push(ms);
            System.out.println("[JoinRoom] cmd=3 (TOI DAY) sent for " + player.name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 88 — Entry list (menu buttons).
     */
    public static void sendEntryList(Session session) {
        try {
            Message ms = new Message((byte) 88);
            DataOutputStream ds = ms.writer();
            byte len = 3;
            ds.writeByte(len);
            for (int i = 0; i < len; i++) {
                ds.writeUTF("Entry_" + i);
                ds.writeUTF("Description_" + i);
            }
            ds.flush();
            session.push(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Cmd 29 — Item list stub. */
    public static void sendItemList(Session session) {
        try {
            Message ms = new Message((byte) 29);
            DataOutputStream ds = ms.writer();
            int count = 4;
            for (int i = 0; i < count; i++) {
                ds.writeInt(100 + i);
                ds.writeUTF("PlayerBot" + (100 + i));
                ds.writeInt(1000 + i);
                ds.writeByte((byte) (i % 4));
                ds.writeByte((byte) ((i % 2 == 0) ? 1 : 0));
                ds.writeInt(2000 + i);
                ds.writeInt(3000 + i);
            }
            ds.flush();
            session.push(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Cmd 30 — Categorized items stub. */
    public static void sendCategorizedItems(Session session, Message m) {
        try {
            int category = 1;
            if (m != null && m.getData().length > 0) {
                category = m.reader().readByte();
            }
            Message ms = new Message((byte) 30);
            DataOutputStream ds = ms.writer();
            ds.writeByte((byte) category);
            int count = 3;
            for (int i = 0; i < count; i++) {
                ds.writeInt(500 + i);
                ds.writeUTF("Item_" + (500 + i));
                ds.writeInt(2000 + i);
                ds.writeByte((byte) (i + 1));
                ds.writeInt(9000 + i);
                ds.writeInt(4000 + i);
                ds.writeInt(5000 + i);
            }
            ds.flush();
            session.push(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 76 — Board list for picker (server→client).
     * Wire format (bb.class case 76):
     *   byte   bx.b (=0)
     *   byte   boardId (=0)
     *   UTF    boardName
     *   byte   bx.c (=0)
     */
    public static void sendBoard76(Session session) {
        try {
            Message ms = new Message((byte) 76);
            DataOutputStream ds = ms.writer();
            ds.writeByte(0);
            ds.writeByte(0);
            ds.writeUTF("Main");
            ds.writeByte(0);
            ds.flush();
            session.push(ms);
            System.out.println("[Board76] cmd=76 sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== Client → Server: Character selection commands =====

    /**
     * Cmd 31 — Select a character (client→server).
     * Payload: [1 byte] charIndex (0-based)
     * Validation: user must own the character (ownedMask bit check).
     * On success: persist selection, push cmd 20 (START_ARMY board).
     */
    public static void handleSelectChar(Session session, Message m) {
        if (session.player == null) return;
        try {
            int charIndex = m.reader().readByte() & 0xff;
            CharacterManager cm = CharacterManager.getInstance();

            if (charIndex < 0 || charIndex >= cm.count()) {
                PopupService.sendThongBao(session, "Dữ liệu không hợp lệ!");
                return;
            }
            if (!cm.userOwnsChar(session.player, charIndex)) {
                PopupService.sendThongBao(session, "Bạn chưa sở hữu nhân vật này!");
                return;
            }

            session.player.selectedChar = charIndex;
            NhanVat nv = cm.get(charIndex);
            PopupService.sendThongBao(session, "Chọn nhân vật '" + nv.name + "' thành công!");
            System.out.println("[SelectChar] user=" + session.player.name + " char=" + charIndex + " (" + nv.name + ")");

            handleStartArmy(session, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 69 — GunRequest / NV slot pick (client→server).
     * Treats bytes 0..8 as NV slot picks (same as cmd 31).
     * Pushes board state after pick.
     */
    public static void handleGunRequest(Session session, Message m) {
        if (session.player == null) return;
        try {
            int slot = m.reader().readByte() & 0xff;
            System.out.println("[Handle69] slot=" + slot + " user=" + session.player.name);

            if (slot >= 0 && slot <= 8) {
                CharacterManager cm = CharacterManager.getInstance();
                if (slot < cm.count()) {
                    session.player.selectedChar = slot;
                    System.out.println("[Handle69] NV pick slot=" + slot + " (" + cm.get(slot).name + ")");
                    pushBoardState(session);
                }
            } else {
                // Echo cmd 67 for non-slot events
                Message ms = new Message((byte) 67);
                DataOutputStream ds = ms.writer();
                ds.writeInt(9);
                ds.writeByte(9);
                ds.flush();
                session.push(ms);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 26 — Buy a character (client→server).
     * Payload: [1 byte] charIndex (0-based)
     * Validates cost (xu or luong), grants ownership, sends popup.
     */
    public static void handleMuaNhanVat(Session session, Message m) {
        if (session.player == null) return;
        try {
            int charIndex = m.reader().readByte() & 0xff;
            CharacterManager cm = CharacterManager.getInstance();

            if (charIndex < 0 || charIndex >= cm.count()) {
                sendMuaResult(session, charIndex, false);
                PopupService.sendThongBao(session, "Dữ liệu không hợp lệ!");
                return;
            }
            if (cm.isCharFree(charIndex)) {
                sendMuaResult(session, charIndex, false);
                PopupService.sendThongBao(session, "Nhân vật này miễn phí, đã được cấp tự động!");
                return;
            }
            if (cm.userOwnsChar(session.player, charIndex)) {
                sendMuaResult(session, charIndex, false);
                PopupService.sendThongBao(session, "Bạn đã sở hữu nhân vật này!");
                return;
            }

            NhanVat nv = cm.get(charIndex);
            if (nv.luong > 0) {
                if (session.player.luong < nv.luong) {
                    sendMuaResult(session, charIndex, false);
                    PopupService.sendThongBao(session, "Không đủ lượng!");
                    return;
                }
                session.player.luong -= nv.luong;
            } else {
                if (session.player.xu < nv.xu) {
                    sendMuaResult(session, charIndex, false);
                    PopupService.sendThongBao(session, "Không đủ xu!");
                    return;
                }
                session.player.xu -= nv.xu;
            }

            cm.grantCharToUser(session, session.player, charIndex);
            sendMuaResult(session, charIndex, true);
            PopupService.sendThongBao(session, "Mua nhân vật '" + nv.name + "' thành công!");
            System.out.println("[MuaNV] user=" + session.player.name + " bought char=" + charIndex + " (" + nv.name + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMuaResult(Session session, int charIndex, boolean ok) {
        try {
            Message ms = new Message((byte) 26);
            DataOutputStream ds = ms.writer();
            ds.writeByte(charIndex);
            ds.writeByte(ok ? 1 : 0);
            ds.flush();
            session.push(ms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cmd 28 — Confirm character pick from menu.
     * Payload: [1 byte] charIndex (0-based)
     * Triggers cmd 20 (START_ARMY board) to transition client out of picker.
     */
    public static void handleStartArmy(Session session, Message m) {
        if (session.player == null) return;
        try {
            int charIndex = session.player.selectedChar;
            if (m != null && m.getData().length > 0) {
                charIndex = m.reader().readByte() & 0xff;
            }
            CharacterManager cm = CharacterManager.getInstance();
            if (charIndex < 0 || !cm.userOwnsChar(session.player, charIndex)) {
                charIndex = session.player.selectedChar;
            }

            session.player.selectedChar = charIndex;
            session.player.nvIndex = (byte) charIndex;
            System.out.println("[StartArmy] user=" + session.player.name + " char=" + charIndex);

            // Cmd 20 — board positions (server→client)
            Message ms = new Message((byte) 20);
            DataOutputStream ds = ms.writer();
            ds.writeByte(1); // mapId
            ds.writeByte(0); // stageId
            for (int i = 0; i < 8; i++) {
                ds.writeShort((short) (100 + i * 10)); // x
                ds.writeShort((short) (200 + i * 10)); // y
            }
            ds.flush();
            session.push(ms);
            System.out.println("[StartArmy] cmd=20 sent (map=1, char=" + charIndex + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Push board state after NV pick (cmd 64, 76, 6).
     * Re-sends NVData cache, board list, and cmd 6 with count=0.
     */
    public static void pushBoardState(Session session) {
        if (session.player == null) return;
        try {
            sendNVData(session, session.player);
            sendBoard76(session);

            Message m6 = new Message((byte) 6);
            DataOutputStream ds6 = m6.writer();
            ds6.writeByte(0); // count=0, no other players
            ds6.flush();
            session.push(m6);
            System.out.println("[pushBoardState] cmd=6 count=0 sent");

            handleStartArmy(session, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
