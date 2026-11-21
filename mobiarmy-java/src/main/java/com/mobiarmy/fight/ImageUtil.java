package com.mobiarmy.fight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    public static class ImageResult {
        public int[] data;
        public int w;
        public int h;

        public ImageResult(int[] data, int w, int h) {
            this.data = data;
            this.w = w;
            this.h = h;
        }
    }

    public static ImageResult loadArgb(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) return new ImageResult(new int[0], 0, 0);

            BufferedImage img = ImageIO.read(f);
            if (img == null) return new ImageResult(new int[0], 0, 0);

            int w = img.getWidth();
            int h = img.getHeight();
            int[] data = new int[w * h];
            img.getRGB(0, 0, w, h, data, 0, w);
            return new ImageResult(data, w, h);
        } catch (IOException e) {
            System.err.println("Failed to load image " + path + ": " + e.getMessage());
            return new ImageResult(new int[0], 0, 0);
        }
    }
}
