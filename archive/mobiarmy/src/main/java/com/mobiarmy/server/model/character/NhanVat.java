package com.mobiarmy.server.model.character;

/**
 * Represents a character template loaded from the nhanvat table.
 * All fields map 1:1 to the SQL columns.
 */
public class NhanVat {

    public final int id;          // 1-based ID
    public final String name;
    public final int xu;         // price in xu (0 = free)
    public final int luong;       // price in luong
    public final int maSatGio;    // ??? angle/speed param
    public final int gocMin;     // ??? angle min
    public final int satThuong;   // normal damage
    public final int satThuongDan; // bullet damage
    public final int soDan;      // bullet count

    public NhanVat(int id, String name, int xu, int luong,
                    int maSatGio, int gocMin, int satThuong,
                    int satThuongDan, int soDan) {
        this.id = id;
        this.name = name;
        this.xu = xu;
        this.luong = luong;
        this.maSatGio = maSatGio;
        this.gocMin = gocMin;
        this.satThuong = satThuong;
        this.satThuongDan = satThuongDan;
        this.soDan = soDan;
    }

    /** Free characters have xu == 0 and luong == 0. */
    public boolean isFree() {
        return xu == 0 && luong == 0;
    }
}
