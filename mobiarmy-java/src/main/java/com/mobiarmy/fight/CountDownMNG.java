package com.mobiarmy.fight;

import com.mobiarmy.server.ServerManager;

public class CountDownMNG {
    private final FightManager fightMNG;
    public byte second;
    private byte countTime;
    private Thread countThread;

    public CountDownMNG(FightManager fightMNG, byte countTime) {
        this.fightMNG = fightMNG;
        this.second = 0;
        this.countTime = countTime;
    }

    public void setCountTime(byte count) {
        this.countTime = count;
    }

    public void resetCount() {
        stopCount();
        second = countTime;
        System.out.println("SecondCount= " + second);

        countThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                    second--;
                    if (second == -10) {
                        fightMNG.nextTurn();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                // Thread stopped
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        countThread.start();
    }

    public void stopCount() {
        if (countThread != null) {
            countThread.interrupt();
            countThread = null;
        }
    }
}
