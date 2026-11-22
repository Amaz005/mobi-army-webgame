package javax.microedition.lcdui;

import web.Js;
import web.KeyMap;

/** MIDP Canvas drawn onto the page's screen canvas. */
public abstract class Canvas extends Displayable {
    public static final int UP = 1, DOWN = 6, LEFT = 2, RIGHT = 5, FIRE = 8;
    public static final int GAME_A = 9, GAME_B = 10, GAME_C = 11, GAME_D = 12;
    public static final int KEY_NUM0 = 48, KEY_NUM1 = 49, KEY_NUM2 = 50, KEY_NUM3 = 51, KEY_NUM4 = 52,
            KEY_NUM5 = 53, KEY_NUM6 = 54, KEY_NUM7 = 55, KEY_NUM8 = 56, KEY_NUM9 = 57;
    public static final int KEY_STAR = 42, KEY_POUND = 35;

    private boolean repaintPending;
    private boolean frameScheduled;

    protected Canvas() {}

    protected abstract void paint(Graphics g);

    protected void keyPressed(int keyCode) {}
    protected void keyReleased(int keyCode) {}
    protected void keyRepeated(int keyCode) {}
    protected void pointerPressed(int x, int y) {}
    protected void pointerReleased(int x, int y) {}
    protected void pointerDragged(int x, int y) {}
    protected void showNotify() {}
    protected void hideNotify() {}
    protected void sizeChanged(int w, int h) {}

    public int getGameAction(int keyCode) {
        return KeyMap.gameAction(keyCode);
    }

    public int getKeyCode(int gameAction) {
        return KeyMap.keyCode(gameAction);
    }

    public String getKeyName(int keyCode) {
        return String.valueOf(keyCode);
    }

    public boolean hasPointerEvents() { return true; }
    public boolean hasPointerMotionEvents() { return true; }
    public boolean hasRepeatEvents() { return true; }
    public boolean isDoubleBuffered() { return true; }

    public void setFullScreenMode(boolean mode) {}

    public final void repaint() {
        repaintPending = true;
        if (!frameScheduled && shown) {
            frameScheduled = true;
            Js.onAnimationFrame(() -> web.EventQueue.post(this::frame));
        }
    }

    public final void repaint(int x, int y, int w, int h) {
        repaint();
    }

    /** Paints now on the calling thread if a repaint is pending (the game loop relies on this). */
    public final void serviceRepaints() {
        if (repaintPending) paintNow();
    }

    private void frame() {
        frameScheduled = false;
        if (repaintPending) paintNow();
    }

    private void paintNow() {
        repaintPending = false;
        if (!shown) return;
        Graphics g = Display.screenGraphics();
        g.reset();
        paint(g);
        // Spec 005: the mouse translator may step through a list now that this frame's bar is known.
        if (g.bars != null) Display.tap(g.bars.frameEnd());
    }

    @Override
    void showNotifyInternal() {
        shown = true;
        showNotify();
        repaint();
    }

    @Override
    void hideNotifyInternal() {
        shown = false;
        hideNotify();
    }

    // Called by Display on the event thread.
    final void dispatchKey(int type, int code) {
        if (type == 0) keyPressed(code);
        else if (type == 1) keyReleased(code);
        else keyRepeated(code);
    }

    final void dispatchPointer(int type, int x, int y) {
        if (type == 0) pointerPressed(x, y);
        else if (type == 1) pointerReleased(x, y);
        else pointerDragged(x, y);
    }
}
