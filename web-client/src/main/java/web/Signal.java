package web;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;

/**
 * Lets one Java (green) thread sleep until a browser callback wakes it. Browser callbacks must never
 * block or take Java monitors, so they only call {@link #signal()}.
 */
public final class Signal {
    private AsyncCallback<Void> waiter;
    private boolean pending;

    /** Blocks the calling Java thread until {@link #signal()} (returns at once if a signal is pending). */
    public void await() {
        if (pending) {
            pending = false;
            return;
        }
        suspend(this);
    }

    /** Wakes the waiting thread, or remembers the wake-up if nobody is waiting yet. Safe from JS callbacks. */
    public void signal() {
        AsyncCallback<Void> w = waiter;
        if (w != null) {
            waiter = null;
            w.complete(null);
        } else {
            pending = true;
        }
    }

    @Async
    private static native Void suspend(Signal s);

    private static void suspend(Signal s, AsyncCallback<Void> callback) {
        s.waiter = callback;
    }
}
