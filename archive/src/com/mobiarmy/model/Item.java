package com.mobiarmy.model;

public class Item {

    public final int id;
    public String name;
    public int price;
    public int category;
    public byte iconIndex;
    public int effectType;
    public int effectValue;
    public int quantity;

    public Item(int id, String name, int price, int category, byte iconIndex,
                int effectType, int effectValue) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.iconIndex = iconIndex;
        this.effectType = effectType;
        this.effectValue = effectValue;
        this.quantity = 0;
    }

    public static final int CATEGORY_WEAPON = 0;
    public static final int CATEGORY_ARMOR = 1;
    public static final int CATEGORY_POTION = 2;
    public static final int CATEGORY_SPECIAL = 3;

    public static final int EFFECT_DAMAGE = 0;
    public static final int EFFECT_HEAL = 1;
    public static final int EFFECT_SHIELD = 2;
    public static final int EFFECT_SPEED = 3;
}
