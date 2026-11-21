package com.mobiarmy.fight.bosses;

import com.mobiarmy.data.Until;
import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;

public class Ghost2 extends Boss {
    public Ghost2(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
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

            this.x = this.x > pl.x ? (short)(pl.x + 30) : (short)(pl.x - 30);
            this.y = (short)(pl.y - 15);
            fightMNG.flyChangeLocation(this.index);
            fightMNG.ghostBullet(this.index, pl.index);

            short wMap = fightMNG.mapMNG.width;
            short hMap = fightMNG.mapMNG.height;
            this.x = (this.x > pl.x && pl.x < wMap - 80) ? (short)(pl.x + 80)
                : pl.x > 80 ? (short)(pl.x - 80)
                : (short)(pl.x + 80);
            this.y = (short)Until.nextInt(0, hMap - 200);
            fightMNG.flyChangeLocation(this.index);

            pl.updateHP(-Until.nextInt(200, 500));
            if (!fightMNG.checkWin()) {
                fightMNG.nextTurn();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
