package com.mobiarmy.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Player {

    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    public final int id;
    public String username;
    public String password;
    public String displayName;
    public int xu;
    public int exp;
    public int selectedChar;
    public int level;
    public int blood;
    public int attack;
    public int defense;
    public int extraAttack;
    public int kill;
    public int death;
    public int power;
    public int mapId;
    public int posX;
    public int posY;
    public int hp;
    public int maxHp;
    public boolean ready;
    public byte currentCharId;
    public int[] charLevels = new int[9];
    public int[] itemCounts = new int[9];
    public long lastPing;
    public String clientVersion;
    public PlayerSession session;

    public Player(String username, String password) {
        this.id = idGenerator.getAndIncrement();
        this.username = username;
        this.password = password;
        this.displayName = username;
        this.xu = 20000;
        this.exp = 250000;
        this.selectedChar = 0;
        this.level = 5;
        this.blood = 320;
        this.attack = 200;
        this.defense = 400;
        this.extraAttack = 50;
        this.kill = 30;
        this.death = 10;
        this.power = 1500;
        this.mapId = 0;
        this.posX = 0;
        this.posY = 0;
        this.hp = 320;
        this.maxHp = 320;
        this.ready = false;
        this.currentCharId = 0;
        for (int i = 0; i < charLevels.length; i++) {
            charLevels[i] = 1000 + i;
        }
        for (int i = 0; i < itemCounts.length; i++) {
            itemCounts[i] = 20;
        }
        this.lastPing = System.currentTimeMillis();
    }

    public void updateStats() {
        this.level = calculateLevel(exp);
        this.power = level * 300;
        this.hp = blood;
        this.maxHp = blood;
    }

    public static int calculateLevel(int exp) {
        if (exp < 100) return 1;
        if (exp < 500) return 2;
        if (exp < 1500) return 3;
        if (exp < 4000) return 4;
        if (exp < 10000) return 5;
        if (exp < 25000) return 6;
        if (exp < 60000) return 7;
        if (exp < 150000) return 8;
        if (exp < 350000) return 9;
        return 10;
    }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense / 10);
        this.hp -= actualDamage;
        if (this.hp < 0) this.hp = 0;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void respawn() {
        this.hp = this.maxHp;
        this.posX = 100;
        this.posY = 200;
    }

    public void addExp(int amount) {
        this.exp += amount;
        updateStats();
    }

    public void addXu(int amount) {
        this.xu += amount;
    }

    public boolean spendXu(int amount) {
        if (this.xu >= amount) {
            this.xu -= amount;
            return true;
        }
        return false;
    }
}
