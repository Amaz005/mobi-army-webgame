package web;

import org.teavm.jso.JSBody;

/**
 * Portal auto-login (specs/004-game-portal research D15). Before the game starts, the page shell asks the
 * portal for a one-time game ticket and leaves it in {@code window.mobiarmyAutoLogin}. The login screen,
 * patched at build time by {@code web.build.AutoLoginPatch}, uses it once: it skips the intro, fills in
 * the name and ticket, and presses "Đăng nhập". The server accepts the ticket in place of the password.
 */
public final class AutoLogin {
    /** The client remembers the last password in this record store. */
    static final String PASSWORD_STORE = "caropass";

    private static boolean loaded;
    private static boolean used;
    private static String user;
    private static String ticket;

    private AutoLogin() {}

    @JSBody(script = "var $t = window.mobiarmyAutoLogin; return $t && $t.username ? String($t.username) : null;")
    private static native String jsUser();

    @JSBody(script = "var $t = window.mobiarmyAutoLogin; return $t && $t.ticket ? String($t.ticket) : null;")
    private static native String jsTicket();

    @JSBody(script = "window.mobiarmyAutoLogin = null;")
    private static native void jsClear();

    private static void load() {
        if (loaded) return;
        loaded = true;
        user = jsUser();
        ticket = jsTicket();
        jsClear();
    }

    /** True while a ticket is waiting to be used. Called every frame by the patched login screen. */
    public static boolean pending() {
        load();
        return !used && user != null && ticket != null;
    }

    public static String user() {
        return user;
    }

    public static String ticket() {
        return ticket;
    }

    public static void consume() {
        used = true;
    }

    /** True when the game tries to remember the ticket as the password (it is single-use, so don't). */
    public static boolean isTicket(String store, byte[] data) {
        return isTicket(store, data, ticket);
    }

    static boolean isTicket(String store, byte[] data, String ticket) {
        if (!PASSWORD_STORE.equals(store) || ticket == null || data == null || data.length != ticket.length()) return false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != (byte) ticket.charAt(i)) return false;
        }
        return true;
    }
}
