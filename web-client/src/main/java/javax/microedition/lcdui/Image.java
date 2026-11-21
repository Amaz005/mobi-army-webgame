package javax.microedition.lcdui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int8Array;
import web.Js;
import web.PngDecoder;

/** MIDP Image backed by an off-screen canvas. PNGs are decoded synchronously by {@link PngDecoder}. */
public class Image {
    final JSObject canvas;
    private final JSObject ctx;
    private final int width, height;
    private final boolean mutable;
    private Graphics graphics;

    private Image(int width, int height, boolean mutable) {
        this.width = width;
        this.height = height;
        this.mutable = mutable;
        this.canvas = Js.createCanvas(width, height);
        this.ctx = Js.context2d(canvas);
    }

    public static Image createImage(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException();
        Image img = new Image(width, height, true);
        Graphics g = img.getGraphics();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, width, height);
        g.setColor(0);
        return img;
    }

    public static Image createImage(String name) throws IOException {
        InputStream in = Image.class.getResourceAsStream(name);
        if (in == null) throw new IOException("missing resource " + name);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            byte[] bytes = out.toByteArray();
            return createImage(bytes, 0, bytes.length);
        } finally {
            in.close();
        }
    }

    public static Image createImage(byte[] data, int offset, int length) {
        PngDecoder.Result r = PngDecoder.decode(data, offset, length);
        return fromArgb(r.argb, 0, r.width, r.width, r.height, true);
    }

    public static Image createImage(Image source) {
        if (!source.mutable) return source;
        Image img = new Image(source.width, source.height, false);
        img.drawFrom(source);
        return img;
    }

    public static Image createImage(Image source, int x, int y, int w, int h, int transform) {
        boolean swap = transform == Graphics.TRANS_ROT90 || transform == Graphics.TRANS_ROT270
                || transform == Graphics.TRANS_MIRROR_ROT90 || transform == Graphics.TRANS_MIRROR_ROT270;
        Image img = new Image(swap ? h : w, swap ? w : h, true);
        img.getGraphics().drawRegion(source, x, y, w, h, transform, 0, 0, Graphics.TOP | Graphics.LEFT);
        return new Image(img);
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException();
        return fromArgb(rgb, 0, width, width, height, processAlpha);
    }

    /** Immutable copy of a mutable image (used by createImage(Image, …)). */
    private Image(Image mutableSource) {
        this(mutableSource.width, mutableSource.height, false);
        drawFrom(mutableSource);
    }

    private void drawFrom(Image src) {
        Graphics g = new Graphics(ctx, width, height);
        g.drawImage(src, 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    private static Image fromArgb(int[] argb, int offset, int scan, int width, int height, boolean processAlpha) {
        Image img = new Image(width, height, false);
        byte[] rgba = toRgba(argb, offset, scan, width, height, processAlpha);
        Js.putRgba(img.ctx, Int8Array.copyFromJavaArray(rgba), 0, 0, width, height);
        return img;
    }

    static byte[] toRgba(int[] argb, int offset, int scan, int width, int height, boolean processAlpha) {
        byte[] rgba = new byte[width * height * 4];
        int o = 0;
        for (int y = 0; y < height; y++) {
            int row = offset + y * scan;
            for (int x = 0; x < width; x++) {
                int p = argb[row + x];
                rgba[o++] = (byte) (p >> 16);
                rgba[o++] = (byte) (p >> 8);
                rgba[o++] = (byte) p;
                rgba[o++] = (byte) (processAlpha ? p >>> 24 : 0xFF);
            }
        }
        return rgba;
    }

    public Graphics getGraphics() {
        if (!mutable) throw new IllegalStateException("immutable image");
        if (graphics == null) {
            graphics = new Graphics(ctx, width, height);
        } else {
            graphics.reset();
        }
        return graphics;
    }

    public boolean isMutable() {
        return mutable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) return;
        byte[] rgba = Js.getRgba(ctx, x, y, w, h).toJavaArray();
        int i = 0;
        for (int row = 0; row < h; row++) {
            int o = offset + row * scanlength;
            for (int col = 0; col < w; col++) {
                int r = rgba[i++] & 0xFF, g = rgba[i++] & 0xFF, b = rgba[i++] & 0xFF, a = rgba[i++] & 0xFF;
                rgbData[o + col] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
    }
}
