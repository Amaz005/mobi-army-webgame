package com.mobiarmy.fight;

import com.mobiarmy.data.Until;
import java.util.Arrays;

public class MapEntry {
    public static int[] mangNhenData;
    public static int mangNhenW;
    public static int mangNhenH;

    public static int[][] holeDatas;
    public static int[] holeW;
    public static int[] holeH;

    private static final String[] HOLE_FILES = {
        "res/effect/hole/h32x26.png",
        "res/effect/hole/smallhole.png",
        "res/effect/hole/smallhole.png", 
        "res/effect/hole/h36x30.png",
        "res/effect/hole/rocket.png",
        "res/effect/hole/rangehole.png",
        "res/effect/hole/hrangcua.png",
        "res/effect/hole/hgrenade.png",
        "res/effect/hole/h14x12.png",
        "res/effect/hole/h55x50.png"
    };

    static {
        ImageUtil.ImageResult mangNhenRes = ImageUtil.loadArgb("res/effect/hole/mangnhen.png");
        mangNhenData = mangNhenRes.data;
        mangNhenW = mangNhenRes.w;
        mangNhenH = mangNhenRes.h;

        holeDatas = new int[HOLE_FILES.length][];
        holeW = new int[HOLE_FILES.length];
        holeH = new int[HOLE_FILES.length];
        for (int i = 0; i < HOLE_FILES.length; i++) {
            ImageUtil.ImageResult res = ImageUtil.loadArgb(HOLE_FILES[i]);
            holeDatas[i] = res.data;
            holeW[i] = res.w;
            holeH[i] = res.h;
        }
    }

    public int id;
    public int[] argb;
    public short width;
    public short height;
    public short x;
    public short y;
    public boolean canCollision;

    public MapEntry(int id, short x, short y, int[] data, short w, short h, boolean show) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.canCollision = show;
        setData(data, w, h);
    }

    public void setData(int[] data, short w, short h) {
        this.width = w;
        this.height = h;
        this.argb = data != null ? Arrays.copyOf(data, data.length) : new int[0];
    }

    public boolean isCollision(short checkX, short checkY) {
        return Until.inRegion(checkX, checkY, x, y, width, height) && Until.isNotAlpha(getArgb(checkX - x, checkY - y));
    }

    public void collision(short bx, short by, Bullet bull) {
        int index = Bullet.getHoleByBulletId(bull.bullId);
        int w = holeW[index];
        int h = holeH[index];
        int[] holePixels = holeDatas[index];

        if (!canCollision || !Until.intersecRegions(bx - w / 2, by - h / 2, w, h, x, y, width, height)) {
            return;
        }

        bx -= (short) (x + w / 2);
        by -= (short) (y + h / 2);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (!Until.inRegion(bx + j, by + i, 0, 0, width, height)) continue;

                int holePixel = holePixels[i * w + j];
                if (holePixel == 0xffff0000 && Until.isNotAlpha(getArgb(bx + j, by + i))) {
                    argb[(by + i) * width + bx + j] = 0xff000000;
                } else if (holePixel == 0xff000000) {
                    argb[(by + i) * width + bx + j] = 0;
                }
            }
        }
    }

    public int getArgb(int px, int py) {
        return argb[py * width + px];
    }
}
