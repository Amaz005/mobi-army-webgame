package model;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class s {
  public int a;
  
  public int b;
  
  public int c;
  
  private Image d;
  
  private int[] e;
  
  private int f;
  
  public s(Image paramImage, int paramInt1, int paramInt2) {
    this.d = paramImage;
    this.a = paramInt1;
    this.b = paramInt2;
    this.f = paramImage.getHeight();
    this.c = this.f / paramInt2;
    this.e = new int[this.c];
    for (byte b = 0; b < this.c; b++)
      this.e[b] = b * paramInt2; 
  }
  
  public final void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Graphics paramGraphics) {
    if (paramInt1 >= 0 && paramInt1 < this.c)
      paramGraphics.drawRegion(this.d, 0, this.e[paramInt1], this.a, this.b, paramInt4, paramInt2, paramInt3, paramInt5); 
  }
  
  public final void a() {
    this.d = null;
    this.e = null;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.s.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */