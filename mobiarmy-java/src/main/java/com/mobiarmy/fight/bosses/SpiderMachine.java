package com.mobiarmy.fight.bosses;

import com.mobiarmy.data.Until;
import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.server.ServerManager;
import java.util.ArrayList;
import java.util.List;

public class SpiderMachine extends Boss {
    public SpiderMachine(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 42;
        this.height = 42;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            if (Math.abs(this.x - pl.x) <= 40 && Math.abs(this.y - pl.y) <= 30) {
                webThenJetpack((short) Until.getArgXY(this.x, this.y, pl.x, pl.y));
                return;
            }

            List<Player> living = new ArrayList<>();
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                if (fightMNG.players[i] != null && !fightMNG.players[i].isDie) {
                    living.add(fightMNG.players[i]);
                }
            }
            if (living.size() > 0) pl = living.get(Until.nextInt(living.size()));
            if (pl == null) return;

            short[] fa;
            switch (Until.nextInt(3)) {
                case 0: // to nhen (spider silk)
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, false, this.x, this.y, pl.x, (short) (pl.y - pl.height / 2), (short) (pl.width / 2), pl.height, (short) 50, (byte) 5, (short) 70, (short) 70);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    webThenJetpack(fa[0]);
                    break;

                case 1: // lazer
                    this.itemUsed = 16;
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, false, this.x, this.y, pl.x, (short) (pl.y - pl.height / 2), (short) (pl.width / 2), pl.height, (short) 50, (byte) 5, (short) 10, (short) 50);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 14, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;

                case 2: // rocket
                    fa = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, true, this.x, this.y, pl.x, (short) (pl.y - pl.height / 2), (short) (pl.width / 2), pl.height, (short) 50, (byte) 5, (short) 30, (short) 50);
                    if (fa == null) { endTurnIfNotWon(); return; }
                    fightMNG.newShoot(this.index, (byte) 33, fa[0], (byte) fa[1], (byte) 0, (byte) 1, false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void webThenJetpack(short webArg) {
        this.itemUsed = 9;
        fightMNG.isNextTurn = false;
        fightMNG.newShoot(this.index, (byte) 8, webArg, (byte) 30, (byte) 0, (byte) 1, false);
        fightMNG.isNextTurn = true;

        byte force = (byte) Until.nextInt(15, 30);
        short arg = (short) Until.nextInt(80, 100);
        fightMNG.newShoot(this.index, (byte) 36, arg, force, (byte) 0, (byte) 1, false);
    }

    private void endTurnIfNotWon() {
        if (!fightMNG.checkWin()) fightMNG.nextTurn();
    }
}
