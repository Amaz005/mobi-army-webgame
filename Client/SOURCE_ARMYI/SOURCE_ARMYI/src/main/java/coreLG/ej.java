package coreLG;

import model.ad;
import model.am;
import model.bk;
import model.bn;
import model.j;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;

import model.q;
import model.u;
import model.z;

public class ej extends ds {
  private int e = 0;
  
  public static String a;
  
  private static String[][] f;
  
  public static int b;
  
  public static int c;
  
  private static String[] g = new String[] { model.u.ct(), u.cu(), "QUAY SỐ", model.u.bE(), model.u.cv(), model.u.cx(), u.cy() };
  
  private static int[] h;
  
  private model.z i;
  
  private z j;
  
  public static boolean d = false;
  
  private int k;
  
  private int l;
  
  private int m;
  
  private int n;
  
  private int o;
  
  public ej() {
    (new String[12])[0] = model.u.cz();
    (new String[12])[1] = u.cA();
    (new String[12])[2] = model.u.cB();
    (new String[12])[3] = model.u.cC();
    (new String[12])[4] = model.u.cu();
    (new String[12])[5] = u.bD();
    (new String[12])[6] = model.u.bC();
    (new String[12])[7] = model.u.bB();
    (new String[12])[8] = model.u.bA();
    (new String[12])[9] = model.u.cv();
    (new String[12])[10] = model.u.cy();
    (new String[12])[11] = model.u.bz();
    ds.Z = el.d;
    ds.aa = el.e;
    b = 0;
    c = 0;
    a(b, c);
    ai ai = new ai(this);
    this.i = new z(model.u.cH(), new af(this));
    this.j = new model.z(model.u.cG(), new ag(this));
    this.ac = new model.z(model.u.cF(), ai);
    this.ad = new model.z("", ai);
    this.ae = this.j;
  }
  
  public void a(int paramInt1, int paramInt2) {
    if (paramInt1 == 0) {
      h = new int[g.length];
    } else if (paramInt1 == 1) {
      h = new int[(f[paramInt2]).length];
    } else if (paramInt1 == 2) {
      h = new int[(f[7]).length];
    } 
    for (byte b = 0; b < h.length; b++)
      h[b] = (b % 2 == 0) ? -ds.Z : (ds.Z << 1); 
    b = paramInt1;
    if (paramInt1 == 0) {
      this.e = paramInt2;
      b(paramInt2, h.length);
      this.ae = this.j;
      return;
    } 
    if (paramInt1 == 1) {
      c = paramInt2;
      this.e = 0;
      b(0, h.length + 2);
      this.ae = this.i;
    } 
  }
  
  protected void a() {
    ac ac = new ac(this);
    el.y.a(model.u.I(), new model.z(u.bi(), ac), new model.z("", ac), new model.z(model.u.bh(), new coreLG.ad(this)));
    model.i i = el.y;
  }
  
  public void c() {
    ej ej1;
    if ((ej1 = this).l != ej1.k) {
      ej1.n = ej1.k - ej1.l << 2;
      ej1.m += ej1.n;
      ej1.l += ej1.m >> 4;
      ej1.m &= 0xF;
    } 
    model.am.a();
    for (byte b = 0; b < h.length; b++) {
      if (h[b] != ds.Z >> 1) {
        int i = ds.Z / 2 - h[b] >> 1;
        h[b] = h[b] + i;
      } 
    } 
    super.c();
  }
  
  protected void e() {
    bk bk;
    if (b == 0) {
      switch (this.e) {
        case 0:
          a(1, this.e);
          return;
        case 1:
          coreLG.l.e();
          el.r.b();
          return;
        case 2:
          el.G.a(el.i);
          return;
        case 4:
          model.ad.a().e();
          el.b(model.u.cs());
          return;
        case 3:
          el.b(u.H(), new bv(this));
          return;
        case 5:
          a(1, this.e);
          return;
        case 6:
          el.F.b();
          break;
      } 
      return;
    } 
    if (b == 1) {
      if (c == 0) {
        switch (this.e) {
          case 0:
            a(2, this.e);
            return;
          case 1:
            model.ad.a().b();
            el.b(u.cs());
            return;
          case 2:
            el.s.a(this);
            return;
          case 3:
            ci.S = true;
            model.ad.a().d((byte)0);
            break;
        } 
        return;
      } 
      if (c == 5) {
        String[] arrayOfString1;
        String[] arrayOfString2;
        switch (this.e) {
          case 0:
            model.ad.a().d(0);
            return;
          case 1:
            model.ad.a().d();
            el.b(model.u.cs());
            return;
          case 2:
            model.ad.a().h(TerrainMidlet.e.b);
            el.b(model.u.cs());
            return;
          case 3:
            el.q.a(this);
            return;
          case 4:
            arrayOfString1 = new String[3];
            (arrayOfString2 = new String[3])[0] = u.aE();
            arrayOfString2[1] = u.aF();
            arrayOfString2[2] = u.aG();
            el.u.a(arrayOfString2[0], new bw(this, arrayOfString1, arrayOfString2), new ex(this), 2);
            el.z = (q)(bk = el.u);
            break;
        } 
        return;
      } 
    } else if (b == 2) {
      byte b = (byte)(this.e - 1);
      model.ad.a().b(b);
      el.b(u.cs());
    } 
  }
  
  public static void a(byte paramByte1, byte paramByte2, short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
    el.p.d.removeAllElements();
    j j;
    (j = new j()).a = TerrainMidlet.e.a;
    j.c = TerrainMidlet.e.c;
    el.p.d.addElement(j);
    (j = new j()).a = "Enemy";
    j.c = 0;
    el.p.d.addElement(j);
    el.j.a(paramByte1, paramByte2, paramArrayOfshort1, paramArrayOfshort2);
    el.j.b();
    ci.S = true;
    ci.M.a(0);
    eb.a((byte)0);
    (eb.b[0]).al = false;
    (eb.b[0]).J = bx.f();
    ci.Y = 0;
    (eb.b[0]).ae = 70;
    ci.X = 0;
  }
  
  public void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    bn.g.a(paramGraphics, "1.6.5", el.d - 2, 2, 1);
    am.a(paramGraphics);
    int i;
    for (i = 0; i <= el.d; i += 32)
      paramGraphics.drawImage(bx.h, i, el.e - 62, 0); 
    if (b == 1 || b == 2) {
      i = ds.aa / 2 - 70;
      int k = ds.Z >> 1;
      if (c == 0) {
        bx.a(TerrainMidlet.e.f, k - 50, i + 5, 17, paramGraphics);
        bl.a(TerrainMidlet.e.c, (el.c % 5 > 2) ? 5 : 4, k - 50, i + 40, 2, paramGraphics);
        k -= 30;
        model.bn.c.a(paramGraphics, String.valueOf(u.cD()) + ": " + TerrainMidlet.e.a, k, i, 0);
        model.bn.c.a(paramGraphics, "Level: " + TerrainMidlet.e.l.a + "+" + TerrainMidlet.e.l.c + "%", k, i + 14, 0);
        model.bn.c.a(paramGraphics, String.valueOf(model.u.cE()) + ": " + TerrainMidlet.e.e + "~", k, i + 28, 0);
      } 
      bn.d.a(paramGraphics, g[c], ds.Z >> 1, 5, 2);
    } else {
      paramGraphics.drawImage(dl.d, el.g, el.f - 60, 3);
    } 
    i = (ds.aa >> 1) - 10;
    int j = (el.f - 20) / 20;
    this.o = j * 20;
    paramGraphics.setClip(0, i, el.d, this.o);
    paramGraphics.translate(0, -this.l);
    paramGraphics.setColor(16777014);
    paramGraphics.fillRect(0, i + this.e * 20, ds.Z, 22);
    for (j = 0; j < h.length; j++) {
      if (b == 0) {
        model.bn.d.a(paramGraphics, g[j], h[j], i + j * 20, 2);
      } else if (b == 1) {
        bn.d.a(paramGraphics, f[c][j], h[j], i + j * 20, 2);
      } else if (b == 2) {
        bn.d.a(paramGraphics, f[7][j], h[j], i + j * 20, 2);
      } 
    } 
    super.a(paramGraphics);
  }
  
  public void d() {
    boolean bool = false;
    if (el.H[2]) {
      el.H[2] = true;
      bool = true;
      this.e = (this.e - 1 < 0) ? (h.length - 1) : (this.e - 1);
    } else if (el.H[8]) {
      bool = true;
      el.H[8] = false;
      this.e = (this.e + 1 > h.length - 1) ? 0 : (this.e + 1);
    } 
    if (bool)
      b(this.e, h.length); 
    super.d();
    ds.t();
  }
  
  private void b(int paramInt1, int paramInt2) {
    this.k = paramInt1 * 20;
    if (this.k > paramInt2 * 20 - this.o)
      this.k = paramInt2 * 20 - this.o; 
    if (this.k < 0)
      this.k = 0; 
  }
  
  static void a(ej paramej, String paramString) {
    String str = paramString;
    try {
      TerrainMidlet.b.platformRequest(str);
      TerrainMidlet.b.notifyDestroyed();
      return;
    } catch (ConnectionNotFoundException connectionNotFoundException) {
      return;
    } 
  }
  
  static {
    (new String[4])[0] = model.u.cz();
    (new String[4])[1] = model.u.cA();
    (new String[4])[2] = u.cB();
    (new String[4])[3] = model.u.cC();
    (f = new String[8][])[0] = new String[4];
    (new String[5])[0] = u.bD();
    (new String[5])[1] = u.bC();
    (new String[5])[2] = model.u.bB();
    (new String[5])[3] = model.u.bA();
    (new String[5])[4] = u.by();
    f[5] = new String[5];
    (new String[7])[0] = model.u.c();
    (new String[7])[1] = model.u.d();
    (new String[7])[2] = "1 VS 1";
    (new String[7])[3] = "2 VS 2";
    (new String[7])[4] = "3 VS 3";
    (new String[7])[5] = "4 VS 4";
    (new String[7])[6] = model.u.a();
    f[7] = new String[7];
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ej.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */