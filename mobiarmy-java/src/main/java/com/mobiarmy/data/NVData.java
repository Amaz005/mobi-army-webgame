package com.mobiarmy.data;

import java.util.ArrayList;
import java.util.List;

public class NVData {

    public static class EquipmentData {
        private byte id;
        private List<EquipmentEntry> entries = new ArrayList<>();

        public byte getId() { return id; }
        public void setId(byte id) { this.id = id; }

        public List<EquipmentEntry> getEntries() { return entries; }
        public void setEntries(List<EquipmentEntry> entries) { this.entries = entries; }
    }

    public static class EquipmentEntry {
        public int indexEquip;
        public int indexSale;
        public byte idNv;
        public byte idEquipDat;
        public short id;
        public String name;
        public int giaXu;
        public int giaLuong;
        public int hanSuDung;
        public byte bullId;
        public short frame;
        public byte levelRequire;
        public short[] bigImageCutX;
        public short[] bigImageCutY;
        public byte[] bigImageSizeX;
        public byte[] bigImageSizeY;
        public byte[] bigImageAlignX;
        public byte[] bigImageAlignY;
        public byte[] invAdd;
        public byte[] percenAdd;
        public boolean onSale;
        public boolean isSet;
        public short[] arraySet;
    }

    public static class NvEntry {
        public byte id;
        public String name;
        public int buyXu;
        public int buyLuong;
        public byte maSatGio;
        public byte gocMin;
        public byte soDan;
        public short satThuong;
        public byte satThuongDan;
        public List<EquipmentData> trangBis = new ArrayList<>();
    }

    public static List<NvEntry> entries = new ArrayList<>();
    public static List<EquipmentEntry> equips = new ArrayList<>();
    public static int saleEquipCount = 0;

    public static void addEquipEntryById(int nvId, int equipDatId, int equipId, EquipmentEntry entry) {
        System.out.println("Set equip nv=" + nvId + " equipType=" + equipDatId + " id=" + equipId);

        NvEntry nvEntry = null;
        for (NvEntry e : entries) {
            if (e.id == (byte) nvId) {
                nvEntry = e;
                break;
            }
        }
        if (nvEntry == null) return;

        EquipmentData equipDataEntry = null;
        for (EquipmentData e : nvEntry.trangBis) {
            if (e.getId() == (byte) equipDatId) {
                equipDataEntry = e;
                break;
            }
        }

        if (equipDataEntry == null) {
            equipDataEntry = new EquipmentData();
            equipDataEntry.setId((byte) equipDatId);
            nvEntry.trangBis.add(equipDataEntry);
        }

        for (EquipmentEntry e : equipDataEntry.getEntries()) {
            if (e.id == (short) equipId) {
                return;
            }
        }

        if (entry.onSale) {
            entry.indexSale = saleEquipCount;
            saleEquipCount++;
        }
        entry.indexEquip = equips.size();
        equipDataEntry.getEntries().add(entry);
        equips.add(entry);
    }

    public static EquipmentEntry getEquipEntryById(int nvId, int equipDatId, int equipId) {
        for (EquipmentEntry e : equips) {
            if (e.idNv == (byte) nvId && e.idEquipDat == (byte) equipDatId && e.id == (short) equipId) {
                return e;
            }
        }
        return null;
    }

    public static EquipmentEntry getEquipEntryByIndexSale(int indexSale) {
        for (EquipmentEntry e : equips) {
            if (e.onSale && e.indexSale == indexSale) {
                return e;
            }
        }
        return null;
    }
}
