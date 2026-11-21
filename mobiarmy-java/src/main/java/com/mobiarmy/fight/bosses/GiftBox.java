package com.mobiarmy.fight.bosses;

import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.FightManager;

public class GiftBox extends Boss {
    public byte[] itemGift;

    public GiftBox(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y, byte[] itemGift) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 100;
        this.width = 24;
        this.height = 24;
        this.XPExist = 0;
        this.itemGift = itemGift;
        this.fly = (idNV == 24);
    }

    @Override
    public void turnAction() {
        // Passive: no turn behavior
    }
}
