package javax.microedition.midlet;

import javax.microedition.io.ConnectionNotFoundException;
import web.Js;

/** MIDP MIDlet lifecycle for the browser. */
public abstract class MIDlet {
    protected MIDlet() {}

    protected abstract void startApp() throws MIDletStateChangeException;

    protected abstract void pauseApp();

    protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;

    /** The game asked to exit: there is nothing to return to in a browser tab, so say how to restart. */
    public final void notifyDestroyed() {
        Js.status("Game closed. Reload the page to play again.");
    }

    public final void notifyPaused() {}

    public final void resumeRequest() {}

    public final String getAppProperty(String key) {
        return null;
    }

    /** Phone calls, SMS and opening other apps aren't available in the browser (research.md R9). */
    public final boolean platformRequest(String url) throws ConnectionNotFoundException {
        Js.status("Not supported in the browser: " + url);
        return false;
    }

    public final int checkPermission(String permission) {
        return 1;
    }
}
