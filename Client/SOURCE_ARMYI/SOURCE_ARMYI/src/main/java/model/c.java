package model;

import coreLG.bg;
import coreLG.el;
import coreLG.m;
import model.aa;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class c {
  public int a;
  
  public int b;
  
  private s c = null;
  
  private static Image d = aa.b;
  
  private int e;
  
  private int[] f;
  
  private int g = 20;
  
  public c(int paramInt1, int paramInt2, int paramInt3) {
    this.a = paramInt1 - 10;
    this.b = 3;
    this.e = el.e / this.g + 2;
    this.f = new int[this.e];
    this.c = new s(d, 40, 32);
    for (paramInt1 = 0; paramInt1 < this.e; paramInt1++)
      this.f[paramInt1] = paramInt1 % 3; 
  }
  
  public final void a(Graphics paramGraphics) {
    if (this.c != null)
      for (byte b = 0; b < this.e; b++)
        this.c.a(this.f[b], this.a, el.e + m.d - b * this.g, 0, 3, paramGraphics);  
  }
  
  public final void a() {
    if (this.b < 0)
      bg.i.removeElement(this); 
    if (el.c % 2 == 0)
      for (byte b = 0; b < this.e; b++) {
        this.f[b] = this.f[b] + 1;
        if (this.f[b] > 2)
          this.f[b] = 0; 
      }  
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.c.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */