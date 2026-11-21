package javax.microedition.media;

import javax.microedition.media.control.VolumeControl;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int8Array;

/** Plays a sound effect through an HTML audio element; a player made with no data is silent. */
final class WebPlayer implements Player, VolumeControl {
    private final JSObject audio;
    private int state = UNREALIZED;
    private int level = 100;
    private boolean muted;
    private int loops = 1;

    WebPlayer(byte[] wav) {
        audio = wav == null ? null : create(Int8Array.copyFromJavaArray(wav));
    }

    @Override public void realize() { if (state < REALIZED) state = REALIZED; }
    @Override public void prefetch() { if (state < PREFETCHED) state = PREFETCHED; }

    @Override
    public void start() {
        state = STARTED;
        if (audio != null) play(audio, loops != 1);
    }

    @Override
    public void stop() {
        if (audio != null) pause(audio);
        if (state == STARTED) state = PREFETCHED;
    }

    @Override public void deallocate() { stop(); state = REALIZED; }
    @Override public void close() { stop(); state = CLOSED; }

    @Override
    public long setMediaTime(long now) {
        if (audio != null) seek(audio, now / 1_000_000.0);
        return now;
    }

    @Override public void setLoopCount(int count) { loops = count; }
    @Override public int getState() { return state; }

    @Override public Control[] getControls() { return new Control[] {this}; }

    @Override
    public Control getControl(String controlType) {
        return controlType != null && controlType.endsWith("VolumeControl") ? this : null;
    }

    @Override
    public int setLevel(int l) {
        level = Math.max(0, Math.min(100, l));
        if (audio != null) volume(audio, muted ? 0 : level / 100.0);
        return level;
    }

    @Override public int getLevel() { return level; }

    @Override
    public void setMute(boolean m) {
        muted = m;
        setLevel(level);
    }

    @Override public boolean isMuted() { return muted; }

    @JSBody(params = "bytes", script =
            "var $b = new Blob([bytes], {type: 'audio/wav'}); var $a = new Audio(URL.createObjectURL($b)); $a.preload = 'auto'; return $a;")
    private static native JSObject create(Int8Array bytes);

    /** Browsers block sound until the player has interacted with the page; that rejection is ignored. */
    @JSBody(params = {"a", "loop"}, script =
            "a.loop = loop; try { a.currentTime = 0; var $p = a.play(); if ($p && $p.catch) $p.catch(function() {}); } catch ($e) {}")
    private static native void play(JSObject audio, boolean loop);

    @JSBody(params = "a", script = "try { a.pause(); } catch ($e) {}")
    private static native void pause(JSObject audio);

    @JSBody(params = {"a", "t"}, script = "try { a.currentTime = t; } catch ($e) {}")
    private static native void seek(JSObject audio, double seconds);

    @JSBody(params = {"a", "v"}, script = "a.volume = v;")
    private static native void volume(JSObject audio, double v);
}
