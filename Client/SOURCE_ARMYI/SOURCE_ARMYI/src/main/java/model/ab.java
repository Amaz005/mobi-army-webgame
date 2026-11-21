package model;

import coreLG.TerrainMidlet;
import coreLG.bn;
import coreLG.bx;
import coreLG.ci;
import coreLG.ds;
import coreLG.el;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class ab extends ds {
  private int a;
  
  private static Image[] b;
  
  private int[] c = new int[2];
  
  private byte d = 0;
  
  private boolean e = bn.a;
  
  private String[] f = u.bj();
  
  static {
    try {
      (b = new Image[2])[0] = Image.createImage("/tick0.png");
      b[1] = Image.createImage("/tick1.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    }
  }
  
  public ab() {
    this.ad = new z("OK", new t(this));
  }
  
  public final void b() {
    this.c[0] = model.bx.b("sound") / 10;
    this.c[1] = model.bx.b("vibrate");
    this.d = (byte)model.bx.b("graphic");
    this.e = (model.bx.b("drawRGB") == 0);
    super.b();
  }
  
  public final void a(Graphics var1) {
    var1.setClip(0, 0, el.d, el.e);
    ds.b(var1);
    am.a(var1);
  
    int var2;
    for(var2 = 0; var2 <= el.d; var2 += 32) {
      var1.drawImage(coreLG.bx.h, var2, el.e - 62, 0);
    }
  
    model.bn.d.a(var1, u.cy(), ds.Z >> 1, 5, 2);
    var2 = el.g - 50;
    int var3 = el.f - 60;
    if (el.c % 10 > 2) {
      model.bn.c.a(var1, "$", var2 - 15, var3 + 14 + 33 * this.a, 0);
      model.bn.c.a(var1, "#", var2 + 103, var3 + 14 + 33 * this.a, 0);
    }
  
    model.bn.c.a(var1, u.bp() + ":", var2, var3, 0);
    var3 += ds.ab;
  
    int var4;
    for(var4 = 0; var4 < 10; ++var4) {
      var1.drawImage(b[var4 < this.c[0] ? 0 : 1], var2 + var4 * 10, var3, 0);
    }
  
    var3 += 14;
    model.bn.c.a(var1, u.bo() + ":", var2, var3, 0);
    var3 += ds.ab;
  
    for(var4 = 0; var4 < 10; ++var4) {
      var1.drawImage(b[var4 < this.c[1] ? 0 : 1], var2 + var4 * 10, var3, 0);
    }
  
    model.bn.c.a(var1, u.bm() + ":", var2, var3 + 13, 0);
    model.bn.c.a(var1, this.f[this.d], el.g, var3 + 29, 2);
    model.bn.c.a(var1, u.bn() + ":", var2, var3 + 48, 0);
    model.bn.c.a(var1, this.e ? u.bk() : u.bl(), el.g, var3 + 62, 2);
    super.a(var1);
  }
  
  public final void d() {
    boolean bool = false;
    if (el.H[8]) {
      el.H[8] = false;
      ds.aj = false;
      this.a++;
      if (this.a > 3)
        this.a = 0; 
    } else if (el.H[2]) {
      el.H[2] = false;
      ds.ai = false;
      this.a--;
      if (this.a < 0)
        this.a = 3; 
    } else if (el.H[4]) {
      bool = true;
      el.H[4] = false;
      ds.ak = false;
      if (this.a == 2) {
        this.d = (byte)(this.d + 1);
        if (this.d > 2)
          this.d = 0; 
      } else if (this.a == 3) {
        this.e = !this.e;
      } else {
        this.c[this.a] = this.c[this.a] - 1;
        if (this.c[this.a] < 0)
          this.c[this.a] = 0; 
      } 
    } else if (el.H[6]) {
      bool = true;
      el.H[6] = false;
      ds.al = false;
      if (this.a == 2) {
        this.d = (byte)(this.d - 1);
        if (this.d < 0)
          this.d = 2; 
      } else if (this.a == 3) {
        this.e = !this.e;
      } else {
        this.c[this.a] = this.c[this.a] + 1;
        if (this.c[this.a] > 10)
          this.c[this.a] = 10; 
      } 
    } 
    if (bool) {
      TerrainMidlet.b(this.c[this.a] * 10);
      v.b(this.c[this.a] * 10);
      ab ab1;
      ci.a = (ab1 = this).d;
      model.bx.a("graphic", ab1.d);
      bn.a = ab1.e;
      model.bx.a("drawRGB", (this.a == 0) ? (this.c[this.a] * 10) : ((this.a == 1) ? this.c[this.a] : (ab1.e ? 0 : 1)));
      ds.t();
    } 
    super.d();
  }
  
  public final void c() {
    am.a();
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ab.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */