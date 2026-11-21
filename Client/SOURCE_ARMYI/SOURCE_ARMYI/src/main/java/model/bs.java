package model;

import coreLG.el;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class bs extends bm {
  private Vector a = new Vector();
  
  private int b;
  
  private int c;
  
  public final void b() {
    boolean var1 = false;
  
    for(int var2 = 0; var2 < el.A.size(); ++var2) {
      if ((bm)el.A.elementAt(var2) instanceof bs) {
        var1 = true;
        break;
      }
    }
  
    if (!var1) {
      super.b();
    }
  }
  
  public final void a(String paramString) {
    if (this.a.size() == 0)
      this.c = -bn.b.a(paramString); 
    this.a.addElement(paramString);
    if (this.a.size() == 1)
      this.b = el.d; 
  }
  
  public final void a() {
    this.b -= 2;
    if (this.b < this.c) {
      this.b = el.d;
      this.a.removeElementAt(0);
      if (this.a.size() <= 0) {
        el.A.removeElement(this);
        return;
      } 
      this.c = -bn.b.a((String)this.a.elementAt(0));
    } 
  }
  
  public final void a(Graphics var1) {
    var1.translate(-var1.getTranslateX(), -var1.getTranslateY());
    var1.setClip(0, 0, el.d, el.e);
    var1.setClip(0, 0, el.d, 50);
    var1.translate(this.b, 0);
    String var2 = (String)this.a.elementAt(0);
    if (el.h != el.j) {
      bn.c.a(var1, var2, 10, 2, 0);
    } else {
      bn.c.a(var1, var2, 10, 20, 0);
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bs.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */