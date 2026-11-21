package com.mobiarmy.fight;

import com.mobiarmy.data.Until;
import com.mobiarmy.network.Message;
import com.mobiarmy.server.ServerManager;
import com.mobiarmy.server.User;
import com.mobiarmy.server.FightWait;
import java.util.ArrayList;
import java.util.List;

public class SuperBossFactory {
    public static final String[] NamePrefixes = {
        "satthukhongten", "vuachientruong", "hacdevuong", "thanchet", "baochua",
        "mavuong", "doccocaubai", "huyenthoai", "amvuong", "tuthan",
        "chienthan", "quydoitruong", "sieunhanquay", "langtugiobui", "daicaxom",
    };

    private static final int[] BossItemPool = { 0, 1, 7, 11 };
    private static final int MaxNameLength = 30;

    public static String randomBossName() {
        String name = NamePrefixes[Until.nextInt(NamePrefixes.length)] + Until.nextInt(1000, 9999);
        return name.length() > MaxNameLength ? name.substring(0, MaxNameLength) : name;
    }

    private static int[] randomLoadout() {
        List<Integer> chosen = new ArrayList<>();
        for (int itemId : BossItemPool) {
            if (Until.nextInt(100) < 50) {
                chosen.add(itemId);
                if ((itemId == 0 || itemId == 1) && Until.nextInt(100) < 50) {
                    chosen.add(itemId);
                }
            }
        }

        for (int i = chosen.size() - 1; i > 0; i--) {
            int j = Until.nextInt(i + 1);
            int temp = chosen.get(i);
            chosen.set(i, chosen.get(j));
            chosen.set(j, temp);
        }

        int[] slots = new int[6];
        for (int i = 0; i < 6; i++) {
            slots[i] = i < chosen.size() ? chosen.get(i) : -1;
        }
        return slots;
    }

    private static final String[] ReadyChats = {
        "ok, chien thoi",
        "san sang roi day",
        "vao tran nao",
        "duoc roi, bat dau thoi",
        "ta da san sang",
        "cho gi nua, chien luon",
    };

    public static void seatInvitedBoss(FightWait target, User bot) {
        target.enterFireOval(bot);

        int slot = -1;
        for (int i = 0; i < target.players.length; i++) {
            if (target.players[i] != null && target.players[i].getIdDb() == bot.getIdDb()) {
                slot = i;
                break;
            }
        }
        if (slot == -1) return;

        target.item[slot] = randomLoadout();

        startIdleChatLoop(target, bot, slot);

        final int finalSlot = slot;
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (target.started) return;
                if (finalSlot >= target.players.length || target.players[finalSlot] != bot) return;

                if (!target.readys[finalSlot]) {
                    target.readys[finalSlot] = true;
                    target.numReady++;
                }
                target.sendToTeam(new Message(16).sendInt(bot.getIdDb()).sendBoolean(true));
                target.sendToTeam(new Message(9).sendInt(bot.getIdDb()).sendUTF(ReadyChats[Until.nextInt(ReadyChats.length)]));
            } catch (Exception e) {
                System.out.println("SuperBoss SeatInvitedBoss ready-delay threw: " + e);
            }
        }).start();
    }

    private static void startIdleChatLoop(FightWait target, User bot, int slot) {
        new Thread(() -> {
            try {
                while (true) {
                    int waitMs = Until.nextInt(5000, 10000);
                    Thread.sleep(waitMs);

                    if (target.started) return;
                    if (slot >= target.players.length || target.players[slot] != bot) return;

                    String text = SuperBoss.IdleTaunts[Until.nextInt(SuperBoss.IdleTaunts.length)];
                    target.sendToTeam(new Message(9).sendInt(bot.getIdDb()).sendUTF(text));
                }
            } catch (Exception e) {
                System.out.println("SuperBoss idle-chat loop threw: " + e);
            }
        }).start();
    }
}
