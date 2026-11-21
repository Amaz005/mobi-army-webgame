package model;

import coreLG.ci;
import coreLG.cj;
import coreLG.cv;
import coreLG.el;
import coreLG.m;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class aa {
  private s d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h;
  
  private int i;
  
  private int j;
  
  private int k;
  
  private boolean l;
  
  private int m;
  
  private boolean n;
  
//  public static Image a = (B = new y("/effect/effect")).a("smoke.png");
  public static Image a;
  
  private static Image o;
  
  private static Image p;
  
  private static Image q;
  
  private static Image r;
  
//  private static Image s = B.a("longga.png");
  private static Image s;
  
  private static Image t;
  
  private static Image u;
  
  private static Image v;
  
  private static Image w;
  
  private static Image x;
  
  private static Image y;
  
  public static Image b;
  
  public static Image c;
  
  private static Image z;
  
  private static Image A;
  
  private static y B;
  
  private byte C;
  
  private int D;
  
  private int E;
  
  private int F;
  
  private int[] G;
  
  private int[] H;
  
  private int[] I;
  
  private int J;
  
  private int K;
  
  private int L;
  
  private int M;
  
  private boolean N;
  
  private int O;
  
  static {
    a = (B = new y("/effect/effect")).a("smoke.png");
    o = B.a("blacksmoke.png");
    u = B.a("blueSmoke.png");
    s = B.a("longga.png");
    w = B.a("chuotbay.png");
    x = B.a("gatruilong.png");
    y = B.a("water.png");
    b = B.a("locxoay.png");
    c = B.a("ex3.png");
    t = B.a("lacay.png");
    v = B.a("blueSmoke2.png");
    z = B.a("lacay2.png");
    A = B.a("khoi.png");
    p = (B = new y("/map/model.bg")).a("rock1.png");
    q = B.a("rock2.png");
    r = B.a("cobay.png");
    try {
      Image.createImage("/effect/bat.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    }
  }
  
  public aa(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    this.D = paramInt1;
    this.E = paramInt2;
    this.e = paramInt3;
    this.f = paramInt4;
    this.K = paramInt5;
    this.k = 0;
    this.C = 16;
    this.N = true;
    if (paramInt5 == 0)
      this.O = 25; 
    if (paramInt5 == 1)
      this.O = 15; 
  }
  
  public aa(int paramInt1, int paramInt2, byte paramByte) {
    byte b;
    byte b1;
    this.e = paramInt1;
    this.f = paramInt2;
    this.i = paramInt2 - cj.b(60, 80);
    this.j = 0;
    this.k = 0;
    this.C = paramByte;
    switch (paramByte) {
      case 0:
        this.d = new s(a, 14, 15);
        return;
      case 7:
        this.d = new s(u, 10, 10);
        return;
      case 13:
        this.d = new s(v, 10, 10);
        return;
      case 15:
        this.d = new s(A, 27, 27);
        return;
      case 11:
        this.d = new s(y, 3, 3);
        return;
      case 1:
        this.d = new s(o, 12, 13);
        return;
      case 16:
        this.D = cj.b(coreLG.m.c, coreLG.m.c + el.d);
        this.E = coreLG.m.d;
        this.N = true;
        return;
      case 4:
      case 5:
        b = 10;
        if (paramByte == 5)
          b = 6; 
        if (ci.a == 1) {
          b = (byte)(b - 4);
        } else if (ci.a == 2) {
          b = (byte)(b - 6);
        } 
        if (b < 3)
          b = 3; 
        this.F = cj.b(b / 2, b);
        this.g = ci.R / 12;
        this.h = cj.b(-3, -1);
        this.J = 25;
        this.G = new int[this.F];
        this.H = new int[this.F];
        this.I = new int[this.F];
        b = 20;
        b1 = 16;
        if (paramByte == 5) {
          b = 4;
          b1 = 8;
        } 
        for (paramByte = 0; paramByte < this.F; paramByte++) {
          this.G[paramByte] = cj.b(paramInt1 - b, paramInt1 + b);
          this.H[paramByte] = cj.b(paramInt2 - b / 2, paramInt2 + b / 2);
          this.I[paramByte] = cj.b(6, b1);
        } 
        break;
    } 
  }
  
  public aa(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte paramByte) {
    this.e = paramInt1;
    this.f = paramInt2;
    this.g = paramInt3;
    this.h = paramInt4;
    this.C = paramByte;
    if (paramByte == 3) {
      this.d = new s(q, 12, 12);
    } else if (paramByte == 6) {
      this.d = new s(r, 16, 10);
    } else if (paramByte == 8) {
      this.d = new s(s, 16, 10);
    } else if (paramByte == 12) {
      this.d = new s(t, 8, 5);
    } else if (paramByte == 9) {
      this.d = new s(w, 22, 18);
    } else if (paramByte == 10) {
      this.d = new s(x, 19, 17);
    } else if (paramByte == 14) {
      this.d = new s(z, 16, 10);
    } 
    this.l = (cj.a.nextInt(3) == 0);
  }
  
  private void b() {
    if (cv.d && !this.n && cv.a(this.e, this.f, (byte)0))
      this.n = true; 
  }
  
  public static int a(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = -1;
    if (paramInt3 >= paramInt1 && paramInt4 <= paramInt2) {
      i = paramInt3 - paramInt1;
      int j = paramInt2 - paramInt4;
      if (i > j) {
        i = 1;
      } else {
        i = 2;
      } 
    } 
    if (paramInt3 >= paramInt1 && paramInt4 >= paramInt2) {
      i = paramInt3 - paramInt1;
      int j = paramInt4 - paramInt2;
      if (i > j) {
        i = 1;
      } else {
        i = 3;
      } 
    } 
    if (paramInt3 <= paramInt1 && paramInt4 <= paramInt2) {
      i = paramInt1 - paramInt3;
      int j = paramInt2 - paramInt4;
      if (i > j) {
        i = 0;
      } else {
        i = 2;
      } 
    } 
    if (paramInt3 <= paramInt1 && paramInt4 >= paramInt2) {
      i = paramInt1 - paramInt3;
      int j = paramInt4 - paramInt2;
      if (i > j) {
        i = 0;
      } else {
        i = 3;
      } 
    } 
    return i;
  }
  
  public final void a() {
    int var1;
    switch(this.C) {
      case 0:
      case 7:
      case 11:
      case 13:
        if (this.j == 3) {
          ci.P.a(this);
          return;
        }
      
        this.c();
        return;
      case 1:
        if (this.j == 3) {
          ci.P.a(this);
          return;
        }
      
        this.c();
        return;
      case 2:
      case 3:
        if (this.C == 3) {
          this.c();
          if (this.j > 3) {
            this.j = 0;
          }
        }
      
        if (this.f > cv.b) {
          ci.P.a(this);
          return;
        }
      
        this.e += this.g;
        this.f += this.h;
        ++this.h;
        this.b();
        if (this.l) {
          boolean var4 = true;
          ++this.m;
          if (this.m > 2) {
            ci.P.a(this.e, this.f, (byte)1);
            this.m = 0;
          }
        
          return;
        }
        break;
      case 4:
      case 5:
        for(var1 = 0; var1 < this.F; ++var1) {
          int[] var10000 = this.G;
          var10000[var1] += this.g;
          var10000 = this.H;
          var10000[var1] += this.h;
          if (this.H[var1] < this.i) {
            ci.P.a(this);
            return;
          }
        }
      
        this.J += 5;
        if (this.J > 255) {
          this.J = 255;
          return;
        }
        break;
      case 6:
      case 8:
      case 12:
      case 14:
        label98: {
          this.c();
          if (this.h > 0) {
            this.g = ci.R / 20;
            if (this.j <= 3) {
              break label98;
            }
          }
        
          this.j = 0;
        }
      
        if (this.f > cv.b) {
          ci.P.a(this);
          return;
        }
      
        this.e += this.g;
        this.f += this.h;
        if (this.h < 2) {
          ++this.h;
          return;
        }
        break;
      case 9:
      case 10:
        if (this.f > cv.b) {
          ci.P.a(this);
          return;
        }
      
        this.e += this.g;
        this.f += this.h;
        ++this.h;
        this.b();
        return;
      case 15:
        if (this.j == 3) {
          ci.P.a(this);
          return;
        }
      
        this.c();
        return;
      case 16:
      default:
        break;
      case 17:
        ++this.k;
        if (this.k == 10) {
          this.k = 0;
        }
      
        if (this.k < 5) {
          this.j = 0;
        } else {
          this.j = 1;
        }
      
        var1 = 0 - this.e;
        int var2 = 0 - this.f;
        if (Math.abs(var1) < 16 && Math.abs(var2) < 16) {
          ci.P.a(this);
          return;
        }
      
        int var3;
        if (Math.abs((var3 = cj.a(var1, var2)) - this.L) < 90 || var1 * var1 + var2 * var2 > 4096) {
          if (Math.abs(var3 - this.L) < 15) {
            this.L = var3;
          } else if ((var3 - this.L < 0 || var3 - this.L >= 180) && var3 - this.L >= -180) {
            this.L = cj.c(this.L - 15);
          } else {
            this.L = cj.c(this.L + 15);
          }
        }
      
        if (this.M < 8192) {
          this.M += 1024;
        }
      
        this.g = this.M * cj.b(this.L) >> 10;
        this.h = this.M * cj.a(this.L) >> 10;
        var1 = var1 + this.g >> 10;
        this.e += var1;
        var1 = var2 + this.h >> 10;
        this.f += var1;
    }
  }
  
  private void c() {
    this.k++;
    if (this.k > 1) {
      this.j++;
      this.k = 0;
    } 
  }
  
  public final void a(Graphics paramGraphics) {
    byte b;
    paramGraphics = paramGraphics;
    switch (this.C) {
      case 0:
      case 1:
      case 7:
      case 13:
      case 15:
        this.d.a(this.j, this.e, this.f, 0, 3, paramGraphics);
        return;
      case 2:
        paramGraphics.drawImage(p, this.e, this.f, 3);
        return;
      case 3:
        this.d.a(this.j, this.e, this.f, 0, 3, paramGraphics);
        return;
      case 4:
      case 5:
        for (b = 0; b < this.F; b++) {
          int i = cj.b(this.J - 20, this.J);
          paramGraphics.setColor(i, i, i);
          paramGraphics.fillArc(this.G[b], this.H[b], this.I[b], this.I[b], 0, 360);
        } 
        return;
      case 6:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 14:
        this.d.a(this.j, this.e, this.f, 0, 3, paramGraphics);
        return;
      case 16:
        if (this.N) {
          for (b = 0; b < this.O; b++) {
            if (b < this.O / 2 + this.O / 4 && b > this.O / 2 - this.O / 4) {
              paramGraphics.setColor(16777215);
            } else {
              if (this.K == 1)
                paramGraphics.setColor(718162); 
              if (this.K == 0)
                paramGraphics.setColor(16729670); 
            } 
            paramGraphics.drawLine(this.D - this.O / 2 + b, this.E, this.e - this.O / 2 + b, this.f);
          } 
          this.O--;
          if (this.O == 0) {
            this.N = false;
            ci.P.a(this);
            return;
          } 
        } 
        break;
      case 17:
        this.d.a(this.j, this.e, this.f, 0, 3, paramGraphics);
        break;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.aa.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */