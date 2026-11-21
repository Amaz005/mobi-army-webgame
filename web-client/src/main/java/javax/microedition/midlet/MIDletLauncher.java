package javax.microedition.midlet;

/** Web-only helper: startApp() is protected, so the launcher calls it from inside this package. */
public final class MIDletLauncher {
    private MIDletLauncher() {}

    public static void start(MIDlet midlet) throws MIDletStateChangeException {
        midlet.startApp();
    }
}
