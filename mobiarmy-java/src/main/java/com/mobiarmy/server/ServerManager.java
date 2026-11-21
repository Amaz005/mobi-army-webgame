package com.mobiarmy.server;

import com.mobiarmy.data.Until;
import com.mobiarmy.network.Message;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerManager {

    private static boolean debug;
    public static boolean isDebug() { return debug; }

    private static byte nArea;
    static String host;
    static short port;
    static String mysqlHost;
    static String mysqlUser;
    static String mysqlPass;
    static String mysqlDatabase;
    static byte equipVersion2;
    static byte iconVersion2;
    static byte levelCVersion2;
    static byte valuesVersion2;
    static byte playerVersion2;
    static byte[] nRoom;
    static byte nRoomAll;

    public static byte maxElementFight;
    public static byte maxPlayers;
    public static byte numbPlayers;
    public static boolean mgtBullNew;

    public static boolean superAdmin;
    static byte nPlayersInitRoom;
    static byte ltapMap;
    public static short[] xLtap, yLtap;
    static byte initMap;
    static byte initMapBoss;
    static String addInfo;
    static String addInfoURL;
    static String regTeamURL;
    static String taiGameName;
    static String taiGameInfo;
    static String taiGameURL;
    static int maxClients;
    static int maxRuongTb;
    static int maxRuongItem;
    static int maxRuongItemSlot;
    static int maxItem;
    static int maxFriends;

    static int numClients;
    static List<ClientEntry> clients;
    static boolean start;
    static int id;

    static List<User> listWait;

    static final String[] ROOM_TYPES = { "PHÒNG SƠ CẤP", "PHÒNG TRUNG CẤP", "PHÒNG VIP", "PHÒNG ĐẤU TRƯỜNG", "PHÒNG TỰ DO", "PHÒNG ĐẤU TRÙM", "PHÒNG ĐẤU ĐỘI" };
    static final String[] ROOM_TYPES_ENG = { "NEWBIE ROOM", "INTERMEDIATE ROOM", "VIP ROOM", "ARENA", "FREEDOM ROOM", "BOSS BATTLE ROOM", "CLAN BATTLE ROOM" };
    static int[] roomTypeStartNum;
    static final String[] NAME_ROOMS = { "Bom 1", "Bom 2", "Thành phố nhện", "Thành phố máy", "Khủng long máy", "Khu vực cấm", "Đỉnh hi mã lạp sơn", "Hang nhện", "Trùm liên hoàn", "Trùm liên hoàn" };
    static final int[] NAME_ROOM_NUMBERS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    static final int[] NAME_ROOM_TYPES = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    public static Room[] rooms;

    public static void initRooms() {
        listWait = new java.util.concurrent.CopyOnWriteArrayList<>();
        rooms = new Room[nRoomAll];
        roomTypeStartNum = new int[ROOM_TYPES.length];
        
        int k = 0;
        for (int i = 0; i < ROOM_TYPES.length; i++) {
            for (int j = 0; j < nRoom[i]; j++) {
                if (j == 0) roomTypeStartNum[i] = k;
                rooms[k] = new Room(k, i, nArea, j);
                k++;
            }
        }
    }

    public static int startRoomBoss;
    public static int startMapBoss;
    public static int numMapBoss;
    public static final byte[] MAP_ID_BOSS = { 12, 12, 13, 14, 15, 16, 17, 9, 25, 26 };

    private static Map<String, String> parseConfigFile(String data) {
        Map<String, String> map = new HashMap<>();
        for (String rawLine : data.split("\n")) {
            String line = rawLine;
            int hashIdx = line.indexOf('#');
            if (hashIdx >= 0) {
                line = line.substring(0, hashIdx);
            }
            line = line.trim();
            if (line.isEmpty()) continue;

            int colonIdx = line.indexOf(':');
            if (colonIdx > 0) {
                String key = line.substring(0, colonIdx).trim();
                String value = line.substring(colonIdx + 1).trim();
                map.put(key, value);
                System.out.println("config: " + key + "-" + value);
            }
        }
        return map;
    }

    private static String configStr(Map<String, String> cfg, String key, String def) {
        return cfg.getOrDefault(key, def);
    }

    private static byte configByte(Map<String, String> cfg, String key, byte def) {
        return cfg.containsKey(key) ? Byte.parseByte(cfg.get(key)) : def;
    }

    private static short configShort(Map<String, String> cfg, String key, short def) {
        return cfg.containsKey(key) ? Short.parseShort(cfg.get(key)) : def;
    }

    private static int configInt(Map<String, String> cfg, String key, int def) {
        return cfg.containsKey(key) ? Integer.parseInt(cfg.get(key)) : def;
    }

    private static boolean configBool(Map<String, String> cfg, String key, boolean def) {
        return cfg.containsKey(key) ? Boolean.parseBoolean(cfg.get(key)) : def;
    }

    public static void loadConfigFile() {
        try {
            byte[] ab = Until.getFile("army2.conf");
            if (ab == null) {
                System.out.println("Config file not found!");
                System.exit(0);
            }

            Map<String, String> cfg = parseConfigFile(new String(ab, "UTF-8"));

            debug = configBool(cfg, "debug", false);
            host = configStr(cfg, "host", "localhost");
            port = configShort(cfg, "port", (short) 19149);
            mysqlHost = configStr(cfg, "mysql-host", "localhost");
            mysqlUser = configStr(cfg, "mysql-user", "root");
            mysqlPass = System.getenv("MYSQL_PASSWORD") != null ? System.getenv("MYSQL_PASSWORD") : configStr(cfg, "mysql-password", "");
            mysqlDatabase = configStr(cfg, "mysql-database", "dbarmy2");
            equipVersion2 = configByte(cfg, "equipVersion2", (byte) 1);
            iconVersion2 = configByte(cfg, "iconversion2", (byte) 1);
            levelCVersion2 = configByte(cfg, "levelCVersion2", (byte) 1);
            valuesVersion2 = configByte(cfg, "valuesversion2", (byte) 1);
            playerVersion2 = configByte(cfg, "playerVersion2", (byte) 1);

            nRoom = new byte[ROOM_TYPES.length];
            roomTypeStartNum = new int[ROOM_TYPES.length];
            nRoomAll = 0;
            startRoomBoss = 0;
            startMapBoss = 0;

            for (int i = 0; i < ROOM_TYPES.length; i++) {
                nRoom[i] = configByte(cfg, "n-room-" + i, (byte) 0);
                roomTypeStartNum[i] = nRoomAll;
                nRoomAll += nRoom[i];
                if (i < 5) startMapBoss += nRoom[i];
            }

            nArea = configByte(cfg, "n-area", (byte) 101);
            maxPlayers = configByte(cfg, "max-player", (byte) 8);
            maxElementFight = configByte(cfg, "max-fight", (byte) 100);
            numbPlayers = configByte(cfg, "numb-player", (byte) 100);
            nPlayersInitRoom = configByte(cfg, "n-players-init-room", (byte) 4);

            xLtap = new short[2];
            yLtap = new short[2];
            if (cfg.containsKey("luyen-tap-map")) {
                ltapMap = 10;
                xLtap[0] = configShort(cfg, "x-ltap", (short) 0);
                xLtap[1] = configShort(cfg, "x-ltap1", (short) 0);
                yLtap[0] = configShort(cfg, "y-ltap", (short) 0);
                yLtap[1] = configShort(cfg, "y-ltap1", (short) 0);
            } else {
                ltapMap = 10;
            }

            initMap = configByte(cfg, "init-map", (byte) 30);
            initMapBoss = configByte(cfg, "init-map-boss", (byte) startMapBoss);
            startMapBoss = configInt(cfg, "start-map-boss", 82);
            numMapBoss = configInt(cfg, "num-map-boss", 8);
            addInfo = configStr(cfg, "add-info", "");
            addInfoURL = configStr(cfg, "add-info-url", "");
            regTeamURL = configStr(cfg, "reg-team-url", "");
            taiGameName = configStr(cfg, "tai-game-name", "");
            taiGameInfo = configStr(cfg, "tai-game-info", "");
            taiGameURL = configStr(cfg, "tai-game-url", "");
            maxClients = configInt(cfg, "max-clients", 1000);
            maxRuongTb = configInt(cfg, "max-ruong-trang-bi", 100);
            maxRuongItem = configInt(cfg, "max-ruong-item", 100);
            maxRuongItemSlot = configInt(cfg, "max-ruong-itemslot", 30000);
            maxItem = configInt(cfg, "max-item", 99);
            maxFriends = configInt(cfg, "max-friends", 60);
            mgtBullNew = configBool(cfg, "mgt-bull-new", true);
            superAdmin = configBool(cfg, "super-admin", true);
            if (superAdmin) {
                System.out.println("WARNING: super-admin is enabled — solo/test-mode fights are allowed on this server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enterWait(User us) {
        if (listWait == null) listWait = new java.util.ArrayList<>();
        synchronized (listWait) {
            boolean found = false;
            for (User w : listWait) {
                if (w.getIdDb() == us.getIdDb()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                listWait.add(us);
            }
        }
        us.setState(User.State.Waiting);
    }

    public static User[] findWaitPlayers(User us) {
        List<User> result = new java.util.ArrayList<>();
        if (listWait != null) {
            synchronized (listWait) {
                for (User w : listWait) {
                    if (w.getIdDb() != us.getIdDb() && w.getState() == User.State.Waiting) {
                        result.add(w);
                    }
                }
            }
        }
        return result.toArray(new User[0]);
    }

    public static User getUser(int iddb) {
        if (clients != null) {
            synchronized (clients) {
                for (ClientEntry cl : clients) {
                    if (cl != null && cl.user != null && cl.user.getIdDb() == iddb) {
                        return cl.user;
                    }
                }
            }
        }
        return null;
    }

    public static void removeWait(int iddb) {
        if (listWait != null) {
            synchronized (listWait) {
                listWait.removeIf(w -> w.getIdDb() == iddb);
            }
        }
    }

    public static void sendRoomInfo(User us) {
        try {
            Message ms = new Message(88).sendByte(ROOM_TYPES.length);
            for (int i = 0; i < 7; i++) {
                ms.sendUTF(ROOM_TYPES[i]).sendUTF(ROOM_TYPES_ENG[i]);
            }
            us.sendMessage(ms);

            Message ms2 = new Message(-19).sendByte(NAME_ROOMS.length);
            for (int i = 0; i < NAME_ROOMS.length; i++) {
                int namen = NAME_ROOM_NUMBERS[i];
                int typen = NAME_ROOM_TYPES[i];
                if (namen > nRoom[typen] + roomTypeStartNum[typen]) continue;

                int notRoom = 0;
                for (int j = 0; j < typen; j++) {
                    if (nRoom[j] > 0) notRoom++;
                }
                ms2.sendByte(roomTypeStartNum[typen] + notRoom)
                   .sendUTF("Phòng " + (roomTypeStartNum[typen] + namen) + ": " + NAME_ROOMS[i])
                   .sendByte(namen);
            }
            us.sendMessage(ms2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMapCollisionInfo(User us) {
        try {
            Message ms = new Message(92).sendShort(com.mobiarmy.data.MapData.ID_NOT_COLLISION.length);
            for (int id : com.mobiarmy.data.MapData.ID_NOT_COLLISION) {
                ms.sendShort((short) id);
            }
            us.sendMessage(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRoomsMessage(User us) {
        try {
            Message ms = new Message(6);
            for (int i = 0; i < rooms.length; i++) {
                Room rs = rooms[i];
                // 0 = red (busy), 1 = yellow, 2 = green (quiet)
                ms.sendByte(i).sendByte(rs.getFully()).sendByte(0).sendByte(rs.type);
            }
            us.sendMessage(ms);
            sendRoomInfo(us);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRoomNumberMessage(User us, Message ms) {
        try {
            int roomNumber = ms.readUnsignedByte();
            Room rs = rooms[roomNumber];
            if (rs.type == 6 && us.getClan() == 0) {
                // Not clan
                return;
            }

            Message reply = new Message(7).sendByte(roomNumber);
            synchronized (rooms) {
                for (int i = 0; i < rs.entrys.length; i++) {
                    FightWait fo = rs.entrys[i];
                    synchronized (fo.players) {
                        if (fo.numPlayer == fo.maxSetPlayer || fo.started || (fo.isLH && fo.ntLH > 0)) continue;

                        reply.sendByte(i).sendByte(fo.numPlayer).sendByte(fo.maxSetPlayer)
                            .sendBoolean(fo.passSet).sendInt(fo.money)
                            .sendBoolean(fo.started) // isPlaying
                            .sendUTF(fo.name == null ? "" : fo.name)
                            .sendByte(0); // kind: 0 = tea, 1 = free
                    }
                }
            }
            us.sendMessage(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestEmptyRoomMessage(User us, Message ms) {
        try {
            byte type = (byte) ms.readByte();
            byte level = 0;
            String searchId = null;
            if (type == 1) level = (byte) ms.readByte();
            if (type == 2) searchId = ms.readUTF();

            Message reply = new Message(-28).sendByte(type);

            for (Room rm : rooms) {
                if (type == 1 && rm.type != level) continue;
                if (type == 2) continue;

                boolean zoneHeaderSent = false;
                for (FightWait fw : rm.entrys) {
                    if (fw.started || fw.numPlayer >= fw.maxSetPlayer) continue;

                    if (!zoneHeaderSent) {
                        reply.sendByte(-1).sendUTF("Phòng " + rm.id + ": " + ROOM_TYPES[rm.type]);
                        zoneHeaderSent = true;
                    }

                    reply.sendByte(rm.id).sendByte(fw.id).sendByte(fw.map)
                        .sendByte(fw.numPlayer).sendByte(fw.maxSetPlayer).sendInt(fw.money);
                }
            }

            us.sendMessage(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enterShortPlayMessage(User us, Message ms) {
        try {
            int type = ms.readUnsignedByte();
            FightWait target = null;

            switch (type) {
                case 5:
                    for (int i = startRoomBoss; i < startRoomBoss + nRoom[5] && target == null; i++) {
                        for (FightWait fw : rooms[i].entrys) {
                            if (fw.numPlayer < fw.maxSetPlayer) { target = fw; break; }
                        }
                    }
                    break;
                case 0:
                    for (Room rm : rooms) {
                        if (target != null) break;
                        for (FightWait fw : rm.entrys) {
                            if (fw.numPlayer == 0) { target = fw; break; }
                        }
                    }
                    break;
                case 0xFFFFFFFF: // -1
                    while (target == null || target.numPlayer >= target.maxSetPlayer) {
                        Room rm = rooms[Until.nextInt(0, rooms.length)];
                        target = rm.entrys[Until.nextInt(0, rm.entrys.length)];
                    }
                    break;
                default:
                    int nPlayer = type * 2;
                    for (Room rm : rooms) {
                        if (target != null) break;
                        for (FightWait fw : rm.entrys) {
                            if (fw.numPlayer < fw.maxSetPlayer && fw.maxSetPlayer == nPlayer) { target = fw; break; }
                        }
                    }
                    break;
            }

            if (target != null) {
                target.enterFireOval(us);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void joinRegionMessage(User us, Message ms) {
        try {
            byte roomIdx = (byte) ms.readByte();
            byte areaIdx = (byte) ms.readByte();
            String password = ms.readUTF();

            if (roomIdx >= rooms.length || areaIdx >= rooms[roomIdx].entrys.length) {
                return;
            }

            FightWait fo = rooms[roomIdx].entrys[areaIdx];
            if (fo.passSet && !fo.pass.equals(password)) {
                return;
            }
            if (us.getXu() < fo.money) {
                return;
            }
            fo.enterFireOval(us);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNvData(User us) {
        try {
            java.util.List<com.mobiarmy.data.NVData.NvEntry> nvDatas = com.mobiarmy.data.NVData.entries;
            int n = nvDatas.size();
            int pad = 22;

            byte[] maSatGio = new byte[pad];
            byte[] satThuong = new byte[pad];
            for (int i = 0; i < n && i < pad; i++) {
                maSatGio[i] = nvDatas.get(i).maSatGio;
                satThuong[i] = (byte) nvDatas.get(i).satThuong;
            }

            byte[] bossLevel = new byte[numMapBoss];
            for (int i = 0; i < numMapBoss; i++) bossLevel[i] = (byte) (10 + i);

            Message ms = new Message(64).sendByte(pad);
            for (byte b : maSatGio) ms.sendByte(b);
            for (byte b : satThuong) ms.sendByte(b);

            int gocMinLen = Math.max(n, 20 + 1);
            ms.sendByte(gocMinLen);
            for (int i = 0; i < gocMinLen; i++) ms.sendShort(i < n ? nvDatas.get(i).gocMin : (short) 0);

            ms.sendByte(n);
            for (com.mobiarmy.data.NVData.NvEntry nv : nvDatas) ms.sendByte(nv.satThuongDan);

            ms.sendByte(n);
            for (com.mobiarmy.data.NVData.NvEntry nv : nvDatas) ms.sendByte(nv.soDan);

            ms.sendByte(maxElementFight);
            ms.sendByte(numMapBoss);
            for (byte b : bossLevel) ms.sendByte(b);
            for (int i = 0; i < numMapBoss; i++) ms.sendByte(MAP_ID_BOSS[i]);
            ms.sendByte(100);

            us.sendMessage(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PackInfo {
        byte type;
        String file;
        PackInfo(byte type, String file) {
            this.type = type;
            this.file = file;
        }
    }

    private static final PackInfo[] ARMY1_PACK_MAP = {
        new PackInfo((byte) 0, "data/army1/tile.pack"),
        new PackInfo((byte) 1, "data/army1/icon.pack"),
        new PackInfo((byte) 2, "data/army1/values.pack"),
        new PackInfo((byte) 3, "data/army1/player.pack")
    };
    private static final byte ARMY1_PACK_VERSION = 1;

    public static void getPackMessage(User us, Message ms) {
        try {
            byte type = (byte) ms.readByte();
            byte clientVersion = (byte) ms.readByte();

            PackInfo entry = null;
            for (PackInfo p : ARMY1_PACK_MAP) {
                if (p.type == type) {
                    entry = p;
                    break;
                }
            }

            if (entry == null) {
                System.out.println("cmd90: unknown army1 pack type " + type);
                return;
            }

            if (!new java.io.File(entry.file).exists()) {
                System.out.println("cmd90: pack not found: " + entry.file);
                return;
            }

            us.sendMessage(new Message(90).sendByte(type).sendByte(ARMY1_PACK_VERSION).sendFile(entry.file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
