package coreLG;

import model.am;
import model.bn;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.u;
import model.z;

public final class fa extends ds {
  private ds g;
  
  private static String[] h = new String[] { String.valueOf(u.cc()) + "        ", String.valueOf(u.cb()) + "   ", String.valueOf(model.u.ca()) + "  " };
  
  private static int[][] i = new int[9][];
  
  private static Image j = ci.k;
  
  public static int a = 2;
  
  private int k = ds.Z / 2 - 50;
  
  private int[] l;
  
  private int m;
  
  private int n = 9;
  
  public static int b = 3;
  
  public static byte[] c = new byte[9];
  
  public static int[] d = new int[9];
  
  public static byte[] e;
  
  public static byte[] f;
  
  private boolean o;
  
  public fa() {
    (new int[3])[0] = f[0];
    (new int[3])[1] = e[0];
    (new int[3])[2] = coreLG.ah.e[0];
    i[0] = new int[3];
    (new int[3])[0] = f[1];
    (new int[3])[1] = e[1];
    (new int[3])[2] = coreLG.ah.e[1];
    i[1] = new int[3];
    (new int[3])[0] = f[2];
    (new int[3])[1] = e[2];
    (new int[3])[2] = coreLG.ah.e[2];
    i[2] = new int[3];
    (new int[3])[0] = f[3];
    (new int[3])[1] = e[3];
    (new int[3])[2] = coreLG.ah.e[9];
    i[3] = new int[3];
    (new int[3])[0] = f[4];
    (new int[3])[1] = e[4];
    (new int[3])[2] = coreLG.ah.e[10];
    i[4] = new int[3];
    (new int[3])[0] = f[5];
    (new int[3])[1] = e[5];
    (new int[3])[2] = coreLG.ah.e[11];
    i[5] = new int[3];
    (new int[3])[0] = f[6];
    (new int[3])[1] = e[6];
    (new int[3])[2] = coreLG.ah.e[19];
    i[6] = new int[3];
    (new int[3])[0] = f[7];
    (new int[3])[1] = e[7];
    (new int[3])[2] = coreLG.ah.e[21];
    i[7] = new int[3];
    (new int[3])[0] = f[8];
    (new int[3])[1] = e[8];
    (new int[3])[2] = coreLG.ah.e[17];
    i[8] = new int[3];
    this.ac = new model.z(u.cF(), new eq(this));
    this.ad = new z("", new eo(this));
    this.ae = new model.z(u.cI(), new ep(this));
    int i = (ds.Z >> 1) - this.n * (24 + this.k) / 2;
    this.l = new int[this.n];
    for (byte b = 0; b < this.n; b++)
      this.l[b] = i + b * this.k; 
    this.m = ds.Z >> 1;
  }
  
  protected final void a() {
    this.g.b();
  }
  
  protected final void e() {
    if (c[a] == 0) {
      el.b(String.valueOf(u.bf()) + TerrainMidlet.e.e + "~", new en(this));
      return;
    } 
    el.b(model.u.cs());
    model.ad.a().g((byte)a);
  }
  
  public final void a(ds paramds) {
    this.g = paramds;
    el.A.removeAllElements();
    el.B.b = 0;
    b();
  }
  
  public final void d() {
    if (el.H[4]) {
      a = b(a, 1);
      int i = b(a, 1);
      int j = b(i, 1);
      this.l[i] = this.l[a] - this.k;
      this.l[j] = this.l[a] - (this.k << 1);
    } 
    if (el.H[6]) {
      a = a(a, 1);
      int i = a(a, 1);
      int j = a(i, 1);
      this.l[i] = this.l[a] + this.k;
      this.l[j] = this.l[a] + (this.k << 1);
    } 
    super.d();
    ds.t();
  }
  
  public final void c() {
    am.a();
    this.o = false;
    int i = Math.max(Math.abs(this.m - this.l[a] >> 1), 1);
    if (this.l[a] < this.m) {
      for (byte b = 0; b < this.n; b++)
        this.l[b] = this.l[b] + i; 
      return;
    } 
    if (this.l[a] > this.m) {
      for (byte b = 0; b < this.n; b++)
        this.l[b] = this.l[b] - i; 
      return;
    } 
    this.o = true;
  }
  
  public final void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    am.a(paramGraphics);
    int i = ds.aa >> 1;
    bn.d.a(paramGraphics, bx.a[a], ds.Z / 2, i - 62, 2);
    Graphics graphics = paramGraphics;
    int j = i - 11;
    fa fa1 = this;
    int b1 = (el.c % 5 > 2) ? 5 : 4;
    for (byte b2 = 0; b2 < fa1.n; b2++) {
      if (a != b2) {
        bl.a(b2, 0, fa1.l[b2], j, 2, graphics);
      } else {
        bl.a(b2, b1, fa1.l[b2], j, 2, graphics);
      } 
      if (c[b2] == 0) {
        graphics.drawImage(coreLG.fa.j, fa1.l[b2], j + 5, 17);
        if (a == b2) {
          bn.c.a(graphics, "~" + d[a], fa1.l[b2], j + 30, 2);
          fa1.o = false;
        } 
      } 
    } 
    if (this.o) {
      i -= 4;
      int k = ds.Z / 2 - 60;
      paramGraphics.drawRegion(bx.g, 0, a << 4, 24, 16, 0, ds.Z / 2, i, 17);
      bn.c.a(paramGraphics, String.valueOf(h[0]) + coreLG.fa.i[a][0], k, i + 20, 0);
      bn.c.a(paramGraphics, h[1], k, i + 34, 0);
      bn.c.a(paramGraphics, h[2], k, i + 48, 0);
      paramGraphics.setColor(0);
      paramGraphics.fillRect(k + 70, i + 38, 50, 10);
      paramGraphics.fillRect(k + 70, i + 52, 50, 10);
      paramGraphics.setColor(4868682);
      paramGraphics.fillRect(k + 72, i + 40, 46, 6);
      paramGraphics.fillRect(k + 72, i + 54, 46, 6);
      paramGraphics.setColor(16741888);
      paramGraphics.fillRect(k + 72, i + 40, coreLG.fa.i[a][1] * 46 / 35, 6);
      paramGraphics.fillRect(k + 72, i + 54, coreLG.fa.i[a][2] * 46 / 100, 6);
    } 
    paramGraphics.setColor(16777215);
    super.a(paramGraphics);
  }
  
  public final void f() {
    this.g.b();
  }
  
  private int a(int paramInt1, int paramInt2) {
    return (paramInt1 + 1 > this.n - 1) ? 0 : (paramInt1 + 1);
  }
  
  private int b(int paramInt1, int paramInt2) {
    return (paramInt1 - 1 < 0) ? (this.n - 1) : (paramInt1 - 1);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\fa.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */