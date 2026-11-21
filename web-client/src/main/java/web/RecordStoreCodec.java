package web;

import java.util.ArrayList;
import java.util.List;

/**
 * Serializes a RecordStore for localStorage (data-model.md › Browser storage).
 * Format: records joined by ',', each base64; a deleted record is '-'. Record id = list index + 1.
 */
public final class RecordStoreCodec {
    private static final String PREFIX = "mobiarmy.rms.";
    private static final char[] ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    private RecordStoreCodec() {}

    public static String storageKey(String storeName) {
        return PREFIX + storeName;
    }

    public static String encode(List<byte[]> records) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < records.size(); i++) {
            if (i > 0) sb.append(',');
            byte[] r = records.get(i);
            sb.append(r == null ? "-" : base64Encode(r));
        }
        // An empty list must still differ from "one empty record", so mark it explicitly.
        return records.isEmpty() ? "" : "v1:" + sb;
    }

    public static List<byte[]> decode(String stored) {
        List<byte[]> out = new ArrayList<>();
        if (stored == null || !stored.startsWith("v1:")) return out;
        String body = stored.substring(3);
        int start = 0;
        while (true) {
            int comma = body.indexOf(',', start);
            String part = comma < 0 ? body.substring(start) : body.substring(start, comma);
            out.add("-".equals(part) ? null : base64Decode(part));
            if (comma < 0) break;
            start = comma + 1;
        }
        return out;
    }

    static String base64Encode(byte[] data) {
        StringBuilder sb = new StringBuilder((data.length + 2) / 3 * 4);
        for (int i = 0; i < data.length; i += 3) {
            int b0 = data[i] & 0xFF;
            int b1 = i + 1 < data.length ? data[i + 1] & 0xFF : 0;
            int b2 = i + 2 < data.length ? data[i + 2] & 0xFF : 0;
            sb.append(ALPHABET[b0 >> 2]);
            sb.append(ALPHABET[((b0 & 3) << 4) | (b1 >> 4)]);
            sb.append(i + 1 < data.length ? ALPHABET[((b1 & 15) << 2) | (b2 >> 6)] : '=');
            sb.append(i + 2 < data.length ? ALPHABET[b2 & 63] : '=');
        }
        return sb.toString();
    }

    static byte[] base64Decode(String s) {
        int pad = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
        byte[] out = new byte[s.length() / 4 * 3 - pad];
        int o = 0;
        for (int i = 0; i < s.length(); i += 4) {
            int v = (val(s.charAt(i)) << 18) | (val(s.charAt(i + 1)) << 12)
                    | (val(s.charAt(i + 2)) << 6) | val(s.charAt(i + 3));
            if (o < out.length) out[o++] = (byte) (v >> 16);
            if (o < out.length) out[o++] = (byte) (v >> 8);
            if (o < out.length) out[o++] = (byte) v;
        }
        return out;
    }

    private static int val(char c) {
        if (c >= 'A' && c <= 'Z') return c - 'A';
        if (c >= 'a' && c <= 'z') return c - 'a' + 26;
        if (c >= '0' && c <= '9') return c - '0' + 52;
        if (c == '+') return 62;
        if (c == '/') return 63;
        return 0; // '='
    }
}
