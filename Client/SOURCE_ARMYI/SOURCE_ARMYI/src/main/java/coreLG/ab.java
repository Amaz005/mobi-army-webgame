package coreLG;

import model.bn;
import model.bu;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import model.u;
import model.w;
import model.z;

public final class ab extends ds {
  public Vector a = new Vector();
  
  int b;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h;
  
  public int c;
  
  private int i;
  
  public ds d;
  
  public final void a(ds paramds) {
    this.d = paramds;
    el.A.removeAllElements();
    el.B.b = 0;
    b();
  }
  
  public ab() {
    this.ad = new z(model.u.k(), new ak(this));
    this.ae = new z(model.u.cI(), new ap(this));
    this.ac = new z("Menu", new an(this));
  }
  
  public final void a(Graphics paramGraphics) {
    this.d.a(paramGraphics);
    paramGraphics.setClip(10, 20, el.d - 19, el.e - 59);
    paramGraphics.setColor(13302783);
    paramGraphics.fillRect(10, 20, el.d - 20, el.e - 60);
    paramGraphics.setColor(5215093);
    paramGraphics.drawRect(10, 20, el.d - 20, el.e - 60);
    paramGraphics.translate(10, 20);
    paramGraphics.translate(this.i, 0);
    bn.d.a(paramGraphics, u.bA(), 10, 3, 0);
    paramGraphics.setColor(1407674);
    paramGraphics.fillRect(1, 25, el.d - 21, ds.ab);
    bn.b.a(paramGraphics, model.u.cT(), 10, 28, 0);
    model.bn.b.a(paramGraphics, model.u.bx(), el.d - 45, 28, 2);
    if (this.a.size() == 0) {
      bn.c.a(paramGraphics, model.u.bu(), el.g - 10, 50, 2);
      bn.c.a(paramGraphics, u.bv(), el.g - 10, 75, 2);
      model.bn.c.a(paramGraphics, model.u.bw(), el.g - 10, 90, 2);
    } 
    Graphics graphics = paramGraphics;
    ab ab1 = this;
    graphics.translate(0, ds.ab + 25);
    graphics.setClip(0, 0, el.d - 20, el.e - 25 - 21 - 40 - ds.ab);
    graphics.translate(0, -ab1.f);
    byte b1 = 0;
    for (byte b2 = 0; b2 < ab1.a.size(); b2++) {
      if (b2 == ab1.b) {
        graphics.setColor(16765440);
        graphics.fillRect(2, b1, el.d - 21 - 2, 38);
      } 
//      bu bu = ab1.a.elementAt(b2);
      bu bu = (model.bu) ab1.a.elementAt(b2);
      graphics.drawImage(w.a[0], 10, b1 + 4, 0);
      bn.c.a(graphics, bu.b, 30, b1 + 3, 0);
      bn.c.a(graphics, model.bn.c.a(bu.c, el.d - 80)[0], 10, b1 + ds.ab, 0);
      graphics.drawImage(bx.v[bu.d ? 0 : 1], el.d - 40, b1 + 20, 3);
      b1 += 40;
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
      this.e = this.b * 40 - el.f - 2 * ds.ab;
      if (this.e > this.c)
        this.e = this.c; 
      if (this.e < 0)
        this.e = 0; 
      if (this.b == this.a.size() - 1 || this.b == 0)
        this.f = this.e; 
    } 
  }
  
  public final void c() {
    if (this.i != 0)
      this.i += -this.i >> 1; 
    if (this.i == -1)
      this.i = 0; 
//    if ((this = this).f != this.e) {
    if (this.f != this.e) {
      this.h = this.e - this.f << 2;
      this.g += this.h;
      this.f += this.g >> 4;
      this.g &= 0xF;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.ab.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */