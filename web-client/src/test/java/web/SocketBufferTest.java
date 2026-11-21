package web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SocketBufferTest {

    @Test
    void readsChunksBackInOrderByteByByte() {
        SocketBuffer b = new SocketBuffer();
        b.push(new byte[] {1, 2});
        b.push(new byte[] {3});
        b.push(new byte[] {(byte) 0xFF, 5});

        assertEquals(5, b.available());
        assertEquals(1, b.read());
        assertEquals(2, b.read());
        assertEquals(3, b.read());
        assertEquals(255, b.read(), "bytes are returned unsigned, like InputStream.read()");
        assertEquals(5, b.read());
        assertEquals(0, b.available());
    }

    @Test
    void bulkReadSpansChunkBoundaries() {
        SocketBuffer b = new SocketBuffer();
        b.push(new byte[] {10, 11, 12});
        b.push(new byte[] {13, 14});

        byte[] out = new byte[4];
        assertEquals(4, b.read(out, 0, 4));
        assertArrayEquals(new byte[] {10, 11, 12, 13}, out);

        byte[] rest = new byte[10];
        assertEquals(1, b.read(rest, 2, 8), "returns only what is buffered");
        assertEquals(14, rest[2]);
    }

    @Test
    void emptyButOpenReturnsZeroFromBulkReadSoCallerWaits() {
        SocketBuffer b = new SocketBuffer();
        assertFalse(b.isClosed());
        assertEquals(0, b.read(new byte[3], 0, 3));
        assertEquals(SocketBuffer.WOULD_BLOCK, b.read());
    }

    @Test
    void endOfStreamOnlyAfterCloseAndDrain() {
        SocketBuffer b = new SocketBuffer();
        b.push(new byte[] {42});
        b.close();

        assertTrue(b.isClosed());
        assertEquals(42, b.read(), "buffered data is still delivered after close");
        assertEquals(-1, b.read());
        assertEquals(-1, b.read(new byte[2], 0, 2));
    }

    @Test
    void ignoresEmptyChunksAndPushAfterClose() {
        SocketBuffer b = new SocketBuffer();
        b.push(new byte[0]);
        assertEquals(0, b.available());
        b.close();
        b.push(new byte[] {1});
        assertEquals(0, b.available());
    }
}
