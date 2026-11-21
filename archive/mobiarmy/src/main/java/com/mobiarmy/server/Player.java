package com.mobiarmy.server;

public class Player {

    public enum State { Waiting, InRoom, Fighting }

    public int iddb;
    public String name;
    public long xu;
    public short luong;
    public boolean superadmin;
    public boolean isActive;
    public int nvIndex;
    public int exp;
    public boolean online;
    public String banbe;
    public String killdead;
    public String itembag;
    public String thongtin;

    // Character selection fields
    public State state = State.Waiting;
    public int selectedChar;
    public int[] charLevels = new int[9];
    public int[] itemCounts = new int[9];
    /**
     * Bitmask of characters the user owns.
     * Bit i (0-based) = 1 if user owns character i (nhanvat_id i+1).
     * Default: 0x7 = owns chars 0,1,2 (Gunner, Miss6, Electician).
     */
    public int ownedMask = 0x7;

    public String getName() {
        return name;
    }

    /** True if user owns the given 0-based character index. */
    public boolean ownsChar(int charIndex) {
        if (charIndex < 0 || charIndex >= 9) return false;
        return (ownedMask & (1 << charIndex)) != 0;
    }

    /** Mark a character as owned. Idempotent. */
    public void grantChar(int charIndex) {
        if (charIndex < 0 || charIndex >= 9) return;
        ownedMask |= (1 << charIndex);
        if (charLevels[charIndex] <= 0) charLevels[charIndex] = 1;
    }
}
