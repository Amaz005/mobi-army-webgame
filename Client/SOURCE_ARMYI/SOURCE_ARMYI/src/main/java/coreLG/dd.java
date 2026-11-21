package coreLG;

import model.am;
import model.bn;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

import model.u;
import model.z;

public final class dd extends ds {
  Vector a;
  
  int b;
  
  public int c;
  
  private int i;
  
  private int j;
  
  private int k;
  
  private int l;
  
  public int d = 0;
  
  z e = new z(u.ba(), new cm(this));
  
  model.z f = new model.z(model.u.bc(), new co(this));
  
  model.z g = new model.z(model.u.cJ(), new cl(this));
  
  model.z h = new z(model.u.bb(), new cp(this));
  
  private String[] m = model.u.bG();
  
  public final void a(Graphics paramGraphics) {
    paramGraphics.setClip(0, 0, el.d, el.e);
    ds.b(paramGraphics);
    am.a(paramGraphics);
    for (byte b1 = 0; b1 <= el.d; b1 += 32)
      paramGraphics.drawImage(bx.h, b1, el.e - 62, 0); 
    model.bn.d.a(paramGraphics, this.m[this.d], 10, 3, 0);
    paramGraphics.setColor(1407674);
    paramGraphics.fillRect(0, 25, el.d, ds.ab);
    bn.b.a(paramGraphics, model.u.cD(), 10, 28, 0);
    if (this.d == 2)
      model.bn.b.a(paramGraphics, "Online", el.d - 25, 28, 2);
    if (this.a.size() == 0 && this.d == 2) {
      model.bn.c.a(paramGraphics, model.u.bN(), el.g, 50, 2);
      model.bn.c.a(paramGraphics, model.u.bO(), el.g, 75, 2);
      model.bn.c.a(paramGraphics, model.u.bP(), el.g, 90, 2);
    } 
    Graphics graphics = paramGraphics;
    dd dd1 = this;
    graphics.translate(0, ds.ab + 25);
    graphics.setClip(0, 1, el.d, el.e - 25 - 21 - ds.ab);
    graphics.translate(0, -dd1.j);
    byte b2 = 0;
    for (byte b3 = 0; b3 < dd1.a.size(); b3++) {
      if (b3 == dd1.b) {
        graphics.setColor(16765440);
        graphics.fillRect(0, b2, el.d, 57);
      } 
      if (b3 * 57 > -graphics.getTranslateY() && b3 * 57 < -graphics.getTranslateY() + ds.aa) {
        model.j j;
        String str = (j = (model.j) dd1.a.elementAt(b3)).a;
        str = String.valueOf(str) + " (" + j.l.a + "+" + j.l.c + "%)";
        model.bn.c.a(graphics, j.i[j.f], 25, b2 + 3, 0);
        model.bn.c.a(graphics, str, 25, b2 + 19, 0);
        model.bn.c.a(graphics, "Kill/Dead: " + j.h + "/" + j.g, 25, b2 + 37, 0);
        bl.a(j.c, 0, 10, b2 + 40, 2, graphics);
        bx.a(j.f, 10, b2 + 15, 3, graphics);
        if (dd1.d == 2)
          graphics.drawImage(bx.v[j.d ? 0 : 1], el.d - 20, b2 + 27, 3); 
      } 
      b2 += 57;
    } 
    super.a(paramGraphics);
  }
  
  public final void d() {
    super.d();
    boolean bool = false;
    if (el.H[8]) {
      el.H[8] = false;
      ds.aj = false;
      this.b++;
      if (this.b == this.a.size())
        this.b = 0; 
      bool = true;
    } else if (el.H[2]) {
      el.H[2] = false;
      ds.ai = false;
      this.b--;
      if (this.b < 0)
        this.b = this.a.size() - 1; 
      bool = true;
    } 
    if (bool) {
      this.i = this.b * 57 - el.f - 3 * ds.ab;
      if (this.i < 0)
        this.i = 0; 
      if (this.i > this.l)
        this.i = this.l; 
      if (this.b == this.a.size() - 1 || this.b == 0)
        this.j = this.i; 
    } 
  }
  
  public final void c() {
    am.a();
    if (this.j != this.i) {
      this.k = this.i - this.j >> 2;
      this.j += this.k;
    } 
  }
  
  public final void a(int paramInt, Vector paramVector) {
    this.d = paramInt;
    this.a = paramVector;
    this.l = paramVector.size() * 57 - el.e - ds.ab * 3 - 7;
    if (this.l < 0)
      this.l = 0; 
    this.b = 0;
    this.j = this.i = 0;
    this.ad = new z(model.u.aQ(), new bu(this));
    this.ac = new z(model.u.cJ(), new df(this));
    this.ae = new model.z(model.u.cI(), new de(this));
  }
  
  public final void b(int paramInt, Vector paramVector) {
    this.d = 3;
    this.a = paramVector;
    this.l = paramVector.size() * 57 - el.e - ds.ab * 3 - 7;
    if (this.l < 0)
      this.l = 0; 
    this.b = 0;
    this.j = this.i = 0;
    this.ad = new z(model.u.aO(), new dh(this));
    this.ac = this.g;
    this.ae = new model.z(model.u.cI(), new dg(this));
  }
  
  static void a(dd paramdd) {
    if (ds.ah == el.p) {
      el.p.b();
      return;
    } 
    el.i.b();
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dd.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */