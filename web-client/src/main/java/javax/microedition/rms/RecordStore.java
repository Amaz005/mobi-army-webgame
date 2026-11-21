package javax.microedition.rms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import web.Js;
import web.RecordStoreCodec;

/** MIDP RecordStore persisted in localStorage (data-model.md › Browser storage). */
public class RecordStore {
    private static final Map<String, RecordStore> OPEN = new HashMap<>();

    private final String name;
    private final List<byte[]> records;
    private int openCount;

    private RecordStore(String name, List<byte[]> records) {
        this.name = name;
        this.records = records;
    }

    public static RecordStore openRecordStore(String name, boolean createIfNecessary) throws RecordStoreException {
        RecordStore rs = OPEN.get(name);
        if (rs == null) {
            String stored = Js.storageGet(RecordStoreCodec.storageKey(name));
            if (stored == null && !createIfNecessary) throw new RecordStoreNotFoundException(name);
            rs = new RecordStore(name, RecordStoreCodec.decode(stored));
            if (stored == null) rs.save();
            OPEN.put(name, rs);
        }
        rs.openCount++;
        return rs;
    }

    public static void deleteRecordStore(String name) throws RecordStoreException {
        String key = RecordStoreCodec.storageKey(name);
        if (Js.storageGet(key) == null) throw new RecordStoreNotFoundException(name);
        OPEN.remove(name);
        Js.storageRemove(key);
    }

    public void closeRecordStore() throws RecordStoreException {
        if (openCount <= 0) throw new RecordStoreNotOpenException(name);
        if (--openCount == 0) OPEN.remove(name);
    }

    public String getName() {
        return name;
    }

    public int getNumRecords() throws RecordStoreNotOpenException {
        checkOpen();
        int n = 0;
        for (byte[] r : records) if (r != null) n++;
        return n;
    }

    public int getNextRecordID() throws RecordStoreException {
        checkOpen();
        return records.size() + 1;
    }

    public int addRecord(byte[] data, int offset, int numBytes) throws RecordStoreException {
        checkOpen();
        records.add(copy(data, offset, numBytes));
        save();
        return records.size();
    }

    public void setRecord(int recordId, byte[] newData, int offset, int numBytes) throws RecordStoreException {
        checkId(recordId);
        records.set(recordId - 1, copy(newData, offset, numBytes));
        save();
    }

    public byte[] getRecord(int recordId) throws RecordStoreException {
        checkId(recordId);
        byte[] r = records.get(recordId - 1);
        return r.length == 0 ? null : r.clone();
    }

    public int getRecord(int recordId, byte[] buffer, int offset) throws RecordStoreException {
        checkId(recordId);
        byte[] r = records.get(recordId - 1);
        System.arraycopy(r, 0, buffer, offset, r.length);
        return r.length;
    }

    public int getRecordSize(int recordId) throws RecordStoreException {
        checkId(recordId);
        return records.get(recordId - 1).length;
    }

    public void deleteRecord(int recordId) throws RecordStoreException {
        checkId(recordId);
        records.set(recordId - 1, null);
        save();
    }

    private void checkOpen() throws RecordStoreNotOpenException {
        if (openCount <= 0) throw new RecordStoreNotOpenException(name);
    }

    private void checkId(int recordId) throws RecordStoreException {
        checkOpen();
        if (recordId < 1 || recordId > records.size() || records.get(recordId - 1) == null) {
            throw new InvalidRecordIDException(String.valueOf(recordId));
        }
    }

    private void save() {
        Js.storageSet(RecordStoreCodec.storageKey(name), RecordStoreCodec.encode(records));
    }

    private static byte[] copy(byte[] data, int offset, int len) {
        byte[] r = new byte[len];
        if (len > 0) System.arraycopy(data, offset, r, 0, len);
        return r;
    }
}
