package model;

import coreLG.el;
import javax.microedition.lcdui.Graphics;

public abstract class bm {
  public abstract void a(Graphics paramGraphics);
  
  public void a() {}
  
  public void b() {
    el.A.addElement(this);
  }
  
  public final void c() {
    if (el.A.contains(this))
      el.A.removeElement(this); 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bm.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */