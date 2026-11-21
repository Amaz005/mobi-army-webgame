package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.data.Until;
import com.mobiarmy.network.Message;

public class SmallBoom extends Boss {
    public SmallBoom(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 100;
        this.width = 18;
        this.height = 18;
        this.XPExist = 2;
    }

    public void bomAction() {
        this.isDie = true;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            while (this.x != pl.x || this.y != pl.y) {
                int preX = this.x;
                int preY = this.y;
                if (pl.x < this.x) move(false);
                else if (pl.x > this.x) move(true);
                else if (!fightMNG.mapMNG.isCollision(this.x, this.y)) this.y++;

                if (preX == this.x && preY == this.y) break;
            }
            fightMNG.changeLocation(this.index);

            if (Math.abs(this.y - pl.y) <= 25 && Math.abs(this.x - pl.x) <= 25) {
                fightMNG.newShoot(this.index, (byte) 7, (short) Until.getArgXY(this.x, this.y, pl.x, pl.y), (byte) 30, (byte) 0, (byte) 1, false);
            } else if (Math.abs(this.x - pl.x) < 25 && this.y - pl.y > 25) {
                fightMNG.changeLocationMessage(this.us, new Message(21).sendByte(this.index).sendShort(this.x).sendShort(this.y));
                fightMNG.newShoot(this.index, (byte) 5, (short) Until.getArgXY(this.x, this.y, pl.x, pl.y), (byte) 30, (byte) 0, (byte) 1, false);
            } else if (pl.y - this.y > 25 && Math.abs(this.x - pl.x) < 25) {
                this.itemUsed = 1;
                short vRd = (short) Until.nextInt(60, 120);
                byte suc = (byte) Until.nextInt(5, 10);
                fightMNG.newShoot(this.index, (byte) 5, vRd, suc, (byte) 0, (byte) 1, false);
            } else if (this.buocDi < this.theLuc) {
                this.itemUsed = 1;
                short vxy = (short) (pl.x > this.y ? 80 : 180 - 80);
                fightMNG.newShoot(this.index, (byte) 5, vxy, (byte) 8, (byte) 0, (byte) 1, false);
            } else if (!fightMNG.checkWin()) {
                fightMNG.nextTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
