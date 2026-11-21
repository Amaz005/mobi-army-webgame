package com.mobiarmy.manager;

import com.mobiarmy.model.GameMap;
import com.mobiarmy.model.Item;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private static DataManager instance;
    private final Map<Integer, GameMap> maps;
    private final Map<Integer, Item> items;
    private final Map<Integer, byte[]> dataPacks;

    private DataManager() {
        this.maps = new ConcurrentHashMap<>();
        this.items = new ConcurrentHashMap<>();
        this.dataPacks = new ConcurrentHashMap<>();
        initializeMaps();
        initializeItems();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private void initializeMaps() {
        GameMap map1 = new GameMap(1, "Tap Luan", 64, 64);
        maps.put(1, map1);

        GameMap map2 = new GameMap(2, "Co Ban", 64, 64);
        map2.tileData[10] = 1;
        map2.tileData[20] = 1;
        maps.put(2, map2);

        for (int i = 3; i <= 10; i++) {
            GameMap map = new GameMap(i, "Map " + i, 64, 64);
            maps.put(i, map);
        }
    }

    private void initializeItems() {
        items.put(0, new Item(0, "Sung Thuong", 100, Item.CATEGORY_WEAPON, (byte) 0, Item.EFFECT_DAMAGE, 10));
        items.put(1, new Item(1, "Sung Luc", 300, Item.CATEGORY_WEAPON, (byte) 1, Item.EFFECT_DAMAGE, 20));
        items.put(2, new Item(2, "Axit", 50, Item.CATEGORY_WEAPON, (byte) 2, Item.EFFECT_DAMAGE, 15));
        items.put(3, new Item(3, "Binh Mang", 200, Item.CATEGORY_ARMOR, (byte) 3, Item.EFFECT_SHIELD, 30));
        items.put(4, new Item(4, "Mau", 80, Item.CATEGORY_POTION, (byte) 4, Item.EFFECT_HEAL, 50));
        items.put(5, new Item(5, "Thuoc Tang Toc", 150, Item.CATEGORY_SPECIAL, (byte) 5, Item.EFFECT_SPEED, 20));
        items.put(6, new Item(6, "Bom", 200, Item.CATEGORY_WEAPON, (byte) 6, Item.EFFECT_DAMAGE, 50));
        items.put(7, new Item(7, "Giap Sat", 400, Item.CATEGORY_ARMOR, (byte) 7, Item.EFFECT_SHIELD, 60));
        items.put(8, new Item(8, "Thuoc HoI Mau", 120, Item.CATEGORY_POTION, (byte) 8, Item.EFFECT_HEAL, 100));
    }

    public GameMap getMap(int id) {
        return maps.get(id);
    }

    public Item getItem(int id) {
        return items.get(id);
    }

    public Map<Integer, GameMap> getAllMaps() {
        return maps;
    }

    public Map<Integer, Item> getAllItems() {
        return items;
    }

    public void registerDataPack(int type, byte[] data) {
        dataPacks.put(type, data);
    }

    public byte[] getDataPack(int type) {
        return dataPacks.get(type);
    }
}
