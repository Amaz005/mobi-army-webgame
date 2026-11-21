package web.build;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

/**
 * Build-time only (specs/003-web-icon). Generates the web icon set from the game's own 32×32
 * {@code icon.png}: integer nearest-neighbour scaling only, so the pixel art stays sharp.
 * Usage: {@code MakeIcons <icon.png> <outDir>}.
 */
public final class MakeIcons {
    static final int SOURCE_SIZE = 32;
    /** Game sky blue: iOS fills transparent home-screen icons with black, so that one gets a background. */
    static final int APPLE_BACKGROUND = 0xFF63C5F2;

    private MakeIcons() {}

    public static void main(String[] args) throws IOException {
        Path source = Paths.get(args[0]);
        Path out = Paths.get(args[1]);
        Files.createDirectories(out);

        byte[] png = Files.readAllBytes(source);
        BufferedImage img = ImageIO.read(source.toFile());
        requireSourceSize(img.getWidth(), img.getHeight());
        int[] argb = img.getRGB(0, 0, SOURCE_SIZE, SOURCE_SIZE, null, 0, SOURCE_SIZE);

        Files.write(out.resolve("favicon.ico"), ico(png, SOURCE_SIZE, SOURCE_SIZE));
        writePng(out.resolve("icon-192.png"), scale(argb, SOURCE_SIZE, SOURCE_SIZE, 6), 192);
        writePng(out.resolve("icon-512.png"), scale(argb, SOURCE_SIZE, SOURCE_SIZE, 16), 512);
        int[] art = scale(argb, SOURCE_SIZE, SOURCE_SIZE, 5); // 160×160
        writePng(out.resolve("apple-touch-icon.png"), pad(art, 160, 180, 10, APPLE_BACKGROUND), 180);
    }

    static void requireSourceSize(int w, int h) {
        if (w != SOURCE_SIZE || h != SOURCE_SIZE) {
            throw new IllegalArgumentException("icon.png must be " + SOURCE_SIZE + "x" + SOURCE_SIZE + ", got " + w + "x" + h);
        }
    }

    /** Each source pixel becomes a factor×factor block. */
    static int[] scale(int[] argb, int w, int h, int factor) {
        int ow = w * factor;
        int[] out = new int[ow * h * factor];
        for (int y = 0; y < h * factor; y++) {
            for (int x = 0; x < ow; x++) {
                out[y * ow + x] = argb[(y / factor) * w + x / factor];
            }
        }
        return out;
    }

    /** Square art of side {@code size} placed at (offset, offset) on an opaque {@code canvas}-sized square. */
    static int[] pad(int[] art, int size, int canvas, int offset, int background) {
        int[] out = new int[canvas * canvas];
        java.util.Arrays.fill(out, background);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int i = (y + offset) * canvas + x + offset;
                out[i] = over(art[y * size + x], background);
            }
        }
        return out;
    }

    /** Source-over compositing onto an opaque background. */
    private static int over(int src, int bg) {
        int a = src >>> 24;
        if (a == 255) return src;
        int rgb = 0;
        for (int shift = 0; shift <= 16; shift += 8) {
            int s = (src >> shift) & 0xFF, d = (bg >> shift) & 0xFF;
            rgb |= ((s * a + d * (255 - a) + 127) / 255) << shift;
        }
        return 0xFF000000 | rgb;
    }

    /** ICO file with one entry holding the PNG bytes unchanged. */
    static byte[] ico(byte[] png, int w, int h) {
        ByteBuffer b = ByteBuffer.allocate(22 + png.length).order(ByteOrder.LITTLE_ENDIAN);
        b.putShort((short) 0).putShort((short) 1).putShort((short) 1);   // reserved, type icon, count
        b.put((byte) w).put((byte) h).put((byte) 0).put((byte) 0);       // size, no palette, reserved
        b.putShort((short) 1).putShort((short) 32);                      // planes, bits per pixel
        b.putInt(png.length).putInt(22);                                 // data size, data offset
        b.put(png);
        return b.array();
    }

    private static void writePng(Path file, int[] argb, int side) throws IOException {
        BufferedImage img = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, side, side, argb, 0, side);
        if (!ImageIO.write(img, "png", file.toFile())) throw new IOException("no PNG writer");
    }
}
