package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.data.Until;

public class UFO extends Boss {
    private boolean turnShoot;

    public UFO(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 51;
        this.height = 46;
        this.fly = true;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        try {
            short ys = this.y, xs = this.x;
            while (turnShoot && ys < fightMNG.mapMNG.height + 200 && !fightMNG.mapMNG.isCollision(xs, ys)) {
                if (ys > fightMNG.mapMNG.height) turnShoot = false;
                ys++;
            }

            if (!turnShoot) {
                turnShoot = true;
                Player pl = fightMNG.getPlayerClosest(this.x, this.y);
                if (pl != null) {
                    this.y = (short) (pl.y - Until.nextInt(150, 500));
                    this.x = pl.x;
                    fightMNG.flyChangeLocation(this.index);
                    if (!fightMNG.checkWin()) fightMNG.nextTurn();
                } else if (!fightMNG.checkWin()) {
                    fightMNG.nextTurn();
                }
            } else {
                turnShoot = false;
                fightMNG.newShoot(this.index, (byte) 42, (short) 270, (byte) 20, (byte) 0, (byte) 1, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
