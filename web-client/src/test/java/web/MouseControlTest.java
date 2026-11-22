package web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** specs/005-mouse-control: contracts/mouse-mapping.md, data-model.md (Selection bar, Click, Seek). */
class MouseControlTest {
    private static final int W = 240, H = 320, SOFT = 19, Y = MouseControl.YELLOW, O = MouseControl.ORANGE;
    private MouseControl mc;
    private long now;

    @BeforeEach
    void setUp() {
        mc = new MouseControl(W, H);
        now = 1000;
    }

    /** One painted frame with a bar at y (h high), full width, no clip. */
    private int frame(int barY, int barH) {
        if (barY >= 0) mc.barCandidate(0, barY, W, barH, Y, 0, 0, W, H);
        return mc.frameEnd();
    }

    private int click(int x, int y) {
        now += 1000;
        return mc.click(x, y, x, y, SOFT, now);
    }

    // --- soft strip (data-model.md › Soft strip) ---

    @Test
    void bottomStripThirdsAreTheSoftKeys() {
        assertEquals(MouseControl.LEFT_SOFT, click(10, H - 5));
        assertEquals(MouseControl.FIRE, click(W / 2, H - 5));
        assertEquals(MouseControl.RIGHT_SOFT, click(W - 10, H - 5));
        assertEquals(MouseControl.NONE, click(W / 2, H - SOFT - 2), "just above the strip, no bar");
    }

    // --- bar filter (data-model.md › Selection bar) ---

    @Test
    void onlyWideHighlightBarsCount() {
        mc.barCandidate(0, 100, W / 2, 20, Y, 0, 0, W, H);       // too narrow
        mc.barCandidate(0, 100, W, 8, Y, 0, 0, W, H);            // too short
        mc.barCandidate(0, 100, W, 80, Y, 0, 0, W, H);           // too tall
        mc.barCandidate(0, 100, W, 20, 0x00FF00, 0, 0, W, H);    // wrong colour
        mc.frameEnd();
        assertNull(mc.bar());
        mc.barCandidate(0, 50, W, 20, Y, 0, 0, W, H);
        mc.barCandidate(0, 120, W, 57, O, 0, 0, W, H);           // last one wins
        mc.frameEnd();
        assertArrayEquals(new int[] { 0, 120, W, 57 }, mc.bar());
        mc.frameEnd();
        assertNull(mc.bar(), "a frame without a bar clears it");
    }

    @Test
    void barIsClippedToTheVisibleArea() {
        mc.barCandidate(0, 40, W, 20, Y, 0, 50, W, 200); // half hidden above the list area
        mc.frameEnd();
        assertArrayEquals(new int[] { 0, 50, W, 10 }, mc.bar());
    }

    // --- click detection (data-model.md › Click.isClick) ---

    @Test
    void aDragIsNotAClick() {
        frame(100, 20);
        now += 1000;
        assertEquals(MouseControl.NONE, mc.click(W / 2, 110, W / 2 + 20, 110, SOFT, now));
        assertFalse(mc.seeking());
    }

    // --- seek ---

    @Test
    void clickOnTheHighlightedRowFires() {
        frame(100, 20);
        assertEquals(MouseControl.FIRE, click(W / 2, 110));
    }

    @Test
    void clickBelowStepsDownThenFires() {
        frame(100, 20);
        assertEquals(MouseControl.NONE, click(W / 2, 165)); // row 3 below (rows of 20 at 120, 140, 160)
        assertTrue(mc.seeking());
        int y = 100;
        int downs = 0;
        int key = frame(100, 20); // the same frame repainted: the bar is stable
        for (int i = 0; i < 40 && mc.seeking(); i++) {
            if (key == MouseControl.DOWN) { downs++; y += 20; }
            key = frame(y, 20);
            if (key == MouseControl.FIRE) break;
        }
        assertEquals(3, downs);
        assertEquals(MouseControl.FIRE, key);
        assertFalse(mc.seeking());
    }

    @Test
    void clickAboveStepsUp() {
        frame(160, 20);
        click(W / 2, 105);
        assertEquals(MouseControl.UP, firstKey(6), "steps up once the bar has settled");
    }

    /** Repaints the current bar up to n times and returns the first key sent. */
    private int firstKey(int n) {
        int[] b = mc.bar();
        for (int i = 0; i < n; i++) {
            int key = frame(b[1], b[3]);
            if (key != MouseControl.NONE) return key;
        }
        return MouseControl.NONE;
    }

    @Test
    void stopsWithoutFiringWhenTheListWraps() {
        frame(260, 20);
        click(W / 2, 290); // two rows below the last one
        int key = frame(260, 20);
        for (int i = 0; i < 5 && key != MouseControl.DOWN; i++) key = frame(260, 20);
        assertEquals(MouseControl.DOWN, key);
        // the game wrapped to the top row
        for (int i = 0; i < 5; i++) key = frame(40, 20);
        assertNotEquals(MouseControl.FIRE, key);
        assertFalse(mc.seeking());
    }

    @Test
    void stopsWhenTheBarDisappearsOrAKeyOrClickArrives() {
        frame(100, 20);
        click(W / 2, 170);
        frame(-1, 0);
        assertFalse(mc.seeking(), "bar gone");

        frame(100, 20);
        click(W / 2, 170);
        mc.realKey();
        assertFalse(mc.seeking(), "real key");

        frame(100, 20);
        click(W / 2, 170);
        mc.cancel();
        assertFalse(mc.seeking(), "cancel (blur, pointer leave)");
    }

    @Test
    void scrollingListKeepsCountingAndFires() {
        // The list scrolls: after the first step the bar stays at the same screen row.
        frame(100, 20);
        click(W / 2, 165); // three rows below
        int downs = 0, y = 100, key = frame(100, 20);
        for (int i = 0; i < 40 && mc.seeking(); i++) {
            if (key == MouseControl.DOWN) { downs++; if (downs == 1) y += 20; }
            key = frame(y, 20);
            if (key == MouseControl.FIRE) break;
        }
        assertEquals(3, downs);
        assertEquals(MouseControl.FIRE, key);
    }

    @Test
    void neverMoreThanThirtySteps() {
        frame(20, 20);
        click(W / 2, 290);
        int downs = 0, key = frame(20, 20);
        for (int i = 0; i < 400 && mc.seeking(); i++) {
            if (key == MouseControl.DOWN) downs++;
            key = frame(20, 20); // bar never moves (scrolling forever)
        }
        assertTrue(downs <= MouseControl.MAX_STEPS, "downs " + downs);
        assertFalse(mc.seeking());
    }

    // --- double click, wheel, right click ---

    @Test
    void doubleClickFiresOnce() {
        frame(100, 20);
        now += 1000;
        assertEquals(MouseControl.FIRE, mc.click(W / 2, 110, W / 2, 110, SOFT, now));
        assertEquals(MouseControl.NONE, mc.click(W / 2, 110, W / 2, 110, SOFT, now + 150));
        assertEquals(MouseControl.FIRE, mc.click(W / 2, 110, W / 2, 110, SOFT, now + 400));
    }

    @Test
    void wheelIsThrottled() {
        assertEquals(MouseControl.DOWN, mc.wheel(1, 1000));
        assertEquals(MouseControl.NONE, mc.wheel(1, 1050));
        assertEquals(MouseControl.UP, mc.wheel(-1, 1100));
        assertEquals(MouseControl.NONE, mc.wheel(0, 2000));
    }

    @Test
    void rightClickIsTheRightSoftKeyAndCancelsASeek() {
        frame(100, 20);
        click(W / 2, 170);
        assertEquals(MouseControl.RIGHT_SOFT, mc.rightClick());
        assertFalse(mc.seeking());
    }

    @Test
    void popUpMenuBarCountsWithinItsBox() {
        // A pop-up menu 100 px wide: the bar is 92 px, inside a clip of 100 px.
        mc.barCandidate(15, 180, 92, SOFT, MouseControl.POPUP, 10, 170, 100, 150);
        mc.frameEnd();
        assertArrayEquals(new int[] { 15, 180, 92, SOFT }, mc.bar());
        mc.barCandidate(15, 180, 92, SOFT, MouseControl.POPUP, 0, 0, W, H); // same bar, full-screen clip: too narrow
        mc.frameEnd();
        assertNull(mc.bar());
    }

    @Test
    void animatedScrollWithPausesStillLandsOnTheClickedRow() {
        // Rows of 20. The bar walks down to y=140, then the list scrolls (animated, with paused frames).
        frame(100, 20);
        click(W / 2, 205); // 5 rows below: rows at 120, 140, 160, 180, 200
        int y = 100, downs = 0, key = frame(100, 20);
        int[][] scrollFrames = { { 0 }, { 6 }, { 6 }, { 12 }, { 12 }, { 20 } }; // bar offset while scrolling settles
        for (int i = 0; i < 200 && mc.seeking(); i++) {
            if (key == MouseControl.DOWN) {
                downs++;
                if (y < 140) {
                    y += 20;
                    for (int f = 0; f < 4 && mc.seeking(); f++) key = frame(y, 20);
                } else {
                    // The selection moved a row but the list scrolls: the bar dips and comes back.
                    for (int[] s : scrollFrames) key = frame(y + 20 - s[0], 20);
                }
                continue;
            }
            key = frame(y, 20);
            if (key == MouseControl.FIRE) break;
        }
        assertEquals(5, downs, "exactly five rows down");
        assertEquals(MouseControl.FIRE, key);
    }
}
