package coreLG;

import javax.microedition.lcdui.Graphics;

public final class m {
  public static byte a;
  
  static int b = 0;
  
  public static int c = 0;
  
  public static int d = 0;
  
  public static int e = 0;
  
  public static int f = 0;
  
  private int i;
  
  private ah j;
  
  private int k = 0;
  
  private int l;
  
  private int m;
  
  public static int g;
  
  public static int h;
  
  private int n;
  
  private int o;
  
  private int p;
  
  private int q;
  
  private int r = 20;
  
  public m() {
    e = 0;
    f = 0;
    b = 0;
  }
  
  public static void a() {
    a = 0;
    g = 0;
    h = 0;
  }
  
  public final void a(int paramInt) {
    a = 1;
    this.i = paramInt;
  }
  
  public final void a(ah paramah) {
    a = 2;
    this.j = paramah;
  }
  
  public final void b(ah paramah) {
    a = 7;
    this.j = paramah;
  }
  
  public final void c(ah paramah) {
    a = 6;
    this.j = paramah;
  }
  
  public final void d(ah paramah) {
    a = 8;
    this.j = paramah;
  }
  
  public final void a(int paramInt1, int paramInt2) {
    a = 4;
    this.p = paramInt1 - ds.Z / 2;
    this.q = paramInt2 - ds.aa / 2;
  }
  
  public final void b() {
    e = c;
    f = d;
    this.n = 0;
    this.o = 0;
    switch (a) {
      case 0:
        c += g;
        if (g != 0)
          g = 0; 
        d += h;
        if (h != 0)
          h = 0; 
        break;
      case 1:
        if (eb.b[this.i] != null) {
          if ((eb.b[this.i]).Z == 0) {
            this.n = (eb.b[this.i]).G - 40 - ds.Z / 2;
          } else if ((eb.b[this.i]).Z == 2) {
            this.n = (eb.b[this.i]).G + 40 - ds.Z / 2;
          } 
          this.o = (eb.b[this.i]).H - ds.aa / 2 - 12;
          b(3);
        } 
        break;
      case 2:
        if (this.j.d != 37 || (this.j.a != -90 && this.j.a != 270) || this.j.g[this.j.h] > -300) {
          this.n = this.j.f[this.j.h] - ds.Z / 2;
          this.o = this.j.g[this.j.h] - ds.aa / 2;
          if (this.j.d != 37) {
            b(2);
            break;
          } 
          b(1);
        } 
        break;
      case 3:
        this.n = bg.g - ds.Z / 4;
        this.o = bg.h - ds.aa / 2;
        b(2);
        break;
      case 6:
        this.n = this.j.f[this.j.h] - ds.Z / 2;
        this.o = this.j.g[this.j.h] - ds.aa / 3;
        b(1);
        break;
      case 7:
        this.n = this.j.f[this.j.h] - ds.Z / 2;
        this.o = this.j.g[this.j.h] - ds.aa / 3;
        b(1);
        break;
      case 8:
        this.n = (eb.b[eb.f]).G - ds.Z / 2;
        this.o = this.j.g[this.j.h] - ds.aa / 2;
        b(2);
        break;
      case 4:
        this.n = this.p;
        this.o = this.q;
        b(2);
        break;
      case 5:
        this.n = this.p;
        this.o = this.q;
        b(3);
        break;
    } 
    if (b != 0) {
      if (b == 1) {
        c += cj.b(-5, 5);
        d += cj.b(-5, 5);
      } else if (b == 2) {
        c += cj.b(-20, 20);
        d += cj.b(-20, 20);
      } else if (b == 3) {
        if (el.c % 3 == 0) {
          d += this.r;
        } else {
          d -= this.r;
        } 
        if (this.k % 2 == 0)
          this.r--; 
        if (this.r < 0)
          this.r = 0; 
      } 
      this.k++;
      if (this.k > ((b != 3) ? 10 : 30)) {
        this.r = 20;
        b = 0;
        this.k = 0;
      } 
    } 
    if (a != 5) {
      int j = cv.b;
      short s = -1000;
      int i = cv.a;
      s = 0;
      if (d < -1000)
        d = -1000; 
      if (d > j - ds.aa)
        f = d = j - ds.aa; 
      if (c < 0)
        c = 0; 
      if (c > i - ds.Z)
        c = i - ds.Z; 
    } 
  }
  
  private void b(int paramInt) {
    if (c != this.n) {
      this.l = c - this.n;
      c -= this.l >> paramInt;
    } 
    if (d != this.o) {
      this.m = d - this.o;
      d -= this.m >> paramInt;
    } 
  }
  
  public static void a(Graphics paramGraphics) {
    paramGraphics.translate(-c, -d);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\m.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */