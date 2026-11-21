package com.mobiarmy.fight;

import com.mobiarmy.data.Until;
import com.mobiarmy.server.ServerManager;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {
    public static class VoiRong {
        public int x;
        public int y;
        public int count;

        public VoiRong(int x, int y, int count) {
            this.x = x;
            this.y = y;
            this.count = count;
        }
    }

    public static class BomHenGio {
        public int id;
        public int x;
        public int y;
        public int count;
        public Bullet bull;

        public BomHenGio(int id, Bullet bull, int count) {
            this.id = id;
            this.x = bull.x;
            this.y = bull.y;
            this.count = count;
            this.bull = bull;
        }
    }

    public static class AddBoss {
        public Player players;
        public int xpe;

        public AddBoss(Player players, int xpe) {
            this.players = players;
            this.xpe = xpe;
        }
    }

    public static class BulletsObj {
        public Bullet bull;
        public BulletsObj(Bullet bull) {
            this.bull = bull;
        }
    }

    public FightManager fm;
    List<Bullet> entrys;
    byte force2;

    public int mangNhenId;
    public boolean hasVoiRong;
    public List<VoiRong> voiRongs;
    public List<BomHenGio> boms;
    public List<AddBoss> addboss;
    public List<BulletsObj> buls;
    public byte mgtAddX;
    public byte mgtAddY;
    public byte typeSC;
    public short xsc;
    public short ysc;
    public short arg;
    public short xpl;
    public short ypl;

    public BulletManager(FightManager fm) {
        this.fm = fm;
        this.entrys = new ArrayList<>();
        this.voiRongs = new ArrayList<>();
        this.boms = new ArrayList<>();
        this.addboss = new ArrayList<>();
        this.buls = new ArrayList<>();
        this.hasVoiRong = false;
        this.force2 = (byte) 255;
        this.mangNhenId = 200;
        this.xsc = 0;
        this.ysc = 0;
        this.typeSC = 0;
    }

    // AddBom, RemoveBom, ExploreBom omitted temporarily for compilation safety

    public void addBullet(Bullet bul) {
        entrys.add(bul);
    }

    public void addShoot(Player pl, byte bull, short angle, byte force, byte force2, byte nshoot) {
        xpl = pl.x;
        ypl = pl.y;
        arg = angle;
        this.force2 = force2;
        if (bull == 49 && !fm.ltap) force += 5;
        typeSC = 0;

        int x, y, vx, vy;
        x = pl.x + ((20 * Until.cos(angle)) >> 10);
        y = pl.y - 12 - ((20 * Until.sin(angle)) >> 10);
        vx = (force * Until.cos(angle)) >> 10;
        vy = -((force * Until.sin(angle)) >> 10);

        int idGun = pl.idNV;
        if (nshoot > 2 || nshoot < 1) return;
        if (idGun == 13) y -= 25;

        for (int k = 0; k < nshoot; k++) {
            switch (bull) {
                // Gunner
                case 0:
                    if (pl.itemUsed >= 0 || (idGun != 0 && idGun != 14)) return;
                    if (ServerManager.superAdmin) {
                        entrys.add(new SpecialBullets.ItemBomB52(this, (byte)4, 600, pl, x, y, vx, vy, 0, 80));
                        break;
                    }
                    entrys.add(new Bullet(this, (byte)0, pl.isUsePow ? 630 : (nshoot == 2 ? 210 : 280), pl, x, y, vx, vy, 80, 100));
                    break;
                // Aka
                case 1:
                    if (pl.itemUsed >= 0 || idGun != 1) return;
                    int nBull = pl.isUsePow ? 6 : 2;
                    for (int i = 0; i < nBull; i++) {
                        entrys.add(new Bullet(this, (byte)1, nshoot == 2 ? 109 : 145, pl, x, y, vx, vy, 50, 50));
                    }
                    break;
                // Electric
                case 2:
                    if (pl.itemUsed >= 0 || (idGun != 2 && idGun != 14)) return;
                    int nBulls = pl.isUsePow ? 4 : 2;
                    boolean xuyenNguoi = angle < 0 || angle > 180;
                    for (int i = 0; i < nBulls; i++) {
                        int arg1 = angle + i * 5;
                        x = pl.x + ((25 * Until.cos((short)arg1)) >> 10);
                        y = pl.y - 12 - ((25 * Until.sin((short)arg1)) >> 10);
                        vx = (force * Until.cos((short)arg1)) >> 10;
                        vy = -((force * Until.sin((short)arg1)) >> 10);
                        Bullet b = new Bullet(this, (byte)2, nshoot == 2 ? 75 : 100, pl, x, y, vx, vy, 80, 60);
                        b.isXuyenPlayer = xuyenNguoi;
                        entrys.add(b);
                        if (i == 0 || i == 4) continue;
                        arg1 = angle - i * 5;
                        x = pl.x + ((25 * Until.cos((short)arg1)) >> 10);
                        y = pl.y - 12 - ((25 * Until.sin((short)arg1)) >> 10);
                        vx = (force * Until.cos((short)arg1)) >> 10;
                        vy = -((force * Until.sin((short)arg1)) >> 10);
                        Bullet b2 = new Bullet(this, (byte)2, nshoot == 2 ? 75 : 100, pl, x, y, vx, vy, 80, 60);
                        b2.isXuyenPlayer = xuyenNguoi;
                        entrys.add(b2);
                    }
                    break;
                case 4:
                    if (pl.itemUsed != 8) return;
                    pl.us.updateMission((byte)5, (byte)1);
                    entrys.add(new SpecialBullets.ItemBomB52(this, (byte)4, 600, pl, x, y, vx, vy, 0, 80));
                    break;
                case 5:
                    if (pl.itemUsed != 1) return;
                    pl.isMM = false;
                    entrys.add(new SpecialBullets.ItemTeleport(this, (byte)5, 0, pl, x, y, vx, vy, 0, 80));
                    break;
                case 6:
                    if (pl.itemUsed != 6) return;
                    for (int i = 0; i < 3; i++) {
                        Bullet bomb = new Bullet(this, (byte)6, 200, pl, x, y, vx, vy, 70, 90);
                        bomb.isXuyenPlayer = true;
                        entrys.add(bomb);
                    }
                    break;
                case 7:
                    if (pl.itemUsed != 7) return;
                    entrys.add(new Bullet(this, (byte)7, 500, pl, x, y, vx, vy, 70, 80));
                    break;
                case 8:
                    if (pl.itemUsed == 9) {
                        entrys.add(new SpecialBullets.ItemToNhen(this, (byte)8, 300, pl, x, y, vx, vy, 70, 70));
                    }
                    if (idGun == 22) {
                        entrys.add(new SpecialBullets.SpiderDropSilk(this, (byte)8, 300, pl));
                    }
                    break;
                case 9:
                    if (pl.itemUsed >= 0 || idGun != 3) return;
                    int arg2 = angle;
                    for (int i = 0; i < 4; i++) {
                        x = pl.x + ((20 * Until.cos((short)arg2)) >> 10);
                        y = pl.y - 12 - ((20 * Until.sin((short)arg2)) >> 10);
                        vx = (force * Until.cos((short)arg2)) >> 10;
                        vy = -((force * Until.sin((short)arg2)) >> 10);
                        entrys.add(new Bullet(this, (byte)9, pl.isUsePow ? 210 : (nshoot == 2 ? 79 : 105), pl, x, y, vx, vy, 40, 90));
                    }
                    break;
                case 10:
                    if (pl.itemUsed >= 0 || (idGun != 4 && idGun != 14)) return;
                    for (int i = 0; i < 3; i++) {
                        entrys.add(new Bullet(this, (byte)10, pl.isUsePow ? 240 : (nshoot == 2 ? 80 : 107), pl, x, y, vx, vy, 50, 80));
                    }
                    break;
                case 11:
                    if (pl.itemUsed >= 0 || idGun != 5) return;
                    int granosCount = Until.nextInt(100) < 20 ? 1 : 5;
                    for (int i = 0; i < granosCount; i++) {
                        entrys.add(new Bullet(this, (byte)11, pl.isUsePow ? 140 : (nshoot == 2 ? 47 : 62), pl, x, y, vx, vy, 30, 90));
                    }
                    break;
                case 13:
                    if (pl.itemUsed != 15) return;
                    pl.isMM = false;
                    entrys.add(new SpecialBullets.ItemVoiRong(this, (byte)13, 0, pl, x, y, vx, vy, 50, 120));
                    break;
                case 14:
                    if (pl.itemUsed != 14) return;
                    entrys.add(new SpecialBullets.ItemLaser(this, (byte)14, 500, pl, x, y, vx, vy, 10, 50));
                    break;
                case 16:
                    if (pl.itemUsed != 11) return;
                    entrys.add(new SpecialBullets.ItemTraiPha(this, (byte)16, 200, pl, x, y, vx, vy, 0, 100));
                    break;
                case 17:
                    if (pl.itemUsed >= 0 || idGun != 8) return;
                    entrys.add(new SpecialBullets.ApaBullet(this, (byte)17, nshoot == 2 ? 81 : 108, pl, x, y, vx, vy, 30, 100, force, force2));
                    break;
                case 19:
                    if (pl.itemUsed >= 0 || idGun != 6) return;
                    entrys.add(new SpecialBullets.ChickyBullet(this, (byte)19, pl.isUsePow ? 500 : (nshoot == 2 ? 169 : 225), pl, x, y, vx, vy, 20, 50, force2));
                    break;
                case 21:
                    if (pl.itemUsed >= 0 || idGun != 7) return;
                    entrys.add(new SpecialBullets.TazranBullet(this, (byte)21, pl.isUsePow ? 800 : (nshoot == 2 ? 225 : 340), pl, x, y, vx, vy, 10, 50));
                    break;
                case 22:
                    if (pl.itemUsed != 16) return;
                    entrys.add(new SpecialBullets.ItemChuotGanBom(this, (byte)22, 500, pl, x, y, force, angle < 89));
                    break;
                case 23:
                    if (pl.itemUsed != 19) return;
                    entrys.add(new SpecialBullets.ItemSaoBang(this, (byte)23, 200, pl, x, y, vx, vy, 20, 100));
                    break;
                case 25:
                    if (pl.itemUsed != 18) return;
                    vy = (force * Until.sin((short)-angle)) >> 10;
                    entrys.add(new SpecialBullets.ItemXuyenDat(this, (byte)25, 500, pl, x, y, vx, vy, 0, -50, force));
                    break;
                case 26:
                    if (pl.itemUsed != 17) return;
                    entrys.add(new SpecialBullets.ItemTenLua(this, (byte)26, 200, pl, x, y, vx, vy, 30, 60, force));
                    break;
                case 28:
                    if (pl.itemUsed != 20) return;
                    vx = 0;
                    vy = -force / 2;
                    arg = 90;
                    entrys.add(new SpecialBullets.ItemMuaDan(this, (byte)28, 200, pl, x, y, vx, vy, 0, 20));
                    break;
                case 30:
                    if (pl.itemUsed != 21) return;
                    pl.isMM = false;
                    entrys.add(new SpecialBullets.ItemKhoangDat(this, (byte)30, pl, pl.x, pl.y, force));
                    break;
                case 31:
                    if (idGun != 12) return;
                    entrys.add(new SpecialBullets.BigBoomBum(this, (byte)31, pl.hp, pl));
                    break;
                case 32:
                    if (idGun != 11) return;
                    entrys.add(new SpecialBullets.SmallBoomBum(this, (byte)32, pl.hp, pl));
                    break;
                case 33:
                    if (idGun != 13) return;
                    for (int i = 0; i < 5; i++) {
                        entrys.add(new Bullet(this, (byte)33, 120, pl, x, y, vx, vy, 50, 80));
                    }
                    break;
                case 34:
                    if (idGun != 12) return;
                    pl.isMM = false;
                    entrys.add(new SpecialBullets.SmallBoomAdd(this, (byte)34, 0, pl, x, y, vx, vy, 0, 80));
                    break;
                case 35:
                    if (idGun != 15 && idGun != 14) return;
                    entrys.add(new SpecialBullets.Jump(this, (byte)35, 1200, pl));
                    break;
                case 36:
                    if (idGun != 14 && idGun != 13) return;
                    entrys.add(new SpecialBullets.JumpOrFly(this, (byte)36, 0, pl, x, y, vx, vy, 0, 80));
                    break;
                case 37:
                    if (idGun != 15) return;
                    entrys.add(new SpecialBullets.BigRocKet(this, (byte)37, 570, pl));
                    break;
                case 40:
                    if (idGun != 15) return;
                    entrys.add(new Bullet(this, (byte)40, 220, pl, x - 20, y - 20, vx, vy, 10, 80));
                    break;
                case 41:
                    if (idGun != 15) return;
                    entrys.add(new Bullet(this, (byte)41, 200, pl, x - 20, y - 20, vx, vy, 10, 80));
                    break;
                case 42:
                    if (idGun != 16) return;
                    entrys.add(new Bullet(this, (byte)42, 1000, pl, pl.x, pl.y, vx, vy + 10, 10, 0));
                    break;
                case 43:
                    if (idGun != 17) return;
                    for (int i = 0; i < 10; i++) {
                        if (i > 0) x += 105;
                        if (x > fm.mapMNG.getWidth()) {
                            x = 105 - (x - fm.mapMNG.getWidth());
                        }
                        entrys.add(new Bullet(this, (byte)43, 300, pl, x, y + 50, vx, vy, 0, 100));
                    }
                    break;
                case 44:
                    if (idGun != 17) return;
                    for (int i = 0; i < 15; i++) {
                        short vxrd = (short)Until.nextInt(-10, 10);
                        entrys.add(new Bullet(this, (byte)44, 100, pl, x + 51, y + 40, vx + vxrd, vy, 40, 40));
                    }
                    break;
                case 45:
                    if (idGun != 17) return;
                    entrys.add(new SpecialBullets.BalloonLazer(this, (byte)45, 500, pl, x + 65, y - 27));
                    break;
                case 47:
                    if (idGun != 22) return;
                    for (int i = 0; i < 5; i++) {
                        int arg3 = angle + i * 5;
                        x = pl.x + ((30 * Until.cos((short)arg3)) >> 10);
                        y = pl.y - 12 - ((30 * Until.sin((short)arg3)) >> 10);
                        vx = (force * Until.cos((short)arg3)) >> 10;
                        vy = -((force * Until.sin((short)arg3)) >> 10);
                        entrys.add(new Bullet(this, (byte)47, 400, pl, x, y, vx, vy, 0, 0));
                    }
                    break;
                case 49:
                    if (pl.itemUsed >= 0 || idGun != 9) return;
                    if (ServerManager.mgtBullNew) {
                        entrys.add(new SpecialBullets.MGTBulletNew(this, (byte)49, pl.isUsePow ? 1000 : (nshoot == 2 ? 308 : 400), pl, x, y, vx, vy, 40, 70, force));
                    } else {
                        vx = (1600 * Until.cos(angle)) >> 10;
                        vy = -((1600 * Until.sin(angle)) >> 10);
                        entrys.add(new SpecialBullets.MGTBulletOld(this, (byte)59, pl.isUsePow ? 1000 : (nshoot == 2 ? 308 : 400), pl, x, y, vx, vy, force));
                    }
                    break;
            }
        }
    }

    public void fillXY() {
        boolean hasNext;
        int safetyFrames = 0;
        final int MAX_SAFETY_FRAMES = 600;

        do {
            hasNext = false;
            for (int i = 0; i < entrys.size(); i++) {
                Bullet bull = entrys.get(i);
                if (bull == null || bull.isCollect()) continue;
                hasNext = true;
                bull.nextXY();
            }
            safetyFrames++;
            if (safetyFrames >= MAX_SAFETY_FRAMES) {
                for (Bullet bull : entrys) {
                    if (bull != null && !bull.isCollect()) bull.collect = true;
                }
                break;
            }
        } while (hasNext);
    }

    public void reset() {
        entrys.clear();
    }

    public short[] getCollisionPoint(short x1, short y1, short x2, short y2, boolean isXuyenPlayer, boolean isXuyenMap) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int xUnit = 0, yUnit = 0, xUnit2 = 0, yUnit2 = 0;
        
        Player us = null;
        int turnIndex = fm.getIdTurn();
        if (turnIndex >= 0 && turnIndex < fm.players.length) {
            us = fm.players[turnIndex];
        }

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
            if (!isXuyenMap && fm.mapMNG.isCollision(x, y)) {
                return new short[]{x, y};
            }
            if (!isXuyenPlayer && (us != null && us.idNV != 16)) {
                for (int j = 0; j < fm.allCount; j++) {
                    Player pl = fm.players[j];
                    if (pl == null) continue;
                    if (pl.idNV > 15 && pl.isDie) continue;
                    if (pl.isCollision(x, y)) return new short[]{x, y};
                }
            }
            if (us != null && us.idNV == 16) {
                for (int j = 0; j < ServerManager.maxPlayers; j++) {
                    Player pl = fm.players[j];
                    if (pl == null || pl.isDie) continue;
                    if (pl.isCollision(x, y)) return new short[]{x, y};
                }
            }

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
        return null;
    }
}
