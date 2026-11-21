package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;
import com.mobiarmy.data.Until;

public class Ghost extends Boss {
    public Ghost(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 35;
        this.height = 31;
        this.fly = true;
        this.XPExist = 5;
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            this.x = this.x > pl.x ? (short) (pl.x + 30) : (short) (pl.x - 30);
            this.y = (short) (pl.y - 15);
            fightMNG.flyChangeLocation(this.index);
            fightMNG.ghostBullet(this.index, pl.index);

            short wMap = fightMNG.mapMNG.width;
            short hMap = fightMNG.mapMNG.height;
            this.x = (short) Until.nextInt(100, wMap - 100);
            this.y = (short) Until.nextInt(0, hMap - 200);
            fightMNG.flyChangeLocation(this.index);

            pl.updateHP(-Until.nextInt(300, 600));
            if (!fightMNG.checkWin()) {
                fightMNG.nextTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
