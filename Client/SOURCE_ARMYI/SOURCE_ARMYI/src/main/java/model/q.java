package model;

import coreLG.el;
import model.bn;

import javax.microedition.lcdui.Graphics;

public abstract class q {
  z a;
  
  z b;
  
  z c;
  
  public void a(Graphics paramGraphics) {
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
    paramGraphics.setClip(0, 0, el.d, el.e);
    if (this.a != null)
      bn.b.a(paramGraphics, this.a.a, 5, el.e - bn.b.a() - 3, 0); 
    if (this.b != null)
      bn.b.a(paramGraphics, this.b.a, el.g, el.e - bn.b.a() - 3, 2); 
    if (this.c != null)
      bn.b.a(paramGraphics, this.c.a, el.d - 5, el.e - bn.b.a() - 3, 1);
  }
  
  public void a(int paramInt) {}
  
  public void a() {
    if (el.H[5]) {
      el.H[5] = false;
      if (this.b != null && this.b.b != null)
        this.b.b.a(); 
    } 
    if (el.H[12]) {
      el.H[12] = false;
      if (this.a != null && this.a.b != null)
        this.a.b.a(); 
    } 
    if (el.H[13]) {
      el.H[13] = false;
      if (this.c != null && this.c.b != null)
        this.c.b.a(); 
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.q.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */