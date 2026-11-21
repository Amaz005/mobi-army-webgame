package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.data.Until;

public class BigBoom extends Boss {
    public BigBoom(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 100;
        this.width = 28;
        this.height = 28;
        this.XPExist = 4;
    }

    public void bomAction() {
        this.isDie = true;
        this.height = 0;
        this.width = 0;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            if (this.index != fightMNG.getIdTurn()) {
                short vGoc = pl.x > this.x
                        ? (short) Until.nextInt(60, 80)
                        : (short) Until.nextInt(180 - 80, 180 - 60);

                short vSuc = (short) (Math.abs(this.x - pl.x) / 20);
                vSuc = (short) (vSuc > 30 ? 30 : vSuc < 15 ? 15 : vSuc);

                fightMNG.newShoot(this.index, (byte) 4, vGoc, (byte) vSuc, (byte) 0, (byte) 1, false);
                return;
            } else {
                while (this.x != pl.x || this.y != pl.y) {
                    int preX = this.x;
                    int preY = this.y;
                    if (pl.x < this.x) move(false);
                    else if (pl.x > this.x) move(true);
                    else if (!fightMNG.mapMNG.isCollision(this.x, this.y)) this.y++;

                    if (preX == this.x && preY == this.y) break;
                }
                fightMNG.changeLocation(this.index);

                if (Math.abs(this.x - pl.x) <= 35 && Math.abs(this.y - pl.y) <= 35) {
                    fightMNG.isNextTurn = false;
                    fightMNG.newShoot(this.index, (byte) 35, (short) 0, (byte) 0, (byte) 0, (byte) 1, false);
                    fightMNG.isNextTurn = true;
                    fightMNG.newShoot(this.index, (byte) 7, (short) Until.getArgXY(this.x, this.y, pl.x, pl.y), (byte) 30, (byte) 0, (byte) 1, false);
                } else if (!fightMNG.checkWin()) {
                    fightMNG.nextTurn();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
