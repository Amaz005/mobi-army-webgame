package coreLG;

import model.aa;
import model.ay;
import model.bl;
import model.bn;
import model.bq;
import model.d;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import model.s;
import model.y;

public class ci extends ds {
  public static byte a = 0;
  
  public static boolean b;
  
  public static boolean c;
  
  private int am;
  
  private int an;
  
  private int ao = 0;
  
  public static int d;
  
  public static int e;
  
  public static int f;
  
  public static int g;
  
  public static Image h;
  
  public static Image i;
  
  public static Image j;
  
  public static Image k;
  
  public static Image l;
  
  public static Image m;
  
  public static Image n;
  
  public static Image o;
  
  public static Image p;
  
  public static Image q;
  
  public static Image r;
  
  public static Image s;
  
  public static Image t;
  
  public static Image u;
  
  public static Image v;
  
  public static Image w;
  
  public static Image x;
  
  public static Image y;
  
  public static Image z;
  
  public static Image A;
  
  public static Image B;
  
  public static Image C;
  
  public static Image D;
  
  public static Image E;
  
  public static Image F;
  
  public static Image G;
  
  public static Image H;
  
  public static Image[] I = new Image[9];
  
  public static Image[] J = new Image[2];
  
  public static cv K;
  
  public static eb L;
  
  public static m M;
  
  public static bg N;
  
  public static Vector O;
  
  public static model.d P;
  
  static dy Q;
  
  public static int R;
  
  public static boolean S;
  
  public static int T;
  
  private static Image ap;
  
  private static s aq;
  
  private static s ar;
  
  public static boolean U;
  
  private byte as;
  
  private int at;
  
  private int au = -100;
  
  static String V;
  
  private boolean av;
  
  int W;
  
  private boolean aw;
  
  private static int ax;
  
  public static byte X;
  
  private Vector ay = new Vector();
  
  private int az;
  
  private int aA = 40;
  
  private model.ay aB;
  
  private int aC = 0;
  
  static int Y;
  
  private static Image aD;
  
  static {
    y filePack;
    h = (filePack = new y("/gui/gui")).a("fighter.png");
    i = filePack.a("mode.png");
    j = filePack.a("lock2.png");
    k = filePack.a("lock.png");
    l = filePack.a("hongTam.png");
    m = filePack.a("popupRound.png");
    n = filePack.a("item.png");
    o = filePack.a("team.png");
    p = filePack.a("fighter.png");
    q = filePack.a("logoGame.png");
    I[0] = filePack.a("r0.png");
    I[1] = filePack.a("r1.png");
    I[2] = filePack.a("r2.png");
    I[3] = filePack.a("arrowup.png");
    I[4] = filePack.a("tile1.png");
    r = filePack.a("quanham.png");
    s = filePack.a("menubg.png");
    z = filePack.a("curMapPos.png");
    A = bx.x;
    B = filePack.a("arrowRed.png");
    C = filePack.a("stat.png");
    D = filePack.a("trs.png");
    E = filePack.a("icon.png");
    F = filePack.a("itembar.png");
    u = filePack.a("shoptile.png");
    t = filePack.a("trangbitile.png");
    v = filePack.a("tienbar.png");
    w = filePack.a("soluongbar.png");
    x = filePack.a("simplebar.png");
    y = filePack.a("seller.png");
    ap = filePack.a("angle.png");
    aq = new model.s(filePack.a("bar.png"), 53, 13);
    ar = new s(filePack.a("wind.png"), 48, 16);
    G = filePack.a("chat.png");
    H = filePack.a("transparent.png");
    J[0] = filePack.a("msg0.png");
    J[1] = filePack.a("msg1.png");
    T = 0;
    U = false;
    V = "";
    try {
      aD = Image.createImage("/arrow.png");
    } catch (IOException iOException) {
    }
  }
  
  public void a(byte paramByte1, byte paramByte2, short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
    U = false;
    this.av = false;
    V = "";
    this.W = 3;
    K = new cv();
    L = new eb();
    coreLG.eb.b = new coreLG.bl[coreLG.eb.a];
    coreLG.eb.c = new int[coreLG.eb.a];
    coreLG.eb.d = new int[coreLG.eb.a];
    coreLG.eb.e = new int[coreLG.eb.a];
    coreLG.eb.g = new short[coreLG.eb.a];
    coreLG.eb.h = new short[coreLG.eb.a];
    N = new coreLG.bg();
    O = new Vector();
    P = new model.d();
    Q = new dy();
    M = new m();
    byte b = paramByte2;
    Q.a = b;
    K.a(paramByte1);
    this.aB = null;
    if (model.ac.c == 2) {
      this.aB = new ay();
      this.aB.a(0);
    } 
    if (model.ac.c == 10) {
      this.aB = new model.ay();
      if (K.e == 34)
        this.aB.a = 35; 
      if (K.e == 35)
        this.aB.a = 30; 
      if (K.e == 38)
        this.aB.a = 80; 
      if (K.e == 39)
        this.aB.a = 0; 
      this.aB.a(1);
    } 
    L.a(paramArrayOfshort1, paramArrayOfshort2);
    M.a(X);
  }
  
  public void d() {
    if (coreLG.m.a == 0) {
      if (!ds.ag) {
        if (el.I[2]) {
          coreLG.m.h = -20;
        }
      
        if (el.I[8]) {
          coreLG.m.h = 20;
        }
      
        if (el.I[4]) {
          coreLG.m.g = -20;
        }
      
        if (el.I[6]) {
          coreLG.m.g = 20;
        }
      }
    
      if (el.H[5] || el.H[12] || el.H[13]) {
        if (coreLG.bg.c && N.a.size() > 0) {
          M.a((coreLG.ah)N.a.elementAt(0));
        } else {
          M.a(eb.f);
        }
      
        ds.t();
      }
    
    } else if (!el.p.u.c().equals("")) {
      if (el.H[5] || el.H[12]) {
        if (this.aC == 0) {
          String var5 = el.p.u.c();
          model.ad.a().c(var5);
          el.p.u.a("");
          this.a(TerrainMidlet.e.b, var5);
          this.aC = this.az;
        } else {
          el.p.u.a("");
        }
      
        ds.t();
      }
    
      if (el.H[13]) {
        el.p.u.a();
        ds.t();
      }
    
    } else if (!V.equals("") && (el.H[5] || el.H[12] || el.H[13])) {
      for(int var2 = 0; var2 < eb.a; ++var2) {
        if (eb.b[var2] != null) {
          eb.b[var2] = null;
        }
      }
    
      el.p.b();
      ds.t();
    } else if (!this.aw) {
      if (el.H[12]) {
        Vector var4;
        (var4 = new Vector()).addElement(new model.z(model.u.K(), new coreLG.u(this)));
        if (eb.b()) {
          var4.addElement(new model.z(model.u.L(), new coreLG.v(this)));
        }
      
        if (eb.b()) {
          var4.addElement(new model.z(model.u.O(), new coreLG.w(this)));
        }
      
        if (eb.b() && !S && !coreLG.bg.c && eb.b[X].ad && this.W > 0) {
          var4.addElement(new model.z(model.u.N(), new coreLG.y(this)));
        }
      
        var4.addElement(new model.z(model.u.M(), new coreLG.n(this)));
        el.w.a(var4);
        ds.t();
      } else {
        if (el.H[13]) {
          coreLG.m.a = 0;
          ds.t();
        }
      
        if (coreLG.m.a == 1) {
          eb.b[X].d();
        }
      
      }
    } else {
      int var1 = eb.b[X].J.length;
      if (el.H[2]) {
        ax = coreLG.bi.a(var1, ax, 3, 2, (byte)0);
        ds.t();
      }
    
      if (el.H[8]) {
        ax = coreLG.bi.a(var1, ax, 3, 2, (byte)1);
        ds.t();
      }
    
      if (el.H[4]) {
        ax = coreLG.bi.a(var1, ax, 3, 2, (byte)2);
        ds.t();
      }
    
      if (el.H[6]) {
        ax = coreLG.bi.a(var1, ax, 3, 2, (byte)3);
        ds.t();
      }
    
      if (el.H[5] || el.H[12]) {
        int[] var3 = eb.b[X].J;
        if (S) {
          ds.t();
          eb.b[X].a(var3[ax], true, ax);
          if (var3[ax] == 0) {
            coreLG.bl var10000 = eb.b[0];
            var10000.ae += 30;
          }
        
          this.aw = false;
          return;
        }
      
        if (eb.b[X].K == -1 && var3[ax] >= 0 && var3[ax] != eb.b[X].K && eb.b()) {
          eb.b[X].a(var3[ax], false, ax);
          this.aw = false;
        }
      
        ds.t();
      }
    
      if (el.H[13]) {
        this.aw = false;
        ds.t();
      }
    
    }
  }
  
  public static void a() {
    if (L != null && eb.b != null && eb.b[X] != null) {
      model.ad.a().c();
      coreLG.el.c(model.u.cU());
      model.ad.a().b();
    } 
  }
  
  public void c() {
    if (S) {
      ci ci2 = this;
      switch (Y) {
        case 0:
          if (!(eb.b[0]).I) {
            Y = -1;
            el.a(model.u.bS(), new t(ci2));
          } 
          break;
        case 1:
          if ((eb.b[0]).O > 20) {
            Y = -1;
            el.a(model.u.bU(), new cs(ci2));
          } 
          break;
        case 2:
          if ((eb.b[1]).H > 514) {
            Y = -1;
            el.a(model.u.bX(), new ct(ci2));
          } 
          break;
        case 3:
          if ((eb.b[X]).ae == 100 || (eb.b[1]).H > cv.b || (eb.b[X]).H > cv.b) {
            Y = -1;
            el.a(model.u.bY(), new cq(ci2));
          } 
          break;
      } 
    } 
    if (this.aC > 0)
      this.aC--; 
    el.p.u.b();
    ci ci1;
    if ((ci1 = this).az > 0) {
      ci1.az--;
      if (ci1.az == 0) {
        if (ci1.ay.size() > 0)
          ci1.ay.removeElementAt(0); 
        if (ci1.ay.size() > 0)
          ci1.az = ci1.aA; 
      } 
    } 
    M.b();
    N.a();
    cv.a();
    eb eb1 = L;
    byte b = 0;
    boolean bool = false;
    for (byte b2 = 0; b2 < eb.b.length; b2++) {
      if (eb.b[b2] != null) {
        eb.b[b2].c();
        if ((eb.b[b2]).Q) {
          eb.e[b] = b2;
          eb.c[b] = (eb.b[b2]).G;
          eb.d[b] = (eb.b[b2]).H;
          b = (byte)(b + 1);
        } 
        if ((eb.b[b2]).I && (eb.b[b2]).aa != 5)
          bool = true; 
      } 
    } 
    if (el.c % 2 == 0) {
      if (++eb.i == 10) {
        eb.j = 0;
        eb.i = 0;
      } 
      if (eb.i <= 5) {
        eb.j++;
      } else {
        eb.j--;
      } 
    } 
    if (bg.j && !bool) {
      model.ad.a().g();
      coreLG.bl.b = false;
      (eb.b[eb.f]).c = false;
      bg.j = false;
    } 
    model.d d1 = P;
    for (b = 0; b < d1.a.size(); b++)
      ((aa)d1.a.elementAt(b)).a(); 
    if (this.aB != null)
      this.aB.a(); 
    for (byte b1 = 0; b1 < O.size(); b1++)
      ((model.bl)O.elementAt(b1)).a();
    dy dy1;
    if ((dy1 = Q).e) {
      dy1.d = System.currentTimeMillis();
      if (dy1.d - dy1.c >= 1000L) {
        dy1.b--;
        if (dy1.b == 0) {
          if (eb.b[eb.f] != null && (eb.b[eb.f]).K != -1)
            (eb.b[eb.f]).K = -1; 
        } else {
          dy1.c = dy1.d;
        } 
      } 
    } 
    if (++T > 10000)
      T = 0; 
    if (this.av) {
      this.au--;
      if (this.au < 50) {
        this.av = false;
        this.au = ds.aa / 2 - 15;
      } 
    } 
  }
  
  public void a(int paramInt1, int paramInt2) {
    int i = paramInt2;
    boolean bool = false;
  }
  
  public void a(byte var1, byte var2, int var3) {
    this.ay.removeAllElements();
    this.as = var2;
    this.at = var3;
    this.au = ds.aa / 2;
    this.av = true;
    Q.e = false;
    if (var1 == 0) {
      V = model.u.m();
    
      for(int var6 = 0; var6 < eb.b.length; ++var6) {
        if (eb.b[var6] != null) {
          eb.b[var6].ad = false;
          if (var6 == X) {
            cj.d("After Draw");
          }
        }
      }
    
    } else {
      if (eb.b[X] != null) {
        boolean var4;
        label66: {
          var4 = false;
          if (var1 == 1) {
            V = model.u.P();
            if (!eb.b[X].a) {
              var4 = false;
              break label66;
            }
          } else {
            V = model.u.Q();
            if (eb.b[X].a) {
              var4 = false;
              break label66;
            }
          }
        
          var4 = true;
        }
      
        if (var4 == eb.b[X].a) {
          var4 = var4;
        
          for(int var5 = 0; var5 < eb.b.length; ++var5) {
            if (eb.b[var5] != null) {
              eb.b[var5].ad = false;
              if (eb.b[var5].ae > 0) {
                if (eb.b[var5].a && var4) {
                  eb.b[var5].f();
                } else if (!eb.b[var5].a && !var4) {
                  eb.b[var5].f();
                }
              
                if (var5 == X) {
                  cj.d("After Set Win");
                }
              }
            }
          }
        }
      }
    
    }
  }
  
  public void a(Graphics paramGraphics) {
    coreLG.m.a(paramGraphics);
    cv.b(paramGraphics);
    if (this.aB != null)
      this.aB.c(paramGraphics); 
    cv.a(paramGraphics);
    if (U)
      bq.a(paramGraphics, coreLG.m.c, coreLG.m.d, ds.Z, ds.aa);
    Graphics graphics2 = paramGraphics;
    for (byte b = 0; b < eb.b.length; b++) {
      if (eb.b[b] != null)
        eb.b[b].a(graphics2); 
    } 
    N.a(paramGraphics);
    Graphics graphics4 = paramGraphics;
    d d1 = P;
    int k;
    for (k = 0; k < d1.a.size(); k++)
      ((model.aa)d1.a.elementAt(k)).a(graphics4);
    if (cv.d)
      cv.c(paramGraphics); 
    int i;
    for (i = 0; i < O.size(); i++)
      ((bl)O.elementAt(i)).a(paramGraphics); 
    if (this.aB != null)
      this.aB.a(paramGraphics); 
    if (b) {
      paramGraphics.setColor(16777215);
      paramGraphics.fillArc(coreLG.m.c + el.d / 2 - this.an, coreLG.m.d + el.d / 2 - this.an, this.am, this.am, 0, 360);
      this.an += 30;
      this.am += 60;
      if (this.an > el.d + 100) {
        this.an = 0;
        this.am = 0;
        k = 7;
        int i1 = e;
        i = d;
        int[] arrayOfInt1 = new int[7];
        int[] arrayOfInt2 = new int[7];
        arrayOfInt1[6] = i - 10;
        arrayOfInt2[6] = i1;
        arrayOfInt1[5] = i + 10;
        arrayOfInt2[5] = i1;
        arrayOfInt1[4] = i;
        arrayOfInt2[4] = i1 - 35;
        arrayOfInt1[3] = i;
        arrayOfInt2[3] = i1 - 70;
        arrayOfInt1[2] = i - 30;
        arrayOfInt2[2] = i1 - 70;
        arrayOfInt1[1] = i + 30;
        arrayOfInt2[1] = i1 - 70;
        arrayOfInt1[0] = i;
        arrayOfInt2[0] = i1 - 85;
        int[][] arrayOfInt = { arrayOfInt1, arrayOfInt2 };
        for (i1 = 0; i1 < 7; i1++);
        b = false;
      } 
    } 
    if (c) {
      this.ao++;
      if (this.ao % 2 == 0);
      if (this.ao == 10) {
        this.ao = 0;
        c = false;
      } 
    } 
    if (coreLG.m.b == 2 && T / 2 % 2 == 0) {
      paramGraphics.setColor(16711680);
      paramGraphics.fillRect(coreLG.m.c, coreLG.m.d, ds.Z, 10);
      paramGraphics.fillRect(coreLG.m.c, coreLG.m.d + ds.aa - 10, ds.Z, 10);
      paramGraphics.fillRect(coreLG.m.c, coreLG.m.d, 10, ds.aa);
      paramGraphics.fillRect(coreLG.m.c + ds.Z - 10, coreLG.m.d, 10, ds.aa);
    } 
    dy dy1;
    if (!S && (dy1 = Q).e)
      bn.d.a(paramGraphics, Integer.toString(dy1.b), coreLG.m.c + ds.Z - 16, coreLG.m.d + 2, 2);
    int j = coreLG.m.c + ds.Z / 2 - 24;
    k = coreLG.m.d + 4;
    ar.a(1, j, k, 0, 0, paramGraphics);
    int n = T / 2 % 3;
    if (R > 0) {
      paramGraphics.setClip(j + 24, k, R / 10 + 2 + n, 16);
    } else {
      paramGraphics.setClip(j + 24 - -R / 10 - 2 - n, k, -R / 10 + 2 + n, 16);
    } 
    if (R != 0)
      ar.a(0, j, k, 0, 0, paramGraphics);
    paramGraphics.setClip(coreLG.m.c, k, ds.Z, ds.aa);
    if (this.aw) {
      k = coreLG.m.d + el.f;
      j = coreLG.m.c + el.g - 27;
      paramGraphics.setColor(16777014);
      paramGraphics.fillRect(coreLG.m.c, k - 1, el.d, 36);
      dj.a(paramGraphics, (eb.b[X]).J, ax, j, k);
      bn.c.a(paramGraphics, model.u.bI(), coreLG.m.c + 5, coreLG.m.d + el.e - bn.b.a() - 4, 0);
      model.bn.c.a(paramGraphics, model.u.cI(), coreLG.m.c + el.d - 5, coreLG.m.d + el.e - bn.b.a() - 4, 1);
    } 
    if (!el.w.a && el.z == null && !this.aw) {
      paramGraphics.drawImage(cj.b, coreLG.m.c + 2, coreLG.m.d + ds.aa - 10, 0);
      if (coreLG.m.a != 0 || el.c % 10 > 5)
        paramGraphics.drawImage(cj.c, coreLG.m.c + ds.Z - 20, coreLG.m.d + ds.aa - 18, 0);
    } 
    if (eb.b()) {
      int i2 = (eb.b[X]).O;
      int i1 = (eb.b[X]).M;
      n = ((eb.b[X]).aa == 3) ? (eb.b[X]).V : 0;
      k = coreLG.m.d + ds.aa - 25;
      j = coreLG.m.c + (ds.Z >> 1);
      aq.a(1, j - 53 - 10, k, 0, 0, paramGraphics);
      aq.a(3, j + 10, k, 0, 0, paramGraphics);
      paramGraphics.setClip(j + 2 - 53 - 10, k, (40 - i2) * 49 / 40, 13);
      aq.a(0, j - 53 - 10, k, 0, 0, paramGraphics);
      paramGraphics.setClip(j + 2 + 10, k, n * 49 / 30, 13);
      aq.a(2, j + 10, k, 0, 0, paramGraphics);
      paramGraphics.setClip(coreLG.m.c, coreLG.m.d, ds.Z, ds.aa);
      if (i1 > 0) {
        paramGraphics.setColor(16482175);
        paramGraphics.drawLine(j + 2 + i1 * 49 / 30 + 10, k + 2, j + 2 + i1 * 49 / 30 + 10, k + 2 + 9);
      } 
      if ((eb.b[X]).U) {
        i1 = (eb.b[X]).N;
        n = ((eb.b[X]).aa == 3) ? (eb.b[X]).W : 0;
        k = coreLG.m.d + ds.aa - 25 - 15;
        j = coreLG.m.c + (ds.Z >> 1);
        aq.a(3, j + 10, k, 0, 0, paramGraphics);
        paramGraphics.setClip(j + 2 + 10, k, n * 49 / 30, 13);
        aq.a(2, j + 10, k, 0, 0, paramGraphics);
        paramGraphics.setClip(coreLG.m.c, coreLG.m.d, ds.Z, ds.aa);
        if (i1 > 0) {
          paramGraphics.setColor(16482175);
          paramGraphics.drawLine(j + 2 + i1 * 49 / 30 + 10, k + 2, j + 2 + i1 * 49 / 30 + 10, k + 2 + 9);
        } 
      } 
      n = (eb.b[X]).X;
      k = coreLG.m.d + ds.aa - 25;
      j = coreLG.m.c + (ds.Z >> 1);
      paramGraphics.drawImage(ap, j, k, 17);
      model.bn.f.a(paramGraphics, "" + (n >= 90 ? 180 - n : n), j, k + 2, 2);
    }
    ci ci1 = this;
    if (!V.equals("")) {
      bn.d.a(paramGraphics, V, coreLG.m.c + ds.Z / 2, coreLG.m.d + 80, 3);
      if (ci1.as != 0) {
        String str1 = (ci1.as >= 0) ? (String.valueOf(model.u.bH()) + ": +") : (String.valueOf(model.u.bH()) + ": ");
        bn.c.a(paramGraphics, String.valueOf(str1) + ci1.as, coreLG.m.c + ds.Z / 2, coreLG.m.d + 120, 3);
      } 
      bn.c.a(paramGraphics, String.valueOf(model.u.cE()) + ": " + ci1.at + "~", coreLG.m.c + ds.Z / 2, coreLG.m.d + ci1.au, 3);
    } 
    String str;
    if ((str = (eb.b[eb.f]).an) != null && !(eb.b[eb.f] instanceof dk))
      ((eb.b[eb.f]).a ? bn.e : bn.f).a(paramGraphics, str, coreLG.m.c + ds.Z - 16, coreLG.m.d + 22, 2);
    eb.a(paramGraphics);
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
    paramGraphics.setClip(0, 0, el.d, el.e);
    if (coreLG.m.a == 0) {
      Graphics graphics = paramGraphics;
      model.bn.c.a(graphics, model.u.bM(), el.g, el.f - 15, 2);
      byte b1 = 0;
      if (el.c % 10 > 4)
        b1 = 2; 
      graphics.drawImage(aD, b1 + 0, el.f, 6);
      graphics.drawRegion(aD, 0, 0, aD.getWidth(), aD.getHeight(), 2, el.d - b1, el.f, 10);
      graphics.drawRegion(aD, 0, 0, aD.getWidth(), aD.getHeight(), 5, el.g, b1 + 25, 17);
      graphics.drawRegion(aD, 0, 0, aD.getWidth(), aD.getHeight(), 6, el.g, el.e - 30 - b1, 33);
    } 
    if (!el.p.u.c().equals(""))
      el.p.u.a(paramGraphics); 
    paramGraphics.setClip(0, 0, ds.Z, ds.aa);
    d(paramGraphics);
    if (el.z != null)
      ds.c(paramGraphics); 
  }
  
  public void e(int paramInt) {
    if (el.z == null && coreLG.m.a != 0)
      el.p.u.a(paramInt); 
    super.e(paramInt);
  }
  
  public static void a(int paramInt) {
    R = paramInt;
  }
  
  public void b() {
    super.b();
  }
  
  public void a(int paramInt, String paramString) {
    this.ay.addElement(String.valueOf(el.p.a(paramInt)) + ": " + paramString);
    if (this.az == 0)
      this.az = this.aA; 
  }
  
  private void d(Graphics paramGraphics) {
    if (this.ay.size() == 0)
      return; 
    String str = (String) this.ay.elementAt(0);
    int i;
    if ((i = this.aA - this.az) > 10)
      i = 10; 
    int j = el.d;
    for (byte b = 0; b < i; b++)
      j >>= 1; 
    bn.c.a(paramGraphics, str, j + 3, el.e - 14, 0);
  }
  
  static void a(ci paramci, boolean paramBoolean) {
    paramci.aw = true;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ci.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */