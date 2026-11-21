package web;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordStoreCodecTest {

    @Test
    void storageKeyUsesDocumentedPrefix() {
        // data-model.md › Browser storage: mobiarmy.rms.<storeName>
        assertEquals("mobiarmy.rms.vtTLIP", RecordStoreCodec.storageKey("vtTLIP"));
    }

    @Test
    void roundTripsRecordsIncludingEmptyAndBinary() {
        List<byte[]> records = new ArrayList<>();
        records.add(new byte[] {1, 2, 3});
        records.add(new byte[0]);
        records.add(new byte[] {(byte) 0xFF, 0, (byte) 0x80, 127, -1});
        records.add("Bắn x2".getBytes(java.nio.charset.StandardCharsets.UTF_8));

        List<byte[]> back = RecordStoreCodec.decode(RecordStoreCodec.encode(records));

        assertEquals(records.size(), back.size());
        for (int i = 0; i < records.size(); i++) {
            assertArrayEquals(records.get(i), back.get(i), "record " + (i + 1));
        }
    }

    @Test
    void deletedRecordsStayDeletedAndKeepIdsStable() {
        List<byte[]> records = new ArrayList<>();
        records.add(new byte[] {9});
        records.add(null); // record id 2 deleted
        records.add(new byte[] {7});

        List<byte[]> back = RecordStoreCodec.decode(RecordStoreCodec.encode(records));

        assertEquals(3, back.size(), "ids are 1-based list positions, so deleted slots are kept");
        assertArrayEquals(new byte[] {9}, back.get(0));
        assertNull(back.get(1));
        assertArrayEquals(new byte[] {7}, back.get(2));
    }

    @Test
    void emptyOrMissingStorageMeansNoRecords() {
        assertTrue(RecordStoreCodec.decode(null).isEmpty());
        assertTrue(RecordStoreCodec.decode("").isEmpty());
        assertTrue(RecordStoreCodec.decode(RecordStoreCodec.encode(new ArrayList<>())).isEmpty());
    }

    @Test
    void base64MatchesJdk() {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++) data[i] = (byte) i;
        for (int len = 0; len <= 10; len++) {
            byte[] part = java.util.Arrays.copyOf(data, len);
            String mine = RecordStoreCodec.base64Encode(part);
            assertEquals(java.util.Base64.getEncoder().encodeToString(part), mine);
            assertArrayEquals(part, RecordStoreCodec.base64Decode(mine));
        }
    }
}
