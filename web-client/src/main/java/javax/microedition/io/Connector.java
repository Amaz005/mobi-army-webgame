package javax.microedition.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * MIDP Connector for the browser. Every {@code socket://} connection goes to this site's {@code /game}
 * WebSocket relay, whatever host the game asks for (research.md R2b, FR-003/FR-005). Other schemes
 * (http:// lookups, sms://) are refused so the game never contacts outside servers (FR-006).
 */
public class Connector {
    public static final int READ = 1, WRITE = 2, READ_WRITE = 3;

    private Connector() {}

    public static Connection open(String name) throws IOException {
        return open(name, READ_WRITE, false);
    }

    public static Connection open(String name, int mode) throws IOException {
        return open(name, mode, false);
    }

    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        if (name != null && name.startsWith("socket://")) {
            return WebSocketConnection.connect();
        }
        throw new ConnectionNotFoundException("not available in the browser: " + name);
    }

    public static InputStream openInputStream(String name) throws IOException {
        return ((InputConnection) open(name, READ)).openInputStream();
    }

    public static DataInputStream openDataInputStream(String name) throws IOException {
        return new DataInputStream(openInputStream(name));
    }

    public static OutputStream openOutputStream(String name) throws IOException {
        return ((OutputConnection) open(name, WRITE)).openOutputStream();
    }

    public static DataOutputStream openDataOutputStream(String name) throws IOException {
        return new DataOutputStream(openOutputStream(name));
    }
}
