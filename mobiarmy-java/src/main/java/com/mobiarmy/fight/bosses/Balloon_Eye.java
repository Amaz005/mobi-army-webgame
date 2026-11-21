package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;

public class Balloon_Eye extends Boss {
    public Balloon_Eye(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 0;
        this.width = 13;
        this.height = 14;
        this.fly = true;
        this.XPExist = 8;
    }

    @Override
    public void turnAction() {
        // Passive: no turn behavior.
    }
}
