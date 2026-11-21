package com.mobiarmy.fight;

import com.mobiarmy.data.NVData;
import com.mobiarmy.data.Until;
import com.mobiarmy.network.Message;
import com.mobiarmy.server.ServerManager;
import com.mobiarmy.server.User;
import com.mobiarmy.server.FightWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SuperBoss extends Player {

    public enum BossDifficulty {
        Easy, Medium, Hard, SuperHard
    }

    public final BossDifficulty difficulty;
    private static final Random chatRng = new Random();

    public SuperBoss(FightManager fightMNG, byte location, short x, short y, int[] item, int teamPoint, User us, BossDifficulty difficulty) {
        super(fightMNG, location, x, y, item, teamPoint, us);
        this.difficulty = difficulty;
    }

    public void takeTurn() {
        if (this.isDie || fightMNG.checkWin()) return;

        maybeSwitchTeamForBalance();

        if (isStandingOnNarrowGround(this.x, this.y) && tryUseFlyItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    if (!this.isDie && !fightMNG.checkWin() && fightMNG.getIdTurn() == this.index) {
                        delayedShoot();
                    }
                } catch (Exception e) {
                    if (!fightMNG.checkWin()) fightMNG.nextTurn();
                }
            }).start();
            return;
        }

        if (isInDanger()) {
            boolean moved = tryMoveToSafety();
            int pauseMs = moved ? 320 + 300 : 300;
            new Thread(() -> {
                try {
                    Thread.sleep(pauseMs);
                    if (this.isDie || fightMNG.checkWin() || fightMNG.getIdTurn() != this.index) return;

                    boolean usedFly = !moved && tryUseFlyItem();
                    if (usedFly) {
                        Thread.sleep(300);
                        if (!this.isDie && !fightMNG.checkWin() && fightMNG.getIdTurn() == this.index) {
                            delayedShoot();
                        }
                    } else {
                        delayedShoot();
                    }
                } catch (Exception e) {
                    if (!fightMNG.checkWin()) fightMNG.nextTurn();
                }
            }).start();
            return;
        }
        delayedShoot();
    }

    private void delayedShoot() {
        int delayMs = Until.nextInt(2000, 5000);
        new Thread(() -> {
            try {
                Thread.sleep(delayMs);
                if (!this.isDie && !fightMNG.checkWin() && fightMNG.getIdTurn() == this.index) {
                    boolean usedHp = useHpIfLethalThreatIncoming();
                    if (usedHp) {
                        Thread.sleep(300);
                        if (!this.isDie && !fightMNG.checkWin() && fightMNG.getIdTurn() == this.index) {
                            performShoot();
                        }
                    } else {
                        performShoot();
                    }
                }
            } catch (Exception e) {
                if (!fightMNG.checkWin()) fightMNG.nextTurn();
            }
        }).start();
    }

    private boolean hasItem(int id) {
        if (this.item == null) return false;
        for (int i : this.item) {
            if (i == id) return true;
        }
        return false;
    }

    private boolean isInDanger() {
        boolean nearEdge = this.x < 40 || this.x > fightMNG.mapMNG.width - 40;
        int worstIncoming = estimateWorstIncomingDamage();
        boolean lowHp = this.hp <= worstIncoming;
        boolean vulnerableToDig = isStandingOnNarrowGround(this.x, this.y);
        return nearEdge || lowHp || vulnerableToDig;
    }

    private int estimateWorstIncomingDamage() {
        int worst = 0;
        for (Player enemy : enemies()) {
            int baseDmg = enemy.idNV < NVData.entries.size() ? NVData.entries.get(enemy.idNV).satThuong : 300;
            int estimate = baseDmg * 2 * enemy.satThuong / 100;
            if (estimate > worst) worst = estimate;
        }
        return worst;
    }

    private boolean tryMoveToSafety() {
        short mapW = fightMNG.mapMNG.width;
        short center = (short) (mapW / 2);
        int dir = this.x < center ? 1 : -1;

        short targetX = (short) Math.max(10, Math.min(mapW - 10, this.x + dir * this.theLuc));
        if (targetX == this.x) return false;

        short fromX = this.x;
        sendFakeChangeLocation(targetX, this.y);

        if (Math.abs(this.x - fromX) < 5) return false;

        broadcastWalkFrames(fromX, this.x, this.y);
        return true;
    }

    private void broadcastWalkFrames(short fromX, short toX, short y) {
        final int steps = 4;
        final FightManager fm = this.fightMNG;
        final byte idx = this.index;
        new Thread(() -> {
            try {
                for (int i = 1; i <= steps; i++) {
                    short midX = (short) (fromX + (toX - fromX) * i / steps);
                    fm.sendToTeam(new Message(21).sendByte(idx).sendShort(midX).sendShort(y));
                    Thread.sleep(80);
                }
            } catch (Exception e) {}
        }).start();
    }

    private boolean tryUseFlyItem() {
        final int flyItemId = 1; // Teleport
        final byte teleportBullId = 5;
        if (!hasItem(flyItemId)) return false;
        if (this.itemUsed != -1 || this.isUseItem) return false;

        short mapW = fightMNG.mapMNG.width;
        short center = (short) (mapW / 2);
        short targetX = (short) Math.max(20, Math.min(mapW - 20, Math.abs(this.x - center) > 20 ? center : this.x));
        short targetY = findGroundY(targetX);

        sendFakeUseItem((byte) flyItemId);

        short arg; byte force;
        short[] res = aimAtPoint(teleportBullId, targetX, targetY, 50, (short)20, (short)20);
        if (res == null) {
            arg = (short) (targetX >= this.x ? 45 : 135);
            force = 20;
        } else {
            arg = res[0];
            force = (byte) res[1];
        }
        sendFakeShoot(teleportBullId, arg, force, (byte)0);
        return true;
    }

    private short findGroundY(short x) {
        short mapH = fightMNG.mapMNG.height;
        for (short y = 0; y < mapH; y += 5) {
            if (fightMNG.mapMNG.isCollision(x, y)) return (short) Math.max(0, y - 5);
        }
        return (short) (mapH - 10);
    }

    private boolean useHpIfLethalThreatIncoming() {
        final int hpItemId = 0;
        if (!hasItem(hpItemId)) return false;
        if (this.itemUsed != -1 || this.isUseItem) return false;

        int worst = estimateWorstIncomingDamage();
        if (worst >= this.hp) {
            sendFakeUseItem((byte) hpItemId);
            return true;
        }
        return false;
    }
    
    private void performShoot() {
        Player target = pickTarget();
        if (target == null) {
            fightMNG.nextTurn();
            return;
        }

        short mx = (short) Math.max(4, target.width / 2);
        short my = (short) Math.max(4, target.height / 2);

        if (this.itemUsed == -1 && !this.isUseItem && Until.nextInt(100) < 50) {
            Map<Integer, Byte> attackItems = new HashMap<>();
            attackItems.put(7, (byte) 7);
            attackItems.put(11, (byte) 16);
            List<Integer> carried = new ArrayList<>();
            for (int k : attackItems.keySet()) {
                if (hasItem(k)) carried.add(k);
            }
            if (carried.size() > 0) {
                int pickItem = carried.get(Until.nextInt(carried.size()));
                sendFakeUseItem((byte) pickItem);
                byte itemBullId = attackItems.get(pickItem);
                short[] res = aimAt(itemBullId, target, 50, mx, my);
                if (res != null) {
                    sendFakeShoot(itemBullId, res[0], (byte)res[1], (byte)0);
                } else {
                    sendFakeShoot(itemBullId, (short) (target.x >= this.x ? 45 : 135), (byte) 18, (byte)0);
                }
                maybeCombatTaunt(target, true);
                return;
            }
        }

        byte bullId = (byte) this.idBullet;
        short arg; byte force; byte force2 = 0;
        
        if (bullId == 17 || bullId == 19) {
            short[] res = aimAt(bullId, target, 50, mx, my);
            if (res != null && Math.abs(this.x - target.x) >= 100) {
                arg = res[0];
                force = (byte) res[1];
                force2 = (byte) res[2]; // hit frame is index 2
            } else {
                res = aimAt((byte) (bullId == 17 ? 18 : 20), target, 50, mx, my);
                if (res != null) {
                    arg = res[0]; force = (byte) res[1];
                } else {
                    arg = (short) (target.x >= this.x ? 45 : 135); force = 20;
                }
            }
        } else {
            short[] res = aimAt(bullId, target, 50, mx, my);
            if (res != null) {
                arg = res[0]; force = (byte) res[1];
            } else {
                arg = (short) (target.x >= this.x ? 45 : 135); force = 20;
            }
        }

        if (this.difficulty == BossDifficulty.Easy) {
            arg += Until.nextInt(-5, 5);
            force += Until.nextInt(-3, 3);
        } else if (this.difficulty == BossDifficulty.Medium) {
            arg += Until.nextInt(-2, 2);
            force += Until.nextInt(-1, 1);
        }

        sendFakeShoot(bullId, arg, force, force2);
        maybeCombatTaunt(target, false);
    }

    private short[] aimAtPoint(byte bulletId, short toX, short toY, int startArg, short mx, short my) {
        int msg = 70; int g100 = 80;
        if (bulletId == 5) { msg = 70; g100 = 80; }
        short[] res = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, false, this.x, this.y, toX, toY, mx, my, (short)startArg, (byte)5, (short)msg, (short)g100);
        if (res == null) return null;
        return new short[] { res[0], res[1] };
    }

    private short[] aimAt(byte bulletId, Player target, int startArg, short mx, short my) {
        int msg = 70; int g100 = 80;
        for (int tryStartArg = startArg; tryStartArg <= 90; tryStartArg += 10) {
            short[] res = fightMNG.getForceArgXY(this.idNV, fightMNG.bullMNG, false, this.x, this.y, target.x, target.y, mx, my, (short)tryStartArg, (byte)5, (short)msg, (short)g100);
            if (res == null) continue;
            short candArg = res[0];
            byte candForce = (byte) res[1];
            int hitFrame = pathClearOfFriendlyOrDead(candArg, candForce, msg, g100, target);
            if (hitFrame != -1) {
                return new short[] { candArg, candForce, (short) hitFrame };
            }
        }
        return null;
    }

    private int pathClearOfFriendlyOrDead(short argDeg, byte force, int msg, int g100, Player intendedTarget) {
        int hitFrame = 15;
        short xs = (short) (this.x + ((20 * Until.cos(argDeg)) >> 10));
        short ys = (short) (this.y - 12 - ((20 * Until.sin(argDeg)) >> 10));
        short vx = (short) ((force * Until.cos(argDeg)) >> 10);
        short vy = (short) -((force * Until.sin(argDeg)) >> 10);
        short ax100 = (short) (fightMNG.getWindX() * msg / 100);
        short ay100 = (short) (fightMNG.getWindY() * msg / 100);
        short vxTemp = 0, vyTemp = 0, vyTemp2 = 0;
        short mapW = fightMNG.mapMNG.width, mapH = fightMNG.mapMNG.height;

        for (int step = 0; step < 400; step++) {
            if (xs < -200 || xs > mapW + 200 || ys > mapH + 200) return hitFrame;

            xs += vx; ys += vy;
            if (Math.abs(xs - intendedTarget.x) <= intendedTarget.width && Math.abs(ys - intendedTarget.y) <= intendedTarget.height) {
                return step;
            }
            if (fightMNG.mapMNG.isCollision(xs, ys)) return hitFrame;
            
            for (Player p : fightMNG.players) {
                if (p == null || p == this || p == intendedTarget) continue;
                if (p.team == this.team || p.isDie) {
                    if (Math.abs(xs - p.x) <= p.width && Math.abs(ys - p.y) <= p.height) {
                        return -1;
                    }
                }
            }
            vxTemp += (short) Math.abs(ax100); vyTemp += (short) Math.abs(ay100); vyTemp2 += (short) g100;
            if (Math.abs(vxTemp) >= 100) { vx += (short) (ax100 > 0 ? vxTemp / 100 : -(vxTemp / 100)); vxTemp %= 100; }
            if (Math.abs(vyTemp) >= 100) { vy += (short) (ay100 > 0 ? vyTemp / 100 : -(vyTemp / 100)); vyTemp %= 100; }
            if (Math.abs(vyTemp2) >= 100) { vy += (short) (vyTemp2 / 100); vyTemp2 %= 100; }
        }
        return hitFrame;
    }

    private boolean isStandingOnNarrowGround(short x, short y) {
        short checkY = (short) (y + 10);
        boolean leftOpen = !fightMNG.mapMNG.isCollision((short) (x - 20), checkY);
        boolean rightOpen = !fightMNG.mapMNG.isCollision((short) (x + 20), checkY);
        return leftOpen && rightOpen;
    }

    private Player pickTarget() {
        List<Player> eList = enemies();
        if (eList.isEmpty()) return null;
        Player killable = null;
        for (Player e : eList) {
            if (e.hp <= estimateOwnDamageAgainst(e) * 2) {
                if (killable == null || e.hp < killable.hp) killable = e;
            }
        }
        if (killable != null) return killable;
        
        Player maxHp = eList.get(0);
        for (Player e : eList) {
            if (e.hp > maxHp.hp) maxHp = e;
        }
        return maxHp;
    }

    private int estimateOwnDamageAgainst(Player target) {
        int baseDmg = this.idNV < NVData.entries.size() ? NVData.entries.get(this.idNV).satThuong : 300;
        return baseDmg * this.satThuong / 100;
    }

    private void maybeSwitchTeamForBalance() {
        if (fightMNG.nTurn > 1) return;
        int teamTrueCount = 0, teamFalseCount = 0;
        for (Player p : fightMNG.players) {
            if (p == null || p.isDie) continue;
            if (p.team) teamTrueCount++; else teamFalseCount++;
        }
        if (this.team && teamTrueCount - teamFalseCount >= 2) this.team = false;
        else if (!this.team && teamFalseCount - teamTrueCount >= 2) this.team = true;
    }

    private List<Player> enemies() {
        List<Player> list = new ArrayList<>();
        for (Player p : fightMNG.players) {
            if (p != null && p != this && !p.isDie && p.team != this.team) list.add(p);
        }
        return list;
    }

    private void sendFakeChangeLocation(short x, short y) {
        try {
            Message built = new Message(21).sendShort(x).sendShort(y);
            fightMNG.changeLocationMessage(this.us, new Message((byte) 21, built.getData()));
        } catch (Exception e) {}
    }

    private void sendFakeUseItem(byte idItem) {
        try {
            Message built = new Message(26).sendByte(idItem);
            fightMNG.useItemMessage(this.us, new Message((byte) 26, built.getData()));
        } catch (Exception e) {}
    }

    private void sendFakeShoot(byte bullId, short arg, byte force, byte force2) {
        try {
            Message built = new Message(22).sendByte(bullId).sendShort(this.x).sendShort(this.y).sendShort(arg).sendByte(force);
            if (bullId == 17 || bullId == 19) built.sendByte(force2);
            built.sendByte((byte) 1);
            fightMNG.shootMessage(this.us, new Message((byte) 22, built.getData()));
        } catch (Exception e) {}
    }
    
    public static final String[] IdleTaunts = {
        "ai vao dau voi ta khong, hay so roi", "buon ngu qua, co ai choi khong dam phat cho tinh"
    };

    private void maybeCombatTaunt(Player target, boolean aboutToShoot) {
        if (Until.nextInt(100) >= 40) return;
        String text = "chuẩn không cần chỉnh";
        if (aboutToShoot && target.hp < target.hpMax / 4) text = "yếu vậy mà cũng dám vào đấu à";
        try {
            fightMNG.sendToTeam(new Message(9).sendInt(this.us.getIdDb()).sendUTF(text));
        } catch (Exception e) {}
    }
    
    public static void sendLeaveTaunt(FightManager fight, FightWait wait, User bot) {
        try {
            int idx = -1;
            for (int i = 0; i < fight.players.length; i++) {
                if (fight.players[i] != null && fight.players[i].us != null && fight.players[i].us.getIdDb() == bot.getIdDb()) {
                    idx = i;
                    break;
                }
            }
            wait.sendToTeam(new Message(9).sendInt(bot.getIdDb()).sendUTF("hen gap lai cac ban o tran sau nhe"));
        } catch (Exception e) {}
    }
}
