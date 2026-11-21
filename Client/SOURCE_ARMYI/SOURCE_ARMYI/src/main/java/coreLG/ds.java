package coreLG;

import model.bn;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.z;

public abstract class ds {
  public static int Z;
  
  public static int aa;
  
  public static final int ab = bn.b.a() + 6;
  
  public z ac;
  
  public z ad;
  
  public model.z ae;
  
  public static int af = bn.b.a() + 6;
  
  private static Image a = ci.m;
  
  public static boolean ag;
  
  public static ds ah;
  
  public static boolean ai;
  
  public static boolean aj;
  
  public static boolean ak;
  
  public static boolean al;
  
  public ds() {
    System.getProperty("microedition.platform").indexOf("RIM");
    ag = (System.getProperty("microedition.platform").indexOf("NX") == 0);
  }
  
  public void a(Graphics paramGraphics) {
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
    paramGraphics.setClip(0, 0, Z, aa);
    c(paramGraphics);
    if (el.z == null && !el.v.a) {
      if (this.ac != null)
        model.bn.b.a(paramGraphics, this.ac.a, 5, el.e - model.bn.b.a() - 3, 0);
      if (this.ad != null)
        model.bn.b.a(paramGraphics, this.ad.a, el.g, el.e - bn.b.a() - 3, 2);
      if (this.ae != null)
        model.bn.b.a(paramGraphics, this.ae.a, el.d - 5, el.e - model.bn.b.a() - 3, 1);
    } 
  }
  
  public static void b(Graphics paramGraphics) {
    paramGraphics.setColor(6606845);
    paramGraphics.fillRect(0, 0, el.d, el.e);
  }
  
  public void c() {}
  
  public final boolean s() {
    return (el.h == this);
  }
  
  public void d() {
    if (el.H[5]) {
      el.H[5] = false;
      if (this.ad != null && this.ad.b != null)
        this.ad.b.a(); 
    } 
    if (el.H[12] && !el.I[5]) {
      el.H[12] = false;
      if (this.ac != null && this.ac.b != null)
        this.ac.b.a(); 
    } 
    if (el.H[13]) {
      el.H[13] = false;
      if (this.ae != null && this.ae.b != null)
        this.ae.b.a(); 
    } 
  }
  
  public static void t() {
    for (byte b = 0; b < el.H.length; b++) {
      el.I[b] = false;
      el.H[b] = false;
    } 
    ai = false;
    aj = false;
    ak = false;
    al = false;
  }
  
  public void b() {
    t();
    el.h = this;
  }
  
  public void a(ds paramds) {
    ah = paramds;
    t();
    el.h = this;
  }
  
  public void e(int paramInt) {}
  
  public static void c(Graphics paramGraphics) {
    paramGraphics.setColor(1133755);
    paramGraphics.fillRect(0, aa - 20, Z, 20);
    paramGraphics.setColor(2263535);
    paramGraphics.drawLine(0, aa - 19, Z, aa - 19);
  }
  
  public static void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Graphics paramGraphics) {
    int i = a.getWidth();
    paramGraphics.setColor(2276847);
    paramGraphics.fillRect(paramInt1 + i, paramInt2, paramInt3 - (i << 1), paramInt4);
    paramGraphics.fillRect(paramInt1, paramInt2 + i, paramInt3, paramInt4 - (i << 1));
    paramGraphics.setColor(1144763);
    paramGraphics.fillRect(paramInt1 + 3, paramInt2 + 3, paramInt3 - 6, paramInt4 - 6);
    paramGraphics.drawLine(paramInt1 + i, paramInt2, paramInt1 + paramInt3 - i, paramInt2);
    paramGraphics.drawLine(paramInt1 + i, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - i, paramInt2 + paramInt4 - 1);
    paramGraphics.drawLine(paramInt1, paramInt2 + i, paramInt1, paramInt2 + paramInt4 - i);
    paramGraphics.drawLine(paramInt1 + paramInt3 - 1, paramInt2 + i, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - i);
    paramGraphics.drawRegion(a, 0, 0, i, i, 0, paramInt1, paramInt2, 0);
    paramGraphics.drawRegion(a, 0, i, i, i, 0, paramInt1 + paramInt3 - i, paramInt2, 0);
    paramGraphics.drawRegion(a, 0, i << 1, i, i, 0, paramInt1 + paramInt3 - i, paramInt2 + paramInt4 - i, 0);
    paramGraphics.drawRegion(a, 0, i * 3, i, i, 0, paramInt1, paramInt2 + paramInt4 - i, 0);
  }
  
  static {
    bn.b.a("_TEXT_");
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ds.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */