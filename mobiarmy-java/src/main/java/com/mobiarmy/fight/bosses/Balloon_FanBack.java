package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;

public class Balloon_FanBack extends Boss {
    public Balloon_FanBack(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 10;
        this.height = 19;
        this.fly = true;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        // Passive: no turn behavior.
    }
}
