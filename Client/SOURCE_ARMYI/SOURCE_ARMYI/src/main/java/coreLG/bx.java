package coreLG;

import model.am;
import model.ar;
import model.bc;
import model.bk;
import model.bn;
import model.j;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import model.p;
import model.y;
import model.z;

public class bx extends ds {
  public static String[] a = new String[] { "GUNNER", "MISS 6", "ELECTICIAN", "KING KONG", "ROCKETER", "GRANOS", "CHICKY", "TARZAN", "APACHE", "MAGENTA" };
  
  private static byte B = 8;
  
  public static byte b;
  
  public static byte c;
  
  public Vector d = new Vector();
  
  public Vector e = new Vector();
  
  public static Image[] f;
  
  public static Image g;
  
  private static Image C;
  
  public static Image h;
  
  public static Image i;
  
  public static byte[] j;
  
  public static byte[] k;
  
  private int[] D;
  
  private int[] E;
  
  private int F;
  
  int l;
  
  int m = -1;
  
  public static byte n = 0;
  
  private byte G = 3;
  
  public static byte o = 6;
  
  public int[] p = new int[o];
  
  public static bi q;
  
  private boolean H = false;
  
  private boolean I = false;
  
  private int J;
  
  private int K;
  
  private int L = 0;
  
  int r = 0;
  
  int s = 0;
  
  public model.p t = new p();
  
  public model.bc u;
  
  private model.z M;
  
  private model.z N;
  
  private model.z O;
  
  private model.z P;
  
  private model.z Q;
  
  private model.z R;
  
  private model.z S;
  
  private model.z T;
  
  private model.z U;
  
  private model.z V;
  
  private z W;
  
  private z X;
  
  private model.z Y;
  
  public static Image[] v;
  
  public static Image[] w;
  
  public static Image x;
  
  public static Image y;
  
  private static Image am;
  
  private static y an;
  
  public static byte[] z;
  
  private int ao = 0;
  
  public int A;
  
  static {
    (v = new Image[9])[0] = ci.I[0];
    v[1] = ci.I[1];
    v[2] = ci.I[2];
    v[3] = ci.I[3];
    v[4] = ci.I[4];
    C = ci.r;
    h = ci.s;
    g = coreLG.ah.c;
    try {
      am = Image.createImage("/randomMap.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    }
  }
  
  public static void a() {
    el.l = new dc();
    f = new Image[cv.f];
    cj.d("MM.NUMBMAP= " + cv.f);
    an = new model.y(z);
    for (byte b = 0; b < cv.f; b++)
      f[b] = an.a(String.valueOf(cv.h[b]) + ".png");
    i = an.a("khungmap.png");
    an = null;
  }
  
  private void u() {
    this.p = coreLG.l.b(this.p);
  }
  
  public void e() {
    for (byte b = 0; b < this.p.length; b++)
      this.p[b] = -2; 
    this.p[0] = 0;
    this.p[1] = 0;
    this.p[2] = 1;
    this.p[3] = 1;
  }
  
  public static int[] f() {
    int[] arrayOfInt;
    (arrayOfInt = new int[o])[0] = 0;
    arrayOfInt[1] = 0;
    arrayOfInt[2] = 0;
    arrayOfInt[3] = 0;
    arrayOfInt[4] = 0;
    arrayOfInt[5] = 0;
    return arrayOfInt;
  }
  
  public int[] g() {
    int[] arrayOfInt = new int[this.p.length];
    for (byte b = 0; b < arrayOfInt.length; b++)
      arrayOfInt[b] = this.p[b]; 
    return arrayOfInt;
  }
  
  public void a(int paramInt, model.j paramj) {
    this.d.setElementAt(paramj, paramInt);
  }
  
  public void h() {
    Vector vector = new Vector();
    for (byte b = 0; b < this.d.size(); b++) {
      j j;
      if ((j = (model.j) this.d.elementAt(b)).b != TerrainMidlet.e.b && j.b != -1)
        vector.addElement(new z(j.a, new dop(this, j)));
    } 
    el.v.a(vector, 0);
  }
  
  public bx() {
    bx bx1;
    (bx1 = this).D = new int[B];
    bx1.E = new int[B];
    bx1.D[0] = 24;
    bx1.E[0] = el.e - 128;
    bx1.D[1] = el.d - 24;
    bx1.E[1] = el.e - 128;
    bx1.D[2] = 62;
    bx1.E[2] = el.e - 80;
    bx1.D[3] = el.d - 62;
    bx1.E[3] = el.e - 80;
    bx1.D[4] = 62;
    bx1.E[4] = el.e - 128;
    bx1.D[5] = el.d - 62;
    bx1.E[5] = el.e - 128;
    bx1.D[6] = 24;
    bx1.E[6] = el.e - 80;
    bx1.D[7] = el.d - 24;
    bx1.E[7] = el.e - 80;
    e();
    this.r = el.d - dj.l * 18 + 5;
    this.s = el.e - 56;
    this.u = new bc();
    this.u.a = 2;
    this.u.b = el.e - ds.ab - 25;
    this.u.c = el.d - 4;
    this.u.d = ds.ab + 2;
    this.u.e = true;
    this.Q = new model.z("Menu", new coreLG.dm(this));
    this.M = new model.z("Chat", new coreLG.dn(this));
    this.R = new model.z(model.u.cR(), new coreLG.cx(this));
    this.S = new model.z(model.u.bR(), new coreLG.cw(this));
    this.X = new model.z(model.u.cP(), new coreLG.cz(this));
    this.Y = new model.z(model.u.cQ(), new coreLG.cy(this));
    this.P = new model.z(model.u.at(), new coreLG.db(this));
    this.O = new model.z(model.u.z(), new coreLG.da(this));
    this.N = new model.z(model.u.cU(), new coreLG.bd(this));
    this.V = new model.z(model.u.cS(), new coreLG.bb(this));
    this.W = new model.z(model.u.cT(), new coreLG.az(this));
    this.T = new model.z(model.u.cF(), new coreLG.aw(this));
    this.U = new model.z(model.u.cH(), new coreLG.aq(this));
    this.ac = this.Q;
    this.ae = this.N;
  }
  
  public static void i() {
    model.ad.a().i();
    coreLG.el.b(model.u.cs());
  }
  
  public void a(int paramInt, byte paramByte) {
    int i = f(paramInt);
    this.d.setElementAt(this.d.elementAt(i), paramByte);
    this.d.setElementAt(new model.j(), i);
    if (paramInt == TerrainMidlet.e.b)
      this.m = paramByte; 
    cj.d("indexOfme= " + this.m);
  }
  
  public void j() {
    Vector vector = new Vector();
    for (byte b = 0; b < this.d.size(); b++) {
      j j;
      if ((j = (model.j) this.d.elementAt(b)).b != TerrainMidlet.e.b && j.b != -1)
        vector.addElement(new model.z(j.a, new ao(this, j)));
    } 
    el.v.a(vector, 0);
  }
  
  public void k() {
    Vector vector = new Vector();
    for (byte b = 0; b < this.d.size(); b++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(b)).b != TerrainMidlet.e.b && j.b != -1)
        vector.addElement(new model.z(j.a, new coreLG.am(this, j)));
    }
    el.v.a(vector, 0);
  }
  
  public void l() {
    for (byte b = 0; b < this.d.size(); b++)
      this.d.elementAt(b); 
    if (this.F > TerrainMidlet.e.e)
      coreLG.el.a(model.u.ao());
    coreLG.el.b(model.u.an());
    model.ad.a().a(this.p);
    model.ad.a().f();
  }
  
  public void m() {
    if (this.ao != 0) return;
    String str;
    if ((str = this.u.c()).trim().equals("")) return;
    this.u.a("");
    model.ad.a().c(str);
    this.a(TerrainMidlet.e.b, str, 90);
    this.ao = 30;
  }
  
  public static void n() {
    if (c == 5) {
      el.l.a = true;
    } else {
      el.l.a = false;
    }
    el.l.b();
  }
  
  public void o() {
    coreLG.el.u.a(model.u.as(), new al(this), new aj(this), 1);
    model.bk bk = el.u;
  }
  
  public static void p() {
    model.ad.a().c();
    if (coreLG.f.b == null) {
      coreLG.el.i.b();
      return;
    }
    coreLG.el.n.b();
  }
  
  public void q() {
    Vector vector = new Vector();
    vector.addElement(new model.z("4 vs 4", new k(this)));
    vector.addElement(new model.z("3 vs 3", new coreLG.j(this)));
    vector.addElement(new z("2 vs 2", new b(this)));
    vector.addElement(new z("1 vs 1", new a(this)));
    coreLG.el.v.a(vector, 0);
  }
  
  private int f(int paramInt) {
    for (byte b = 0; b < this.d.size(); b++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(b)).b == paramInt)
        return b; 
    } 
    return -1;
  }
  
  public void d() {
    if (this.H)
      q.a(); 
    if (el.H[2]) {
      if (!this.H) {
        this.J -= 3;
        if (this.J < 0)
          this.J = 0; 
      } 
    } else if (el.H[8]) {
      if (!this.H) {
        this.J += 3;
        if (this.J > o - 1)
          this.J = o - 1; 
      } 
    } else if (el.H[4]) {
      if (!this.H) {
        this.J--;
        if (this.J < 0)
          this.J = o - 1; 
      } 
    } else if (el.H[6] && !this.H) {
      this.J++;
      if (this.J % 3 == 0)
        this.J = 0; 
    } 
    super.d();
    ds.t();
  }
  
  public void c() {
    if (this.A > 0)
      this.A--; 
    if (this.ao > 0)
      this.ao--; 
    if (this.u.c().equals("")) {
      this.ac = this.Q;
      this.ae = this.N;
      if (this.l == TerrainMidlet.e.b) {
        this.ae = this.O;
        this.O.a = model.u.z();
      } else {
        this.ae = this.P;
        this.P.a = model.u.at();
        for (byte b = 0; b < this.d.size(); b++) {
          model.j j;
          if ((j = (model.j) this.d.elementAt(b)).b == TerrainMidlet.e.b && j.d) {
            this.P.a = model.u.n();
            if (this.A == 0) {
              this.ad = this.P;
            } else {
              this.ad = null;
            } 
          } 
        } 
      } 
      if (this.l != TerrainMidlet.e.b && ((j)this.d.elementAt(this.m)).d) {
        this.ad = null;
      } else {
        this.ad = this.T;
      } 
      if (this.I || this.H)
        this.ac = this.ae = null; 
      if (this.H || this.I)
        this.ae = this.U; 
    } else {
      this.ad = this.M;
      this.ae = this.u.h;
    } 
    this.u.b();
    q.b();
    model.am.a();
  }
  
  public void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    Graphics graphics1 = paramGraphics;
    byte b;
    for (b = 0; b < el.d; b += 32)
      graphics1.drawImage(h, b, el.e - 144 - 15, 0); 
    model.am.a(paramGraphics);
    graphics1 = paramGraphics;
    for (b = 0; b < 2; b++) {
      for (byte b1 = 0; b1 < el.d; b1 += 32)
        graphics1.drawImage(v[4], b1, el.e - 48 * (b + 1) - 20, 0); 
    } 
    Graphics graphics2 = paramGraphics;
    bx bx1 = this;
    int i;
    for (i = 0; i < 8; i++) {
      model.j j;
      if ((j = (model.j) bx1.d.elementAt(i)).b != -1) {
        boolean bool = (i % 2 == 0) ? false : true;
        bl.a(j.c, 5, bx1.D[i] + (!bool ? 2 : -2), bx1.E[i] + 13, !bool ? 2 : 0, graphics2);
        a(j.f, bx1.D[i], bx1.E[i] - 15, 3, graphics2);
        ((i % 2 == 0) ? model.bn.e : model.bn.f).a(graphics2, j.a, bx1.D[i], bx1.E[i] + 15, 2);
        if (j.d && j.b != bx1.l)
          model.bn.c.a(graphics2, "ok", bx1.D[i], bx1.E[i] - 32, 2);
        if (j.b == bx1.l)
          bn.c.a(graphics2, model.u.bt(), bx1.D[i], bx1.E[i] - 32, 2);
      } 
    } 
    if (el.e > 300)
      try {
        if (c == 5) {
          i = j.length - cv.f - n;
          bl.a(k[i], (el.c % 10 > 5) ? 0 : 1, el.d / 2, bx1.E[0] + 13, 2, graphics2);
        } 
      } catch (Exception exception) {} 
    graphics2 = paramGraphics;
    bx1 = this;
    if (el.e < 300) {
      model.bn.c.a(graphics2, String.valueOf(model.u.cL()) + ": " + b, el.g, 10, 2);
      model.bn.c.a(graphics2, coreLG.f.a, el.g, 25, 2);
      model.bn.c.a(graphics2, String.valueOf(n + 1) + ". " + cv.g[n], el.g, 40, 2);
      bn.c.a(graphics2, String.valueOf(bx1.F) + "~", el.g, 55, 2);
      coreLG.l.a(0, el.e - 50, graphics2);
    } else {
      model.bn.c.a(graphics2, String.valueOf(model.u.cL()) + ": " + b, el.g, 85, 2);
      model.bn.c.a(graphics2, coreLG.f.a, el.g, 100, 2);
      bn.c.a(graphics2, String.valueOf(bx1.F) + "~", el.g, 115, 2);
      coreLG.l.a(0, el.e - 50, graphics2);
    } 
    this.a(paramGraphics, this.r, this.s);
    if (el.e > 300) {
      Graphics graphics;
      (graphics = paramGraphics).setColor(16777215);
      graphics.fillRect(el.g - 38, 13, 76, 46);
      if (n != 100) {
        graphics.drawImage(f[n], el.g, 36, 3);
        model.bn.c.a(graphics, String.valueOf(n + 1) + ". " + cv.g[n], el.g, 70, 2);
      } else {
        graphics.drawImage(am, el.g, 36, 3);
        model.bn.c.a(graphics, "Unknow", el.g, 70, 2);
      } 
      graphics.drawImage(coreLG.bx.i, el.g, 36, 3);
    } 
    if (this.H) {
      Graphics graphics = paramGraphics;
      int k = this.s - 102;
      int j = ds.Z - q.e >> 1;
      coreLG.l.a((byte)1, j - 5, k - 15, graphics);
      coreLG.l.a(q, j, k, graphics);
    } 
    if (this.I) {
      a(this.r - 72, this.s - 20, paramGraphics);
      int b1 = (el.c % 5 > 2) ? 5 : 4;
      bl.a(this.K, b1, this.r + dj.l * 18 + 12, this.s + 30, 2, paramGraphics);
    } 
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
    paramGraphics.setClip(0, 0, el.d, el.e);
    if (!this.u.c().equals(""))
      this.u.a(paramGraphics); 
    super.a(paramGraphics);
  }
  
  public static void a(byte paramByte, int paramInt1, int paramInt2, int paramInt3, Graphics paramGraphics) {
    paramGraphics.drawRegion(C, 0, paramByte * 12, 12, 12, 0, paramInt1, paramInt2, paramInt3);
  }
  
  private void a(int paramInt1, int paramInt2, Graphics paramGraphics) {
    for (byte b = 0; b < 9; b++) {
      if (b == this.K) {
        paramGraphics.setColor((el.c % 10 > 5) ? 16776960 : 16711680);
        paramGraphics.fillRect(paramInt1 + b * 26 - 1, paramInt2 - 1, 26, 18);
      } 
      paramGraphics.drawRegion(g, 0, b << 4, 24, 16, 0, paramInt1 + b * 26, paramInt2, 0);
    } 
  }
  
  public void b() {
    super.b();
    this.u();
    coreLG.bl.b = false;
  }
  
  private void a(Graphics paramGraphics, int paramInt1, int paramInt2) {
    int b = !this.H ? this.J : -1;
    coreLG.dj.a(paramGraphics, this.p, b, paramInt1, paramInt2);
    for (b = 0; b < this.p.length; b++) {
      if (b == this.p.length - 1)
        a((coreLG.l.a(13)).d, paramInt1 + 36 + 9, paramInt2 + 28, paramGraphics);
      if (b == this.p.length - 2)
        a((coreLG.l.a(12)).d, paramInt1 + 18 + 9, paramInt2 + 28, paramGraphics);
    } 
  }
  
  private static void a(int paramInt1, int paramInt2, int paramInt3, Graphics paramGraphics) {
    model.bn.f.a(paramGraphics, (new StringBuffer(String.valueOf(paramInt1))).toString(), paramInt2, paramInt3, 0);
  }
  
  public String a(int paramInt) {
    for (byte b = 0; b < this.d.size(); b++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(b)).b == paramInt)
        return j.a; 
    } 
    return "";
  }
  
  public void a(int paramInt1, int paramInt2, Vector paramVector) {
    this.F = paramInt2;
    this.d = paramVector;
    b(paramInt1);
    for (paramInt1 = 0; paramInt1 < paramVector.size(); paramInt1++) {
      if (((j)paramVector.elementAt(paramInt1)).b == TerrainMidlet.e.b) {
        this.m = paramInt1;
        return;
      } 
    } 
  }
  
  public void b(int paramInt) {
    this.l = paramInt;
    for (paramInt = 0; paramInt < this.d.size(); paramInt++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(paramInt)).b == this.l)
        j.d = true; 
    } 
  }
  
  public void a(int paramInt1, String paramString, int paramInt2) {
    paramInt1 = f(paramInt1);
    ar ar;
    (ar = new ar()).a(90, this.D[paramInt1], this.E[paramInt1] - 10, paramString);
  }
  
  public void c(int paramInt) {
    for (byte b = 0; b < this.d.size(); b++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(b)).b == paramInt) {
        j.b = -1;
        j.a = "";
        j.d = false;
        if (b == this.m)
          this.u();
      } 
    } 
  }
  
  public void r() {
    for (byte b = 0; b < this.d.size(); b++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(b)).b == this.l) {
        j.d = true;
      } else {
        j.d = false;
      }
    } 
  }
  
  public void a(int paramInt, boolean paramBoolean) {
    for (byte b = 0; b < this.d.size(); b++) {
      model.j j;
      if ((j = (model.j) this.d.elementAt(b)).b == paramInt)
        j.d = paramBoolean;
    } 
  }
  
  public void d(int paramInt) {
    this.F = paramInt;
    for (paramInt = 0; paramInt < this.d.size(); paramInt++)
      ((j)this.d.elementAt(paramInt)).d = false; 
  }
  
  public void e(int paramInt) {
    if (el.z == null)
      this.u.a(paramInt); 
    super.e(paramInt);
  }
  
  static void a(bx parambx) {
    Vector vector = new Vector();
    if (parambx.l == TerrainMidlet.e.b) {
      vector.addElement(parambx.X);
      vector.addElement(parambx.Y);
    } 
    z z1 = new model.z(model.u.A(), new d(parambx));
    model.z z2 = new model.z("Kick..", new coreLG.z(parambx));
    if (!((model.j)parambx.d.elementAt(parambx.m)).d || parambx.l == TerrainMidlet.e.b)
      vector.addElement(parambx.R); 
    if (parambx.l == TerrainMidlet.e.b)
      vector.addElement(z1); 
    boolean bool = false;
    for (byte b = 0; b < parambx.d.size(); b++) {
      model.j j;
      if ((j = (model.j) parambx.d.elementAt(b)).b != TerrainMidlet.e.b && j.b != -1) {
        bool = true;
        break;
      } 
    } 
    if (bool)
      vector.addElement(parambx.S); 
    if (bool && parambx.l == TerrainMidlet.e.b)
      vector.addElement(z2); 
    if (bool) {
      model.z z3 = new model.z(model.u.s(), new x(parambx));
      vector.addElement(z3);
    } 
    vector.addElement(parambx.V);
    vector.addElement(parambx.W);
    vector.addElement(parambx.N);
    el.v.a(vector, 0);
  }
  
  static void b(bx parambx) {
    model.ad.a().a(true, -1);
    coreLG.el.a(model.u.cs());
  }
  
  static void c(bx parambx) {
    if (parambx.A == 0) {
      boolean bool;
      if (bool = ((j)parambx.d.elementAt(parambx.m)).d ? false : true) {
        if (TerrainMidlet.e.e < parambx.F) {
          el.a(model.u.av());
          return;
        } 
        parambx.A = 30;
      }
      coreLG.el.b(model.u.au());
      if (bool)
        model.ad.a().a(parambx.p);
      model.ad.a().a(bool);
    } 
  }
  
  static void d(bx parambx) {
    model.ad.a().d();
    el.b(model.u.cs());
  }
  
  static void e(bx parambx) {
    el.q.a(parambx);
  }
  
  static int f(bx parambx) {
    return 0;
  }
  
  static int g(bx parambx) {
    return parambx.J;
  }
  
  static boolean h(bx parambx) {
    return parambx.H;
  }
  
  static void a(bx parambx, boolean paramBoolean) {
    parambx.H = paramBoolean;
  }
  
  static void i(bx parambx) {
    (parambx = parambx).t.a(20, parambx.r + 10 + (parambx.J << 4), parambx.s, (parambx.p[parambx.J] >= 0) ? dj.a[parambx.p[parambx.J]] : "Slot Trống");
  }
  
  static boolean j(bx parambx) {
    return parambx.I;
  }
  
  static void b(bx parambx, boolean paramBoolean) {
    parambx.I = paramBoolean;
  }
  
  static int k(bx parambx) {
    return parambx.K;
  }
  
  static void a(bx parambx, int paramInt1, int paramInt2) {
    paramInt2 = paramInt2;
    paramInt1 = paramInt1;
    (parambx = parambx).t.a(50, paramInt1 + parambx.K * 24, paramInt2, a[parambx.K]);
  }
  
  static byte l(bx parambx) {
    return parambx.G;
  }
  
  static void m(bx parambx) {
    el.u.a(model.u.cg(), new g(parambx), new e(parambx), 3);
    model.bk bk = el.u;
  }
  
  static void n(bx parambx) {
    el.u.a(model.u.cd(), new i(parambx), new h(parambx), 0);
    model.bk bk = el.u;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bx.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */