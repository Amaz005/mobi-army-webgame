package coreLG;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.y;

public final class bn {
  private Image h;
  
  private int[] i;
  
  public static boolean a = true;
  
  public static Image b;
  
  private static byte j;
  
  private static Image[] k = new Image[10];
  
  private static int[] l;
  
  private static int m;
  
  private static int n;
  
  public int c;
  
  public int d;
  
  public int e;
  
  public int f;
  
  public boolean g;
  
  private boolean o = false;
  
  static {
    b = (cj.d = new y("/effect/Hole")).a("mangnhen.png");
    k[0] = coreLG.cj.d.a("h32x26.png");
    k[1] = coreLG.cj.d.a("h36x30.png");
    k[2] = coreLG.cj.d.a("smallhole.png");
    k[3] = coreLG.cj.d.a("h44x39.png");
    k[4] = coreLG.cj.d.a("rocket.png");
    k[5] = coreLG.cj.d.a("rangehole.png");
    k[6] = coreLG.cj.d.a("hrangcua.png");
    k[7] = coreLG.cj.d.a("hgrenade.png");
    k[8] = coreLG.cj.d.a("h14x12.png");
    coreLG.cj.d = null;
  }
  
  private static byte c(int paramInt) {
    switch (paramInt) {
      case 0:
      case 32:
        return 0;
      case 1:
        return 8;
      case 2:
        return 2;
      case 6:
        return 6;
      case 7:
      case 31:
      case 37:
        return 7;
      case 3:
        return 7;
      case 9:
        return 5;
      case 10:
        return 4;
      case 11:
        return 2;
      case 12:
        return 6;
      case 15:
        return 7;
      case 22:
        return 7;
      case 24:
        return 3;
      case 25:
        return 8;
      case 19:
        return 2;
      case 20:
        return 0;
      case 27:
        return 1;
      case 17:
      case 18:
        return 2;
      case 30:
        return 0;
      case 21:
        return 2;
      case 42:
      case 43:
        return 7;
      case 44:
        return 2;
      case 45:
        return 7;
      case 47:
        return 8;
    } 
    return 0;
  }
  
  public bn(int paramInt1, int paramInt2, Image paramImage, boolean paramBoolean) {
    this.e = paramInt1;
    this.f = paramInt2;
    this.g = paramBoolean;
    this.h = paramImage;
    this.c = this.h.getWidth();
    this.d = this.h.getHeight();
    this.i = new int[this.c * this.d];
    this.h.getRGB(this.i, 0, this.c, 0, 0, this.c, this.d);
  }
  
  public final void a(Graphics paramGraphics) {
    if (this.e >= coreLG.m.c - this.c && this.e <= coreLG.m.c + ds.Z + this.c && this.f >= coreLG.m.d - this.d && this.f <= coreLG.m.d + ds.aa + this.d) {
      if (a) {
        paramGraphics.drawRGB(this.i, 0, this.c, this.e, this.f, this.c, this.d, true);
        return;
      } 
      paramGraphics.drawImage(this.h, this.e, this.f, 0);
    } 
  }
  
  public final void a() {
    if (this.o) {
      if (!a)
        this.h = Image.createRGBImage(this.i, this.c, this.d, true); 
      this.o = false;
    } 
  }
  
  public final void a(int paramInt1, int paramInt2, int paramInt3) {
    j = c(paramInt3);
    m = k[j].getWidth();
    n = k[j].getHeight();
    l = new int[m * n];
    k[j].getRGB(l, 0, m, 0, 0, m, n);
    paramInt1 = paramInt1 - this.e - m / 2;
    paramInt2 = paramInt2 - this.f - n / 2;
    for (paramInt3 = 0; paramInt3 < n; paramInt3++) {
      for (byte b = 0; b < m; b++) {
        if (cj.a(paramInt1 + b, paramInt2 + paramInt3, 0, 0, this.c, this.d))
          if (l[paramInt3 * m + b] == -65536 && cj.e(a(paramInt1 + b, paramInt2 + paramInt3))) {
            this.i[(paramInt2 + paramInt3) * this.c + paramInt1 + b] = -16777216;
          } else if (l[paramInt3 * m + b] == -16777216) {
            this.i[(paramInt2 + paramInt3) * this.c + paramInt1 + b] = 0;
          }  
      } 
    } 
    this.o = true;
  }
  
  public static int a(int paramInt) {
    return k[c(paramInt)].getWidth();
  }
  
  public static int b(int paramInt) {
    return k[c(paramInt)].getHeight();
  }
  
  public final int a(int paramInt1, int paramInt2) {
    return this.i[paramInt2 * this.c + paramInt1];
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bn.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */