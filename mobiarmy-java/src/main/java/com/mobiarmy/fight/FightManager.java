package com.mobiarmy.fight;

import com.mobiarmy.network.Message;
import com.mobiarmy.server.Room;
import com.mobiarmy.server.ServerManager;
import com.mobiarmy.server.User;
import com.mobiarmy.server.FightWait;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
public class FightManager {
    public final FightWait wait;
    private final User userLt;
    private boolean isShoot;
    private int teamlevel;
    public boolean isNextTurn;
    private Date matchTime;
    public boolean ltap;

    public boolean adminTestMode;
    public byte type;
    public boolean isBossTurn;
    public byte bossTurn;
    public byte playerTurn; 

    public int nTurn;
    public byte nHopQua;
    public int playerCount;
    public int allCount;
    public int windX;
    public int windY;
    public boolean isFight;
    private static final byte TIME_COUNT_MAX = 30;
    
    public Player[] players;
    public MapManager mapMNG;
    public BulletManager bullMNG;
    public CountDownMNG countDownMNG;

    private final Object turnLock = new Object();

    public FightManager(User us, byte map) {
        this.wait = null;
        this.ltap = true;
        this.isShoot = false;
        this.isNextTurn = true;
        this.type = 0;
        this.playerCount = 1;
        this.playerTurn = -1;
        this.nTurn = 0;
        this.isBossTurn = false;
        this.bossTurn = 0;
        this.allCount = 1;
        this.windX = 0;
        this.windY = 0;
        this.isFight = false;
        this.nHopQua = 0;
        this.mapMNG = new MapManager(this);
        this.bullMNG = new BulletManager(this);
        this.countDownMNG = null;
        this.userLt = us;
        this.mapMNG.setMapId(map);
    }

    public FightManager(FightWait fo) {
        this.isShoot = false;
        this.isNextTurn = true;
        this.wait = fo;
        this.userLt = null;
        this.ltap = false;
        this.type = fo.type;
        this.playerCount = 0;
        this.allCount = 0;
        this.playerTurn = -1;
        this.isBossTurn = false;
        this.bossTurn = 0;
        this.windX = 0;
        this.windY = 0;
        this.nHopQua = 0;
        this.players = new Player[ServerManager.maxElementFight];
        this.isFight = false;
        this.mapMNG = new MapManager(this);
        this.bullMNG = new BulletManager(this);
        this.countDownMNG = new CountDownMNG(this, TIME_COUNT_MAX);
        this.countDownMNG.second = TIME_COUNT_MAX; // using dummy stub for now
    }

    public void setMap(byte map) {
        mapMNG.setMapId(map);
    }

    public void sendToTeam(Message ms) {
        try {
            if (ltap) {
                userLt.sendMessage(ms);
                return;
            }
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                User user = players[i] != null ? players[i].us : null;
                if (user != null) user.sendMessage(ms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeUser(User us) {
        synchronized (players) {
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                if (players[i] != null && players[i].us != null && players[i].us.getIdDb() == us.getIdDb()) {
                    players[i] = null;
                    break;
                }
            }
        }
    }

    public int getIndexByIdDb(int iddb) {
        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            if (players[i] != null && players[i].us != null && players[i].us.getIdDb() == iddb) {
                return i;
            }
        }
        return -1;
    }

    public int getIdTurn() {
        if (ltap) return 0;
        if (isBossTurn && type == 5) return bossTurn;
        return playerTurn;
    }

    public Player getPlayerTurn() {
        return isBossTurn ? players[bossTurn] : players[playerTurn];
    }

    private byte safeTurnByte() {
        int raw = isBossTurn ? bossTurn : playerTurn;
        int maxValid = ServerManager.maxPlayers;

        if (raw < 0 || raw > maxValid) {
            System.out.println("FightManager.safeTurnByte: turn index KHÔNG hợp lệ (raw=" + raw + ", isBossTurn=" + isBossTurn + ", bossTurn=" + bossTurn + ", playerTurn=" + playerTurn + ", allCount=" + allCount + ") — kẹp về " + maxValid + " để không làm hỏng client.");
            return (byte) maxValid;
        }

        return (byte) raw;
    }

    public Player getPlayerClosest(short x, short y) {
        int closestDist = -1;
        Player closest = null;
        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = players[i];
            if (pl == null || pl.isDie) continue;

            int kcX = Math.abs(pl.x - x);
            if (closestDist == -1 || kcX < closestDist) {
                closestDist = kcX;
                closest = pl;
            }
        }
        return closest;
    }

    public int getWindX() { return windX; }
    public int getWindY() { return windY; }
    public int getLevelTeam() { return teamlevel; }
    public boolean getIsLh() { 
        return wait != null && wait.isLH; 
    }

    public void startGame(int nTeamPointBlue, int nTeamPointRed) {
        if (isFight) return;

        if (!ltap) {
            mapMNG.entrys.clear();
            setMap(wait.map);
        } else {
            mapMNG.entrys.clear();
            mapMNG.setMapId(mapMNG.id);
        }

        playerTurn = -1;
        nTurn = 0;
        windX = 0;
        windY = 0;
        isFight = true;
        playerCount = ltap ? 1 : wait.numPlayer;
        allCount = ServerManager.maxPlayers;

        if (mapMNG.xPlayerInit == null || mapMNG.xPlayerInit.length == 0) {
            isFight = false;
            System.out.println("StartGame: map id=" + (ltap ? mapMNG.id : wait.map) + " has no spawn data. Aborting.");
            return;
        }

        if (ltap) {
            userLt.setFightManager(this);
            userLt.setState(User.State.Fighting);
            players = new Player[ServerManager.maxPlayers];
            players[0] = new Player(this, (byte)0, ServerManager.xLtap[0], ServerManager.yLtap[0], Player.getLuyenTapItem(), 0, userLt);
            players[1] = new Player(this, (byte)1, ServerManager.xLtap[1], ServerManager.yLtap[1], Player.getLuyenTapItem(), 0, userLt);
            for (int i = 2; i < 8; i++) players[i] = null;
            
            System.out.println("LuyenTap StartGame - Map ID: " + mapMNG.id);
            System.out.println("LuyenTap - Player 0 (x,y): " + players[0].x + ", " + players[0].y);
            System.out.println("LuyenTap - Player 1 (x,y): " + players[1].x + ", " + players[1].y);
        } else {
            if (type == 5) nHopQua = (byte)(playerCount / 2);

            adminTestMode = ServerManager.superAdmin && wait.numPlayer == 1;

            int[] location = new int[8];
            int count = 0;
            teamlevel = 0;

            for (int i = 0; i < wait.maxSetPlayer; i++) {
                User us = wait.players[i];
                if (us == null) {
                    players[i] = null;
                    continue;
                }

                teamlevel += us.getLevel();
                us.updateXu(-wait.money);
                us.setFightManager(this);
                // In-match commands (shoot, move, items…) are only accepted in Fighting state;
                // endGame resets this to WaitFight.
                us.setState(User.State.Fighting);

                int locaCount;
                boolean exists;
                int attempts = 0;
                do {
                    locaCount = com.mobiarmy.data.Until.nextInt(0, mapMNG.xPlayerInit.length);
                    exists = false;
                    for (int j = 0; j < count; j++) {
                        if (location[j] == locaCount) { exists = true; break; }
                    }
                    attempts++;
                } while (exists && attempts < mapMNG.xPlayerInit.length * 4);

                location[count++] = locaCount;
                short x = mapMNG.xPlayerInit[locaCount];
                short y = mapMNG.yPlayerInit[locaCount];

                int[] item = wait.item[i];
                for (int j = 0; j < 2; j++) {
                    if (item[4 + j] >= 0) {
                        us.updateItem((byte)(12 + j), -1);
                    }
                }

                int teamPoint = type == 5 || i % 2 == 0 ? nTeamPointBlue : nTeamPointRed;
                // players[i] = us.isBot ? new SuperBoss(this, (byte)i, x, y, item, teamPoint, us, us.botDifficulty) : new Player(this, (byte)i, x, y, item, teamPoint, us);
                players[i] = new Player(this, (byte)i, x, y, item, teamPoint, us);
            }
        }

        bullMNG.mangNhenId = 200;
        sendFightInfoMessage();
        if (type == 5) nextBoss();

        matchTime = new Date();
        nextTurn();
    }

    public void sendFightInfoMessage() {
        if (!isFight) return;
        if (!ltap && wait.money > 0) {
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = players[i];
                if (pl == null || pl.us == null) continue;
                try {
                    sendToTeam(new Message(52).sendInt(pl.us.getIdDb()).sendInt(-wait.money).sendInt(pl.us.getXu()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        byte timeCount = ltap ? (byte) 0 : (byte) TIME_COUNT_MAX;
        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = players[i];
            if (pl == null || pl.us == null) continue;

            try {
                Message ms = new Message(20);
                ms.sendByte(com.mobiarmy.data.MapData.getClientIndex((byte) mapMNG.id));
                ms.sendByte(timeCount);

                for (int j = 0; j < 8; j++) {
                    Player pl2 = j < players.length ? players[j] : null;
                    if (pl2 == null) {
                        ms.sendShort((short) -1);
                    } else {
                        ms.sendShort(pl2.x).sendShort(pl2.y);
                    }
                }
                System.out.println("sendFightInfoMessage: Sending Message(20) to player " + i);
                pl.us.sendMessage(ms);
            } catch (Exception e) {
                System.out.println("sendFightInfoMessage ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void countOut() {
        if (isFight && !checkWin()) {
            nextTurn();
        }
    }

    public void changeLocation(int index) {
        Player pl = players[index];
        System.out.println("Player " + index + " change location X=" + pl.x + " Y=" + pl.y);
        try {
            sendToTeam(new Message(21).sendByte((byte) index).sendShort(pl.x).sendShort(pl.y));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pl.y > mapMNG.getHeight() || pl.x < 0 || pl.x > mapMNG.getWidth()) {
            pl.isDie = true;
            pl.hp = 0;
            pl.isUpdateHP = true;
            if (!checkWin() && index == getIdTurn()) {
                nextTurn();
            }
        }
    }

    public void flyChangeLocation(int index) {
        Player pl = players[index];
        try {
            sendToTeam(new Message(93).sendByte((byte) index).sendShort(pl.x).sendShort(pl.y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBoss(Player pl) {
        if (allCount >= ServerManager.maxElementFight) return;
        players[allCount] = pl;
        try {
            sendToTeam(new Message(89).sendByte((byte) 1).sendInt(-1).sendUTF("Boss")
                    .sendShort((short) 0).sendByte(pl.idNV).sendShort(pl.x).sendShort(pl.y));
        } catch (Exception e) {
            e.printStackTrace();
        }
        allCount++;
    }

    public void nextBoss() {
        // stub
    }

    public void newShoot(int index, byte bullId, short arg, byte force, byte force2, byte nshoot, boolean ltap) {
        System.out.println("New shoot index=" + index + " bullId: " + bullId + " arg: " + arg + " force: " + force + " force2: " + force2 + " nshoot: " + nshoot);

        Player pl = players[index];
        short x = pl.x, y = pl.y;

        if (!ltap) {
            // CalcMM(); // Stub if missing
        }
        bullMNG.addShoot(pl, bullId, arg, force, force2, nshoot);
        bullMNG.fillXY();
        if (!this.ltap) {
            // NextMM(); // Stub if missing
        }

        List<Bullet> bullets = bullMNG.entrys;
        if (bullets.isEmpty()) {
            isShoot = false;
            return;
        }

        bullId = bullets.get(0).bullId;
        Message ms = new Message(ltap ? 84 : 22);

        try {
            ms.sendByte((byte) index);
            ms.sendByte(bullId);
            ms.sendShort(x).sendShort(y).sendShort(arg);

            if (bullId == 17 || bullId == 19) {
                ms.sendByte(bullMNG.force2);
            }
            if (bullId == 14 || bullId == 40) {
                ms.sendByte((byte) 0).sendByte((byte) 0);
            }
            if (bullId == 44 || bullId == 45 || bullId == 47) {
                ms.sendByte((byte) 0);
            }

            ms.sendByte(nshoot);
            ms.sendByte((byte) bullets.size());

            int maxFrames = 0;
            for (Bullet bull : bullets) {
                List<Short> bx = bull.xArray;
                List<Short> by = bull.yArray;
                if (bx.size() > maxFrames) maxFrames = bx.size();

                ms.sendShort((short) bx.size());
                for (int j = 0; j < bx.size(); j++) {
                    ms.sendShort(bx.get(j)).sendShort(by.get(j));
                }
            }

            bullMNG.reset();
            sendToTeam(ms);

            pl.itemUsed = -1;

            int delayMs = Math.max(300, Math.min(20000, maxFrames * 40 + 300));

            if (this.ltap) {
                new Thread(() -> {
                    try {
                        Thread.sleep(delayMs);
                        nextHP();
                        nextTurn();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (isNextTurn) {
                new Thread(() -> {
                    try {
                        Thread.sleep(delayMs);
                        // Damage was applied while the shot was simulated; show it once the bullet lands.
                        nextHP();
                        if (!checkWin()) {
                            nextTurn();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                try {
                    Thread.sleep(delayMs);
                    nextHP();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shootMessage(User us, Message ms) {
        try {
            int index = getIndexByIdDb(us.getIdDb());
            boolean waitOk = ltap || wait.started;

            synchronized (turnLock) {
                if (index == -1 || index != playerTurn || isShoot || !waitOk) return;
                isShoot = true;
            }

            Player pl = players[index];
            byte bullId = (byte) ms.readByte();
            short x = ms.readShort();
            short y = ms.readShort();
            short arg = ms.readShort();
            byte force = (byte) ms.readByte();
            
            byte force2 = 0;
            if (bullId == 17 || bullId == 19) {
                force2 = (byte) ms.readByte();
            }
            
            ms.readByte(); // nshoot ignored
            byte nshoot;
            if (pl.banX2) {
                nshoot = 2;
                pl.banX2 = false;
            } else {
                nshoot = 1;
            }

            if (x != pl.x || y != pl.y) {
                pl.setXY(x, y);
            }

            newShoot(index, bullId, 
                     arg > 360 ? (short)360 : arg < -360 ? (short)-360 : arg, 
                     force > 30 ? (byte)30 : force, 
                     force2, 
                     nshoot, 
                     ltap);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void changeLocationMessage(User us, Message ms) {
        try {
            int index = getIndexByIdDb(us.getIdDb());
            boolean waitOk = ltap || wait.started;

            if (index == -1 || isBossTurn || index != playerTurn || isShoot || !waitOk) return;

            Player pl = players[index];
            short x = ms.readShort();
            short y = ms.readShort();
            pl.updateXY(x, y);
            changeLocation(index);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void boLuotMessage(User us) {
        int index = getIndexByIdDb(us.getIdDb());
        if (index == -1 || index != playerTurn || checkWin()) return;
        nextTurn();
    }

    public void useItemMessage(User us, Message ms) {
        try {
            byte idItem = (byte) ms.readByte();
            int index = getIndexByIdDb(us.getIdDb());
            if (index == -1) return;

            if (!ltap) {
                if (!isFight) return;
                if (index != playerTurn) return;
            }

            if (idItem > com.mobiarmy.data.ItemData.MAX_CARRY_COUNT.length - 1 && idItem != 100) return; // Note: you might need to adjust this depending on ItemData

            if (idItem == 12 || idItem == 13) {
                us.sendMessage(new Message(45).sendUTF("Không thể dùng item này!"));
                return;
            }

            Player pl = players[index];
            if (pl == null || pl.isUseItem) return;

            if (type == 5 && (idItem == 9 || idItem == 26 || idItem == 23 || idItem == 27 || idItem == 28 || idItem == 30 || idItem == 31)) {
                us.sendMessage(new Message(45).sendUTF("Không thể dùng item này!"));
                return;
            }

            int indexItem = -1;
            if (idItem != 100) {
                for (int i = 0; i < pl.item.length; i++) {
                    if (pl.item[i] == idItem) indexItem = i;
                }
                if (indexItem == -1) return;
            }

            sendToTeam(new Message(26).sendByte(index).sendByte(idItem));
            pl.isUseItem = true;
            pl.itemUsed = (byte) idItem;

            if (indexItem >= 0) {
                if (idItem > 1) pl.us.updateItem(idItem, -1);
                pl.item[indexItem] = -1;
            }

            switch (idItem) {
                case 0:
                    pl.updateHP(350);
                    nextHP();
                    break;
                case 2:
                    pl.banX2 = true;
                    break;
                case 3:
                    pl.diX2 = true;
                    pl.theLuc = 60;
                    break;
                case 4:
                    pl.tangHinhCount = 5;
                    break;
                case 5:
                    pl.ngungGioCount = 4;
                    nextWind();
                    break;
                case 10:
                    int step = type == 5 ? 1 : 2;
                    int i = pl.team ? 0 : 1;
                    for (; i < ServerManager.maxPlayers; i += step) {
                        Player pl2 = players[i];
                        if (pl2 == null || pl2.isDie) continue;
                        pl2.updateHP(300);
                    }
                    nextHP();
                    break;
            }

            if (idItem == 0 || idItem == 2 || idItem == 3 || idItem == 4 || idItem == 5 || idItem == 10 || idItem == 100) {
                pl.itemUsed = -1;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void removeBullMessage(User us, Message ms) {
        try {
            int lent = ms.readUnsignedByte();
            for (int i = 0; i < lent; i++) {
                ms.readInt();
                ms.readInt();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void luyenTapMessage(User us) {
        try {
            us.sendMessage(new Message(-6).sendByte(com.mobiarmy.data.MapData.getClientIndex((byte) mapMNG.id)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void startLuyenTapMessage(User us, Message ms) {
        try {
            byte typeS = ms.readByte();
            if (typeS == 0) {
                if (us.getState() == User.State.Waiting) {
                    startGame(0, 0);
                }
            } else if (typeS == 1) {
                if (us.getState() == User.State.Fighting) {
                    isFight = false;
                    ServerManager.enterWait(userLt);
                    userLt.sendMessage(new Message(83));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private boolean isBalloonPart(Player pl) {
        if (pl == null) return false;
        return pl.idNV == 18 || pl.idNV == 19 || pl.idNV == 20 || pl.idNV == 21 || pl.idNV == 23 || pl.idNV == 24;
    }

    public void nextTurn() {
        synchronized (turnLock) {
            nextTurnLocked();
        }
    }

    private void nextTurnLocked() {
        if (!isNextTurn) return;

        nTurn++;
        for (int i = 0; i < allCount; i++) {
            if (players[i] != null) players[i].chuanHoaXY();
        }

        if (ltap) {
            playerTurn = 0;
        } else {
            if (countDownMNG != null) countDownMNG.stopCount();

            if (playerTurn == -1) {
                pickInitialTurn();
            } else {
                nextAngry();
                if (!isBossTurn) {
                    Player plTurn = playerTurn >= 0 && playerTurn < players.length ? players[playerTurn] : null;
                    if (plTurn != null) {
                        plTurn.isUsePow = false;
                        plTurn.isUseItem = false;
                        plTurn.itemUsed = -1;
                    }
                }

                if (type == 5) {
                    if (isBossTurn) {
                        isBossTurn = false;
                        int turn = playerTurn + 1;
                        while (turn != playerTurn) {
                            if (turn == ServerManager.maxPlayers) turn = 0;
                            if (players[turn] != null && !players[turn].isDie && !isBalloonPart(players[turn])) {
                                playerTurn = (byte) turn;
                                break;
                            }
                            turn++;
                        }
                    } else {
                        isBossTurn = true;
                        int turn = bossTurn + 1;
                        while (turn != bossTurn) {
                            if (turn >= allCount) turn = ServerManager.maxPlayers;
                            if (players[turn] != null && !players[turn].isDie && !isBalloonPart(players[turn])) {
                                bossTurn = (byte) turn;
                                break;
                            }
                            turn++;
                        }
                    }
                } else {
                    int turn = playerTurn + 1;
                    while (turn != playerTurn) {
                        if (turn == allCount) turn = 0;
                        if (players[turn] != null && !players[turn].isDie && !isBalloonPart(players[turn])) {
                            playerTurn = (byte) turn;
                            break;
                        }
                        turn++;
                    }
                }
            }
        }

        if (!isBossTurn) {
            isShoot = false;
            Player pl = players[playerTurn];
            if (pl != null) {
                pl.buocDi = 0;
                if (pl.hutMauCount > 0) pl.hutMauCount--;

                if (pl.voHinhCount > 0) {
                    pl.voHinhCount--;
                    if (pl.voHinhCount == 0) huyVoHinh((byte) playerTurn);
                }
                if (pl.tangHinhCount > 0) {
                    pl.tangHinhCount--;
                    if (pl.tangHinhCount == 0) huyVoHinh((byte) playerTurn);
                }
                if (pl.cantSeeCount > 0) {
                    pl.cantSeeCount--;
                    if (pl.cantSeeCount == 0) huyCantSee((byte) playerTurn);
                }
                if (pl.cantMoveCount > 0) {
                    pl.cantMoveCount--;
                    if (pl.cantMoveCount == 0) huyCantSee((byte) playerTurn);
                }
                if (pl.isBiDoc) pl.updateHP(-150);
                pl.updateAngry(10);
            }
        } else {
            if (players[bossTurn] != null) players[bossTurn].buocDi = 0;
        }

        if (!checkWin() && players[getIdTurn()] != null && players[getIdTurn()].isDie) {
            nextTurn();
            return;
        }

        try {
            sendToTeam(new Message(24).sendByte(safeTurnByte()));
        } catch (Exception e) {}

        nextWind();

        if (ltap) return;
        
        if (countDownMNG != null) countDownMNG.resetCount();
    }

    private void pickInitialTurn() {
        int pool = type != 5 ? ServerManager.maxPlayers : allCount;
        int bound = Math.max(pool, 1) * 8;

        for (int attempts = 0; attempts < bound; attempts++) {
            int next = type != 5 ? com.mobiarmy.data.Until.nextInt(0, ServerManager.maxPlayers) : com.mobiarmy.data.Until.nextInt(0, allCount);
            if (players[next] == null || isBalloonPart(players[next])) continue;

            if (next < ServerManager.maxPlayers) {
                playerTurn = (byte) next;
                isBossTurn = false;
                bossTurn = (byte) ServerManager.maxPlayers;
            } else {
                bossTurn = (byte) next;
                isBossTurn = true;
                playerTurn = 0;
            }
            return;
        }
    }

    public boolean checkWin() {
        if (ltap) return false;
        
        boolean redLive = false;
        boolean blueLive = false;

        for (int i = 0; i < allCount; i++) {
            if (players[i] != null && !players[i].isDie && players[i].us != null) {
                if (players[i].team) blueLive = true;
                else redLive = true;
            }
        }

        if (type == 5) {
            boolean bossLive = false;
            for (int i = ServerManager.maxPlayers; i < allCount; i++) {
                if (players[i] != null && !players[i].isDie) {
                    bossLive = true;
                    break;
                }
            }
            if (blueLive && !bossLive) {
                fightComplete(0);
                return true;
            } else if (!blueLive) {
                fightComplete(1);
                return true;
            }
        } else {
            if (blueLive && !redLive) {
                fightComplete(0);
                return true;
            } else if (!blueLive && redLive) {
                fightComplete(1);
                return true;
            } else if (!blueLive && !redLive) {
                fightComplete(2);
                return true;
            }
        }
        return false;
    }

    private byte getPlayerResult(Player pl, int checkWin) {
        return (byte) (pl.team ? checkWin : -checkWin);
    }

    public void fightComplete(int checkWin) {
        if (!isFight) return;
        isFight = false;
        windX = 0;
        windY = 0;
        nHopQua = 0;
        bullMNG.hasVoiRong = false;
        bullMNG.voiRongs.clear();
        bullMNG.boms.clear();
        bullMNG.addboss.clear();

        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = players[i];
            if (pl != null && pl.us != null) {
                pl.us.setState(User.State.WaitFight);
                pl.us.setFightManager(null);
            }
        }

        boolean lhFinish = false;
        boolean lhSuccess = false;

        if (type == 5 && checkWin == 1) {
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = players[i];
                if (pl != null && pl.us != null) pl.us.updateXP(10, true);
            }
        }

        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = players[i];
            if (pl == null || pl.us == null) continue;

            byte win = getPlayerResult(pl, checkWin);
            if (win == 1 && nTurn > 2) {
                if (playerCount == 2) pl.us.updateMission((byte) 0, 1);
                else if (playerCount >= 5) pl.us.updateMission((byte) 17, 1);

                switch (pl.idNV) {
                    case 0: pl.us.updateMission((byte) 13, 1); break;
                    case 1: pl.us.updateMission((byte) 14, 1); break;
                    case 2: pl.us.updateMission((byte) 15, 1); break;
                }
                switch (mapMNG.id) {
                    case 35: pl.us.updateMission((byte) 2, 1); break;
                    case 36: pl.us.updateMission((byte) 3, 1); break;
                    case 38:
                    case 39: pl.us.updateMission((byte) 4, 1); break;
                }
            }

            try {
                pl.us.sendMessage(new Message(50).sendByte(win).sendByte(0).sendInt(wait.money));
            } catch (Exception e) {}
        }

        if (!wait.isLH && type == 5 && checkWin == 1) {
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = players[i];
                if (pl == null || pl.us == null) continue;

                byte idItem = (byte) com.mobiarmy.data.Until.nextInt(2, com.mobiarmy.data.ItemData.entries.size() - 1);
                byte numItem = (byte) com.mobiarmy.data.Until.nextInt(1, 5);
                pl.us.updateItem(idItem, numItem);

                String strItem = numItem + "x " + com.mobiarmy.data.ItemData.entries.get(idItem).getName();
                try {
                    sendToTeam(new Message(45).sendUTF("Nhận được phần thưởng " + strItem));
                } catch (Exception e) {}
            }
        }

        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = players[i];
            if (pl == null || pl.us == null) continue;

            try {
                pl.us.sendMessage(new Message(97)
                    .sendInt(pl.allXpUp)
                    .sendInt(pl.us.getXp())
                    .sendInt(pl.us.getLevel() * (pl.us.getLevel() + 1) * 1000)
                    .sendByte(0)
                    .sendByte(pl.us.getLevelPercen()));
            } catch (Exception e) {}
        }

        if (wait.money > 0) {
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                Player pl = players[i];
                if (pl == null || pl.us == null) continue;

                byte win = getPlayerResult(pl, checkWin);
                if (win >= 0) {
                    int payout = wait.money * (win == 1 ? 2 : 1);
                    pl.us.updateXu(payout);
                    try {
                        sendToTeam(new Message(52).sendInt(pl.us.getIdDb()).sendInt(payout).sendInt(pl.us.getXu()));
                    } catch (Exception e) {}
                }
            }
        }

        wait.started = false;
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        wait.fightComplete();

        // LH logic removed

        nTurn = 0;
    }

    /**
     * Sends message 51 for each player whose HP changed. Client 1.6.5 reads one player per message:
     * byte index, short hp, byte pixel (checked against bb.class bytecode).
     */
    public void nextHP() {
        for (int i = 0; i < allCount; i++) {
            Player pl = players[i];
            if (pl == null || !pl.isUpdateHP) continue;
            pl.isUpdateHP = false;
            try {
                sendToTeam(new Message(51).sendByte((byte) i)
                        .sendShort((short) Math.min(pl.hp, Short.MAX_VALUE)).sendByte(pl.pixel));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void nextWind() {
        if (!isFight) return;
        
        int wX = com.mobiarmy.data.Until.nextInt(0, 100);
        int wY = com.mobiarmy.data.Until.nextInt(0, 100);
        
        Player pl = getPlayerTurn();
        if (pl != null && pl.ngungGioCount > 0) {
            wX = 0;
            wY = 0;
            pl.ngungGioCount--;
        }
        
        if (com.mobiarmy.data.Until.nextInt(0, 100) > 50) wX = -wX;
        if (com.mobiarmy.data.Until.nextInt(0, 100) > 50) wY = -wY;

        try {
            Message ms = new Message(25);
            ms.sendByte(wX);
            sendToTeam(ms);
            windX = wX;
            windY = wY;
        } catch (Exception e) {}
    }

    public void nextAngry() {
        try {
            Message ms = new Message(52);
            ms.sendByte(allCount);
            for (int i = 0; i < allCount; i++) {
                Player pl = players[i];
                if (pl != null) {
                    ms.sendByte(pl.angry);
                } else {
                    ms.sendByte(0);
                }
            }
            sendToTeam(ms);
        } catch (Exception e) {}
    }

    public void huyVoHinh(byte id) {
        try {
            sendToTeam(new Message(66).sendByte(id));
        } catch (Exception e) {}
    }

    public void huyCantSee(byte id) {
        try {
            sendToTeam(new Message(67).sendByte(id));
        } catch (Exception e) {}
    }

    public void addBom(int id, int x, int y) {
        try {
            sendToTeam(new Message(109).sendByte((byte) 0).sendByte((byte) id).sendInt(x).sendInt(y));
        } catch (Exception e) {}
    }

    public void exploreBom(int id, int x, int y, com.mobiarmy.fight.Bullet bull) {
        try {
            mapMNG.collision((short) x, (short) y, bull);
            sendToTeam(new Message(109).sendByte((byte) 1).sendByte((byte) id));
        } catch (Exception e) {}
    }

    public void updateCantSee(Player pl) { pl.cantSeeCount = 5; }
    public void updateCantMove(Player pl) { pl.cantMoveCount = 5; }
    public void updateBiDoc(Player pl) { pl.isBiDoc = true; }

    public short[] getForceArgXY(int idGun, com.mobiarmy.fight.BulletManager bull, boolean isXuyenMap, short x, short y, short toX, short toY, short mx, short my, short arg, byte force, short msg, short g100) {
        byte i = (byte) (com.mobiarmy.data.Until.nextInt(2) == 0 ? -1 : 1);
        short argS = (short) (i == 1 ? arg : 180 - arg);
        byte forceS = force;

        for (int outerAttempts = 0; outerAttempts < 400; outerAttempts++) {
            short xs = (short) (x + ((20 * com.mobiarmy.data.Until.cos(argS)) >> 10));
            short ys = (short) (y - 12 - ((20 * com.mobiarmy.data.Until.sin(argS)) >> 10));
            short vx = (short) ((forceS * com.mobiarmy.data.Until.cos(argS)) >> 10);
            short vy = (short) -((forceS * com.mobiarmy.data.Until.sin(argS)) >> 10);
            short ax100 = (short) (bull.fm.getWindX() * msg / 100);
            short ay100 = (short) (bull.fm.getWindY() * msg / 100);
            short vxTemp = 0, vyTemp = 0, vyTemp2 = 0;

            if (idGun == 13) ys -= 25;

            while (true) {
                if (xs < -200 || xs > bull.fm.mapMNG.width + 200 || ys > bull.fm.mapMNG.height + 200) break;

                short preX = xs, preY = ys;
                xs += vx;
                ys += vy;

                byte collision = getCollisionPoint(bull, preX, preY, xs, ys, toX, toY, mx, my, isXuyenMap);
                if (collision == 1) return new short[] { argS, forceS };
                if (collision == 2) break;

                vxTemp += (short) Math.abs(ax100);
                vyTemp += (short) Math.abs(ay100);
                vyTemp2 += Math.abs(g100);

                if (Math.abs(vxTemp) >= 100) {
                    vx += (short) (ax100 > 0 ? vxTemp / 100 : -(vxTemp / 100));
                    vxTemp %= 100;
                }
                if (Math.abs(vyTemp) >= 100) {
                    vy += (short) (ay100 > 0 ? vyTemp / 100 : -(vyTemp / 100));
                    vyTemp %= 100;
                }
                if (Math.abs(vyTemp2) >= 100) {
                    vy += (short) (vyTemp2 / 100);
                    vyTemp2 %= 100;
                }
            }

            forceS++;
            if (forceS > 30) {
                argS += i;
                forceS = force;
                argS = (short) com.mobiarmy.data.Until.toArg0_360(argS);
                if (argS == arg) break;
            }
        }
        return null;
    }

    private byte getCollisionPoint(com.mobiarmy.fight.BulletManager bull, short x1, short y1, short x2, short y2, short toX, short toY, short mx, short my, boolean isXuyenMap) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int xUnit = 0, yUnit = 0, xUnit2 = 0, yUnit2 = 0;

        if (dx < 0) xUnit = xUnit2 = -1;
        else if (dx > 0) xUnit = xUnit2 = 1;
        if (dy < 0) yUnit = yUnit2 = -1;
        else if (dy > 0) yUnit = yUnit2 = 1;

        int k1 = Math.abs(dx);
        int k2 = Math.abs(dy);
        if (k1 > k2) {
            yUnit2 = 0;
        } else {
            k1 = Math.abs(dy);
            k2 = Math.abs(dx);
            xUnit2 = 0;
        }

        int k = k1 >> 1;
        short x = x1, y = y1;
        for (int i = 0; i <= k1; i++) {
            if (Math.abs(x - toX) <= mx && Math.abs(y - toY) <= my) return 1;
            if (!isXuyenMap && bull.fm.mapMNG.isCollision(x, y)) return 2;

            k += k2;
            if (k >= k1) {
                k -= k1;
                x += (short) xUnit;
                y += (short) yUnit;
            } else {
                x += (short) xUnit2;
                y += (short) yUnit2;
            }
        }
        return 0;
    }

    public void ghostBullet(int index, int toIndex) {
        try {
            sendToTeam(new Message(124).sendByte((byte) index).sendByte((byte) toIndex));
        } catch (Exception e) {}
    }

    public void capture(byte index, byte toIndex) {
        try {
            sendToTeam(new Message(95).sendByte(index).sendByte(toIndex));
        } catch (Exception e) {}
    }

    public void thadocBullet(byte index, byte toIndex) {
        try {
            sendToTeam(new Message(96).sendByte(index).sendByte(toIndex));
        } catch (Exception e) {}
    }

    public void cLocation(byte toIndex) {
        try {
            Player pl = players[toIndex];
            pl.us.sendMessage(new Message(21).sendByte(toIndex).sendShort(pl.x).sendShort(pl.y));
        } catch (Exception e) {}
    }
}
