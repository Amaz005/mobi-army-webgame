package com.mobiarmy.data;

import java.util.Date;
import java.util.Random;

public class Until {
    private static final Random random = new Random();

    public static int nextInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static int nextInt(int max) {
        return random.nextInt(max);
    }

    public static byte[] getFile(String path) {
        try {
            return java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path));
        } catch (Exception e) {
            return null;
        }
    }

    public static int getNumDay(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    public static boolean inRegion(int x, int y, int rx, int ry, int rw, int rh) {
        return x >= rx && x < rx + rw && y >= ry && y < ry + rh;
    }

    public static boolean isNotAlpha(int argb) {
        return (argb >>> 24) != 0;
    }

    public static boolean intersecRegions(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }
    
    public static short getShort(byte[] data, int offset) {
        return (short) (((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF));
    }

    public static int cos(int angle) {
        return (int) (Math.cos(angle * Math.PI / 180) * 1024);
    }

    public static int sin(int angle) {
        return (int) (Math.sin(angle * Math.PI / 180) * 1024);
    }

    public static int getArg(int dx, int dy) {
        int a = (int) (Math.atan2(-dy, dx) * 180 / Math.PI);
        while (a < 0) a += 360;
        while (a >= 360) a -= 360;
        return a;
    }

    public static int getArgXY(int x1, int y1, int x2, int y2) {
        return getArg(x2 - x1, y2 - y1);
    }

    public static int toArg0_360(int a) {
        while (a < 0) a += 360;
        while (a >= 360) a -= 360;
        return a;
    }
}
