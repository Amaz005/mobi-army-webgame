package javax.microedition.media;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** MIDP Manager: .wav through the browser's audio element; MIDI is silent in v1 (research.md R9). */
public final class Manager {
    private Manager() {}

    public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
        if (stream == null || type == null || type.contains("midi")) return new WebPlayer(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = stream.read(buf)) > 0) out.write(buf, 0, n);
        return new WebPlayer(out.toByteArray());
    }

    public static Player createPlayer(String locator) throws IOException, MediaException {
        return new WebPlayer(null);
    }

    public static String[] getSupportedContentTypes(String protocol) {
        return new String[] {"audio/x-wav"};
    }

    public static void playTone(int note, int duration, int volume) {}
}
