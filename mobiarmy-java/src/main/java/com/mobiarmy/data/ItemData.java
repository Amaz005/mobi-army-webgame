package com.mobiarmy.data;

import java.util.ArrayList;
import java.util.List;

public class ItemData {
    public static class ItemEntry {
        public String name;
        public int buyXu;
        public int buyLuong;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getBuyXu() { return buyXu; }
        public void setBuyXu(int buyXu) { this.buyXu = buyXu; }

        public int getBuyLuong() { return buyLuong; }
        public void setBuyLuong(int buyLuong) { this.buyLuong = buyLuong; }
    }

    public static List<ItemEntry> entries = new ArrayList<>();

    public static final byte[] MAX_CARRY_COUNT = {
        2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1
    };
}
