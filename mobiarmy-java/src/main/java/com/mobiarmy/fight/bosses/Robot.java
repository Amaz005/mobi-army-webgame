package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.data.Until;
import com.mobiarmy.server.ServerManager;
import java.util.ArrayList;
import java.util.List;

public class Robot extends Boss {
    public Robot(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 24;
        this.height = 25;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            if (Math.abs(this.x - pl.x) <= 40 && Math.abs(this.y - pl.y) <= 40) {
                fightMNG.isNextTurn = false;
                fightMNG.newShoot(this.index, (byte) 35, (short) 0, (byte) 0, (byte) 0, (byte) 1, false);
                fightMNG.isNextTurn = true;
            }
            if (Math.abs(this.x - pl.x) <= 40) {
                byte force = (byte) Until.nextInt(15, 30);
                short arg = (short) Until.nextInt(80, 100);
                fightMNG.newShoot(this.index, (byte) 36, arg, force, (byte) 0, (byte) 1, false);
                return;
            }

            List<Player> living = new ArrayList<>();
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                if (fightMNG.players[i] != null && !fightMNG.players[i].isDie) {
                    living.add(fightMNG.players[i]);
                }
            }
            if (living.size() > 0) {
                pl = living.get(Until.nextInt(living.size()));
            }
            if (pl == null) return;

            short[] fa;
            switch (Until.nextInt(9)) {
                case 0:
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, pl.y, (short) (pl.width / 2), (short) (pl.height / 2), (short) 50, (byte) 5, (short) 80, (short) 100);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 0, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 1:
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, pl.y, (short) (pl.width / 2), (short) (pl.height / 2), (short) 50, (byte) 5, (short) 80, (short) 60);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 2, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 2:
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, pl.y, (short) (pl.width / 2), (short) (pl.height / 2), (short) 50, (byte) 5, (short) 50, (short) 80);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 10, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 3:
                    this.itemUsed = 6;
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, pl.y, (short) (pl.width / 2), (short) (pl.height / 2), (short) 50, (byte) 5, (short) 70, (short) 90);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 6, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 4:
                    this.itemUsed = 7;
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, pl.y, (short) (pl.width / 2), (short) (pl.height / 2), (short) 50, (byte) 5, (short) 70, (short) 80);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 7, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 5:
                case 6:
                case 7:
                case 8:
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, pl.y, (short) pl.width, (short) pl.height, (short) 50, (byte) 5, (short) 0, (short) 80);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 36, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endTurnIfNotWon() {
        if (!fightMNG.checkWin()) {
            fightMNG.nextTurn();
        }
    }
}
