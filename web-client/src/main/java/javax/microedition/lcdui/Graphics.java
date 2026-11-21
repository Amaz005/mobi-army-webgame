package javax.microedition.lcdui;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int8Array;
import web.Js;

/**
 * MIDP Graphics on an HTML canvas 2D context. Only the operations the MobiArmy client uses
 * (specs/002-web-client/research.md R3). Translation and clip are tracked here in device pixels.
 */
public class Graphics {
    public static final int HCENTER = 1, VCENTER = 2, LEFT = 4, RIGHT = 8, TOP = 16, BOTTOM = 32, BASELINE = 64;
    public static final int SOLID = 0, DOTTED = 1;

    private final JSObject ctx;
    private final int surfaceW, surfaceH;
    private int tx, ty;
    private int clipX, clipY, clipW, clipH;
    private int color;
    private String colorCss = "#000000";

    Graphics(JSObject ctx, int surfaceW, int surfaceH) {
        this.ctx = ctx;
        this.surfaceW = surfaceW;
        this.surfaceH = surfaceH;
        save(ctx);
        reset();
    }

    /** Fresh state, as MIDP gives to every paint(): no translation, full clip, black. */
    final void reset() {
        tx = 0;
        ty = 0;
        applyClip(0, 0, surfaceW, surfaceH);
        setColor(0);
    }

    // ---- colour ---------------------------------------------------------------------------------

    public void setColor(int rgb) {
        rgb &= 0xFFFFFF;
        if (rgb != color || colorCss == null) {
            color = rgb;
            colorCss = css(rgb);
        }
        setStyle(ctx, colorCss);
    }

    public void setColor(int r, int g, int b) {
        setColor(((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
    }

    public int getColor() {
        return color;
    }

    public int getRedComponent() { return (color >> 16) & 0xFF; }
    public int getGreenComponent() { return (color >> 8) & 0xFF; }
    public int getBlueComponent() { return color & 0xFF; }

    public void setStrokeStyle(int style) {}
    public int getStrokeStyle() { return SOLID; }

    private static String css(int rgb) {
        String h = Integer.toHexString(rgb | 0x1000000);
        return "#" + h.substring(1);
    }

    // ---- translate / clip -----------------------------------------------------------------------

    public void translate(int x, int y) {
        tx += x;
        ty += y;
    }

    public int getTranslateX() { return tx; }
    public int getTranslateY() { return ty; }

    public void setClip(int x, int y, int w, int h) {
        applyClip(x + tx, y + ty, w, h);
    }

    public void clipRect(int x, int y, int w, int h) {
        int ax = Math.max(clipX, x + tx), ay = Math.max(clipY, y + ty);
        int bx = Math.min(clipX + clipW, x + tx + w), by = Math.min(clipY + clipH, y + ty + h);
        applyClip(ax, ay, Math.max(0, bx - ax), Math.max(0, by - ay));
    }

    public int getClipX() { return clipX - tx; }
    public int getClipY() { return clipY - ty; }
    public int getClipWidth() { return clipW; }
    public int getClipHeight() { return clipH; }

    private void applyClip(int x, int y, int w, int h) {
        clipX = x;
        clipY = y;
        clipW = Math.max(0, w);
        clipH = Math.max(0, h);
        clip(ctx, clipX, clipY, clipW, clipH);
        if (colorCss != null) setStyle(ctx, colorCss); // restore() inside clip() resets styles
    }

    // ---- shapes ---------------------------------------------------------------------------------

    public void fillRect(int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) return;
        fillRect(ctx, x + tx, y + ty, w, h);
    }

    /** MIDP outlines cover (w+1)×(h+1) pixels. */
    public void drawRect(int x, int y, int w, int h) {
        if (w < 0 || h < 0) return;
        strokeRect(ctx, x + tx + 0.5, y + ty + 0.5, w, h);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        x1 += tx; x2 += tx; y1 += ty; y2 += ty;
        if (y1 == y2) {
            fillRect(ctx, Math.min(x1, x2), y1, Math.abs(x2 - x1) + 1, 1);
        } else if (x1 == x2) {
            fillRect(ctx, x1, Math.min(y1, y2), 1, Math.abs(y2 - y1) + 1);
        } else {
            line(ctx, x1 + 0.5, y1 + 0.5, x2 + 0.5, y2 + 0.5);
        }
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        triangle(ctx, x1 + tx, y1 + ty, x2 + tx, y2 + ty, x3 + tx, y3 + ty);
    }

    public void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
        if (w <= 0 || h <= 0 || arcAngle == 0) return;
        arc(ctx, x + tx, y + ty, w, h, startAngle, arcAngle, true);
    }

    public void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
        if (w < 0 || h < 0 || arcAngle == 0) return;
        arc(ctx, x + tx + 0.5, y + ty + 0.5, w, h, startAngle, arcAngle, false);
    }

    public void fillRoundRect(int x, int y, int w, int h, int arcW, int arcH) {
        if (w <= 0 || h <= 0) return;
        roundRect(ctx, x + tx, y + ty, w, h, Math.min(arcW, w) / 2.0, Math.min(arcH, h) / 2.0, true);
    }

    public void drawRoundRect(int x, int y, int w, int h, int arcW, int arcH) {
        if (w < 0 || h < 0) return;
        roundRect(ctx, x + tx + 0.5, y + ty + 0.5, w, h, Math.min(arcW, w) / 2.0, Math.min(arcH, h) / 2.0, false);
    }

    // ---- images ---------------------------------------------------------------------------------

    public void drawImage(Image img, int x, int y, int anchor) {
        if (img == null) throw new NullPointerException();
        int w = img.getWidth(), h = img.getHeight();
        x = anchorX(x, w, anchor) + tx;
        y = anchorY(y, h, anchor) + ty;
        drawWhole(ctx, img.canvas, x, y);
    }

    public static final int TRANS_NONE = 0, TRANS_MIRROR_ROT180 = 1, TRANS_MIRROR = 2, TRANS_ROT180 = 3,
            TRANS_MIRROR_ROT270 = 4, TRANS_ROT90 = 5, TRANS_ROT270 = 6, TRANS_MIRROR_ROT90 = 7;

    public void drawRegion(Image src, int xSrc, int ySrc, int w, int h, int transform, int xDest, int yDest, int anchor) {
        if (src == null) throw new NullPointerException();
        if (w <= 0 || h <= 0) return;
        boolean swap = transform == TRANS_ROT90 || transform == TRANS_ROT270
                || transform == TRANS_MIRROR_ROT90 || transform == TRANS_MIRROR_ROT270;
        int dw = swap ? h : w, dh = swap ? w : h;
        int dx = anchorX(xDest, dw, anchor) + tx;
        int dy = anchorY(yDest, dh, anchor) + ty;
        // Canvas transform(a,b,c,d,e,f) maps region pixel (u,v) to (a*u + c*v + e, b*u + d*v + f).
        int a, b, c, d, e, f;
        switch (transform) {
            case TRANS_NONE:          a = 1;  b = 0;  c = 0;  d = 1;  e = dx;     f = dy;     break;
            case TRANS_ROT90:         a = 0;  b = 1;  c = -1; d = 0;  e = dx + h; f = dy;     break;
            case TRANS_ROT180:        a = -1; b = 0;  c = 0;  d = -1; e = dx + w; f = dy + h; break;
            case TRANS_ROT270:        a = 0;  b = -1; c = 1;  d = 0;  e = dx;     f = dy + w; break;
            case TRANS_MIRROR:        a = -1; b = 0;  c = 0;  d = 1;  e = dx + w; f = dy;     break;
            case TRANS_MIRROR_ROT180: a = 1;  b = 0;  c = 0;  d = -1; e = dx;     f = dy + h; break;
            case TRANS_MIRROR_ROT90:  a = 0;  b = -1; c = -1; d = 0;  e = dx + h; f = dy + w; break;
            case TRANS_MIRROR_ROT270: a = 0;  b = 1;  c = 1;  d = 0;  e = dx;     f = dy;     break;
            default: throw new IllegalArgumentException("transform " + transform);
        }
        drawRegion(ctx, src.canvas, xSrc, ySrc, w, h, a, b, c, d, e, f);
    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
        if (width <= 0 || height <= 0) return;
        byte[] rgba = Image.toRgba(rgbData, offset, scanlength, width, height, processAlpha);
        JSObject tmp = Js.createCanvas(width, height);
        Js.putRgba(Js.context2d(tmp), Int8Array.copyFromJavaArray(rgba), 0, 0, width, height);
        drawWhole(ctx, tmp, x + tx, y + ty);
    }

    private static int anchorX(int x, int w, int anchor) {
        if ((anchor & HCENTER) != 0) return x - w / 2;
        if ((anchor & RIGHT) != 0) return x - w;
        return x;
    }

    private static int anchorY(int y, int h, int anchor) {
        if ((anchor & VCENTER) != 0) return y - h / 2;
        if ((anchor & BOTTOM) != 0) return y - h;
        return y;
    }

    // ---- canvas 2D calls ------------------------------------------------------------------------

    @JSBody(params = {"ctx"}, script = "ctx.save();")
    private static native void save(JSObject ctx);

    @JSBody(params = {"ctx", "x", "y", "w", "h"}, script =
            "ctx.restore(); ctx.save(); ctx.beginPath(); ctx.rect(x, y, w, h); ctx.clip();")
    private static native void clip(JSObject ctx, int x, int y, int w, int h);

    @JSBody(params = {"ctx", "s"}, script = "ctx.fillStyle = s; ctx.strokeStyle = s; ctx.lineWidth = 1;")
    private static native void setStyle(JSObject ctx, String css);

    @JSBody(params = {"ctx", "x", "y", "w", "h"}, script = "ctx.fillRect(x, y, w, h);")
    private static native void fillRect(JSObject ctx, int x, int y, int w, int h);

    @JSBody(params = {"ctx", "x", "y", "w", "h"}, script = "ctx.strokeRect(x, y, w, h);")
    private static native void strokeRect(JSObject ctx, double x, double y, int w, int h);

    @JSBody(params = {"ctx", "x1", "y1", "x2", "y2"}, script =
            "ctx.beginPath(); ctx.moveTo(x1, y1); ctx.lineTo(x2, y2); ctx.stroke();")
    private static native void line(JSObject ctx, double x1, double y1, double x2, double y2);

    @JSBody(params = {"ctx", "x1", "y1", "x2", "y2", "x3", "y3"}, script =
            "ctx.beginPath(); ctx.moveTo(x1, y1); ctx.lineTo(x2, y2); ctx.lineTo(x3, y3); ctx.closePath(); ctx.fill();")
    private static native void triangle(JSObject ctx, int x1, int y1, int x2, int y2, int x3, int y3);

    /** MIDP angles: degrees, 0 = 3 o'clock, counter-clockwise positive. Canvas y grows downwards. */
    @JSBody(params = {"ctx", "x", "y", "w", "h", "start", "arc", "fill"}, script =
            "var $rx = w / 2, $ry = h / 2, $cx = x + $rx, $cy = y + $ry; var $full = Math.abs(arc) >= 360;"
            + "var $a0 = -start * Math.PI / 180, $a1 = -(start + arc) * Math.PI / 180;"
            + "ctx.beginPath(); if (fill && !$full) ctx.moveTo($cx, $cy);"
            + "ctx.ellipse($cx, $cy, $rx, $ry, 0, $full ? 0 : $a0, $full ? 2 * Math.PI : $a1, !$full && arc > 0);"
            + "if (fill) { ctx.closePath(); ctx.fill(); } else ctx.stroke();")
    private static native void arc(JSObject ctx, double x, double y, int w, int h, int start, int arc, boolean fill);

    @JSBody(params = {"ctx", "x", "y", "w", "h", "rx", "ry", "fill"}, script =
            "ctx.beginPath(); ctx.moveTo(x + rx, y); ctx.lineTo(x + w - rx, y);"
            + "ctx.ellipse(x + w - rx, y + ry, rx, ry, 0, -Math.PI / 2, 0); ctx.lineTo(x + w, y + h - ry);"
            + "ctx.ellipse(x + w - rx, y + h - ry, rx, ry, 0, 0, Math.PI / 2); ctx.lineTo(x + rx, y + h);"
            + "ctx.ellipse(x + rx, y + h - ry, rx, ry, 0, Math.PI / 2, Math.PI); ctx.lineTo(x, y + ry);"
            + "ctx.ellipse(x + rx, y + ry, rx, ry, 0, Math.PI, 1.5 * Math.PI); ctx.closePath();"
            + "if (fill) ctx.fill(); else ctx.stroke();")
    private static native void roundRect(JSObject ctx, double x, double y, int w, int h, double rx, double ry, boolean fill);

    @JSBody(params = {"ctx", "img", "x", "y"}, script = "ctx.drawImage(img, x, y);")
    private static native void drawWhole(JSObject ctx, JSObject img, int x, int y);

    @JSBody(params = {"ctx", "img", "sx", "sy", "w", "h", "a", "b", "c", "d", "e", "f"}, script =
            "ctx.setTransform(a, b, c, d, e, f); ctx.drawImage(img, sx, sy, w, h, 0, 0, w, h); ctx.setTransform(1, 0, 0, 1, 0, 0);")
    private static native void drawRegion(JSObject ctx, JSObject img, int sx, int sy, int w, int h,
                                          int a, int b, int c, int d, int e, int f);
}
