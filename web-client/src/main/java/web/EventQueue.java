package web;

import java.util.ArrayDeque;

/**
 * Runs browser events (keys, pointer, scheduled repaints) on one Java thread, in order. Browser
 * callbacks only enqueue; the game code then runs in a normal Java thread where it may block.
 */
public final class EventQueue {
    private static final ArrayDeque<Runnable> queue = new ArrayDeque<>();
    private static final Signal signal = new Signal();
    private static boolean started;

    private EventQueue() {}

    /** Safe to call from JS callbacks. */
    public static void post(Runnable r) {
        queue.addLast(r);
        signal.signal();
    }

    public static synchronized void start() {
        if (started) return;
        started = true;
        Thread t = new Thread(EventQueue::loop, "midp-events");
        t.start();
    }

    private static void loop() {
        while (true) {
            Runnable r = queue.pollFirst();
            if (r == null) {
                signal.await();
                continue;
            }
            try {
                r.run();
            } catch (Throwable e) {
                Js.error("Game event failed: " + e);
                e.printStackTrace();
            }
        }
    }
}
