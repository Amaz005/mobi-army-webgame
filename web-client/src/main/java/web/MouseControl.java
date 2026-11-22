package web;

import java.util.Arrays;

/**
 * Turns mouse actions on the game canvas into the phone keys the original client understands
 * (specs/005-mouse-control, research.md D1–D3, D7; contracts/mouse-mapping.md). Pure logic: the web
 * MIDP layer feeds it painted rectangles, frames, clicks and wheel events, and injects the keys it
 * returns.
 *
 * <p>Row clicking follows the game's own highlight bar: each frame the last wide {@code fillRect} in a
 * highlight colour is the "selection bar". A click on another row sends up/down, one at a time once
 * the bar has settled, until the bar is under the click, then OK. Lists that scroll instead of moving
 * the bar are handled by moving the click target with the content.
 */
public final class MouseControl {
    public static final int YELLOW = 0xFFFF36;
    public static final int ORANGE = 0xFFD200;
    /** Pop-up menus (coreLG.el, bg): the bar spans the menu box, not the screen. */
    public static final int POPUP = 0xF0AA01;

    /** MIDP key codes (002 contracts/key-mapping.md); NONE = nothing to send. */
    public static final int NONE = 0, UP = -1, DOWN = -2, FIRE = -5, LEFT_SOFT = -6, RIGHT_SOFT = -7;

    static final int CLICK_SLOP = 6;
    static final int MAX_STEPS = 30;
    /** Frames to wait after sending a step before judging where the bar went (the game lags a frame or two). */
    static final int SETTLE_FRAMES = 3;
    /** Consecutive unchanged frames before the bar counts as settled (scrolling lists animate, with pauses). */
    static final int STABLE_FRAMES = 3;
    static final int WHEEL_MS = 80;
    static final int DOUBLE_MS = 300;
    static final int MIN_BAR_H = 12, MAX_BAR_H = 60;

    private final int width;
    private final int height;

    private int[] candidate;
    private int[] bar;
    private int stableFrames;
    private boolean pitchLearned;

    private boolean seeking;
    private int targetY;
    private int dir;
    private int steps;
    private int pitch;
    private int framesSinceStep;
    private int[] stepFrom;

    private long lastClick = Long.MIN_VALUE / 2;
    private long lastWheel = Long.MIN_VALUE / 2;

    public MouseControl(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /** A painted fillRect, in screen pixels, with the current colour and clip (data-model.md › Selection bar). */
    public void barCandidate(int x, int y, int w, int h, int rgb, int clipX, int clipY, int clipW, int clipH) {
        int c = rgb & 0xFFFFFF;
        if (c != YELLOW && c != ORANGE && c != POPUP) return;
        // At least 80 % of the visible area it is drawn in (the screen, or a pop-up menu's box).
        int ref = Math.max(Math.min(width, clipW), width / 4);
        if (w * 5 < ref * 4 || h < MIN_BAR_H || h > MAX_BAR_H) return;
        int x0 = Math.max(x, clipX), y0 = Math.max(y, clipY);
        int x1 = Math.min(x + w, clipX + clipW), y1 = Math.min(y + h, clipY + clipH);
        if (x1 <= x0 || y1 <= y0) return;
        candidate = new int[] { x0, y0, x1 - x0, y1 - y0 };
    }

    /** End of a painted frame. Returns the key to send now, or NONE. */
    public int frameEnd() {
        int[] prev = bar;
        bar = candidate;
        candidate = null;
        stableFrames = bar != null && Arrays.equals(prev, bar) ? stableFrames + 1 : 0;
        if (!seeking) return NONE;
        if (bar == null) {
            stop();
            return NONE;
        }
        framesSinceStep++;
        if (stableFrames < STABLE_FRAMES || framesSinceStep < SETTLE_FRAMES) return NONE;

        if (stepFrom != null) {
            int moved = bar[1] - stepFrom[1];
            if (moved != 0 && (dir == DOWN) != (moved > 0)) { // jumped the other way: the list wrapped
                stop();
                return NONE;
            }
            int dist = Math.abs(moved);
            if (!pitchLearned && dist * 10 >= bar[3] * 6 && dist * 10 <= bar[3] * 16) { // a plain one-row move
                pitch = dist;
                pitchLearned = true;
            }
            // The selection moved one row; whatever the bar did not move, the list scrolled under it,
            // taking the clicked row along.
            int scrolled = Math.max(0, pitch - dist);
            targetY += dir == DOWN ? -scrolled : scrolled;
            stepFrom = null;
        }

        if (contains(bar, targetY)) {
            stop();
            return FIRE;
        }
        int want = targetY < bar[1] ? UP : DOWN;
        if ((steps > 0 && want != dir) || steps >= MAX_STEPS) { // passed it (between rows) or too far
            stop();
            return NONE;
        }
        dir = want;
        steps++;
        stepFrom = bar;
        framesSinceStep = 0;
        return dir;
    }

    /** A left-button press and release, in canvas pixels. Returns the key to send now, or NONE. */
    public int click(int pressX, int pressY, int releaseX, int releaseY, int softH, long nowMs) {
        if (Math.abs(releaseX - pressX) > CLICK_SLOP || Math.abs(releaseY - pressY) > CLICK_SLOP) return NONE;
        if (nowMs - lastClick < DOUBLE_MS) return NONE;
        lastClick = nowMs;
        cancel();
        if (releaseY >= height - softH) {
            if (releaseX < width / 3) return LEFT_SOFT;
            if (releaseX >= width - width / 3) return RIGHT_SOFT;
            return FIRE;
        }
        if (bar == null) return NONE;
        if (contains(bar, releaseY)) return FIRE;
        seeking = true;
        targetY = releaseY;
        steps = 0;
        dir = 0;
        pitch = bar[3];
        pitchLearned = false;
        stepFrom = null;
        framesSinceStep = SETTLE_FRAMES;
        return NONE;
    }

    /** Right button: the right soft key (back / exit). */
    public int rightClick() {
        cancel();
        return RIGHT_SOFT;
    }

    /** Mouse wheel: {@code dir} > 0 down, < 0 up. At most one key per {@link #WHEEL_MS}. */
    public int wheel(int dir, long nowMs) {
        if (dir == 0 || nowMs - lastWheel < WHEEL_MS) return NONE;
        lastWheel = nowMs;
        cancel();
        return dir > 0 ? DOWN : UP;
    }

    /** A real key was pressed: the player took over. */
    public void realKey() {
        cancel();
    }

    /** Pointer left the game, window blurred, tab hidden. */
    public void cancel() {
        stop();
    }


    public boolean seeking() {
        return seeking;
    }

    /** The current selection bar {x, y, w, h}, or null. */
    int[] bar() {
        return bar;
    }

    private void stop() {
        seeking = false;
        stepFrom = null;
    }

    private static boolean contains(int[] r, int y) {
        return y >= r[1] && y < r[1] + r[3];
    }
}
