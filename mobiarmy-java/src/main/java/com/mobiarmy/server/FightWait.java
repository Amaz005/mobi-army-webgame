package com.mobiarmy.server;

import com.mobiarmy.fight.FightManager;
import com.mobiarmy.data.MapData;
import com.mobiarmy.data.NVData;
import com.mobiarmy.data.ItemData;
import com.mobiarmy.network.Message;
import com.mobiarmy.data.Until;
import java.util.ArrayList;
import java.util.List;

public class FightWait {
    public final User[] players;
    public FightManager fight;
    private final Room parent;
    public final byte id;
    public final boolean[] readys;
    public final int[][] item;
    public boolean started;
    public int numReady;
    public int maxSetPlayer;
    public int maxPlayerInit;
    public int maxPlayer;
    public int numPlayer;
    public boolean passSet;
    public String pass;
    public int money;
    public String name;
    public byte type;
    public byte teaFree;
    public byte map;
    public int boss;
    public long timeStart;
    public boolean isLH;
    public byte ntLH;
    
    public final byte[] LHMap;
    
    private int soloTimeoutGeneration;
    private Thread kickBossThread;

    public FightWait(Room parent, byte type, byte id, int maxPlayers, byte maxPlayerInit, byte map, byte team, boolean isLH) {
        this.parent = parent;
        this.id = id;
        this.maxPlayer = maxPlayers;
        this.maxPlayerInit = maxPlayerInit;
        this.maxSetPlayer = maxPlayerInit;
        this.numPlayer = 0;
        this.numReady = 0;
        this.players = new User[maxPlayers];
        this.readys = new boolean[maxPlayers];
        this.item = new int[maxPlayers][6];
        for (int i = 0; i < maxPlayers; i++) {
            for (int j = 0; j < 6; j++) {
                this.item[i][j] = -1;
            }
        }
        this.type = type;
        this.teaFree = team; // teaFree is team
        this.money = parent.minXu;
        this.name = "";
        this.pass = "";
        this.isLH = isLH;
        this.ntLH = (byte) (isLH ? 0 : -1);
        
        List<Byte> lhMapList = new ArrayList<>();
        for (MapData.MapDataEntry m : MapData.entries) {
            if (m.isBoss) lhMapList.add((byte) m.id);
        }
        this.LHMap = new byte[lhMapList.size()];
        for (int i = 0; i < lhMapList.size(); i++) {
            this.LHMap[i] = lhMapList.get(i);
        }
        
        this.map = isLH && LHMap.length > 0 && ntLH >= 0 ? LHMap[this.ntLH] : map;
        this.fight = new FightManager(this);
        this.started = false;
        this.boss = -1;
        this.timeStart = 0L;
    }

    private void refreshFightWait() {
        maxSetPlayer = maxPlayerInit;
        numPlayer = 0;
        money = parent.minXu;
        name = "";
        pass = "";
        passSet = false;
        boss = -1;
        if (isLH && LHMap.length > 0) {
            ntLH = 0;
            map = LHMap[ntLH];
        }
    }

    public int getIndexByIdDb(int iddb) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].getIdDb() == iddb) return i;
        }
        return -1;
    }
    
    private void changeBoss(int index) {
        boss = index;
        if (kickBossThread != null) kickBossThread.interrupt();
        
        kickBossThread = new Thread(() -> {
            try {
                int remaining = 300;
                while (!started && boss != -1 && remaining > 0) {
                    Thread.sleep(1000);
                    remaining--;
                }
                if (!started && boss != -1 && remaining <= 0) {
                    kick(boss);
                }
            } catch (InterruptedException e) {
                // Ignore
            }
        });
        kickBossThread.start();
    }
    
    public void sendToTeam(Message ms) {
        for (User pl : players) {
            if (pl != null) {
                pl.sendMessage(ms);
            }
        }
    }
    
    private void startSoloTimeout() {
        int myGeneration = soloTimeoutGeneration;
        new Thread(() -> {
            try {
                Thread.sleep(300000); // 5 mins
                synchronized (players) {
                    if (soloTimeoutGeneration != myGeneration || numPlayer != 1 || boss < 0 || players[boss] == null) return;
                    kick(boss);
                }
            } catch (Exception e) {}
        }).start();
    }
    
    public void kick(int index) {
        try {
            sendToTeam(new Message(11).sendShort(index).sendInt(players[index].getIdDb()).sendUTF("Bạn đã bị đuổi khỏi phòng"));
            leave(players[index]);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void leave(User us) {
        // if (started) fight.leave(us); // stub for fight
        leaveBoard(us);
        if (numPlayer == 0) return;
        try {
            sendToTeam(new Message(14).sendInt(us.getIdDb()).sendInt(players[boss].getIdDb()));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void leaveBoard(User us) {
        synchronized (players) {
            int max = ServerManager.maxPlayers;
            for (int i = 0; i < max; i++) {
                if (players[i] == null || players[i].getIdDb() != us.getIdDb()) continue;

                ServerManager.enterWait(players[i]);
                players[i] = null;
                if (boss == i) {
                    for (int j = 0; j < max; j++) {
                        if (players[j] != null) {
                            changeBoss(j);
                            break;
                        }
                    }
                }
                numPlayer--;
                soloTimeoutGeneration++;
                if (numPlayer == 1) startSoloTimeout();
                if (numPlayer == 0) {
                    started = false;
                    refreshFightWait();
                }
                break;
            }
        }
    }
    
    public void enterFireOval(User us) {
        synchronized (players) {
            try {
                if (numPlayer >= maxSetPlayer) {
                    us.sendMessage(new Message(45).sendUTF("Phòng đã đầy"));
                    return;
                }

                int bestLocation = -1;
                for (int i = 0; i < players.length; i++) {
                    if (players[i] == null) { bestLocation = i; break; }
                }
                if (bestLocation == -1) {
                    us.sendMessage(new Message(45).sendUTF("Phòng đã đầy"));
                    return;
                }

                us.setFightWait(this);
                ServerManager.removeWait(us.getIdDb());
                
                if (numPlayer == 0) changeBoss(bestLocation);
                
                sendToTeam(new Message(12)
                    .sendByte(bestLocation)
                    .sendInt(us.getIdDb())
                    .sendUTF(us.getUserName())
                    .sendInt(us.getXu())
                    .sendShort((short) us.getLevel())
                    .sendInt(us.getXp())
                    .sendByte((byte) us.getNvUse()));

                players[bestLocation] = us;
                readys[bestLocation] = false;
                numPlayer++;
                soloTimeoutGeneration++;
                if (numPlayer == 1) startSoloTimeout();

                Message roster = new Message(8)
                    .sendInt(players[boss].getIdDb())
                    .sendInt(money)
                    .sendByte(map)
                    .sendByte(type);
                for (int i = 0; i < players.length; i++) {
                    User u = players[i];
                    if (u != null) {
                        roster.sendInt(u.getIdDb())
                            .sendUTF(u.getUserName())
                            .sendInt(u.getXu())
                            .sendShort((short) u.getLevel())
                            .sendInt(u.getXp())
                            .sendByte((byte) u.getNvUse())
                            .sendBoolean(readys[i]);
                    } else {
                        roster.sendInt(-1);
                    }
                }
                us.sendMessage(roster);

                us.sendMessage(new Message(76).sendByte(parent.id).sendByte(id).sendUTF(name == null ? "" : name).sendByte(0));
                us.sendMessage(new Message(75).sendByte(MapData.getClientIndex(map)));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
    public void kickMessage(User us, Message ms) {
        try {
            int iddb = ms.readInt();
            if (players[boss].getIdDb() != us.getIdDb()) {
                leave(us);
                return;
            }
            if (started) return;

            int index = getIndexByIdDb(iddb);
            if (index == -1) return;
            if (readys[index]) return;

            kick(index);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void chatMessage(User us, Message ms) {
        try {
            int index = getIndexByIdDb(us.getIdDb());
            if (index == -1) return;
            String s = ms.readUTF();
            sendToTeam(new Message(9).sendInt(us.getIdDb()).sendUTF(s));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void setPassMessage(User us, Message ms) {
        try {
            if (players[boss].getIdDb() != us.getIdDb() || started) return;
            String matKhau = ms.readUTF();
            if (matKhau == null || matKhau.isEmpty()) {
                passSet = false;
                pass = "";
            } else {
                passSet = true;
                pass = matKhau;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void setMoneyMessage(User us, Message ms) {
        try {
            if (players[boss].getIdDb() != us.getIdDb() || started) return;
            int xu = Math.abs(ms.readInt());
            if (xu < parent.minXu || xu > parent.maxXu) {
                us.sendMessage(new Message(45).sendUTF("Mức cược không hợp lệ"));
                return;
            }
            if (xu > us.getXu()) {
                us.sendMessage(new Message(45).sendUTF("Không đủ xu"));
                return;
            }

            for (int i = 0; i < players.length; i++) {
                if (players[i] == null || i == getIndexByIdDb(us.getIdDb())) continue;
                readys[i] = false;
            }
            numReady = 0;
            money = xu;
            sendToTeam(new Message(19).sendShort(0).sendInt(xu));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void readyMessage(User us, Message ms) {
        try {
            boolean ready = ms.readBoolean();
            if (players[boss].getIdDb() == us.getIdDb() || started) return;

            int index = getIndexByIdDb(us.getIdDb());
            if (index == -1) return;

            if (readys[index] != ready) {
                readys[index] = ready;
                numReady += ready ? 1 : -1;
            }

            sendToTeam(new Message(16).sendInt(us.getIdDb()).sendBoolean(ready));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void setMapMessage(User us, Message ms) {
        try {
            if (players[boss].getIdDb() != us.getIdDb() || started) return;
            byte mapUp = (byte) ms.readByte();
            if (isLH) return;
            map = mapUp;

            for (int i = 0; i < players.length; i++) {
                if (players[i] == null || i == boss) continue;
                readys[i] = false;
            }
            numReady = 0;

            sendToTeam(new Message(75).sendByte(MapData.getClientIndex(map)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void setNameMessage(User us, Message ms) {
        try {
            if (players[boss].getIdDb() != us.getIdDb() || started) return;
            String nameS = ms.readUTF();
            name = nameS == null ? "" : nameS;
            sendToTeam(new Message(54).sendUTF(name));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void setMaxPlayerMessage(User us, Message ms) {
        try {
            if (players[boss].getIdDb() != us.getIdDb()) return;
            byte numPl = ms.readByte();
            if (numPl > 0 && numPl < 9 && numPl % 2 == 0 && numPlayer < numPl) {
                maxSetPlayer = numPl;
                sendToTeam(new Message(56).sendByte((byte) maxSetPlayer));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void doiPheMessage(User us, Message ms) {
        try {
            if (started) return;
            int i = -1, j = 0;
            synchronized (players) {
                for (i = players.length - 1; i >= 0; i--) {
                    if (players[i] == null || players[i].getIdDb() != us.getIdDb()) continue;
                    j = i % 2 == 0 ? 1 : 0;
                    for (; j < players.length; j += 2) {
                        if (players[j] == null) {
                            players[j] = us;
                            players[i] = null;
                            if (i == boss) boss = j;
                            break;
                        }
                    }
                    break;
                }
            }
            if (i >= 0) {
                sendToTeam(new Message(71).sendInt(us.getIdDb()).sendByte(j));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void setItemMessage(User us, Message ms) {
        try {
            if (started) return;
            int index = -1;
            synchronized (players) {
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null && players[i].getIdDb() == us.getIdDb()) { index = i; break; }
                }
                if (index == -1) return;
                
                // Read 6 bytes
                for (int i = 0; i < 6 && ms.available() > 0; i++) {
                    byte raw = ms.readByte();
                    if (raw == -1) {
                        item[index][i] = -1;
                        continue;
                    }
                    int id = raw & 0xFF;
                    boolean ok;
                    if (i >= 4) {
                        int bagId = 12 + (i - 4);
                        ok = us.getItemNum(bagId) > 0;
                    } else {
                        ok = us.getItemNum(id) > 0;
                    }
                    if (ok) {
                        item[index][i] = id;
                    } else {
                        us.sendMessage(new Message(45).sendUTF("Không đủ trang bị ở slot " + i));
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void findPlayerMessage(User us, Message ms) {
        try {
            boolean find = ms.readBoolean();
            int iddb = ms.readInt();
            if (find) {
                if (players[boss].getIdDb() != us.getIdDb() || started) return;
                Message reply = new Message(78);
                User[] found = ServerManager.findWaitPlayers(us);
                reply.sendBoolean(true).sendByte(found.length);
                for (User u0 : found) {
                    reply.sendUTF(u0.getUserName())
                         .sendInt(u0.getIdDb())
                         .sendByte(u0.getNvUse())
                         .sendInt(u0.getXu())
                         .sendInt(u0.getLevel())
                         .sendInt(u0.getXp());
                }
                us.sendMessage(reply);
            } else {
                User target = ServerManager.getUser(iddb);
                if (target == null) {
                    us.sendMessage(new Message(45).sendUTF("Người chơi đã offline"));
                    return;
                }
                if (target.getState() != User.State.Waiting) {
                    us.sendMessage(new Message(45).sendUTF("Người chơi đang bận"));
                    return;
                }
                target.sendMessage(new Message(78)
                    .sendBoolean(false)
                    .sendUTF(us.getUserName() + " mời bạn vào phòng")
                    .sendByte(parent.id)
                    .sendByte(id)
                    .sendUTF(pass));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void fightComplete() {
        started = false;
        for (int i = 0; i < players.length; i++) {
            readys[i] = false;
            User us = players[i];
            if (us == null) continue;
            
            try {
                Message ms = new Message(112);
                for (int j = 0; j < 2; j++) ms.sendByte(us.getItemNum(12 + j));
                us.sendMessage(ms);
            } catch (Exception e) {
                e.printStackTrace();
            }
            us.setFightWait(this);
        }
        
        numReady = 0;
        if (boss != -1) changeBoss(boss);
        try {
            sendToTeam(new Message(75).sendByte(com.mobiarmy.data.MapData.getClientIndex((byte) map)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        new Thread(() -> {
            try {
                while (timeStart > 0L) {
                    Thread.sleep(1000);
                    timeStart -= 1000L;
                }
            } catch (Exception e) {}
        }).start();
    }

    public void startGameMessage(ClientEntry cl) {
        try {
            if (started) return;
            boolean ok = true;
            synchronized (players) {
                for (int i = 0; i < maxSetPlayer; i++) {
                    if (players[i] == null) continue;
                    if (!readys[i] && i != boss) {
                        ok = false;
                        break;
                    }
                }
                if (numPlayer > 1 && ok) {
                    started = true;
                    if (isLH) {
                        ntLH++;
                    }
                    fight = new FightManager(this);
                    fight.startGame(0, 0);
                    return;
                }
            }
            // The client waits on "Xin chờ..." after asking to start; answer so it doesn't hang.
            cl.sendMessage(new Message(4).sendUTF(numPlayer < 2
                    ? "Cần ít nhất 2 người chơi để bắt đầu."
                    : "Còn người chơi chưa sẵn sàng."));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
