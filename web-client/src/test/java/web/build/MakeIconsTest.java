package web.build;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.jupiter.api.Test;

class MakeIconsTest {
    private static final int RED = 0xFFFF0000, BLUE = 0xFF0000FF, CLEAR = 0x00000000, BG = 0xFF63C5F2;

    @Test
    void scaleTurnsEachPixelIntoAnExactBlock() {
        int[] src = {RED, BLUE, CLEAR, RED}; // 2×2
        int[] out = MakeIcons.scale(src, 2, 2, 3); // 6×6
        assertEquals(36, out.length);
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                assertEquals(src[(y / 3) * 2 + x / 3], out[y * 6 + x], "pixel " + x + "," + y);
            }
        }
    }

    @Test
    void padCentresArtOnOpaqueBackground() {
        int[] art = {RED, CLEAR, BLUE, RED}; // 2×2
        int[] out = MakeIcons.pad(art, 2, 6, 2, BG); // 6×6, art at (2,2)
        assertEquals(BG, out[0]);                // margin
        assertEquals(BG, out[5 * 6 + 5]);        // margin
        assertEquals(RED, out[2 * 6 + 2]);       // art
        assertEquals(BG, out[2 * 6 + 3]);        // transparent art pixel shows the background
        assertEquals(BLUE, out[3 * 6 + 2]);
        for (int p : out) assertEquals(0xFF, p >>> 24, "every pixel opaque");
    }

    @Test
    void padBlendsHalfTransparentPixels() {
        int halfWhite = 0x80FFFFFF;
        int out = MakeIcons.pad(new int[] {halfWhite}, 1, 1, 0, 0xFF000000)[0];
        assertEquals(0xFF, out >>> 24);
        int r = (out >> 16) & 0xFF;
        assertTrue(r >= 127 && r <= 129, "r=" + r);
    }

    @Test
    void icoWrapsThePngUnchanged() {
        byte[] png = {(byte) 0x89, 'P', 'N', 'G', 1, 2, 3};
        byte[] ico = MakeIcons.ico(png, 32, 32);
        ByteBuffer b = ByteBuffer.wrap(ico).order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(0, b.getShort(0));   // reserved
        assertEquals(1, b.getShort(2));   // type: icon
        assertEquals(1, b.getShort(4));   // one image
        assertEquals(32, ico[6]);         // width
        assertEquals(32, ico[7]);         // height
        assertEquals(1, b.getShort(10));  // colour planes
        assertEquals(32, b.getShort(12)); // bits per pixel
        assertEquals(png.length, b.getInt(14));
        assertEquals(22, b.getInt(18));   // data offset = 6 + 16
        assertEquals(22 + png.length, ico.length);
        for (int i = 0; i < png.length; i++) assertEquals(png[i], ico[22 + i]);
    }

    @Test
    void rejectsSourceThatIsNot32x32() {
        assertThrows(IllegalArgumentException.class, () -> MakeIcons.requireSourceSize(16, 16));
        assertThrows(IllegalArgumentException.class, () -> MakeIcons.requireSourceSize(32, 31));
        MakeIcons.requireSourceSize(32, 32);
    }
}
