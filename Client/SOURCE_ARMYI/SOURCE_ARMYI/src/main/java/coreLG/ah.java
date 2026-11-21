package coreLG;

import model.c;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.s;
import model.y;

public final class ah {
  private int i;
  
  private int j;
  
  private int k;
  
  private int l;
  
  private int m;
  
  private int n;
  
  private int o;
  
  private int p;
  
  private int q;
  
  private int r;
  
  private int s;
  
  private int t = 0;
  
  private boolean u = false;
  
  private boolean v = false;
  
  private boolean w = false;
  
  private byte x;
  
  private boolean y = false;
  
  private boolean z = false;
  
  private Image A = null;
  
  private s B = null;
  
  private int C = 0;
  
  private int D = 0;
  
  private boolean E = false;
  
  private boolean F = false;
  
  private boolean G = false;
  
  private boolean H = false;
  
  private boolean I = false;
  
  private boolean J;
  
  private boolean K = false;
  
  private boolean L = true;
  
  private boolean M = false;
  
  int a;
  
  /*
  private static Image N = (ak = new y("/item/item")).a("missile.png");
  
  private static Image O = ak.a("stone.png");
  
  private static Image P = ak.a("electric.png");
  
  static Image b = ak.a("bomb.png");
  
  private static Image Q = ak.a("specialbullet.png");
  
  private static Image R = ak.a("rangcuaBull.png");
  
  private static Image S = ak.a("grenade.png");
  
  private static Image T = ak.a("web.png");
  
  private static Image U = ak.a("chuoi.png");
  */
  private static Image N ;
  
  private static Image O ;
  
  private static Image P;
  
  static Image b;
  
  private static Image Q;
  
  private static Image R;
  
  private static Image S;
  
  private static Image T;
  
  private static Image U;
  
  private static Image V;
  
  private static Image W;
  
//  private static Image X = ak.a("gaBull.png");
  private static Image X;
  
  private static Image Y;
  
  private static Image Z;
  
  private static Image aa;
  
  private static Image ab;
  
  private static Image ac;
  
  private static Image ad;
  
  private static Image ae;
  
  private static Image af;
  
  private static Image ag;
  
  static Image c;
  
  private static Image ah;
  
  private static Image ai;
  
  private static Image aj;
  
  private static model.y ak;
  
  byte d;
  
  public static byte[] e;
  
  public short[] f;
  
  public short[] g;
  
  public int h = 0;
  
  private int al;
  
  private int am;
  
  private boolean an = false;
  
  private int ao = 0;
  
  private int ap = 0;
  
  private int aq = 4;
  
  private int ar;
  
  private int as;
  
  private int at;
  
  private int au;
  
  private int av = 0;
  
  private boolean aw = false;
  
  static {
     N = (ak = new y("/item/item")).a("missile.png");
     O = ak.a("stone.png");
     P = ak.a("electric.png");
     b = ak.a("bomb.png");
    Q = ak.a("specialbullet.png");
    R = ak.a("rangcuaBull.png");
    S = ak.a("grenade.png");
    T = ak.a("web.png");
    U = ak.a("chuoi.png");
    V = ak.a("rocket.png");
    W = ak.a("rocket2.png");
    X = ak.a("gaBull.png");
    Y = ak.a("trungvang.png");
    Z = ak.a("boomerang.png");
    aa = ak.a("chuotBull.png");
    ab = ak.a("thienthach.png");
    ac = ak.a("hammer.png");
    ad = ak.a("13Missile.png");
    ae = ak.a("khoang.png");
    af = ak.a("daodat.png");
    ag = ak.a("saobang.png");
    c = ak.a("gunIcon.png");
    ah = ak.a("bigMissile.png");
    ai = ak.a("laser.png");
    ak = null;
    try {
      aj = Image.createImage("/item/axit.png");
    } catch (IOException IOEx) {
      IOEx.printStackTrace();
    }
    e = null;
  }
  
  public ah(byte paramByte1, short[] paramArrayOfshort1, short[] paramArrayOfshort2, byte paramByte2) {
    this.f = paramArrayOfshort1;
    this.g = paramArrayOfshort2;
    this.i = paramArrayOfshort1[0];
    this.j = paramArrayOfshort2[0];
    this.d = paramByte1;
    this.x = paramByte2;
    byte b = paramByte1;
    switch (b) {
      case 0:
      case 32:
      case 41:
        this.A = O;
        this.E = true;
        break;
      case 1:
        this.B = new s(N, 12, 12);
        this.y = true;
        this.E = true;
        break;
      case 44:
        this.B = new s(ai, 19, 19);
        this.y = true;
        break;
      case 2:
        this.B = new s(P, 16, 16);
        this.G = true;
        break;
      case 8:
        this.B = new s(T, 12, 12);
        this.b();
        this.E = true;
        break;
      case 4:
      case 16:
        this.B = new s(Q, 32, 32);
        this.E = true;
        this.G = true;
        break;
      case 5:
      case 36:
        this.B = new s(Q, 32, 32);
        if (!(eb.b[eb.f]).D)
          this.E = true;
        break;
      case 6:
      case 12:
        this.B = new s(R, 12, 12);
        this.b();
        this.F = true;
        break;
      case 7:
      case 31:
        this.B = new model.s(S, 16, 16);
        this.b();
        this.E = true;
        break;
      case 3:
//        ah1.B = new s(b, 24, 24);
        this.B = new s(this.b, 24, 24);
        this.G = true;
        this.F = true;
        break;
      case 43:
        this.A = bl.q;
        break;
      case 9:
        this.B = new s(U, 14, 14);
        this.b();
        break;
      case 10:
        this.B = new s(V, 12, 12);
        this.y = true;
        this.F = true;
        break;
      case 11:
        this.B = new s(R, 12, 12);
        this.b();
        this.F = true;
        break;
      case 14:
      case 40:
        this.H = true;
        break;
      case 15:
        this.K = true;
        this.L = false;
        break;
      case 13:
        this.M = true;
        break;
      case 17:
        this.B = new model.s(ac, 20, 20);
        this.G = true;
        this.z = true;
        break;
      case 18:
        this.B = new s(ac, 20, 20);
        this.G = true;
        this.z = true;
        break;
      case 19:
        this.B = new s(X, 20, 20);
        this.y = true;
        break;
      case 20:
        this.B = new s(Y, 12, 11);
        this.G = true;
        break;
      case 21:
        this.B = new s(Z, 16, 16);
        this.G = true;
        break;
      case 22:
        this.B = new model.s(aa, 25, 13);
        this.G = true;
        this.z = true;
        break;
      case 23:
        this.B = new s(ag, 15, 15);
        this.G = true;
        this.y = true;
        break;
      case 24:
        this.B = new model.s(ab, 27, 28);
        this.G = true;
        break;
      case 25:
        this.B = new model.s(af, 16, 16);
        this.G = true;
        break;
      case 26:
      case 27:
      case 33:
        this.B = new s(W, 15, 15);
        this.y = true;
        break;
      case 28:
        this.B = new s(ad, 23, 23);
        this.G = false;
        break;
      case 29:
        this.B = new s(W, 15, 15);
        this.y = true;
        break;
      case 30:
        this.B = new s(ae, 32, 32);
        this.G = true;
        break;
      case 34:
        this.M = true;
        break;
      case 37:
        this.B = new s(ah, 18, 39);
        this.G = true;
        break;
      case 47:
        this.B = new s(aj, 15, 15);
        this.y = true;
        break;
    } 
    if (this.B != null) {
      this.q = this.B.a;
      this.r = this.B.b;
    } else if (this.A != null) {
      this.q = this.A.getWidth();
      this.r = this.A.getHeight();
    } else {
      this.q = 2;
      this.r = 2;
    } 
//    m.b = 0;
    coreLG.m.b = 0;
    if (paramByte1 == 31 || paramByte1 == 32 || paramByte1 == 35) {
      d();
      if ((eb.b[eb.f]).P != 13)
//        m.b = 2;
        coreLG.m.b = 2;
    }
    if ((eb.b[eb.f]).P == 11) {
      (eb.b[eb.f]).aA = -5;
      (eb.b[eb.f]).aB = 5;
      if (this.d == 36) {
        (eb.b[eb.f]).F = 1;
        (eb.b[eb.f]).k = this.f[this.f.length - 1];
        this.aw = true;
      } 
    } 
    if ((eb.b[eb.f]).P == 12 && this.d == 36) {
      (eb.b[eb.f]).k = this.f[this.f.length - 1];
      (eb.b[eb.f]).f = true;
      this.aw = true;
    } 
    if (paramByte1 != 42 && paramByte1 != 47)
      ci.P.a(paramArrayOfshort1[0], paramArrayOfshort2[0], (byte)0); 
    if (paramByte1 == 45) {
      el.O = true;
      el.P = 0;
    } 
//    v.a(1);
    model.v.a(1);
  }
  
  public static byte a(byte paramByte) {
    switch (paramByte) {
      case 0:
        return 0;
      case 1:
        return 1;
      case 2:
        return 2;
      case 3:
        return 9;
      case 4:
        return 10;
      case 5:
        return 11;
      case 6:
        return 19;
      case 7:
        return 21;
      case 8:
        return 17;
      case 12:
        return 33;
      case 13:
        return 37;
    } 
    return 0;
  }
  
  private void b() {
    if (this.i > (eb.b[eb.f]).G) {
      this.G = true;
      return;
    } 
    this.I = true;
  }
  
  private void c(int paramInt) {
    if (this.an) {
      this.ao += this.aq;
    } else {
      this.ao -= this.aq;
    } 
    this.aq = 2;
    this.m = this.i + (cj.b(cj.c(bg.b)) * this.ao >> 10);
    this.o = this.j - (cj.a(cj.c(bg.b)) * this.ao >> 10);
    this.n = this.i - (cj.b(cj.c(bg.b)) * this.ao >> 10);
    this.p = this.j - -(cj.a(cj.c(bg.b)) * this.ao >> 10);
    ci.P.a(this.m, this.o, (byte) ((paramInt == 0) ? 7 : 13));
    ci.P.a(this.n, this.p, (byte) ((paramInt == 0) ? 7 : 13));
    if (paramInt == 1) {
      this.ar = this.i + (cj.b(cj.c(bg.b)) * this.ao >> 9);
      this.as = this.j - (cj.a(cj.c(bg.b)) * this.ao >> 9);
      this.at = this.i - (cj.b(cj.c(bg.b)) * this.ao >> 9);
      this.au = this.j - -(cj.a(cj.c(bg.b)) * this.ao >> 9);
      ci.P.a(this.ar, this.as, (byte)7);
      ci.P.a(this.at, this.au, (byte)7);
    } 
    this.ap++;
    if (this.ap == 3) {
      this.ap = 0;
      this.an = !this.an;
    } 
  }
  
  private void c() {
    if (this.d == 26) {
      this.u = false;
      ci.N.e = 1;
    } 
    if (this.d == 17) {
      this.v = false;
      ci.N.e = 1;
    } 
  }
  
  public final void a() {
    if (this.h > 0 && this.f.length > 1)
      this.h++; 
    if (this.d == 36) {
      if (this.h < 1)
        this.h = 1; 
      if ((eb.b[eb.f]).D) {
        (eb.b[eb.f]).G = this.f[this.h - 1];
        (eb.b[eb.f]).H = this.g[this.h - 1];
      } 
    } 
    if (this.h > this.f.length - 1) {
      this.h = this.f.length - 1;
      c();
      this.i = this.f[this.h];
      this.j = this.g[this.h];
      if (this.d != 28) {
        if (this.d == 26)
          if (bg.k != 1) {
            this.u = true;
            ci.N.e = 4;
          } else {
            ci.N.e = 1;
          }  
        d();
      } 
      if (this.d == 28) {
        this.w = true;
        this.G = true;
      } 
      if (this.d != 6 && this.d != 5 && this.d != 22 && this.d != 30)
        for (byte b1 = 0; b1 < eb.b.length; b1++) {
          if (eb.b[b1] != null) {
            this.al = (eb.b[b1]).G - 12;
            this.am = (eb.b[b1]).H - 24;
            if (cj.a(this.i - this.q / 2, this.j - this.r / 2, this.q, this.r, this.al, this.am, 24, 24)) {
              eb.b[b1].c(((eb.b[b1]).Z == 0) ? 0 : 2);
              break;
            } 
          } 
        }  
    } 
    int i = this.i;
    int j = this.j;
    this.i = this.f[this.h];
    this.j = this.g[this.h];
    if (b((int)this.d)) {
      this.av++;
      if (bg.k > 1) {
        if (this.av == bg.n && this.d == 17) {
          this.v = true;
          ci.N.e += 3;
          d();
        } 
        if (this.av == bg.n && this.d == 19)
          bg.d(this.i, this.j); 
      } 
    } 
    if (this.H) {
      if (this.d == 14)
        c(0); 
      if (this.d == 40)
        c(1); 
    } 
    if (this.d == 25) {
      ah ah2;
      (ah2 = this).av++;
      if (ci.K.a(ah2.i, ah2.j)) {
        ci.P.a(ah2.i, ah2.j, cj.d(4), cj.b(-8, -5), (byte)2);
        ci.K.b(ah2.i, ah2.j, ah2.d);
      } 
      if (ah2.i < 0 || ah2.i > cv.a || ah2.j > cv.b || ah2.j < 100)
        ah2.d(); 
    } 
    if (this.d == 28 && this.w) {
      this.av++;
      if (this.av == 20) {
        this.av = 0;
        this.d();
      } 
    } 
    if (this.d == 30) {
      this .j += 2;
      if (ci.K.a(this.i, this.j)) {
        ci.K.b(this.i, this.j - 5, this.d);
        ci.P.a(this.i, this.j, cj.d(5), cj.d(5), (byte)2);
        ci.P.a(this.i, this.j, -cj.d(5), cj.d(5), (byte)2);
      } 
      this.av++;
      if (this.av == 0)
        this.d();
    } 
    if (this.M) {
      ci.P.a(this.i, this.j, (byte)1);
    } 
    if (this.y) {
      int k = cj.a(this.i - i, this.j - j);
      this.C = (k << 1) / 45;
      if (this.d == 44 && this.C > 7)
        this.C = 15 - this.C; 
    } 
    if (this.z)
      if (this.i > (eb.b[eb.f]).G) {
        this.t = 0;
      } else {
        this.t = 2;
      }  
    if (this.G) {
      if (this.d == 37) {
        this.a = cj.a(this.i - i, this.j - j);
        if (this.a == -90 || this.a == 270) {
          this.t = 0;
          if (el.c % 2 == 0)
            ci.P.a(this.i, this.j + 15, (byte)15); 
        } 
        if (this.a == 90) {
          this.t = 3;
          if (el.c % 2 == 0)
            ci.P.a(this.i, this.j - 15, (byte)15); 
        } 
      } 
      a(false);
    } else if (this.I) {
      a(true);
    } 
    if (this.E)
      b((byte)0); 
    if (this.F)
      b((byte)1); 
    if (this.d == 4 && ci.a == 0)
      ci.U = true; 
    if (this.d == 23) {
      ci.P.a(this.i - cj.d(5), this.j - cj.d(5), (byte)11);
      ci.P.a(this.i + cj.d(5), this.j + cj.d(5), (byte)11);
      ci.P.a(this.i + cj.d(5), this.j - cj.d(5), (byte)11);
    } 
    if (this.d == 15) {
      if (!this.L) {
        this.s++;
        if (this.s == 26) {
          ci.N.a(this, true, this.i, this.j, this.k, this.l, this.x);
          return;
        } 
        if (this.s == 5 || this.s == 8)
          this.K = true; 
      } 
      if (this.K) {
        this.K = false;
        d();
      } 
    } 
    if (this.h == 0 && this.f.length > 1)
      this.h++;
    if (cv.d && !this.J) {
      byte var4 = 0;
      if (this.d == 0) {
        var4 = 1;
      }
    
      if (cv.a(this.i, this.j, var4)) {
        this.J = true;
      }
    }
    
//    if (cv.d && !this.J) {
//      boolean bool = false;
//      if (this.d == 0)
//        bool = true;
////      if (cv.a(this.i, this.j, bool))
//      if (cv.a(this.i, this.j, bool))
//        this.J = true;
//    }
//    byte b = (this.d != 19) ? 100 : 500;
    byte b = (byte) ((this.d != 19) ? 100 : 500);
    if (this.i < -b || this.i > cv.a + b || this.j > cv.b + 100) {
      if (ci.U)
        ci.U = false; 
      this.k = this.i;
      this.l = this.j;
      c();
      ci.N.a(this, false, this.i, this.j, this.k, this.l, this.x);
      return;
    } 
  }
  
  private void d() {
    if (this.d == 34) {
    
    } else if (this.d == 4) {
      bg.a(this.i, this.j);
    } else if (this.d == 14) {
      bg.b(this.i, this.j);
    } else if (this.d == 13) {
      bg.c(this.i, this.j);
    } else if (this.d == 23) {
      bg.f(this.i, this.j);
    } else if (this.d == 16) {
      ci.N.e(this.i, this.j);
    } else if (this.d == 17) {
      if (this.v)
        ci.N.a(this.i, this.j, 0, this.a); 
      ci.K.b(this.i, this.j, this.d);
    } else if (this.d == 26) {
      System.out.println("VY= " + Character.MIN_VALUE);
      if (this.u)
        bg.b(0, 0, 0, 0); 
      ci.K.b(this.i, this.j, this.d);
    } else if (this.d == 28) {
      bg.g(this.i, this.j);
    } else if (this.d == 30) {
    
    } else if (this.d == 5 || this.d == 36) {
      eb.c(this.x, (short)this.i, (short)this.j);
      (eb.b[this.x]).I = true;
      (eb.b[this.x]).ad = false;
      if (!(eb.b[eb.f]).D) {
      
      } else {
        if ((eb.b[eb.f]).P == 11) {
          (eb.b[eb.f]).F = 0;
          (eb.b[eb.f]).aB = 8;
          ci.P.a(this.i, this.j, cj.d(4), cj.b(-8, -5), (byte)3);
          ci.P.a(this.i, this.j, cj.d(3), cj.b(-8, -4), (byte)3);
          ci.P.a(this.i, this.j, cj.d(2), cj.b(-8, -4), (byte)3);
        } 
        if ((eb.b[eb.f]).P == 12) {
          (eb.b[eb.f]).g = false;
          (eb.b[eb.f]).f = false;
        } 
      } 
    } else if (this.d == 8) {
      cv.a(new bn(this.i - 21, this.j - 20, bn.b, true));
    } else if (this.d == 47) {
      ci.K.b(this.i, this.j, this.d);
    } else {
      cj.d("model.x Last= " + this.i + " model.y Last= " + this.j);
      ci.K.b(this.i, this.j, this.d);
      byte b1 = 3;
      if (this.d == 0 || this.d == 7 || this.d == 3 || this.d == 31 || this.d == 32) {
        if (this.d == 31 || this.d == 32)
          (eb.b[eb.f]).e = false; 
        b1 = 2;
      } 
      if (this.d == 31) {
        ci.P.a(this.i - 30, this.j + 5, (byte)4);
        ci.P.a(this.i, this.j, (byte)4);
        ci.P.a(this.i + 30, this.j + 5, (byte)4);
        ci.P.a(this.i, this.j, cj.d(4), cj.b(-8, -5), (byte)3);
        ci.P.a(this.i, this.j, cj.d(3), cj.b(-8, -4), (byte)3);
        ci.P.a(this.i, this.j, cj.d(2), cj.b(-8, -4), (byte)3);
      } 
      byte b2 = 0;
      byte b3;
      for (b3 = 0; b3 < 2; b3++) {
//        b2 = (cj.a.nextInt(b1) < 1) ? 3 : 2;
        b2 = (byte) ((cj.a.nextInt(b1) < 1) ? 3 : 2);
        if (this.d == 19)
          b2 = 8; 
        ci.P.a(this.i, this.j, cj.d(4), cj.b(-8, -5), b2);
        ci.P.a(this.i, this.j, cj.d(3), cj.b(-8, -4), b2);
      } 
      if (this.d == 3) {
        ci.P.a(this.i - 30, this.j + 5, (byte)4);
        ci.P.a(this.i, this.j, (byte)4);
        ci.P.a(this.i + 30, this.j + 5, (byte)4);
      } else if (this.d == 15) {
        ci.P.a(this.i, this.j, (byte)4);
      } else if (this.d != 13) {
        if (this.d == 19) {
          ci.P.a(this.i, this.j, cj.d(4), cj.b(-8, -5), (byte)10);
        } else if (this.d == 22) {
          ci.P.a(this.i, this.j, cj.d(4), cj.b(-8, -5), (byte)9);
        } else if (this.d == 35) {
          if ((eb.b[eb.f]).P != 13);
        } else if (this.d == 37) {
          ci.b = true;
          ci.d = this.i;
          ci.e = this.j;
        } else if (this.d == 40) {
          ci.c = true;
          ci.g = this.i;
          ci.f = this.j;
        } else if (this.d == 42) {
          for (b3 = 0; b3 < eb.b.length; b3++) {
            bl bl;
            if ((bl = eb.b[b3]) != null && bl.P == 14 && bl.aa != 5)
              bg.m.addElement(new bp(bl.G, bl.H, this.i, this.j)); 
          } 
          bg.l = true;
        } else if (this.d == 45) {
          ci.P.a((eb.b[this.x]).G, (eb.b[this.x]).H, this.i, this.j, 0);
          ci.c = true;
          ci.g = this.i;
          ci.f = this.j;
        } else if (this.d == 43) {
        
        } else if (this.d == 44) {
        
        } else {
          ci.P.a(this.i, this.j, (byte)4);
        } 
      } 
      if (bg.d) {
        System.out.println("voi rong ");
        Vector vector = bg.i;
        for (b1 = 0; b1 < vector.size(); b1++) {
//          c c = vector.elementAt(b1);
          c c = (model.c) vector.elementAt(b1);
          if (this.i < c.a + 10)
            c.getClass(); 
        } 
      } 
//      v.a(0);
      model.v.a(0);
      TerrainMidlet.a(100);
    } 
    if (this.d == 43) {
      // m.b = 0;
      coreLG.m.b = 0;
    } else {
      if ((this.d == 3 || this.d == 15)) {
        //m.b = 2;
        coreLG.m.b = 2;
      } else {
//        m.b = 1;
        coreLG.m.b = 1;
      }
    } 
    if (this.d != 15)
      ci.N.a(this, true, this.i, this.j, this.k, this.l, this.x); 
    if (bl.d) {
      eb.a();
      bl.d = false;
    } 
  }
  
  public static boolean a(int paramInt) {
    return (paramInt == 4 || paramInt == 14 || paramInt == 16 || paramInt == 23 || paramInt == 28);
  }
  
  public static boolean b(int paramInt) {
    return (paramInt == 17 || paramInt == 19);
  }
  
  private void a(boolean paramBoolean) {
    if (!paramBoolean) {
      if (this.d == 3) {
        this.C = ci.T / 2 % 2;
      } else if (this.d == 4) {
        this.C = ci.T / 2 % 2;
      } else if (this.d == 20) {
        this.C = ci.T / 2 % 2;
      } else {
        this.C++;
      } 
      if (this.C == this.B.c) {
        this.C = 0;
        return;
      } 
    } else {
      if (this.C < 0) {
        this.C = this.B.c - 1;
        return;
      } 
      if (this.d == 4) {
        this.C = ci.T / 2 % 2;
        return;
      } 
      this.C--;
    } 
  }
  
  private void b(byte paramByte) {
    this.D++;
    if (this.D > 3) {
      ci.P.a(this.i, this.j, paramByte);
      this.D = 0;
    } 
  }
  
  public final void a(Graphics paramGraphics) {
    if (this.aw)
      return; 
    if (this.d == 31)
      return; 
    if (this.B != null) {
      this.B.a(this.C, this.i, this.j, this.t, 3, paramGraphics);
      return;
    } 
    if (this.A != null) {
      paramGraphics.drawImage(this.A, this.i, this.j, 3);
      return;
    } 
    if (!this.L) {
      paramGraphics = paramGraphics;
      int i = this.i - 10;
      if (this.s < 15) {
        paramGraphics.setColor(16771821);
        paramGraphics.fillRect(i, -500, 20, this.j + 20 - -500);
        paramGraphics.setColor(16756407);
        paramGraphics.fillRect(i + 2, -500, 16, this.j + 20 - -500);
        paramGraphics.setColor(16737907);
        paramGraphics.fillRect(i + 5, -500, 10, this.j + 20 - -500);
        paramGraphics.setColor(15745615);
        paramGraphics.fillRect(i + 8, -500, 4, this.j + 20 - -500);
      } 
      if (this.s >= 15 && this.s < 17) {
        paramGraphics.setColor(16771821);
        paramGraphics.fillRect(i + 3, -500, 14, this.j + 20 - -500);
        paramGraphics.setColor(16737907);
        paramGraphics.fillRect(i + 5, -500, 10, this.j + 20 - -500);
        paramGraphics.setColor(15745615);
        paramGraphics.fillRect(i + 8, -500, 4, this.j + 20 - -500);
      } 
      if (this.s >= 17 && this.s < 20) {
        paramGraphics.setColor(16771821);
        paramGraphics.fillRect(i + 6, -500, 8, this.j + 20 - -500);
        paramGraphics.setColor(15745615);
        paramGraphics.fillRect(i + 8, -500, 4, this.j + 20 - -500);
      } 
      if (this.s >= 20 && this.s < 23) {
        paramGraphics.setColor(16771821);
        paramGraphics.fillRect(i + 8, -500, 4, this.j + 20 - -500);
      } 
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ah.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */