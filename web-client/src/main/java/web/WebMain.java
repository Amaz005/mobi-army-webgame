package web;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDletLauncher;

/** Browser entry point: points the existing client at this site's server and starts it (research.md R2). */
public final class WebMain {
    private static final int GAME_PORT = 19149;

    private WebMain() {}

    public static void main(String[] args) {
        // Phones always define these; the client calls .indexOf(...) on the platform name.
        System.setProperty("microedition.platform", "MobiArmyWeb");
        System.setProperty("microedition.configuration", "CLDC-1.1");
        System.setProperty("microedition.profiles", "MIDP-2.0");

        int w = intParam("w", 240), h = intParam("h", 320);
        Display.initScreen("screen", w, h);
        // Spec 005: the game's soft-key bar is ds.ab px high (font height + 6); clicks there are soft keys.
        Display.setSoftBarHeight(() -> coreLG.ds.ab);

        // The client connects to TerrainMidlet.d:c. The socket layer always uses this site's relay anyway.
        coreLG.TerrainMidlet.d = Js.host();
        coreLG.TerrainMidlet.c = GAME_PORT;

        Js.status("");
        try {
            MIDletLauncher.start(new coreLG.TerrainMidlet());
        } catch (Throwable e) {
            Js.status("The game failed to start: " + e);
            e.printStackTrace();
        }
    }

    private static int intParam(String name, int def) {
        String v = Js.queryParam(name);
        if (v == null) return def;
        try {
            int n = Integer.parseInt(v);
            return n >= 128 && n <= 1024 ? n : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
