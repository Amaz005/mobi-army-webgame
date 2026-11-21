package model;

import coreLG.ci;
import coreLG.el;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class ar extends bm {
  private static Image a = ci.G;
  
  private int b;
  
  private int c;
  
  private int d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h = 60;
  
  private String[] i;
  
  public void a(int paramInt1, int paramInt2, int paramInt3, String paramString) {
    int i = paramInt3;
    paramInt3 = paramInt2;
    paramInt2 = paramInt1;
    this.i = bn.f.a(paramString, this.h);
    this.g = 10 * this.i.length + 4;
    this.c = paramInt3 - 2;
    this.d = i - 4;
    this.e = this.c - this.h / 2;
    this.f = this.d - this.g;
    this.b = paramInt2;
    b();
  }
  
  public void a() {
    if (this.b > 0) {
      this.b--;
      if (this.b == 0)
        el.A.removeElement(this); 
    } 
  }
  
  public void a(Graphics paramGraphics) {
    paramGraphics.setColor(16711553);
    paramGraphics.fillRect(this.e, this.f, this.h, this.g);
    paramGraphics.setColor(0);
    paramGraphics.drawRect(this.e, this.f, this.h, this.g);
    paramGraphics.drawImage(a, this.c, this.d, 0);
    int i = this.f + 2;
    for (byte b = 0; b < this.i.length; b++) {
      bn.g.a(paramGraphics, this.i[b], this.e + this.h / 2, i, 2);
      i += 10;
    } 
  }
}


/* Location:              D:\Downloads\jmodel.ar\Army165.jar!\model.ar.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */