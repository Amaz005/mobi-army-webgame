package web;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int8Array;

/** Small browser helpers used by the MIDP web layer. All DOM access goes through here. */
public final class Js {
    private Js() {}

    @JSFunctor
    public interface Callback extends JSObject {
        void call();
    }

    @JSFunctor
    public interface DataCallback extends JSObject {
        void call(JSObject data);
    }

    @JSFunctor
    public interface KeyCallback extends JSObject {
        /** @return true if the game uses this key (the page then calls preventDefault). */
        boolean call(String type, String key, String code, boolean repeat, boolean shortcut);
    }

    @JSFunctor
    public interface PointerCallback extends JSObject {
        void call(String type, int x, int y);
    }

    // ---- canvases -------------------------------------------------------------------------------

    // NOTE for every @JSBody below: TeaVM's minifier renames parameters to single letters but not our own
    // locals, so locals and inner-function parameters are always $-prefixed to avoid collisions.

    @JSBody(params = {"w", "h"}, script = "var $c = document.createElement('canvas'); $c.width = w; $c.height = h; return $c;")
    public static native JSObject createCanvas(int w, int h);

    @JSBody(params = {"id"}, script = "return document.getElementById(id);")
    public static native JSObject byId(String id);

    @JSBody(params = {"c"}, script = "var $x = c.getContext('2d'); $x.imageSmoothingEnabled = false; return $x;")
    public static native JSObject context2d(JSObject canvas);

    @JSBody(params = {"c", "w", "h"}, script = "c.width = w; c.height = h;")
    public static native void resizeCanvas(JSObject canvas, int w, int h);

    /** Paints RGBA bytes (Int8Array) at (x,y) of the canvas, replacing pixels (no clipping/blending). */
    @JSBody(params = {"ctx", "rgba", "x", "y", "w", "h"}, script =
            "var $d = ctx.createImageData(w, h); $d.data.set(new Uint8ClampedArray(rgba.buffer, rgba.byteOffset, w * h * 4));"
            + "ctx.putImageData($d, x, y);")
    public static native void putRgba(JSObject ctx, Int8Array rgba, int x, int y, int w, int h);

    @JSBody(params = {"ctx", "x", "y", "w", "h"}, script =
            "var $d = ctx.getImageData(x, y, w, h).data; return new Int8Array($d.buffer, $d.byteOffset, $d.length);")
    public static native Int8Array getRgba(JSObject ctx, int x, int y, int w, int h);

    // ---- page / timing --------------------------------------------------------------------------

    @JSBody(params = {"cb"}, script = "requestAnimationFrame(function() { cb(); });")
    public static native void onAnimationFrame(Callback cb);

    @JSBody(params = {"cb", "ms"}, script = "setTimeout(function() { cb(); }, ms);")
    public static native void setTimeout(Callback cb, int ms);

    @JSBody(script = "return location.host;")
    public static native String host();

    @JSBody(script = "return (location.protocol === 'https:' ? 'wss://' : 'ws://') + location.host + '/game';")
    public static native String gameSocketUrl();

    @JSBody(params = {"name"}, script = "var $v = new URLSearchParams(location.search).get(name); return $v === null ? null : $v;")
    public static native String queryParam(String name);

    @JSBody(params = {"text"}, script = "if (window.mobiarmyStatus) window.mobiarmyStatus(text); else console.log(text);")
    public static native void status(String text);

    @JSBody(params = {"text"}, script = "console.error(text);")
    public static native void error(String text);

    // ---- input ----------------------------------------------------------------------------------

    /** Keyboard events on the whole page; keys the game uses don't scroll or trigger browser shortcuts. */
    @JSBody(params = {"cb"}, script =
            "function $h($e) { if ($e.isComposing) return;"
            + " if (cb($e.type, $e.key, $e.code, $e.repeat, $e.ctrlKey || $e.metaKey)) $e.preventDefault(); }"
            + "document.addEventListener('keydown', $h); document.addEventListener('keyup', $h);")
    public static native void listenKeys(KeyCallback cb);

    /** Lets the page's keypad (keypad.js) send MIDP key codes directly. */
    @JSBody(params = {"cb"}, script = "window.mobiarmyKeypad = function($t, $k) { cb($t, '', 'MIDP:' + $k, false, false); };")
    public static native void exposeKeypad(KeyCallback cb);

    /** Mouse/touch on the screen canvas, converted to game pixels. */
    @JSBody(params = {"canvas", "cb"}, script =
            "function $pos($e) { var $r = canvas.getBoundingClientRect();"
            + " var $p = $e.touches && $e.touches.length ? $e.touches[0] : ($e.changedTouches && $e.changedTouches.length ? $e.changedTouches[0] : $e);"
            + " return [Math.floor(($p.clientX - $r.left) * canvas.width / $r.width), Math.floor(($p.clientY - $r.top) * canvas.height / $r.height)]; }"
            + "var $down = false;"
            + "function $send($t, $e) { var $q = $pos($e); cb($t, $q[0], $q[1]); }"
            + "canvas.addEventListener('mousedown', function($e) { $down = true; $send('pressed', $e); });"
            + "window.addEventListener('mousemove', function($e) { if ($down) $send('dragged', $e); });"
            + "window.addEventListener('mouseup', function($e) { if ($down) { $down = false; $send('released', $e); } });"
            + "canvas.addEventListener('touchstart', function($e) { $e.preventDefault(); $send('pressed', $e); }, {passive: false});"
            + "canvas.addEventListener('touchmove', function($e) { $e.preventDefault(); $send('dragged', $e); }, {passive: false});"
            + "canvas.addEventListener('touchend', function($e) { $e.preventDefault(); $send('released', $e); }, {passive: false});")
    public static native void listenPointer(JSObject canvas, PointerCallback cb);

    // ---- storage --------------------------------------------------------------------------------

    @JSBody(params = {"key"}, script = "try { return localStorage.getItem(key); } catch ($e) { return null; }")
    public static native String storageGet(String key);

    @JSBody(params = {"key", "value"}, script = "try { localStorage.setItem(key, value); } catch ($e) { console.warn('localStorage full or blocked', $e); }")
    public static native void storageSet(String key, String value);

    @JSBody(params = {"key"}, script = "try { localStorage.removeItem(key); } catch ($e) {}")
    public static native void storageRemove(String key);
}
