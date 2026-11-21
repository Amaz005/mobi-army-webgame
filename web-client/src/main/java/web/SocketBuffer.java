package web;

import java.util.ArrayDeque;

/**
 * Bytes received from the WebSocket, waiting to be read by the game's (blocking) input stream.
 * Pure logic: waiting for more data is done by the caller (see javax.microedition.io.WebSocketConnection).
 */
public final class SocketBuffer {
    /** {@link #read()} result when no byte is buffered yet but the connection is still open. */
    public static final int WOULD_BLOCK = -2;

    private final ArrayDeque<byte[]> chunks = new ArrayDeque<>();
    private int headPos;
    private int available;
    private boolean closed;

    public void push(byte[] chunk) {
        if (closed || chunk == null || chunk.length == 0) return;
        chunks.addLast(chunk);
        available += chunk.length;
    }

    public void close() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public int available() {
        return available;
    }

    /** Next byte (0–255), {@code -1} at end of stream, or {@link #WOULD_BLOCK}. */
    public int read() {
        if (available == 0) return closed ? -1 : WOULD_BLOCK;
        byte[] head = chunks.peekFirst();
        int b = head[headPos++] & 0xFF;
        available--;
        if (headPos == head.length) {
            chunks.removeFirst();
            headPos = 0;
        }
        return b;
    }

    /** Copies up to {@code len} buffered bytes; returns the count, {@code 0} if empty-but-open, {@code -1} at end. */
    public int read(byte[] dst, int off, int len) {
        if (len == 0) return 0;
        if (available == 0) return closed ? -1 : 0;
        int done = 0;
        while (done < len && available > 0) {
            byte[] head = chunks.peekFirst();
            int n = Math.min(len - done, head.length - headPos);
            System.arraycopy(head, headPos, dst, off + done, n);
            headPos += n;
            done += n;
            available -= n;
            if (headPos == head.length) {
                chunks.removeFirst();
                headPos = 0;
            }
        }
        return done;
    }
}
