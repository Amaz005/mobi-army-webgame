package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.data.Until;

public class Trex extends Boss {
    public Trex(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 45;
        this.height = 50;
        this.fly = false;
        this.XPExist = 4;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            if (Math.abs(this.x - pl.x) <= 90 && Math.abs(this.y - pl.y) <= 250) {
                fightMNG.newShoot(this.index, (byte) 35, (short) 0, (byte) 0, (byte) 0, (byte) 1, false);
                return;
            }

            short[] fa;
            switch (Until.nextInt(3)) {
                case 0:
                    fightMNG.newShoot(this.index, (byte) 37, (short) 110, (byte) 30, (byte) 0, (byte) 1, false);
                    break;

                case 1:
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, false, this.x, this.y, pl.x, pl.y, (short) 70, (short) 70, (short) 110, (byte) 5, (byte) 10, (short) 80);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 40, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 2:
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, false, this.x, this.y, pl.x, pl.y, (short) 70, (short) 70, (short) 110, (byte) 5, (byte) 10, (short) 80);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 41, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endTurnIfNotWon() {
        if (!fightMNG.checkWin()) { // checkWin returns int in Java
            fightMNG.nextTurn();
        }
    }
}
