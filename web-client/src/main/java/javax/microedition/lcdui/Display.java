package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;
import org.teavm.jso.JSObject;
import web.EventQueue;
import web.Js;
import web.KeyMap;
import web.MouseControl;

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
        mouse = new MouseControl(width, height);
        screenGraphics.bars = mouse;
        EventQueue.start();

        Js.listenKeys((type, key, code, repeat, shortcut) -> {
            int midp = KeyMap.fromBrowser(key, code, shortcut);
            if (midp == KeyMap.PASS_THROUGH) return false;
            int kind = "keyup".equals(type) ? 1 : repeat ? 2 : 0;
            track(kind, midp);
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
            track(kind, midp);
            EventQueue.post(() -> getDisplay(null).key(kind, midp));
            return true;
        });
        // JS callbacks only enqueue (as for keys): the translation runs on the Java event thread, where it may
        // touch game classes (ds.ab) without clashing with the running game thread.
        Js.listenPointer(screenCanvas, (type, x, y) -> EventQueue.post(() -> pointerEvent(type, x, y)));
        Js.exposeReleaseAll(() -> EventQueue.post(Display::releaseAll));
    }

    // ---- mouse control (specs/005-mouse-control) -------------------------------------------------

    private static MouseControl mouse;
    private static int pressX, pressY;
    /** Keys currently held down from the keyboard or the on-screen pad, so they can be released on blur. */
    private static final java.util.Set<Integer> held = new java.util.HashSet<>();
    /** Height of the game's soft-key bar; set by the launcher (the client knows it). */
    private static java.util.function.IntSupplier softBar = () -> 20;

    public static void setSoftBarHeight(java.util.function.IntSupplier supplier) {
        softBar = supplier;
    }

    private static int softBarHeight() {
        try {
            return softBar.getAsInt();
        } catch (Throwable e) {
            return 20;
        }
    }

    /** On the event thread: forward to the game, and translate clicks, right clicks and the wheel. */
    private static void pointerEvent(String type, int x, int y) {
        Display d = getDisplay(null);
        switch (type) {
            case "right":
                tapNow(d, mouse.rightClick());
                return;
            case "wheel":
                tapNow(d, mouse.wheel(x, (long) Js.now()));
                return;
            case "leave":
                mouse.cancel();
                return;
            case "blur":
                releaseAll();
                return;
            default:
                break;
        }
        int kind = "pressed".equals(type) ? 0 : "released".equals(type) ? 1 : 2;
        // The game keeps getting the pointer (its text keyboard uses it); clicks are also translated.
        d.pointer(kind, x, y);
        if (kind == 0) {
            pressX = x;
            pressY = y;
        } else if (kind == 1) {
            tapNow(d, mouse.click(pressX, pressY, x, y, softBarHeight(), (long) Js.now()));
        }
    }

    private static void tapNow(Display d, int code) {
        if (code == MouseControl.NONE) return;
        d.key(0, code);
        d.key(1, code);
    }

    private static void track(int kind, int code) {
        if (kind == 0) {
            held.add(code);
            mouse.realKey();
        } else if (kind == 1) {
            held.remove(code);
        }
    }

    /** Sends one key press and release, as if a phone key were tapped (no-op for {@link MouseControl#NONE}). */
    static void tap(int code) {
        // Called from paint, on whichever Java thread is painting. Delivered directly: posting to the event
        // queue from a running Java thread trips TeaVM ("Another thread is running").
        tapNow(getDisplay(null), code);
    }

    /** Window blurred, tab hidden or pad hidden: nothing may stay held (FR-008). */
    private static void releaseAll() {
        mouse.cancel();
        Display d = getDisplay(null);
        for (Integer code : held.toArray(new Integer[0])) d.key(1, code); // on the event thread already
        held.clear();
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
