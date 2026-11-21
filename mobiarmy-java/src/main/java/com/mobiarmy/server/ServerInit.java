package com.mobiarmy.server;

import com.mobiarmy.data.ItemData;
import com.mobiarmy.data.MapData;
import com.mobiarmy.data.NVData;
import com.mobiarmy.data.SqlConnectionManager;
import com.mobiarmy.data.Until;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerInit {
    
    private interface RowAction {
        void accept(ResultSet rs) throws Exception;
    }

    private static void forEachRow(String sql, RowAction action) {
        try (Connection conn = SqlConnectionManager.createConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                action.accept(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        ServerManager.start = false;
        ServerManager.loadConfigFile();
        SqlConnectionManager.configure(ServerManager.mysqlHost, ServerManager.mysqlDatabase, ServerManager.mysqlUser, ServerManager.mysqlPass);
        
        SqlConnectionManager.useMySql();
        try (Connection conn = SqlConnectionManager.createConnection()) {
            System.out.println("[ServerInit] Connected to MySQL " + ServerManager.mysqlHost + "/" + ServerManager.mysqlDatabase);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect to MySQL " + ServerManager.mysqlHost + "/" + ServerManager.mysqlDatabase, e);
        }

        System.out.println("Load map data");
        try {
            MapData.entries = new ArrayList<>();
            MapData.brickEntries = new ArrayList<>();
            forEachRow("SELECT * FROM `map` ORDER BY `id`;", r -> {
                String file = r.getString("file");
                MapData.MapDataEntry e = new MapData.MapDataEntry();
                e.id = (byte) (r.getInt("id") - 1);
                e.name = r.getString("name");
                e.file = file;
                e.bg = r.getShort("bg");
                e.mapAddY = r.getShort("mapAddY");
                e.bullEffShower = r.getShort("bullEffShower");
                e.inWaterAddY = r.getShort("inWaterAddY");
                e.cl2AddY = r.getShort("cl2AddY");

                try {
                    e.isBoss = r.getInt("is_boss") != 0;
                } catch (Exception ex) {
                    e.isBoss = e.id >= 82;
                }

                byte[] data = Until.getFile("res/map/" + file);
                if (data == null) {
                    System.out.println("[army1] THIẾU file res/map/" + file + " — map id " + e.id + " (" + e.name + ") sẽ crash khi vào trận!");
                    System.exit(0);
                }
                e.data = data;
                MapData.entries.add(e);
            });
            System.out.println("Map readed size=" + MapData.entries.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Load NV Data!");
        try {
            NVData.entries = new ArrayList<>();
            NVData.equips = new ArrayList<>();
            NVData.saleEquipCount = 0;
            forEachRow("SELECT * FROM `nhanvat` ORDER BY `nhanvat_id`;", r -> {
                NVData.NvEntry entry = new NVData.NvEntry();
                entry.id = (byte) (r.getInt("nhanvat_id") - 1);
                entry.name = r.getString("name");
                entry.buyXu = r.getInt("xu");
                entry.buyLuong = r.getShort("luong");
                entry.maSatGio = (byte) r.getShort("ma_sat_gio");
                entry.gocMin = (byte) r.getShort("goc_min");
                entry.soDan = (byte) r.getShort("so_dan");
                entry.satThuong = r.getShort("sat_thuong");
                entry.satThuongDan = (byte) r.getShort("sat_thuong_dan");
                entry.trangBis = new ArrayList<>();
                NVData.entries.add(entry);
            });
            System.out.println("NV Loader Size= " + NVData.entries.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        int[] army1CharacterBulletId = { 0, 1, 2, 9, 10, 11, 19, 21, 17 };
        User.nvEquipDefault = new NVData.EquipmentEntry[NVData.entries.size()][];
        for (int i = 0; i < NVData.entries.size(); i++) {
            User.nvEquipDefault[i] = new NVData.EquipmentEntry[5];
            int bulletId = i < army1CharacterBulletId.length ? army1CharacterBulletId[i] : 0;
            NVData.EquipmentEntry eq = new NVData.EquipmentEntry();
            eq.id = 0;
            eq.name = NVData.entries.get(i).name;
            eq.bullId = (byte) bulletId;
            User.nvEquipDefault[i][0] = eq;
        }

        System.out.println("Load Item Data!");
        try {
            ItemData.entries = new ArrayList<>();
            forEachRow("SELECT * FROM `item` ORDER BY `item_id`;", r -> {
                ItemData.ItemEntry e = new ItemData.ItemEntry();
                e.name = r.getString("name");
                e.buyXu = r.getShort("xu");
                e.buyLuong = r.getShort("luong");
                ItemData.entries.add(e);
            });
            System.out.println("Item readed size=" + ItemData.entries.size());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
