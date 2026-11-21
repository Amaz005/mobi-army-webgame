package com.mobiarmy.server;

import com.mobiarmy.data.MissionData;
import com.mobiarmy.data.NVData;
import com.mobiarmy.data.SpecialItemData;
import com.mobiarmy.data.Until;
import com.mobiarmy.network.Message;
import com.mobiarmy.data.SqlConnectionManager;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.server.FightWait;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    public static class RuongDoItemEntry {
        public int numb;
        public SpecialItemData.SpecialItemEntry entry;
    }

    public static class RuongDoTbEntry {
        public int index;
        public Date dayBuy;
        public byte slotNull;
        public byte vipLevel;
        public short[] invAdd;
        public short[] percenAdd;
        public int[] slot;
        public boolean isUse;
        public NVData.EquipmentEntry entry;
    }

    public enum State {
        Waiting, WaitFight, Fighting
    }

    public static class QuaInfo {
        public int type;
        public int idItem;
        public int numb;
    }

    public final ClientEntry client;
    private State state;
    private int iddb;
    private String username;
    private byte nv;
    private int xu;
    private int dvong;
    private int eventScore;
    private Date baodanhsk;
    private short clan;
    private Date xpX2Time;
    private Date xpX0Time;
    private short csinh;
    private byte[] nvCSinh;

    boolean[] nvBuy;
    private int[] lever;
    private byte[] leverPercen;
    private int[] xp;
    private short[] point;
    private short[][] pointAdd;
    byte[] item;
    private int[][] nvData;
    private RuongDoTbEntry[][] nvEquip;
    private int[] mission;
    private byte[] missionLevel;
    private final List<RuongDoItemEntry> ruongDoItem;
    private final List<RuongDoTbEntry> ruongDoTB;
    private List<Integer> friends = new ArrayList<>();

    boolean isSuperAdmin;
    int bangXhPage;
    Date bangXhLastUpdate = new Date(0);
    int exp;
    String killdeadJson;
    String thongtinJson;

    private byte hopNgocAction;
    private int hopNgocNum;
    private int hopNgocGia;
    private RuongDoTbEntry hopNgocTB;
    private RuongDoItemEntry hopNgocItem;
    private List<RuongDoItemEntry> hopNgocItemArray;
    private List<QuaInfo> quas;
    private byte moQua;
    private final boolean[] dataQua = new boolean[12];
    private int timeQua;
    public boolean startQua;
    
    FightWait waitFight;
    FightManager luyentap;
    FightManager fight;
    static NVData.EquipmentEntry[][] nvEquipDefault;

    private static final short[][] GIFT_DATA = {
        { 54, 52, 53, 55, 56 }, { 51, 49, 50, 53, 52 }, { 51, 49, 50, 53, 52 },
        { 52, 50, 51, 54, 53 }, { 51, 49, 50, 53, 52 }, { 50, 48, 49, 52, 51 },
        { 51, 49, 50, 53, 52 }, { 52, 54, 53, 50, 51 }, { 51, 53, 52, 49, 50 },
        { 51, 53, 52, 49, 50 }
    };

    public User(ClientEntry client) {
        this.client = client;
        this.state = State.Waiting;
        this.ruongDoItem = new ArrayList<>();
        this.ruongDoTB = new ArrayList<>();
        this.hopNgocItemArray = new ArrayList<>();
        this.quas = new ArrayList<>();
        this.moQua = 0;
    }

    public State getState() { return state; }
    public void setState(State st) { state = st; }

    public FightWait getFightWait() { return waitFight; }
    public void setFightWait(FightWait fw) {
        this.waitFight = fw;
        if (fw != null) {
            this.state = State.WaitFight;
        }
    }

    void clearFightReferences() {
        waitFight = null;
        fight = null;
    }

    public FightManager getFightManager() { return fight; }
    public void setFightManager(FightManager fm) { this.fight = fm; }

    public int getIdDb() { return iddb; }
    public String getUserName() { return username; }
    public byte getNvUse() { return nv; }
    public int getXu() { return xu; }
    public Date getBaodanhsk() { return baodanhsk; }

    public synchronized void updateEventScore(int score) {
        eventScore += score;
    }

    public synchronized void updateBaoDanhSk(Date nd) {
        baodanhsk = nd;
    }

    public synchronized void updateXu(int xuUp) {
        if (xuUp == 0) return;
        long sum = (long) xuUp + xu;
        xu = sum > Integer.MAX_VALUE ? Integer.MAX_VALUE : (sum < Integer.MIN_VALUE ? Integer.MIN_VALUE : (int) sum);
        try {
            sendMessage(new Message(105).sendInt(xu).sendInt(getLuong()));
        } catch (Exception ignored) {}
    }

    public synchronized void updateItem(byte id, int numb) {
        int index = id;
        item[index] = (byte) Math.max(0, Math.min(item[index] + numb, ServerManager.maxItem));
    }

    public int getLuong() { return 0; }
    public void updateLuong(int luongUp) { }
    public int getDvong() { return dvong; }
    public void updateDvong(int dvUp) { }

    public int getClan() { return clan; }
    public int getLevel() { return lever[nv]; }
    public int getCs() { return nvCSinh[nv]; }
    public byte getLevelPercen() { return leverPercen[nv]; }
    public int getXp() { return xp[nv]; }

    public synchronized void updateXP(int addXP, boolean canX2) {
        if (addXP > 0) {
            if (canX2 && xpX2Time != null && xpX2Time.after(new Date())) addXP *= 2;
            if (xpX0Time != null && xpX0Time.after(new Date())) return;
        }

        int oldExp = exp;
        long sum = (long) addXP + oldExp;
        int newExp = sum > Integer.MAX_VALUE ? Integer.MAX_VALUE : (sum < 0 ? 0 : (int) sum);
        addXP = newExp - oldExp;
        if (addXP == 0) return;

        int oldLevel = levelFromExpValue(oldExp);
        int newLevel = levelFromExpValue(newExp);
        boolean updateLevel = newLevel != oldLevel;

        exp = newExp;

        if (lever != null) {
            for (int i = 0; i < lever.length; i++) {
                lever[i] = newLevel;
                xp[i] = newExp;
                leverPercen[i] = 0;
            }
        }

        try {
            Message ms = new Message(97).sendInt(addXP).sendInt(newExp).sendInt(expForLevelValue(newLevel + 1));
            if (updateLevel) {
                byte levelByte = (byte) (client != null && "2.2.3".equals(client.versionARM) ? Math.min(newLevel, 127) : Math.min(newLevel, 255));
                ms.sendByte(1).sendByte(levelByte).sendByte(leverPercen[nv]).sendShort(point[nv]);
            } else {
                ms.sendByte(0).sendByte(leverPercen[nv]);
            }
            sendMessage(ms);
        } catch (Exception ignored) {}

        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE account SET exp=? WHERE id=?")) {
            stmt.setInt(1, exp);
            stmt.setInt(2, iddb);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int levelFromExpValue(int exp) {
        int level = 1;
        int remaining = exp;
        while (remaining - level * 100 >= 0) {
            remaining -= level * 100;
            level++;
        }
        return level;
    }

    static int expForLevelValue(int level) {
        long e = 100L * level * (level + 1) / 2;
        return e > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) e;
    }

    public void sendMissionInfo() {
        // Implementation based on MissionData if available
    }

    public void updateMission(int missionId, int add) {
        if (mission == null || missionId < 0 || missionId >= mission.length) return;
        mission[missionId] += add;
    }

    public int getPoint() { return point[nv]; }
    public short[] getPointAdd() { return pointAdd[nv]; }

    public int[] getAbility() {
        int[] ability = new int[5];
        int[] envAdd = new int[5];
        int[] percenAdd = new int[5];

        for (int i = 0; i < 6; i++) {
            if (nvData == null || nvData[nv][i] < 0 || nvData[nv][i] >= ruongDoTB.size()) continue;

            RuongDoTbEntry rdE = ruongDoTB.get(nvData[nv][i]);
            int hanSD = rdE.entry.hanSuDung - Until.getNumDay(rdE.dayBuy, new Date());
            if (hanSD <= 0) continue;

            for (int j = 0; j < 5; j++) {
                envAdd[j] += rdE.invAdd[j];
                percenAdd[j] += rdE.percenAdd[j];
            }
        }

        ability[0] = 1000 + pointAdd[nv][0] * 10 + envAdd[0] * 10;
        ability[0] += (1000 + pointAdd[nv][0]) * percenAdd[0] / 100;

        ability[1] = 100;

        ability[2] = (envAdd[2] + pointAdd[nv][2]) * 10;
        ability[2] += ability[2] * percenAdd[2] / 100;
        ability[3] = (envAdd[3] + pointAdd[nv][3]) * 10;
        ability[3] += ability[3] * percenAdd[3] / 100;
        ability[4] = (envAdd[4] + pointAdd[nv][4]) * 10;
        ability[4] += ability[4] * percenAdd[4] / 100;

        return ability;
    }

    public synchronized byte getItemNum(int index) {
        return (item != null && index >= 0 && index < item.length) ? item[index] : (byte) 0;
    }

    public short[] getEquip() {
        short[] equip = new short[5];
        if (nvEquip == null) return equip;

        RuongDoTbEntry setEntry = nvEquip[nv][5];
        if (setEntry != null && setEntry.entry.isSet) {
            System.arraycopy(setEntry.entry.arraySet, 0, equip, 0, 5);
        } else {
            for (int i = 0; i < 5; i++) {
                RuongDoTbEntry e = nvEquip[nv][i];
                if (e != null && !e.entry.isSet) {
                    equip[i] = e.entry.id;
                } else if (nvEquipDefault != null && nvEquipDefault[nv][i] != null) {
                    equip[i] = nvEquipDefault[nv][i].id;
                } else {
                    equip[i] = -1;
                }
            }
        }
        return equip;
    }

    public short getGunId() {
        if (nvEquip == null) return nvEquipDefault[nv][0].id;
        return nvEquip[nv][0] != null ? nvEquip[nv][0].entry.id : nvEquipDefault[nv][0].id;
    }

    public short getIdBullet() {
        if (nvEquip == null) return nvEquipDefault[nv][0].bullId;
        return nvEquip[nv][0] != null ? nvEquip[nv][0].entry.bullId : nvEquipDefault[nv][0].bullId;
    }

    public void sendMessage(Message msg) {
        if (client != null) client.sendMessage(msg);
    }

    public static User login(ClientEntry cl, String userName, String pass) {
        try {
            User us = null;
            try (Connection conn = SqlConnectionManager.createConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE username=? AND password=? LIMIT 1")) {
                stmt.setString(1, userName);
                stmt.setString(2, pass);
                try (java.sql.ResultSet row = stmt.executeQuery()) {
                    if (row.next()) {
                        us = new User(cl);
                        us.iddb = row.getInt("id");
                        us.username = row.getString("username");
                        us.xu = row.getInt("xu");
                        us.isSuperAdmin = row.getInt("superadmin") != 0;
                        us.exp = row.getInt("exp");
                        us.nv = (byte) Math.max(0, Math.min(row.getInt("nhanvat") - 1, NVData.entries.size() - 1));
                        
                        String itembagJson = row.getString("itembag");
                        us.item = new byte[com.mobiarmy.data.ItemData.entries.size()];
                        boolean hasAnyItem = false;
                        if (itembagJson != null && !itembagJson.isEmpty() && !itembagJson.equals("[]")) {
                            itembagJson = itembagJson.substring(1, itembagJson.length() - 1);
                            String[] pairs = itembagJson.split("],\\[");
                            for (String pair : pairs) {
                                pair = pair.replace("[", "").replace("]", "");
                                String[] parts = pair.split(",");
                                if (parts.length == 2) {
                                    int id = Integer.parseInt(parts[0].trim());
                                    int qty = Integer.parseInt(parts[1].trim());
                                    if (id >= 0 && id < us.item.length) {
                                        us.item[id] = (byte) Math.max(0, Math.min(qty, 255));
                                        if (qty > 0) hasAnyItem = true;
                                    }
                                }
                            }
                        }
                        
                        if (us.item.length > 0) us.item[0] = 99;
                        if (us.item.length > 1) us.item[1] = 99;
                        if (!hasAnyItem) {
                            if (us.item.length > 12) us.item[12] = 1;
                            if (us.item.length > 13) us.item[13] = 1;
                        }
                        
                        int len = NVData.entries.size();
                        us.nvBuy = new boolean[len];
                        for (int i = 0; i < len && i < 3; i++) us.nvBuy[i] = true;
                        
                        us.lever = new int[len];
                        us.leverPercen = new byte[len];
                        us.xp = new int[len];
                        us.point = new short[len];
                        us.pointAdd = new short[len][5];
                        us.nvData = new int[len][6];
                        us.nvCSinh = new byte[len];
                        us.nvEquip = new RuongDoTbEntry[len][6];
                        
                        int computedLevel = levelFromExpValue(us.exp);
                        for (int i = 0; i < len; i++) {
                            us.lever[i] = computedLevel;
                            us.xp[i] = us.exp;
                            us.leverPercen[i] = 0;
                        }
                        
                        Message ms = new Message(3)
                            .sendInt(us.iddb)
                            .sendInt(us.xu)
                            .sendByte(us.nv)
                            .sendInt(us.exp);

                        for (int i = 0; i < 22; i++) {
                            byte qty = i < us.item.length ? us.item[i] : (byte) 0;
                            short buyXu = i < com.mobiarmy.data.ItemData.entries.size() ? (short) com.mobiarmy.data.ItemData.entries.get(i).buyXu : (short) 0;
                            ms.sendByte(qty).sendShort(buyXu);
                        }

                        for (int i = 3; i < NVData.entries.size() && i < 9; i++) {
                            int priceThousand = NVData.entries.get(i).buyXu / 1000;
                            ms.sendByte(0).sendShort((short) priceThousand);
                        }

                        ms.sendUTF("").sendUTF("");
                        cl.sendMessage(ms);
                        cl.sendMessage(new Message(87).sendUTF(us.username));
                        
                        // Kick duplicate not implemented yet
                        // Update online status
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE account SET online=1 WHERE id=?")) {
                            updateStmt.setInt(1, us.iddb);
                            updateStmt.executeUpdate();
                        }
                        
                        us.luyentap = new FightManager(us, ServerManager.ltapMap);
                        ServerManager.enterWait(us);
                        return us;
                    } else {
                        cl.sendMessage(new Message(4).sendUTF("Tài khoản hoặc mật khẩu không chính xác!"));
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
