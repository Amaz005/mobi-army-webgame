package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;
import org.teavm.jso.JSObject;
import web.EventQueue;
import web.Js;
import web.KeyMap;

/** MIDP Display: one screen (the page's {@code <canvas id="screen">}). */
public class Display {
    static int screenWidth = 240, screenHeight = 320;

    private static Display instance;
    private static JSObject screenCanvas;
    private static Graphics screenGraphics;
    /** See {@link KeyMap#needsQwertyPrime}. Only the physical keyboard primes; the on-screen keypad keeps multi-tap. */
    private static boolean qwertyPrimed;

    private Displayable current;

    private Display() {}

    /** Called once by the web launcher before the MIDlet starts. */
    public static void initScreen(String canvasId, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        screenCanvas = Js.byId(canvasId);
        Js.resizeCanvas(screenCanvas, width, height);
        screenGraphics = new Graphics(Js.context2d(screenCanvas), width, height);
        EventQueue.start();

        Js.listenKeys((type, key, code, repeat, shortcut) -> {
            int midp = KeyMap.fromBrowser(key, code, shortcut);
            if (midp == KeyMap.PASS_THROUGH) return false;
            int kind = "keyup".equals(type) ? 1 : repeat ? 2 : 0;
            if (kind == 0) {
                if (KeyMap.isLetter(midp)) {
                    qwertyPrimed = true;
                } else if (KeyMap.needsQwertyPrime(midp, qwertyPrimed)) {
                    // Switch the client's text fields to typed-character mode without leaving a trace.
                    qwertyPrimed = true;
                    EventQueue.post(() -> {
                        Display d = getDisplay(null);
                        d.key(0, 'A');
                        d.key(1, 'A');
                        d.key(0, KeyMap.BACKSPACE);
                        d.key(1, KeyMap.BACKSPACE);
                    });
                }
            }
            EventQueue.post(() -> getDisplay(null).key(kind, midp));
            return true;
        });
        Js.exposeKeypad((type, key, code, repeat, shortcut) -> {
            int midp = Integer.parseInt(code.substring(5));
            int kind = "up".equals(type) ? 1 : "repeat".equals(type) ? 2 : 0;
            EventQueue.post(() -> getDisplay(null).key(kind, midp));
            return true;
        });
        Js.listenPointer(screenCanvas, (type, x, y) -> {
            int kind = "pressed".equals(type) ? 0 : "released".equals(type) ? 1 : 2;
            EventQueue.post(() -> getDisplay(null).pointer(kind, x, y));
        });
    }

    static Graphics screenGraphics() {
        return screenGraphics;
    }

    public static Display getDisplay(MIDlet m) {
        if (instance == null) instance = new Display();
        return instance;
    }

    public void setCurrent(Displayable next) {
        if (next == current) return;
        Displayable old = current;
        current = next;
        if (old != null) old.hideNotifyInternal();
        if (next != null) next.showNotifyInternal();
    }

    public Displayable getCurrent() {
        return current;
    }

    public void callSerially(Runnable r) {
        EventQueue.post(r);
    }

    public boolean vibrate(int duration) {
        return false;
    }

    public boolean flashBacklight(int duration) {
        return false;
    }

    public boolean isColor() {
        return true;
    }

    public int numColors() {
        return 1 << 24;
    }

    private void key(int kind, int code) {
        if (current instanceof Canvas) ((Canvas) current).dispatchKey(kind, code);
    }

    private void pointer(int kind, int x, int y) {
        if (current instanceof Canvas) ((Canvas) current).dispatchPointer(kind, x, y);
    }
}
