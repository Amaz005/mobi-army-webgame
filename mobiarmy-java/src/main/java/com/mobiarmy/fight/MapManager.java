package com.mobiarmy.fight;

import com.mobiarmy.data.MapData;
import com.mobiarmy.data.Until;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private final FightManager fm;
    public short width;
    public short height;
    public int id;
    public List<MapEntry> entrys;
    public short[] xPlayerInit;
    public short[] yPlayerInit;

    public MapManager(FightManager fm) {
        this.fm = fm;
        this.id = 0;
        this.entrys = new ArrayList<>();
        this.width = 0;
        this.height = 0;
    }

    public MapManager(FightManager fm, int map) {
        this.fm = fm;
        this.entrys = new ArrayList<>();
        this.id = map;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setMapId(int id) {
        this.id = id;

        byte[] ab = null;
        for (MapData.MapDataEntry mEntry : MapData.entries) {
            if (mEntry.id == id) {
                ab = mEntry.data;
                break;
            }
        }
        if (ab == null) return;

        int off = 0;
        width = Until.getShort(ab, off);
        off += 2;
        height = Until.getShort(ab, off);
        off += 2;
        int len = ab[off++] & 0xFF;

        for (int i = 0; i < len; i++) {
            int brickId = ab[off] & 0xFF;

            if (!MapData.existsMapBrick(brickId)) {
                MapData.loadMapBrick(brickId);
            }

            MapEntry me;
            if (MapData.existsMapBrick(brickId)) {
                MapData.MapBrickEntry mb = MapData.getMapBrickEntry(brickId);
                me = new MapEntry(brickId, Until.getShort(ab, off + 1), Until.getShort(ab, off + 3),
                        mb.data, (short) mb.width, (short) mb.height, !MapData.isNotCollision(brickId));
            } else {
                me = new MapEntry(brickId, Until.getShort(ab, off + 1), Until.getShort(ab, off + 3),
                        null, (short) 0, (short) 0, !MapData.isNotCollision(brickId));
            }
            entrys.add(me);
            off += 5;
        }

        int nPlayerPoint = ab[off++] & 0xFF;
        xPlayerInit = new short[nPlayerPoint];
        yPlayerInit = new short[nPlayerPoint];
        for (int i = 0; i < nPlayerPoint; i++) {
            xPlayerInit[i] = Until.getShort(ab, off);
            off += 2;
            yPlayerInit[i] = Until.getShort(ab, off);
            off += 2;
        }
    }

    public void addEntry(MapEntry me) {
        entrys.add(me);
    }

    public boolean isCollision(short x, short y) {
        for (MapEntry m : entrys) {
            if (m.isCollision(x, y)) return true;
        }
        return false;
    }

    public void collision(short x, short y, Bullet bull) {
        for (MapEntry m : entrys) {
            m.collision(x, y, bull);
        }
        // Player / BulletManager integration goes here
    }
}
