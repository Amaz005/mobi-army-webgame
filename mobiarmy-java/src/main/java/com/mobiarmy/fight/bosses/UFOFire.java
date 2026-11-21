package com.mobiarmy.fight.bosses;

import com.mobiarmy.data.Until;
import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.server.ServerManager;

public class UFOFire extends Boss {
    private boolean acll;
    private final Player targetPlayer;
    private byte turnDie;

    public UFOFire(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y, Player pl, byte tdie) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 51;
        this.height = 46;
        this.fly = true;
        this.targetPlayer = pl;
        this.turnDie = tdie;
        this.XPExist = 0;
        this.team = pl.index % 2 == 0;
    }

    @Override
    public void turnAction() {
        try {
            if (turnDie == 0) {
                this.hp = 0;
                this.isUpdateHP = true;
                this.isDie = true;
                if (!fightMNG.checkWin()) fightMNG.nextTurn();
                return;
            }

            short ys = this.y, xs = this.x;
            while (acll && ys < fightMNG.mapMNG.height + 200 && !fightMNG.mapMNG.isCollision(xs, ys)) {
                if (ys > fightMNG.mapMNG.height) acll = false;
                ys++;
            }

            if (!acll) {
                acll = true;
                Player lastAlive = null;
                int i = this.team ? 1 : 0;
                for (; i < ServerManager.maxPlayers; i += 2) {
                    Player candidate = fightMNG.players[i];
                    if (candidate == null || candidate.isDie) continue;
                    lastAlive = candidate;
                }
                if (lastAlive != null) {
                    this.y = (short)(lastAlive.y - Until.nextInt(150, 500));
                    this.x = lastAlive.x;
                    fightMNG.flyChangeLocation(this.index);
                }
            } else {
                acll = false;
                turnDie--;
                fightMNG.newShoot(this.index, (byte) 42, (short) 270, (byte) 20, (byte) 0, (byte) 1, false);
                return;
            }

            if (!fightMNG.checkWin()) {
                fightMNG.nextTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
