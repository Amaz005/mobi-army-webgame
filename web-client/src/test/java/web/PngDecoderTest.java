package web;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

/** Decodes every game PNG and compares each pixel with the JDK's decoder. */
class PngDecoderTest {

    private static final Path GAME_RES = Paths.get("..", "Army165local");

    @Test
    void decodesEveryGamePngLikeTheJdk() throws IOException {
        List<Path> pngs;
        try (Stream<Path> s = Files.walk(GAME_RES)) {
            pngs = s.filter(p -> p.toString().endsWith(".png") && !p.toString().contains(".svn"))
                    .collect(Collectors.toList());
        }
        assertFalse(pngs.isEmpty(), "expected game PNGs under " + GAME_RES.toAbsolutePath());
        for (Path p : pngs) {
            assertSameAsJdk(Files.readAllBytes(p), p.toString());
        }
    }

    @Test
    void decodesTruecolorAlphaGrayAndPalettesWrittenByJdk() throws IOException {
        int[] types = {BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_RGB,
                BufferedImage.TYPE_BYTE_GRAY, BufferedImage.TYPE_BYTE_BINARY,
                BufferedImage.TYPE_BYTE_INDEXED};
        for (int type : types) {
            BufferedImage img = new BufferedImage(13, 7, type);
            for (int y = 0; y < 7; y++) {
                for (int x = 0; x < 13; x++) {
                    int a = type == BufferedImage.TYPE_INT_ARGB ? (x * 20) & 0xFF : 0xFF;
                    img.setRGB(x, y, (a << 24) | ((x * 19) << 16) | ((y * 33) << 8) | ((x + y) * 9));
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "png", out);
            assertSameAsJdk(out.toByteArray(), "synthetic type " + type);
        }
    }

    @Test
    void rejectsNonPng() {
        assertThrows(IllegalArgumentException.class, () -> PngDecoder.decode(new byte[] {1, 2, 3}, 0, 3));
    }

    private static void assertSameAsJdk(byte[] bytes, String what) throws IOException {
        BufferedImage ref = ImageIO.read(new ByteArrayInputStream(bytes));
        PngDecoder.Result r = PngDecoder.decode(bytes, 0, bytes.length);
        assertEquals(ref.getWidth(), r.width, what + " width");
        assertEquals(ref.getHeight(), r.height, what + " height");
        // For gray PNGs the JDK's getRGB() applies a linear→sRGB gamma curve; the PNG spec says the stored
        // sample IS the gray level, so compare against the raw raster sample instead.
        boolean gray = ref.getColorModel().getColorSpace().getType() == java.awt.color.ColorSpace.TYPE_GRAY;
        int maxSample = (1 << ref.getColorModel().getComponentSize(0)) - 1;
        for (int y = 0; y < r.height; y++) {
            for (int x = 0; x < r.width; x++) {
                int expected;
                if (gray) {
                    int v = ref.getRaster().getSample(x, y, 0) * 255 / maxSample;
                    expected = 0xFF000000 | (v << 16) | (v << 8) | v;
                } else {
                    expected = ref.getRGB(x, y);
                }
                int actual = r.argb[y * r.width + x];
                if ((expected >>> 24) == 0 && (actual >>> 24) == 0) continue; // colour of transparent pixels is irrelevant
                assertEquals(Integer.toHexString(expected), Integer.toHexString(actual),
                        what + " pixel " + x + "," + y);
            }
        }
    }
}
