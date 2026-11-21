package com.mobiarmy.fight;

import com.mobiarmy.server.ServerManager;

import java.util.ArrayList;
import java.util.List;

public class Bullet {
    FightManager fm;
    BulletManager bullMNG;
    boolean collect;
    boolean hitRealCollision;
    byte bullId;
    int satThuong;
    short x;
    short y;
    short lastX;
    short lastY;
    short vx;
    short vy;
    short ax100;
    short ay100;
    short g100;
    short vxTemp;
    short vyTemp;
    short vyTemp2;
    boolean isMaxY;
    short xMaxY;
    short maxY;
    short frame;
    byte typeSC;
    Player pl;
    boolean isXuyenPlayer;
    boolean isXuyenMap;
    boolean isCanCollision;
    public List<Short> xArray;
    public List<Short> yArray;

    public Bullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
        this.fm = bullMNG.fm;
        this.bullMNG = bullMNG;
        this.bullId = bullId;
        this.satThuong = satThuong * pl.satThuong / 100;
        this.pl = pl;
        this.x = (short) x;
        this.y = (short) y;
        this.lastX = (short) x;
        this.lastY = (short) y;
        this.vx = (short) vx;
        this.vy = (short) vy;
        this.ax100 = (short) (bullMNG.fm.windX * msg / 100);
        this.ay100 = (short) (bullMNG.fm.windY * msg / 100);
        this.g100 = (short) g100;
        this.vxTemp = 0;
        this.vyTemp = 0;
        this.vyTemp2 = 0;
        this.collect = false;
        this.isMaxY = false;
        this.xMaxY = -1;
        this.maxY = -1;
        this.frame = 0;
        this.typeSC = 0;
        this.xArray = new ArrayList<>();
        this.yArray = new ArrayList<>();
        this.isXuyenPlayer = false;
        this.isXuyenMap = false;
        this.isCanCollision = true;
    }

    public boolean isCollect() {
        return collect;
    }

    public interface PlayerEffect {
        void apply(Player p);
    }

    protected void applyAreaEffect(PlayerEffect effect) {
        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player target = fm.players[i];
            if (target == null) continue;

            int tamAH = getTamAhByBulletId(bullId);
            int kcX = Math.abs(target.x - x);
            int kcY = Math.abs(target.y - target.height / 2 - y);
            int kc = (int) Math.sqrt(kcX * kcX + kcY * kcY);

            if (!target.isDie && target.voHinhCount <= 0 && kc <= tamAH + target.width / 2) {
                try {
                    effect.apply(target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void nextXY() {
        frame++;
        xArray.add(x);
        yArray.add(y);

        if (x < -200 || x > fm.mapMNG.getWidth() + 200 || y > fm.mapMNG.getHeight() + 200) {
            collect = true;
            return;
        }

        short preX = x, preY = y;
        x += vx;
        lastX = x;
        y += vy;
        lastY = y;

        short[] collisionPoint = bullMNG.getCollisionPoint(preX, preY, x, y, isXuyenPlayer, isXuyenMap);
        if (collisionPoint != null) {
            collect = true;
            hitRealCollision = true;
            x = collisionPoint[0];
            y = collisionPoint[1];
            xArray.add(x);
            yArray.add(y);

            if (pl.itemUsed == -1 && !pl.isUsePow) {
                if (isMaxY) {
                    if (y - maxY > 350 && y - maxY < 450) typeSC = 1;
                    else if (y - maxY >= 450) typeSC = 2;
                }
                if ((pl.gunId == 2 || pl.gunId == 3) && Math.abs(lastX - xArray.get(0)) > 375) {
                    typeSC = 4;
                }
            }
            if (isCanCollision) {
                fm.mapMNG.collision(x, y, this);
            }
            return;
        }

        vxTemp += (short) Math.abs(ax100);
        vyTemp += (short) Math.abs(ay100);
        vyTemp2 += g100;

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

        if (vy > 0 && !isMaxY) {
            isMaxY = true;
            xMaxY = x;
            maxY = y;
        }

        if (bullMNG.hasVoiRong) {
            for (BulletManager.VoiRong vr : bullMNG.voiRongs) {
                if (x >= vr.x - 5 && x <= vr.x + 10) {
                    vx -= 2;
                    vy -= 2;
                    break;
                }
            }
        }
    }

    public static byte getHoleByBulletId(byte bullId) {
        switch (bullId) {
            case 0: case 32: return 3;
            case 1: return 1;
            case 2: return 0;
            case 6: return 6;
            case 7: case 31: case 37: return 7;
            case 3: return 9;
            case 9: return 5;
            case 10: return 4;
            case 11: return 2;
            case 12: return 6;
            case 15: return 7;
            case 22: return 7;
            case 24: return 3;
            case 25: return 8;
            case 19: return 2;
            case 20: return 0;
            case 27: return 1;
            case 17: case 18: return 2;
            case 21: return 2;
            case 30: return 0;
            case 35: return 3;
            case 40: case 41: return 2;
            case 42: case 43: return 7;
            case 44: return 2;
            case 45: return 7;
            case 47: return 8;
            case 48: return 3;
            case 52: return 3;
            case 57: return 7;
            default: return 0;
        }
    }

    public static int getTamAhByBulletId(int bullId) {
        switch (bullId) {
            case 0: return 21;
            case 1: return 13;
            case 2: return 18;
            case 3: return 100;
            case 6: return 22;
            case 7: return 30;
            case 8: return 22;
            case 9: return 18;
            case 10: return 19;
            case 11: return 13;
            case 12: return 20;
            case 14: return 30;
            case 15: return 28;
            case 16: return 19;
            case 17: case 18: return 13;
            case 19: return 13;
            case 20: return 18;
            case 21: return 13;
            case 22: return 30;
            case 23: return 19;
            case 24: return 18;
            case 25: return 8;
            case 26: return 13;
            case 27: return 11;
            case 28: return 0;
            case 29: return 20;
            case 30: return 16;
            case 31: return 40;
            case 32: return 50;
            case 33: return 25;
            case 35: return 50;
            case 37: return 150;
            case 40: return 30;
            case 41: return 30;
            case 42: case 43: return 32;
            case 44: return 11;
            case 45: return 28;
            case 47: return 7;
            case 48: return 18;
            case 49: return 18;
            case 50: return 30;
            case 51: return 30;
            case 52: return 20;
            case 54: return 30;
            case 55: return 30;
            case 57: return 70;
            case 59: return 16;
            default: return 0;
        }
    }

    public static boolean isItemk(int bullId) {
        return bullId == 4 || bullId == 14 || bullId == 16 || bullId == 23 || bullId == 28;
    }

    public static boolean isChicApa(int bullId) {
        return bullId == 17 || bullId == 19;
    }
}
