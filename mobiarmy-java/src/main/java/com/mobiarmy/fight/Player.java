package com.mobiarmy.fight;

import com.mobiarmy.data.Until;
import com.mobiarmy.network.Message;
import com.mobiarmy.server.ServerManager;
import com.mobiarmy.server.User;

import java.util.Arrays;

public class Player {
    public FightManager fightMNG;
    public User us;
    public boolean team;
    public short idBullet;
    public short x;
    public short y;
    public short width;
    public short height;
    public int[] itemInit;
    public int[] item;
    public byte itemUsed;
    public boolean isUseItem;
    public boolean isUsePow;
    public boolean isDie;
    public boolean isUpdateHP;
    public boolean isUpdateAngry;
    public boolean isUpdateXP;
    public int xpUp;
    public int allXpUp;

    public byte idNV;
    public byte index;
    public short gunId;
    public int angry;
    public byte pixel;
    public int hp;
    public int hpMax;
    public int satThuong;
    public int mayMan;
    public int dongDoi;
    public byte ngungGioCount;
    public byte hutMauCount;
    public byte tangHinhCount;
    public byte voHinhCount;
    public byte cantSeeCount;
    public byte cantMoveCount;
    public boolean isBiDoc;
    public boolean diX2;
    public boolean banX2;
    public byte buocDi;
    public byte theLuc;
    public boolean isMM;
    public boolean fly;
    public int XPExist;

    public Player(FightManager fightMNG, byte location, short x, short y, int[] item, int teamPoint, User us) {
        this.fightMNG = fightMNG;
        this.index = location;
        this.team = fightMNG.type == 5 || location % 2 == 0;
        this.idBullet = -1;
        this.gunId = -1;
        this.x = x;
        this.y = y;
        this.theLuc = 30; 
        this.width = 24;
        this.height = 24;
        this.itemInit = item;
        this.fly = false;
        this.XPExist = 0;
        if (item != null) {
            this.item = Arrays.copyOf(item, item.length);
        }
        this.us = us;
        this.itemUsed = -1;
        this.isUseItem = false;
        this.isUsePow = false;
        this.isDie = false;
        this.angry = 0;
        this.pixel = 25;
        this.dongDoi = teamPoint;
        this.ngungGioCount = 0;
        this.hutMauCount = 0;
        this.voHinhCount = 0;
        this.cantSeeCount = 0;
        this.cantMoveCount = 0;
        this.isBiDoc = false;
        this.diX2 = false;
        this.banX2 = false;
        this.buocDi = 0;
        this.isMM = false;
        this.isUpdateAngry = false;
        this.isUpdateHP = false;
        this.isUpdateXP = false;
        this.xpUp = 0;
        this.idNV = 0;
        this.hpMax = 0;
        this.satThuong = 0;
        this.mayMan = 0;
        this.hp = 0;

        if (us == null) return;

        this.idBullet = us.getIdBullet();
        this.gunId = us.getGunId();
        this.idNV = us.getNvUse();
        int[] ability = us.getAbility();
        this.hpMax = 1000 + teamPoint * 5; 
        this.satThuong = ability[1] + teamPoint;
        
        this.hp = hpMax;
    }

    public static int[] getLuyenTapItem() {
        return new int[8];
    }

    public void setXY(short x, short y) {
        if (x >= 0 && x < fightMNG.mapMNG.getWidth() && y < fightMNG.mapMNG.getHeight()) {
            this.x = x;
            this.y = y;
        }
    }

    public void updateXY(short x, short y) {
        while (x != this.x || y != this.y) {
            int preX = this.x;
            int preY = this.y;
            if (x < this.x) move(false);
            else if (x > this.x) move(true);

            if (preX == this.x && preY <= this.y) return;
        }
    }

    public void chuanHoaXY() {
        while (y < fightMNG.mapMNG.getHeight() + 200) {
            if (fightMNG.mapMNG.isCollision(x, y) || fly) return;
            y++;
        }
    }

    protected void move(boolean addX) {
        if (cantMoveCount > 0) return;

        byte step = (byte) (diX2 ? 2 : 1);
        if (buocDi > theLuc) return;
        buocDi++;

        x += (short) (addX ? step : -step);

        if (fightMNG.mapMNG.isCollision(x, (short) (y - 5))) {
            buocDi--;
            x -= (short) (addX ? step : -step);
            return;
        }

        for (int i = 4; i >= 0; i--) {
            if (fightMNG.mapMNG.isCollision(x, (short) (y - i))) {
                y -= (short) i;
                return;
            }
        }
        chuanHoaXY();
    }

    public void updateHP(int addHP) {
        isUpdateHP = true;
        hp += addHP;
        if (hp <= 0) hp = 0;
        else if (hp < 10) hp = 10;
        else if (hp > hpMax) hp = hpMax;

        byte oldPixel = pixel;
        pixel = hpMax > 0 ? (byte) (hp * 25 / hpMax) : (byte) 0;

        if (addHP < 0) {
            updateAngry((oldPixel - pixel) * 4);
        }
        if (hp <= 0) {
            die();
        }
    }

    public void updateAngry(int addAngry) {
        isUpdateAngry = true;
        angry += addAngry;
        if (angry < 0) angry = 0;
        if (angry > 100) angry = 100;
    }

    protected void updateExp(int addXP) {
        if (us == null || addXP == 0) return;
        isUpdateXP = true;

        xpUp += addXP;
        addXP -= 2;
        if (addXP < 1) return;

        int i = team ? 0 : 1;
        int step = fightMNG.type == 5 ? 1 : 2;
        for (; i < ServerManager.maxPlayers; i += step) {
            Player pl = fightMNG.players[i];
            if (pl == null || pl == this) continue;
            pl.isUpdateXP = true;
            pl.xpUp += addXP;
            pl.allXpUp += addXP;
        }
    }

    private void die() {
        if (isMM && x > 0 && y < fightMNG.mapMNG.getHeight() && x < fightMNG.mapMNG.getWidth()) {
            hp = 10;
        } else {
            isDie = true;
            if (us != null) {
                // us.notifyNetWait();
            }
        }
    }

    public void netWait() {
        if (fightMNG.countDownMNG != null) {
            fightMNG.countDownMNG.second += 2;
        }
        if (us != null) {
            // us.netWait();
        }
    }

    public void notifyNetWait() {
        if (us != null) {
            // us.notifyNetWait();
        }
    }

    public boolean isCollision(short x, short y) {
        if (voHinhCount > 0 || isDie) return false;
        return Until.inRegion(x, y, this.x - width / 2, this.y - height, width, height);
    }

    public void collision(short bx, short by, Bullet bull) {
        if (fightMNG.ltap) return;

        // Will be fleshed out more deeply later
        // int tamAH = Bullet.getTamAhByBulletId(bull.bullId);
        // ...
    }

    public void nextMM() {
        isMM = false;
    }

    public void flyNotice(String text) {
        try {
            Message ms = new Message((byte) 119);
            ms.sendByte(0).sendByte(0).sendByte(4).sendUTF(text);
            fightMNG.sendToTeam(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
