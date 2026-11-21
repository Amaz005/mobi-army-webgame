package coreLG;

import model.am;
import model.bk;
import model.bn;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.q;
import model.r;
import model.u;
import model.z;

public final class f extends ds {
  private static Image h = ci.i;
  
  private static Image i = ci.j;
  
  public static String a;
  
  private int j = el.d / 55;
  
  public static Vector b;
  
  static int c;
  
  public static int d;
  
  public static int e;
  
  private static int k;
  
  private static int l;
  
  private static int m;
  
  public byte f;
  
  private int n;
  
  private int o;
  
  static boolean g;
  
  private static boolean p = true;
  
  private static Vector q;
  
  public f() {
    this.ad = new z(model.u.B(), new ew(this));
    this.ac = new model.z(u.cJ(), new ev(this));
    this.ae = new z(model.u.cI(), new eu(this));
    this.o = ds.Z;
  }
  
  public static void a(int paramInt, String paramString) {
    if (paramString != null && !paramString.equals("")) {
      a = String.valueOf(model.u.cN()) + ": " + paramString;
      return;
    } 
    a = String.valueOf(model.u.cN()) + ": " + paramInt;
  }
  
  public final void a() {
    bk bk;
    r r;
    a((r = (model.r) b.elementAt(c)).a, r.e);
    if (r.c) {
      el.u.a(String.valueOf(model.u.co()) + ":", new es(this, r), new er(this), 0);
      el.z = (q)(bk = el.u);
      return;
    } 
    bx.b = this.f;
    model.ad.a().a(this.f, r.a, "");
    el.c(model.u.cs());
  }
  
  public final void b() {
    super.b();
    this.n = ds.Z;
    g = true;
    if ((d = c * ds.ab - el.f - 2 * ds.ab) > m)
      d = m; 
    if (d < 0)
      d = 0; 
    if (c == b.size() - 1 || c == 0)
      e = d; 
  }
  
  public final void c() {
    model.am.a();
    if (e != d) {
      l = d - e << 2;
      k += l;
      e += k >> 4;
      k &= 0xF;
    } 
    if (g && this.n > 0)
      this.n -= Math.max(this.n / 2, 1); 
  }
  
  public final void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    am.a(paramGraphics);
    for (byte b = 0; b <= el.d; b += 32)
      paramGraphics.drawImage(bx.h, b, el.e - 62, 0); 
    paramGraphics.setClip(0, 0, el.d, el.e);
    q.elementAt(el.m.a);
    bn.d.a(paramGraphics, String.valueOf(model.u.cM()) + " " + this.f, 10, 3, 0);
    paramGraphics.setColor(1407674);
    paramGraphics.fillRect(0, 25, el.d, ds.ab);
    bn.b.a(paramGraphics, model.u.cO(), 10, 28, 0);
    if (p)
      a(this.n, 0, paramGraphics); 
    super.a(paramGraphics);
  }
  
  private static void a(int paramInt1, int paramInt2, Graphics paramGraphics) {
    paramGraphics.translate(paramInt1, 0 + ds.ab + 25);
    paramGraphics.setClip(paramInt1, 1, el.d, el.e - 25 - 23 - ds.ab);
    paramGraphics.translate(paramInt1, 0 - e);
    paramInt2 = 0 - ds.ab;
    for (byte b = 0; b < coreLG.f.b.size(); b++) {
      if ((paramInt2 += ds.ab) >= e - ds.ab && paramInt2 <= e + el.e) {
        if (b == c) {
          paramGraphics.setColor(16777014);
          paramGraphics.fillRect(paramInt1, paramInt2, el.d, ds.ab);
        } 
        r r1 = (r) coreLG.f.b.elementAt(b);
        String str = String.valueOf(model.u.cN()) + " " + r1.a;
        if (!r1.e.equals(""))
          str = r1.e; 
        paramGraphics.drawRegion(h, 0, r1.g * 17, 18, 17, 0, paramInt1 + 8, paramInt2 + 1, 0);
        bn.c.a(paramGraphics, str, paramInt1 + 30, paramInt2 + 2, 0);
        bn.c.a(paramGraphics, String.valueOf(r1.b) + "/" + r1.f, paramInt1 + el.d - 3, paramInt2 + 2, 1);
        model.bn.c.a(paramGraphics, String.valueOf(r1.d) + "~", paramInt1 + el.d - 30, paramInt2 + 2, 1);
        r r2;
        if ((r2 = (r) coreLG.f.b.elementAt(b)).c)
          paramGraphics.drawImage(i, paramInt1 + el.d - 30 - model.bn.c.a(String.valueOf(r1.d) + "~") - 5, paramInt2 + 1, 24);
      } 
    } 
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
  }
  
  public final void d() {
    super.d();
    boolean bool = false;
    if (el.H[8]) {
      el.H[8] = false;
      ds.aj = false;
      if (++c >= b.size())
        c = 0; 
      bool = true;
    } else if (el.H[2]) {
      el.H[2] = false;
      ds.ai = false;
      if (--c < 0)
        c = b.size() - 1; 
      bool = true;
    } 
    if (bool) {
      if ((d = c * ds.ab - el.f - 2 * ds.ab) > m)
        d = m; 
      if (d < 0)
        d = 0; 
      if (c == b.size() - 1 || c == 0)
        e = d; 
    } 
  }
  
  public static void a(Vector paramVector) {
    q = el.m.c;
    m = (b = paramVector).size() * ds.ab - el.e - (ds.ab << 2);
  }
  
  static void a(f paramf) {
    paramf = paramf;
    el.c(model.u.cs());
    model.ad.a().a(paramf.f);
  }
  
  static void b(f paramf) {
    model.ad.a().b();
    el.b(u.cs());
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\f.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */