package com.mobiarmy.fight;

import com.mobiarmy.server.ServerManager;
import java.util.ArrayList;
import java.util.List;

public abstract class Boss extends Player {
    public String name;
    public List<Player> list = new ArrayList<>();

    protected Boss(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, location, x, y, null, 0, null);
        this.idNV = idGun;
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.isUpdateHP = true;
        this.name = name;
        this.satThuong = 100;
        this.mayMan = 0;

        for (int i = 0; i < ServerManager.maxPlayers; i++) {
            Player pl = fightMNG.players[i];
            if (pl != null) {
                list.add(pl);
            }
        }
    }

    public abstract void turnAction();
}
