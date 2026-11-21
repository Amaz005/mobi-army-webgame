package javax.microedition.io;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int8Array;
import web.Js;
import web.Signal;
import web.SocketBuffer;

/** A MIDP SocketConnection carried over a binary WebSocket to the relay (contracts/web-endpoints.md › /game). */
final class WebSocketConnection implements SocketConnection {
    private final JSObject ws;
    private final SocketBuffer incoming = new SocketBuffer();
    private final Signal dataSignal = new Signal();
    private final Signal openSignal = new Signal();
    private boolean open;
    private boolean failed;
    private boolean closedByGame;
    private boolean gotData;
    private InputStream in;
    private OutputStream out;

    private static final String LOST =
            "Lost connection to the game server. It may be restarting; try again in a moment.";

    private WebSocketConnection(String url) {
        ws = create(url,
                () -> {
                    open = true;
                    openSignal.signal();
                },
                data -> {
                    if (!gotData) {
                        gotData = true;
                        Js.status(""); // the server answered: clear any earlier connection message
                    }
                    incoming.push(toBytes(data));
                    dataSignal.signal();
                },
                () -> {
                    failed = !open;
                    // The relay accepts the WebSocket even when the game server is down, then drops it,
                    // so a close the game didn't ask for is the "can't reach the server" case (spec edge case).
                    if (!closedByGame) Js.status(LOST);
                    incoming.close();
                    openSignal.signal();
                    dataSignal.signal();
                });
    }

    static WebSocketConnection connect() throws IOException {
        WebSocketConnection c = new WebSocketConnection(Js.gameSocketUrl());
        c.openSignal.await();
        if (!c.open || c.failed) {
            throw new IOException("Can't reach the game server");
        }
        return c;
    }

    @Override
    public InputStream openInputStream() {
        if (in == null) in = new SocketIn();
        return in;
    }

    @Override
    public DataInputStream openDataInputStream() {
        return new DataInputStream(openInputStream());
    }

    @Override
    public OutputStream openOutputStream() {
        if (out == null) out = new SocketOut();
        return out;
    }

    @Override
    public DataOutputStream openDataOutputStream() {
        return new DataOutputStream(openOutputStream());
    }

    @Override
    public void close() {
        closedByGame = true;
        incoming.close();
        dataSignal.signal();
        close(ws);
    }

    @Override public void setSocketOption(byte option, int value) {}
    @Override public int getSocketOption(byte option) { return 0; }
    @Override public String getLocalAddress() { return "0.0.0.0"; }
    @Override public int getLocalPort() { return 0; }
    @Override public String getAddress() { return Js.host(); }
    @Override public int getPort() { return 0; }

    private final class SocketIn extends InputStream {
        @Override
        public int read() throws IOException {
            while (true) {
                int b = incoming.read();
                if (b != SocketBuffer.WOULD_BLOCK) return b;
                dataSignal.await();
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (len == 0) return 0;
            while (true) {
                int n = incoming.read(b, off, len);
                if (n != 0) return n;
                dataSignal.await();
            }
        }

        @Override
        public int available() {
            return incoming.available();
        }
    }

    /** Buffers writes and sends one WebSocket frame per flush() (the client flushes after each message). */
    private final class SocketOut extends OutputStream {
        private final ByteArrayOutputStream pending = new ByteArrayOutputStream();

        @Override
        public void write(int b) throws IOException {
            check();
            pending.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            check();
            pending.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            check();
            if (pending.size() == 0) return;
            send(ws, Int8Array.copyFromJavaArray(pending.toByteArray()));
            pending.reset();
        }

        @Override
        public void close() throws IOException {
            flush();
        }

        private void check() throws IOException {
            if (incoming.isClosed()) throw new IOException("connection closed");
        }
    }

    private static byte[] toBytes(JSObject arrayBuffer) {
        return toInt8(arrayBuffer).toJavaArray();
    }

    @JSBody(params = "buf", script = "return new Int8Array(buf);")
    private static native Int8Array toInt8(JSObject buf);

    @JSBody(params = {"url", "onOpen", "onData", "onClose"}, script =
            "var $ws = new WebSocket(url, ['binary']); $ws.binaryType = 'arraybuffer';"
            + "$ws.onopen = function() { onOpen(); };"
            + "$ws.onmessage = function($e) { if ($e.data instanceof ArrayBuffer) onData($e.data); };"
            + "$ws.onclose = function() { onClose(); };"
            + "$ws.onerror = function() {};"
            + "return $ws;")
    private static native JSObject create(String url, Js.Callback onOpen, Js.DataCallback onData, Js.Callback onClose);

    @JSBody(params = {"ws", "bytes"}, script = "if (ws.readyState === 1) ws.send(bytes);")
    private static native void send(JSObject ws, Int8Array bytes);

    @JSBody(params = "ws", script = "try { ws.close(); } catch ($e) {}")
    private static native void close(JSObject ws);
}
