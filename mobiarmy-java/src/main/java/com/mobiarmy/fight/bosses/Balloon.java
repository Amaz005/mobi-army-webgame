package com.mobiarmy.fight.bosses;

import com.mobiarmy.data.Until;
import com.mobiarmy.fight.Boss;
import com.mobiarmy.fight.Bullet;
import com.mobiarmy.fight.FightManager;
import com.mobiarmy.fight.Player;

public class Balloon extends Boss {
    private final byte[] part;
    private int turns;

    public Balloon(FightManager fightMNG, byte idGun, String name, byte location, int hpMax, short x, short y) {
        super(fightMNG, idGun, name, location, hpMax, x, y);
        this.theLuc = 40;
        this.width = 138;
        this.height = 0;
        this.fly = true;
        this.part = new byte[] { this.index, (byte) (this.index + 1), (byte) (this.index + 2), (byte) (this.index + 3), -1 };
        this.turns = -1;
    }

    @Override
    public boolean isCollision(short x, short y) {
        return false;
    }

    @Override
    public void collision(short bx, short by, Bullet bull) {
    }

    @Override
    public void turnAction() {
        try {
            Player pl = fightMNG.getPlayerClosest(this.x, this.y);
            if (pl == null) return;

            boolean allSecondaryPartsDown = true;

            int turn = turns + 1;
            for (int attempts = 0; attempts < part.length; attempts++, turn++) {
                if (turn == part.length) turn = 0;

                Player candidate = part[turn] != -1 ? fightMNG.players[part[turn]] : null;
                if (candidate != null && !candidate.isDie && turn != 1 && turn != 3) {
                    if (turn != 2 || !fightMNG.players[part[3]].isDie) {
                        turns = turn;
                        break;
                    }
                }
            }

            for (int i = 0; i < 5; i++) {
                if (part[i] == -1) continue;
                Player boss = fightMNG.players[part[i]];
                if (boss != null && !boss.isDie && (i == 1 || i == 2 || i == 4)) {
                    allSecondaryPartsDown = false;
                }
            }

            if ((fightMNG.players[part[1]].isDie && fightMNG.players[part[2]].isDie || fightMNG.players[part[3]].isDie) && part[4] == -1) {
                part[4] = (byte) fightMNG.allCount;
                fightMNG.addBoss(new Balloon_Eye(fightMNG, (byte) 21, "Balloon Eye", (byte) fightMNG.allCount,
                        1500 + fightMNG.getLevelTeam() * 10, (short) (this.x + 55), (short) (this.y - 27)));
            } else if (allSecondaryPartsDown) {
                Player body = fightMNG.players[part[0]];
                body.isDie = true;
                body.hp = 0;
                body.isUpdateHP = true;

                Player fanBack = fightMNG.players[part[3]];
                fanBack.isDie = true;
                fanBack.hp = 0;
                fanBack.isUpdateHP = true;

                if (!fightMNG.checkWin()) {
                    fightMNG.nextTurn();
                    return;
                }
            }

            if (turns == 0) {
                if (!allSecondaryPartsDown) {
                    short toX = (short) Until.nextInt(100, fightMNG.mapMNG.width - 100);
                    short toY = (short) Until.nextInt(-150, 50);
                    for (int i = 0; i < 5; i++) {
                        if (part[i] == -1) continue;
                        Boss boss = (Boss) fightMNG.players[part[i]];
                        if (boss == null || boss.isDie) continue;

                        switch (i) {
                            case 0: boss.x = toX; boss.y = toY; break;
                            case 1: boss.x = (short) (toX + 51); boss.y = (short) (toY + 19); break;
                            case 2: boss.x = (short) (toX - 5); boss.y = (short) (toY + 30); break;
                            case 3: boss.x = (short) (toX - 67); boss.y = (short) (toY - 6); break;
                            case 4: boss.x = (short) (toX + 57); boss.y = (short) (toY - 27); break;
                        }
                    }
                    fightMNG.flyChangeLocation(this.index);
                }
                if (!fightMNG.players[part[1]].isDie) {
                    fightMNG.newShoot(this.index, (byte) 44, (short) Until.getArgXY(this.x, this.y, pl.x, pl.y), (byte) 10, (byte) 0, (byte) 1, false);
                    return;
                }
            } else if (turns == 2 && !fightMNG.players[part[2]].isDie) {
                fightMNG.newShoot(this.index, (byte) 43, (short) 270, (byte) 20, (byte) 0, (byte) 1, false);
                return;
            } else if (turns == 4 && !fightMNG.players[part[4]].isDie) {
                fightMNG.newShoot(this.index, (byte) 45, (short) Until.getArgXY(this.x, this.y, pl.x, pl.y), (byte) 20, (byte) 0, (byte) 1, false);
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
