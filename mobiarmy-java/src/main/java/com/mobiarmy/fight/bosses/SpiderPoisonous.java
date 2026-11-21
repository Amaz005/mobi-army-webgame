package com.mobiarmy.fight.bosses;

import com.mobiarmy.data.Until;
import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.server.ServerManager;
import java.util.ArrayList;
import java.util.List;

public class SpiderPoisonous extends Boss {
    private byte nturn;
    public Player target;

    public SpiderPoisonous(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.fly = true;
        this.width = 45;
        this.height = 48;
        this.XPExist = 8;
        this.nturn = 0;
    }

    @Override
    public void turnAction() {
        try {
            short xPre = this.x;
            List<Player> farPlayers = new ArrayList<>();
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                Player candidate = fightMNG.players[i];
                if (candidate != null && !candidate.isDie && candidate.y - 150 > this.y) {
                    farPlayers.add(candidate);
                }
            }

            if (farPlayers.size() > 0 && nturn == 0) {
                // Pull a distant player up with a web.
                nturn = 3;
                Player pl = farPlayers.get(Until.nextInt(0, farPlayers.size() - 1));
                this.x = pl.x;
                fightMNG.flyChangeLocation(this.index);
                fightMNG.capture(this.index, pl.index);

                fightMNG.isNextTurn = false;
                target = pl;
                fightMNG.newShoot(this.index, (byte) 8, (short) 270, (byte) 10, (byte) 0, (byte) 1, false);
                fightMNG.isNextTurn = true;

                this.x = xPre;
                fightMNG.flyChangeLocation(this.index);
                if (!fightMNG.checkWin()) fightMNG.nextTurn();
            } else {
                if (nturn > 0) nturn--;

                Player pl = fightMNG.getPlayerClosest(this.x, this.y);
                if (!pl.isBiDoc) {
                    this.x = pl.x;
                    fightMNG.flyChangeLocation(this.index);
                    fightMNG.thadocBullet(this.index, pl.index);
                    fightMNG.updateBiDoc(pl);
                    this.x = xPre;
                    fightMNG.flyChangeLocation(this.index);
                    if (!fightMNG.checkWin()) fightMNG.nextTurn();
                } else {
                    this.x = (short) Until.nextInt(50, fightMNG.mapMNG.width - 50);
                    fightMNG.flyChangeLocation(this.index);
                    fightMNG.newShoot(this.index, (byte) 47, (short) Until.getArgXY(this.x, this.y, pl.x, pl.y), (byte) 10, (byte) 0, (byte) 1, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
