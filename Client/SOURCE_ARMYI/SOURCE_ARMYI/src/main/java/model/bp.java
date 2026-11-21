package model;

import coreLG.TerrainMidlet;
import coreLG.bx;
import coreLG.ds;
import coreLG.el;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bp extends ds {
  private Vector a;
  
  private int b;
  
  private int c;
  
  private int d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h;
  
  private static Image i;
  
  static {
    try {
      i = Image.createImage("/coin.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    }
  }
  
  public bp() {
    model.ai ai = new ai(this);
    this.ac = new z(u.cw(), ai);
    this.ad = new z("", ai);
    this.ae = new z(u.cI(), new aj(this));
  }
  
  public final void a() {
    br var1;
    String var2;
    if ((var1 = (br)this.a.elementAt(this.b)).b.indexOf("http://") != -1) {
      var2 = bn.a(var1.b, "@username", TerrainMidlet.e.a);
      el.a(u.I(), (as)(new an(this, var2)));
    } else if (var1.b.indexOf("napthe:") != -1) {
      var2 = var1.b.substring(0, var1.b.indexOf("napthe:") + "napthe:".length());
      String var4 = bn.a(var1.b, var2, "");
      String[] var5 = new String[2];
      String[] var3;
      (var3 = new String[2])[0] = u.U() + ":";
      var3[1] = u.T() + ":";
      el.u.a(var3[0], new ak(this, var5, var3, var4), new ah(this), 0);
      el.z = el.u;
    } else {
      el.a(u.W());
      var2 = null;
      if (var1.b.indexOf("@username") != -1) {
        var2 = bn.a(var1.b, "@username", TerrainMidlet.e.a);
      } else {
        var2 = var1.b + TerrainMidlet.e.a;
      }
    
      TerrainMidlet.a(var2, "sms://" + var1.c, new bh(this), new bj(this, var1));
    }
  }
  
  public final void a(Graphics paramGraphics) {
    paramGraphics.setClip(0, 0, el.d, el.e);
    ds.b(paramGraphics);
    am.a(paramGraphics);
    for (byte b1 = 0; b1 <= el.d; b1 += 32)
      paramGraphics.drawImage(bx.h, b1, el.e - 62, 0); 
    paramGraphics.translate(this.h, 0);
    bn.d.a(paramGraphics, u.cv(), 10, 3, 0);
    paramGraphics.setColor(1407674);
    paramGraphics.fillRect(0, 25, el.d, ds.ab);
    bn.b.a(paramGraphics, u.bg(), 10, 28, 0);
    Graphics graphics = paramGraphics;
    bp bp1 = this;
    graphics.translate(0, ds.ab + 25);
    graphics.setClip(0, 1, el.d, el.e - 25 - 28 - ds.ab);
    graphics.translate(0, -bp1.d);
    byte b2 = 0;
    for (byte b3 = 0; b3 < bp1.a.size(); b3++) {
      if (b3 == bp1.b) {
        graphics.setColor(16765440);
        graphics.fillRect(0, b2, el.d, 20);
      } 
      br br = (model.br) bp1.a.elementAt(b3);
      graphics.drawImage(i, 10, b2 + 2, 0);
      bn.c.a(graphics, br.a, 40, b2 + 4, 0);
      b2 += 20;
    } 
    super.a(paramGraphics);
  }
  
  public final void d() {
    boolean bool = false;
    if (el.H[8]) {
      ds.t();
      this.b++;
      if (this.b == this.a.size())
        this.b = 0; 
      bool = true;
    } else if (el.H[2]) {
      ds.t();
      this.b--;
      if (this.b < 0)
        this.b = this.a.size() - 1; 
      bool = true;
    } 
    if (bool) {
      this.c = this.b * 20 - el.f - 40;
      if (this.c < 0)
        this.c = 0; 
      if (this.c > this.g)
        this.c = this.g; 
      if (this.b >= this.a.size() - 1 || this.b == 0)
        this.d = this.c; 
    } 
    super.d();
  }
  
  public final void c() {
    if (this.h != 0)
      this.h += -this.h >> 1; 
    if (this.h == -1)
      this.h = 0; 
    if (this.d != this.c) {
      this.f = this.c - this.d << 2;
      this.e += this.f;
      this.d += this.e >> 4;
      this.e &= 0xF;
    } 
  }
  
  public final void a(Vector paramVector) {
    this.a = paramVector;
    this.b = 0;
    this.d = this.c = 0;
    this.g = paramVector.size() * 20 - el.f - 40;
    if (this.g < 0)
      this.g = 0; 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bp.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */