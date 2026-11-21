package web;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Minimal synchronous PNG decoder → ARGB pixels. MIDP's Image.createImage(byte[]) must return at once,
 * but browsers only decode images asynchronously, so the web layer decodes PNGs itself.
 * Supports every colour type and bit depth, palette/tRNS transparency, all filters; not Adam7 interlacing.
 */
public final class PngDecoder {

    public static final class Result {
        public final int width, height;
        public final int[] argb;

        Result(int width, int height, int[] argb) {
            this.width = width;
            this.height = height;
            this.argb = argb;
        }
    }

    private PngDecoder() {}

    public static Result decode(byte[] data, int offset, int length) {
        int end = offset + length;
        if (length < 8 || (data[offset] & 0xFF) != 0x89 || data[offset + 1] != 'P' || data[offset + 2] != 'N'
                || data[offset + 3] != 'G') {
            throw new IllegalArgumentException("not a PNG");
        }
        int p = offset + 8;
        int width = 0, height = 0, depth = 0, colorType = 0, interlace = 0;
        int[] palette = null;
        int[] trnsGray = null; // gray or rgb key, -1 when absent
        ByteArrayOutputStream idat = new ByteArrayOutputStream();

        while (p + 8 <= end) {
            int len = readInt(data, p);
            String type = new String(new char[] {(char) data[p + 4], (char) data[p + 5], (char) data[p + 6], (char) data[p + 7]});
            int body = p + 8;
            if (len < 0 || body + len > end) throw new IllegalArgumentException("truncated PNG");
            switch (type) {
                case "IHDR":
                    width = readInt(data, body);
                    height = readInt(data, body + 4);
                    depth = data[body + 8] & 0xFF;
                    colorType = data[body + 9] & 0xFF;
                    interlace = data[body + 12] & 0xFF;
                    break;
                case "PLTE":
                    palette = new int[len / 3];
                    for (int i = 0; i < palette.length; i++) {
                        palette[i] = 0xFF000000 | ((data[body + i * 3] & 0xFF) << 16)
                                | ((data[body + i * 3 + 1] & 0xFF) << 8) | (data[body + i * 3 + 2] & 0xFF);
                    }
                    break;
                case "tRNS":
                    if (colorType == 3 && palette != null) {
                        for (int i = 0; i < len && i < palette.length; i++) {
                            palette[i] = (palette[i] & 0x00FFFFFF) | ((data[body + i] & 0xFF) << 24);
                        }
                    } else if (colorType == 0 && len >= 2) {
                        trnsGray = new int[] {readShort(data, body)};
                    } else if (colorType == 2 && len >= 6) {
                        trnsGray = new int[] {readShort(data, body), readShort(data, body + 2), readShort(data, body + 4)};
                    }
                    break;
                case "IDAT":
                    idat.write(data, body, len);
                    break;
                default:
                    break;
            }
            if ("IEND".equals(type)) break;
            p = body + len + 4; // skip CRC
        }
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("PNG without IHDR");
        if (interlace != 0) throw new IllegalArgumentException("interlaced PNG not supported");

        int channels;
        switch (colorType) {
            case 0: channels = 1; break;
            case 2: channels = 3; break;
            case 3: channels = 1; break;
            case 4: channels = 2; break;
            case 6: channels = 4; break;
            default: throw new IllegalArgumentException("bad PNG colour type " + colorType);
        }
        int bitsPerPixel = channels * depth;
        int bpp = Math.max(1, bitsPerPixel / 8);
        int stride = (width * bitsPerPixel + 7) / 8;
        byte[] raw = inflate(idat.toByteArray(), (stride + 1) * height);

        int[] out = new int[width * height];
        byte[] prev = new byte[stride];
        byte[] cur = new byte[stride];
        int maxSample = (1 << depth) - 1;
        for (int y = 0; y < height; y++) {
            int rowStart = y * (stride + 1);
            int filter = raw[rowStart] & 0xFF;
            System.arraycopy(raw, rowStart + 1, cur, 0, stride);
            unfilter(filter, cur, prev, bpp);
            for (int x = 0; x < width; x++) {
                int argb;
                switch (colorType) {
                    case 0: {
                        int g = sample(cur, x, depth);
                        int a = trnsGray != null && g == trnsGray[0] ? 0 : 0xFF;
                        int v = depth == 16 ? g >> 8 : g * 255 / maxSample;
                        argb = (a << 24) | (v << 16) | (v << 8) | v;
                        break;
                    }
                    case 3: {
                        int idx = sample(cur, x, depth);
                        argb = palette != null && idx < palette.length ? palette[idx] : 0;
                        break;
                    }
                    case 2: {
                        int r, g, b, a = 0xFF;
                        if (depth == 16) {
                            int r16 = u16(cur, x * 6), g16 = u16(cur, x * 6 + 2), b16 = u16(cur, x * 6 + 4);
                            if (trnsGray != null && r16 == trnsGray[0] && g16 == trnsGray[1] && b16 == trnsGray[2]) a = 0;
                            r = r16 >> 8; g = g16 >> 8; b = b16 >> 8;
                        } else {
                            r = cur[x * 3] & 0xFF; g = cur[x * 3 + 1] & 0xFF; b = cur[x * 3 + 2] & 0xFF;
                            if (trnsGray != null && r == trnsGray[0] && g == trnsGray[1] && b == trnsGray[2]) a = 0;
                        }
                        argb = (a << 24) | (r << 16) | (g << 8) | b;
                        break;
                    }
                    case 4: {
                        int g = depth == 16 ? cur[x * 4] & 0xFF : cur[x * 2] & 0xFF;
                        int a = depth == 16 ? cur[x * 4 + 2] & 0xFF : cur[x * 2 + 1] & 0xFF;
                        argb = (a << 24) | (g << 16) | (g << 8) | g;
                        break;
                    }
                    default: { // 6
                        int o = depth == 16 ? x * 8 : x * 4;
                        int s = depth == 16 ? 2 : 1;
                        int r = cur[o] & 0xFF, g = cur[o + s] & 0xFF, b = cur[o + 2 * s] & 0xFF, a = cur[o + 3 * s] & 0xFF;
                        argb = (a << 24) | (r << 16) | (g << 8) | b;
                        break;
                    }
                }
                out[y * width + x] = argb;
            }
            byte[] t = prev; prev = cur; cur = t;
        }
        return new Result(width, height, out);
    }

    private static void unfilter(int filter, byte[] cur, byte[] prev, int bpp) {
        int n = cur.length;
        switch (filter) {
            case 0:
                break;
            case 1:
                for (int i = bpp; i < n; i++) cur[i] = (byte) (cur[i] + cur[i - bpp]);
                break;
            case 2:
                for (int i = 0; i < n; i++) cur[i] = (byte) (cur[i] + prev[i]);
                break;
            case 3:
                for (int i = 0; i < n; i++) {
                    int left = i >= bpp ? cur[i - bpp] & 0xFF : 0;
                    cur[i] = (byte) (cur[i] + ((left + (prev[i] & 0xFF)) >> 1));
                }
                break;
            case 4:
                for (int i = 0; i < n; i++) {
                    int a = i >= bpp ? cur[i - bpp] & 0xFF : 0;
                    int b = prev[i] & 0xFF;
                    int c = i >= bpp ? prev[i - bpp] & 0xFF : 0;
                    int pa = Math.abs(b - c), pb = Math.abs(a - c), pc = Math.abs(a + b - 2 * c);
                    int pred = pa <= pb && pa <= pc ? a : pb <= pc ? b : c;
                    cur[i] = (byte) (cur[i] + pred);
                }
                break;
            default:
                throw new IllegalArgumentException("bad PNG filter " + filter);
        }
    }

    private static int sample(byte[] row, int x, int depth) {
        switch (depth) {
            case 8: return row[x] & 0xFF;
            case 16: return u16(row, x * 2);
            default: {
                int perByte = 8 / depth;
                int shift = 8 - depth * (x % perByte + 1);
                return ((row[x / perByte] & 0xFF) >> shift) & ((1 << depth) - 1);
            }
        }
    }

    private static byte[] inflate(byte[] zipped, int expected) {
        Inflater inf = new Inflater();
        inf.setInput(zipped);
        byte[] out = new byte[expected];
        int n = 0;
        try {
            while (n < expected && !inf.finished()) {
                int r = inf.inflate(out, n, expected - n);
                if (r == 0 && (inf.needsInput() || inf.needsDictionary())) break;
                n += r;
            }
        } catch (DataFormatException e) {
            throw new IllegalArgumentException("corrupt PNG data");
        } finally {
            inf.end();
        }
        if (n < expected) throw new IllegalArgumentException("truncated PNG data");
        return out;
    }

    private static int u16(byte[] b, int i) {
        return ((b[i] & 0xFF) << 8) | (b[i + 1] & 0xFF);
    }

    private static int readShort(byte[] b, int i) {
        return u16(b, i);
    }

    private static int readInt(byte[] b, int i) {
        return ((b[i] & 0xFF) << 24) | ((b[i + 1] & 0xFF) << 16) | ((b[i + 2] & 0xFF) << 8) | (b[i + 3] & 0xFF);
    }
}
