package model;

import coreLG.ci;
import coreLG.dj;
import coreLG.eb;
import coreLG.el;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bl {
  private static Image a;
  
  private static Image b;
  
  private static Image c;
  
  private static Image d;
  
  private static Image e;
  
  private static Image f;
  
  private static Image g;
  
  private static Image h;
  
  private static y i;
  
  private static Image j;
  
  private static Image k;
  
  private s l;
  
  private int m;
  
  private int n;
  
  private int o;
  
  private int p;
  
  private int q = 0;
  
  private byte r;
  
  private byte s;
  
  private byte t;
  
  private int u = 1;
  
  static {
    a = (i = new y("/effect/effect")).a("ex3.png");
    b = i.a("teleport_eff.png");
    c = i.a("waterBum.png");
    d = i.a("Eff.png");
    e = i.a("tangHinhEffect.png");
    f = i.a("bomnguyentu.png");
    g = i.a("electric.png");
    h = i.a("electric2.png");
    j = i.a("mua.png");
    try {
      k = Image.createImage("/effect/poison-attack.png");
    } catch (Exception exception) {
    }
  }
  
  public bl(int paramInt1, int paramInt2, byte paramByte) {
    this.m = paramInt1;
    this.n = paramInt2;
    this.t = paramByte;
    switch (paramByte) {
      case 0:
        this.l = new s(a, 59, 64);
        break;
      case 1:
        this.l = new s(b, 32, 32);
        break;
      case 2:
        this.l = new s(c, 32, 48);
        break;
      case 6:
        this.l = new s(j, 13, 7);
        break;
      case 7:
        this.l = new s(f, 48, 62);
        break;
      case 8:
        this.l = new s(g, 59, 64);
        break;
      case 9:
        this.l = new s(h, 59, 64);
        break;
      case 10:
        this.l = new s(k, 12, 12);
        break;
    } 
    ci.O.addElement(this);
  }
  
  public bl(int paramInt1, int paramInt2, byte paramByte1, byte paramByte2, byte paramByte3) {
    this.m = paramInt1;
    this.n = paramInt2;
    this.t = paramByte1;
    this.s = paramByte3;
    this.r = paramByte2;
    if (paramByte1 == 3) {
      this.l = new s(d, 40, 40);
    } else if (paramByte1 == 5) {
      this.n -= 24;
      this.o = this.n - 50;
    } 
    if (paramByte3 == 4)
      this.l = new s(e, 24, 32); 
    ci.O.addElement(this);
  }
  
  public final void a() {
    switch (this.t) {
      case 0:
        if (this.q == 5) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 8:
      case 9:
        this.u = 2;
        if (this.q == 7) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 1:
        if (this.q == 4) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 2:
        if (this.q == 4) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 7:
        if (this.q == 3) {
          ci.O.removeElement(this);
          return;
        } 
        this.u = 3;
        break;
      case 10:
        if (this.q == 3) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 3:
        if (eb.b[this.r] != null) {
          this.m = (eb.b[this.r]).G;
          this.n = (eb.b[this.r]).H;
        } 
        if (this.q == 4) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 5:
        this.m = (eb.b[this.r]).G;
        this.n--;
        if (this.n < this.o) {
          ci.O.removeElement(this);
          return;
        } 
        break;
      case 4:
        if (eb.b[this.r] != null) {
          this.m = (eb.b[this.r]).G;
          this.n = (eb.b[this.r]).H - 13;
          if (this.s == 4) {
            if (!(eb.b[this.r]).ah) {
              ci.O.removeElement(this);
              return;
            } 
            if (this.q == 4)
              this.q = 0; 
            break;
          } 
          if (this.s == 3 && !(eb.b[this.r]).ai) {
            ci.O.removeElement(this);
            return;
          } 
        } 
        break;
      case 6:
        if (this.q == 2)
          ci.O.removeElement(this); 
        break;
    } 
    this.p++;
    if (this.p > this.u) {
      this.q++;
      this.p = 0;
    } 
  }
  
  public final void a(Graphics paramGraphics) {
    switch (this.t) {
      case 0:
      case 1:
      case 3:
      case 7:
      case 8:
      case 9:
        this.l.a(this.q, this.m, this.n - 12, 0, 3, paramGraphics);
        return;
      case 2:
        this.l.a(this.q, this.m, this.n + 6, 0, 33, paramGraphics);
        return;
      case 10:
        this.l.a(this.q, this.m, this.n - 6, 0, 3, paramGraphics);
        return;
      case 5:
        dj.a(paramGraphics, this.s, this.m - 8, this.n);
        return;
      case 4:
        if (this.s == 4) {
          if ((eb.b[this.r]).a == (eb.b[ci.X]).a || this.r == ci.X) {
            this.l.a(this.q, this.m, this.n - 2, 0, 3, paramGraphics);
            if (el.c % 10 > 2) {
              dj.a(paramGraphics, this.s, this.m - 8, this.n - 37);
              return;
            } 
          } 
          break;
        } 
        if (!(eb.b[this.r]).ah && el.c % 10 > 2) {
          dj.a(paramGraphics, this.s, this.m - 8, this.n - 37);
          return;
        } 
        break;
      case 6:
        this.l.a(this.q, this.m, this.n - 5, 0, 3, paramGraphics);
        break;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */