package coreLG;

import model.am;
import model.ap;
import model.bc;
import model.bi;
import model.bk;
import model.bn;
import model.bx;
import model.i;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import model.q;
import model.s;
import model.z;

public class dl extends ds {
  public bc a;
  
  public model.bc b;
  
  int c;
  
  private int q;
  
  private int r;
  
  public static Image d;
  
  private static Image s;
  
  public static boolean e;
  
  public static int f = 0;
  
  public static int g = 25;
  
  private boolean t;
  
  private boolean u;
  
  private boolean v;
  
  public boolean h;
  
  private boolean w;
  
  public int i;
  
  private int x;
  
  private int y;
  
  private int z;
  
  private int A;
  
  private int B;
  
  private int C;
  
  private int D;
  
  private int E;
  
  private int[] F;
  
  private int[] G;
  
  private int[] H;
  
  private int[] I;
  
  public static s j;
  
  public static model.s k;
  
  public static model.s l;
  
  private Image J;
  
  private Image K;
  
  public Vector m;
  
  public Vector n;
  
  public Vector o;
  
  String p = "";
  
  private int L;
  
  private static int M = 50;
  
  public void b() {
    h();
    super.b();
  }
  
  public dl() {
    dl dl1;
    (dl1 = this).h = true;
    dl1.i = 0;
    if ((M = model.bx.b("sound")) == 0)
      M = 50; 
    dl1.J = ci.o;
    if (TerrainMidlet.a == 5)
      try {
        dl1.K = Image.createImage("/vmb.png");
      } catch (IOException iOException) {} 
    dl1.B = el.e + 50;
    dl1.C = el.g;
    dl1.D = el.g;
    dl1.E = el.e + 15;
    s = ci.p;
    dl1.x = -s.getWidth();
    dl1.y = 20;
    dl1.z = 2;
    dl1.u = true;
    dl1.F = new int[] { el.d, el.d + 100, el.d - 10, el.d + 50 };
    dl1.G = new int[] { 40, 70, 90, 160 };
    dl1.H = new int[] { -5, -3, -4, -3 };
    dl1.I = new int[4];
    dl1.v = true;
    j = new model.s(model.aa.a, 14, 15);
    k = new model.s(coreLG.ah.b, 24, 24);
    l = new model.s(model.aa.c, 59, 64);
    dl1.m = new Vector();
    dl1.n = new Vector();
    dl1.o = new Vector();
    d = ci.q;
    ci.a = (byte) model.bx.b("graphic");
    coreLG.bn.a = (model.bx.b("drawRGB") == 0);
    this.r = el.d - 30;
    if (this.r < 70)
      this.r = 70; 
    if (this.r > 99)
      this.r = 99; 
    this.q = (el.d - this.r >> 1) + 29;
    this.a = new model.bc();
    this.a.b = el.f - ds.ab - 9;
    this.a.c = this.r;
    this.a.d = ds.ab + 2;
    this.a.e = true;
    this.a.b(3);
    this.b = new model.bc();
    this.b.b = el.f - 4;
    this.b.c = this.r;
    this.b.d = coreLG.ds.ab + 2;
    this.b.e = false;
    this.b.b(2);
    this.a.a(model.bx.a("caroun"));
    this.b.a(model.bx.a("caropass"));
    h();
    this.c = 0;
    this.ad = new model.z(model.u.cl(), new ch(this));
    this.ac = new model.z("Menu", new ca(this));
  }
  
  public static void a() {
    coreLG.el.b(model.u.cr());
    model.bi.a().a("socket://" + TerrainMidlet.d + ":" + TerrainMidlet.c);
    model.ad.a().c(TerrainMidlet.a);
    coreLG.cj.d("DATA= " + ap.a);
    model.ad.a().f(model.ap.a);
  }
  
  protected void a(String paramString) {
    coreLG.el.a(model.u.h(), new ec(this, paramString), new ee(this));
  }
  
  protected void e() {
    i i;
    String str1 = this.a.c().toLowerCase().trim();
    String str2 = this.b.c();
    if (str1.equals("")) {
      el.a(model.u.aL());
      return;
    } 
    if (str2.equals("")) {
      el.y.a(model.u.aK(), null, new model.z("OK", new ed(this)), null);
      el.z = (q)(i = el.y);
      return;
    } 
    el.u.a(model.u.aJ(), new eg((dl)this), new ei((dl)this), 2);
    bk bk = el.u;
  }
  
  protected void f() {
    if (!(bi.a()).c) {
      a();
    } else {
      el.b(model.u.cs());
    } 
    model.ad.a().d(this.a.c().toLowerCase());
  }
  
  protected void g() {
    i i;
    String str1 = this.a.c().toLowerCase().trim();
    String str2 = this.b.c();
    if (str1.equals("")) {
      el.a(model.u.aH());
      return;
    } 
    if (str2.equals("")) {
      el.y.a(model.u.aN(), null, new z("OK", new eh(this)), null);
      el.z = (q)(i = el.y);
      return;
    } 
    if (!(model.bi.a()).c) {
      a();
    } else {
      el.b(model.u.aM());
    } 
    model.ad.a().a(this.a.c().toLowerCase(), this.b.c(), "1.6.5");
  }
  
  public void c() {
    int b1 = this.w ? 4 : 1;
    for (byte b2 = 0; b2 < b1; b2++) {
      if (this.v) {
        for (byte b = 0; b < 4; b++) {
          this.F[b] = this.F[b] + this.H[b] * (2 - b % 2);
          this.G[b] = this.G[b] + this.I[b] * (2 - b % 2);
          if (this.i == 0 && this.F[b] + model.am.b[b % 2].getWidth() < 0)
            this.F[b] = el.d + 50;
          if (this.i == 1 && this.G[b] + model.am.b[b % 2].getHeight() < 0) {
            this.G[b] = el.e + b * 30;
            this.I[b] = -10;
            this.H[b] = 0;
            this.F[b] = el.c % 100 + b * 30;
          } 
        } 
      } 
      if (this.h) {
        am.a();
      } else if (this.t) {
        if (this.u) {
          this.x += this.z;
          this.y += this.A;
          if (this.y < -s.getHeight())
            this.u = false;
          if (el.c % 5 == 0)
            this.m.addElement(new bh(this, this.x - 15, this.y + 18));
          if (el.c % 7 == 0)
            this.m.addElement(new bh(this, this.x - 10, this.y + 13));
          if (this.x > el.g - 120 && this.n.size() < 8 && el.c % 8 == 0) {
            this.n.addElement(new cn(this, this.x + 24, this.y + 28, this.n.size()));
            if (this.n.size() == 8) {
              this.i = 1;
              this.A = -8;
              for (byte b = 0; b < 2; b++)
                this.I[b] = -8;
            } 
          } 
        }
        int i;
        for (i = 0; i < this.m.size(); i++) {
          bh bh;
          (bh = (coreLG.bh) this.m.elementAt(i)).c++;
          if (bh.c == 4)
            bh.d.m.removeElement(bh); 
        }
        for (i = 0; i < this.n.size(); i++) {
          cn cn;
          if ((cn = (coreLG.cn) this.n.elementAt(i)).f.i == 0) {
            cn.b += cn.e + 4 + (8 - cn.d >> 1);
          } else if (cn.f.i == 1) {
            cn.b += cn.e + (8 - cn.d >> 1);
          } else {
            cn.b += cn.e + 8 + (8 - cn.d >> 1);
            int j;
            if (cn.f.i == 2 && (j = cn.b - el.e) >= 0 && j < 30) {
              cn.f.o.addElement(new fc(cn.f, cn.a, cn.b - 60));
              cn.f.n.removeElement(cn);
            } 
          } 
          if (el.c % 5 == 0)
            cn.f.m.addElement(new bh(cn.f, cn.a - 3, cn.b - 3)); 
          if (el.c % 3 == 0)
            cn.c = (cn.c == 0) ? 1 : 0; 
        }
        for (i = 0; i < this.o.size(); i++) {
          fc fc;
          (fc = (coreLG.fc) this.o.elementAt(i)).c++;
          if (fc.c == 5)
            fc.d.o.removeElement(fc); 
        } 
        if (this.i == 1) {
          this.C--;
          this.B -= 5;
          if (this.B < -this.J.getHeight()) {
            this.i = 2;
            for (i = 0; i < 2; i++)
              this.I[i] = -4;
            this.L = 20;
          } 
        } 
        if (this.L > 0) {
          this.L--;
          if (this.L == 0) {
            this.i = 3;
            if (j != null)
              j.a(); 
            if (l != null)
              l.a(); 
            if (k != null)
              k.a(); 
            j = null;
            l = null;
            k = null;
            s = null;
            this.J = null;
          } 
        } 
        if (this.i == 3) {
          this.E -= 5;
          i = this.a.b - 40;
          if (this.E < i) {
            this.E = i;
            this.t = false;
            this.w = false;
            this.ae = this.a.h;
          } 
        } 
      } else {
        model.am.a();
        if (this.a.a != this.q)
          this.a.a += this.q - this.a.a >> 1; 
        if (this.b.a != this.q)
          this.b.a += this.q - this.b.a >> 1; 
        this.a.b();
        this.b.b();
        if (e && (f += 2) > g)
          f = g; 
      } 
    } 
  }
  
  public void e(int paramInt) {
    if (el.z == null)
      if (this.c == 0) {
        this.a.a(paramInt);
      } else {
        this.b.a(paramInt);
      }  
    super.e(paramInt);
  }
  
  public void a(Graphics paramGraphics) {
    paramGraphics.setColor(6606845);
    paramGraphics.fillRect(0, 0, ds.Z, ds.aa);
    ds.c(paramGraphics);
    int i;
    for (i = 0; i <= el.d; i += 32)
      paramGraphics.drawImage(coreLG.bx.h, i, el.e - 62, 0);
    if (this.h) {
      model.am.a(paramGraphics);
      i = el.f - 38;
      bn.c.a(paramGraphics, model.u.ch(), el.g, i, 2);
      model.bn.c.a(paramGraphics, String.valueOf(M) + "%", el.g, i + 18, 2);
      paramGraphics.drawRegion(coreLG.bx.v[3], 0, 0, 13, 11, 4, el.g - 30 + el.c % 3, i + 27, 3);
      paramGraphics.drawRegion(coreLG.bx.v[3], 0, 0, 13, 11, 7, el.g + 30 - el.c % 3, i + 27, 3);
      model.bn.b.a(paramGraphics, model.u.ci(), 3, el.e - 17, 0);
      bn.b.a(paramGraphics, model.u.bF(), el.d - 3, el.e - 17, 1);
      if (coreLG.el.d > 200) {
        paramGraphics.drawImage(el.N, el.g, 10, 3);
        return;
      } 
    } else {
      if (this.t) {
        paramGraphics.drawImage(model.am.a, 30, 40, 0);
        if (!this.w)
          bn.b.a(paramGraphics, model.u.cj(), el.d - 3, el.e - 17, 1);
        paramGraphics.setClip(0, 0, el.d, el.e - 20);
        paramGraphics.drawImage(d, this.D, this.E, 3);
        if (this.J != null)
          paramGraphics.drawImage(this.J, this.C - 40, this.B - 20 + el.c % 6 / 2, 0);
        if (this.K != null)
          paramGraphics.drawImage(this.K, el.d - this.C - 30, this.B + el.c % 10 / 2, 0);
        if (this.u) {
          if (s != null)
            paramGraphics.drawImage(s, this.x, this.y + el.c % 8 / 3, 0);
        } 
        d(paramGraphics);
        if (this.v)
          g(paramGraphics); 
        e(paramGraphics);
        f(paramGraphics);
        return;
      } 
      model.bn.g.a(paramGraphics, "1.6.5", el.d - 2, 2, 1);
      model.am.a(paramGraphics);
      if (this.v)
        g(paramGraphics); 
      paramGraphics.drawImage(d, this.D, this.E, 3);
      bn.c.a(paramGraphics, String.valueOf(model.u.cn()) + ":", this.a.a - 56, this.a.b + 4, 0);
      this.a.a(paramGraphics);
      paramGraphics.setClip(0, 0, el.d, el.e);
      bn.c.a(paramGraphics, String.valueOf(model.u.co()) + ":", this.b.a - 56, this.b.b + 4, 0);
      bn.c.a(paramGraphics, model.u.cp(), this.q - 56, el.f + 36, 0);
      bn.c.a(paramGraphics, model.u.cq(), this.q - 56, el.f + 50, 0);
      this.b.a(paramGraphics);
      super.a(paramGraphics);
      if (e)
        el.y.a(String.valueOf(model.u.b()) + " " + f + "%", null, null, null);
    } 
  }
  
  private void d(Graphics paramGraphics) {
    if (k != null)
      for (byte b = 0; b < this.n.size(); b++) {
        coreLG.cn cn = (coreLG.cn) this.n.elementAt(b);
        k.a(cn.c, cn.a, cn.b, 0, 0, paramGraphics);
      }  
  }
  
  private void e(Graphics paramGraphics) {
    if (j != null)
      for (byte b = 0; b < this.m.size(); b++) {
        coreLG.bh bh = (coreLG.bh) this.m.elementAt(b);
        j.a(bh.c, bh.a, bh.b, 0, 0, paramGraphics);
      }  
  }
  
  private void f(Graphics paramGraphics) {
    if (l != null)
      for (byte b = 0; b < this.o.size(); b++) {
        coreLG.fc fc = (coreLG.fc) this.o.elementAt(b);
        l.a(fc.c, fc.a, fc.b, 0, 0, paramGraphics);
      }  
  }
  
  private void g(Graphics paramGraphics) {
    for (byte b = 0; b < 4; b++)
      paramGraphics.drawImage(model.am.b[b % 2], this.F[b], this.G[b], 0);
  }
  
  public void d() {
    if ((el.H[2] || el.H[8]) && !this.h) {
      coreLG.ds.t();
      coreLG.ds.ai = false;
      coreLG.ds.aj = false;
      if (this.c == 0) {
        this.c = 1;
        this.a.e = false;
        this.b.e = true;
        this.ae = this.b.h;
      } else {
        this.c = 0;
        this.a.e = true;
        this.b.e = false;
        this.ae = this.a.h;
      } 
    } 
    if (this.h) {
      if (el.H[12]) {
        this.h = false;
        this.t = true;
        TerrainMidlet.c(0);
        TerrainMidlet.a();
        TerrainMidlet.b(M);
        model.v.b(M);
        model.bx.a("sound", M);
        coreLG.ds.t();
      } 
      if (coreLG.el.H[13]) {
        this.h = false;
        this.t = true;
        TerrainMidlet.c(0);
        TerrainMidlet.a();
        TerrainMidlet.b(M = 0);
        model.v.b(M = 0);
        model.bx.a("sound", M);
        coreLG.ds.t();
      } 
      if (el.H[4]) {
        if ((M -= 10) < 0)
          M = 0;
        coreLG.ds.t();
      } 
      if (coreLG.el.H[6]) {
        if ((M += 10) > 100)
          M = 100;
        coreLG.ds.t();
        return;
      } 
    } else if (this.t) {
      if (el.H[13]) {
        this.w = true;
        coreLG.ds.t();
        return;
      } 
    } else {
      if (this.w)
        return; 
      super.d();
    } 
  }
  
  private void h() {
    this.a.a = -this.q;
    this.b.a = el.d + this.q;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */