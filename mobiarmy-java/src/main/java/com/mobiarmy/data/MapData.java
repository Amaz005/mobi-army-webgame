package com.mobiarmy.data;

import java.util.ArrayList;
import java.util.List;

public class MapData {

    public static class MapDataEntry {
        public byte id;
        public String name;
        public String file;
        public byte[] data;
        public short bg;
        public short mapAddY;
        public short cl2AddY;
        public short inWaterAddY;
        public short bullEffShower;
        public short[] xPlayer;
        public short[] yPlayer;
        public boolean isBoss;
    }

    public static class MapBrickEntry {
        public final int id;
        public final int[] data;
        public final int width;
        public final int height;

        public MapBrickEntry(int id, int[] data, int width, int height) {
            this.id = id;
            this.data = data;
            this.width = width;
            this.height = height;
        }

        public int getId() { return id; }
        public int[] getData() { return data; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }

    public static List<MapDataEntry> entries = new ArrayList<>();
    public static List<MapBrickEntry> brickEntries = new ArrayList<>();

    public static MapDataEntry getById(int mapId) {
        for (MapDataEntry e : entries) {
            if (e.id == (byte) mapId) {
                return e;
            }
        }
        return null;
    }

    public static int getIdByFile(String file) {
        for (MapDataEntry e : entries) {
            if (e.file != null && e.file.equals(file)) {
                return e.id;
            }
        }
        return -1;
    }

    public static byte getClientIndex(byte mapId) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).id == mapId) {
                return (byte) i;
            }
        }
        return mapId;
    }

    public static final short[] ID_NOT_COLLISION = { 70, 71, 73, 74, 75, 77, 78, 79, 97 };

    public static boolean isNotCollision(int id) {
        for (short v : ID_NOT_COLLISION) {
            if (v == (short) id) return true;
        }
        return false;
    }

    public static MapBrickEntry getMapBrickEntry(int id) {
        for (MapBrickEntry b : brickEntries) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public static boolean existsMapBrick(int id) {
        for (MapBrickEntry b : brickEntries) {
            if (b.getId() == id) return true;
        }
        return false;
    }

    public static void loadMapBrick(int id) {
        System.out.println("Load Map Brick id=" + id);
        com.mobiarmy.fight.ImageUtil.ImageResult result = com.mobiarmy.fight.ImageUtil.loadArgb("res/icon/map/" + id + ".png");
        if (result == null || result.data == null) return;
        brickEntries.add(new MapBrickEntry(id, result.data, (short) result.w, (short) result.h));
    }
}
