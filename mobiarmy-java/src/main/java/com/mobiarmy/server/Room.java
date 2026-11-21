package com.mobiarmy.server;

import com.mobiarmy.data.MapData;
import com.mobiarmy.data.Until;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
    public byte id;
    public byte type;
    public String name;
    public int maxXu;
    public int minXu;
    public int minMap;
    public int maxMap;
    public final FightWait[] entrys;
    public int nType;

    public List<Byte> slMap = new ArrayList<>();

    public Room(int id, int type, int maxEntrys, int nType) {
        this.id = (byte) id;
        this.type = (byte) type;
        this.nType = nType;
        this.slMap = new ArrayList<>();

        byte maxPlayerInit = 0, map = 0;
        boolean isLH = false;

        byte[] continuityType = { 5, 5 };
        byte[] continuityNumbers = { 8, 9 };
        for (int i = 0; i < continuityNumbers.length; i++) {
            if (type == continuityType[i] && nType == continuityNumbers[i]) {
                isLH = true;
                break;
            }
        }

        int normalMapMin = 0, normalMapMax = 0;
        boolean any = false;
        for (MapData.MapDataEntry m : MapData.entries) {
            if (m.isBoss) continue;
            if (!any) { 
                normalMapMin = normalMapMax = m.id; 
                any = true; 
                continue; 
            }
            if (m.id < normalMapMin) normalMapMin = m.id;
            if (m.id > normalMapMax) normalMapMax = m.id;
        }

        if (!isLH) {
            if (type == 5) {
                List<Byte> bossMaps = new ArrayList<>();
                for (MapData.MapDataEntry m : MapData.entries) {
                    if (m.isBoss) bossMaps.add(m.id);
                }
                Collections.sort(bossMaps);
                
                if (bossMaps.size() > 0 && nType >= 0) {
                    slMap.add(bossMaps.get(nType % bossMaps.size()));
                }
            }
        }

        switch (type) {
            case 0:
                minXu = 0; maxXu = 1000; minMap = normalMapMin; maxMap = normalMapMax;
                maxPlayerInit = ServerManager.nPlayersInitRoom; map = ServerManager.initMap;
                break;
            case 1:
                minXu = 1000; maxXu = 10000; minMap = normalMapMin; maxMap = normalMapMax;
                maxPlayerInit = ServerManager.nPlayersInitRoom; map = ServerManager.initMap;
                break;
            case 2:
                minXu = 10000; maxXu = 100000; minMap = normalMapMin; maxMap = normalMapMax;
                maxPlayerInit = ServerManager.nPlayersInitRoom; map = ServerManager.initMap;
                break;
            case 3:
                minXu = 100000; maxXu = 1000000; minMap = normalMapMin; maxMap = normalMapMax;
                maxPlayerInit = ServerManager.nPlayersInitRoom; map = ServerManager.initMap;
                break;
            case 4:
                minXu = 10000; maxXu = 10000; minMap = normalMapMin; maxMap = normalMapMax;
                maxPlayerInit = ServerManager.nPlayersInitRoom; map = ServerManager.initMap;
                break;
            case 5:
                minXu = 100; maxXu = 500000; minMap = 30; maxMap = 39;
                maxPlayerInit = ServerManager.maxPlayers; map = ServerManager.initMapBoss;
                break;
            case 6:
                minXu = 0; maxXu = Integer.MAX_VALUE; minMap = normalMapMin; maxMap = normalMapMax;
                maxPlayerInit = ServerManager.nPlayersInitRoom; map = ServerManager.initMap;
                break;
        }

        if (slMap.size() > 0) map = slMap.get(0);

        entrys = new FightWait[maxEntrys];
        for (int i = 0; i < maxEntrys; i++) {
            entrys[i] = new FightWait(this, this.type, (byte) i, ServerManager.maxPlayers, maxPlayerInit, map, (byte) Until.nextInt(0, 2), isLH);
        }
    }

    public int getFully() {
        int maxPlayers = 0;
        int player = 0;
        synchronized (entrys) {
            for (FightWait fw : entrys) {
                if (fw != null) {
                    maxPlayers += fw.maxSetPlayer;
                    player += fw.started ? fw.maxSetPlayer : fw.numPlayer;
                }
            }
            if (maxPlayers == 0) return 2;

            int percent = player * 100 / maxPlayers;
            return percent < 50 ? 2 : percent < 75 ? 1 : 0;
        }
    }
}
