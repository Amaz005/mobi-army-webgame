package com.mobiarmy.model;

import java.util.Arrays;

public class GameMap {

    public final int id;
    public final String name;
    public final int width;
    public final int height;
    public final byte[] tileData;
    public final short[] spawnX;
    public final short[] spawnY;
    public final byte[] teamIndices;

    public GameMap(int id, String name, int width, int height) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.tileData = new byte[width * height];
        this.spawnX = new short[16];
        this.spawnY = new short[16];
        this.teamIndices = new byte[16];
        Arrays.fill(tileData, (byte) 0);
        Arrays.fill(spawnX, (short) 100);
        Arrays.fill(spawnY, (short) 200);
    }

    public boolean isWalkable(int tileX, int tileY) {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return false;
        }
        return tileData[tileY * width + tileX] != 1;
    }
}
