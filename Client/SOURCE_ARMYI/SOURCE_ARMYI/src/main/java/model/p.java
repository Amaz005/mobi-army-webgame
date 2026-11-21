package model;

import coreLG.ci;
import coreLG.el;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class p extends bm {
  private static Image a = ci.G;
  
  private int b;
  
  private int c;
  
  private int d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h = 80;
  
  private String[] i;
  
  public void a(int var1, int var2, int var3, String var4) {
    this.i = bn.h.a(var4, this.h - 30);
    this.g = 14 * this.i.length + 4;
    this.c = var2 - 2;
    this.d = var3 - 4;
    this.e = this.c - this.h / 2;
    this.f = this.d - this.g;
    if (this.e < 2) {
      this.e = 2;
    }
    
    if (this.e + this.h > el.d - 10) {
      this.e = el.d - (this.h + 10);
    }
    
    this.b = var1;
    if (el.A.contains(this)) {
      el.A.removeElement(this);
    }
    
    this.b();
  }
  
  
  public final void a() {
    if (this.b > 0) {
      this.b--;
      if (this.b == 0)
        el.A.removeElement(this); 
    } 
  }
  
  public final void a(Graphics paramGraphics) {
    paramGraphics.setColor(16711553);
    paramGraphics.fillRect(this.e + 5, this.f, this.h, this.g);
    paramGraphics.setColor(0);
    paramGraphics.drawRect(this.e + 5, this.f, this.h, this.g);
    paramGraphics.drawImage(a, this.c, this.d, 0);
    int i = this.f + 2;
    for (byte b = 0; b < this.i.length; b++) {
      bn.h.a(paramGraphics, this.i[b], this.e + this.h / 2, i, 2);
      i += 14;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.p.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */