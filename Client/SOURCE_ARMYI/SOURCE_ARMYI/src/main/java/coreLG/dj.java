package coreLG;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.u;

public final class dj {
  private static Image m = ci.n;
  
  public static final String[] a = u.bQ();
  
  private static byte[] n = new byte[] { 
      2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 
      1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 
      1, 1 };
  
  private static byte[] o = new byte[] { 
      0, 0, 30, 20, 20, 20, 30, 10, 10, 30, 
      30, 10, 5, 5, 10, 5, 10, 10, 10, 10, 
      10, 20, 10, 10 };
  
  public byte b;
  
  public String c;
  
  public byte d;
  
  public short e;
  
  public byte f;
  
  public byte g;
  
  public byte h;
  
  public byte i;
  
  public boolean j;
  
  public boolean k;
  
  private static int p = 2;
  
  static int l = 3;
  
  public dj(byte paramByte1, byte paramByte2, short paramShort) {
    this.b = paramByte1;
    this.d = paramByte2;
    this.f = 0;
    this.g = 0;
    this.e = paramShort;
    this.c = a[paramByte1];
    this.h = o[paramByte1];
    this.i = n[paramByte1];
    switch (this.b) {
      case 0:
        this.k = true;
        this.d = 99;
        return;
      case 1:
        this.k = true;
        this.d = 99;
        return;
      case 12:
        this.j = true;
        return;
      case 13:
        this.j = true;
        break;
    } 
  }
  
  public static void a(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3) {
    paramGraphics.drawRegion(m, 0, paramInt1 + 2 << 4, 16, 16, 0, paramInt2, paramInt3, 0);
  }
  
  public final void a(Graphics paramGraphics, int paramInt1, int paramInt2) {
    paramGraphics.drawRegion(m, 0, this.b + 2 << 4, 16, 16, 0, paramInt1, paramInt2, 0);
  }
  
  public static void a(Graphics paramGraphics, int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
    byte b1 = 0;
    byte b2 = 0;
    for (byte b3 = 0; b3 < paramArrayOfint.length; b3++) {
      int i = paramInt2 + b1 * (16 + p);
      int j = paramInt3 + b2 * (16 + p);
      if (paramInt1 >= 0 && b3 == paramInt1) {
        paramGraphics.setColor((el.c % 5 > 2) ? 16776960 : 16711680);
        paramGraphics.fillRect(i - p / 2, j - p / 2, 16 + p, 16 + p);
      } 
      paramGraphics.drawRegion(m, 0, paramArrayOfint[b3] + 2 << 4, 16, 16, 0, i, j, 0);
      if (++b1 > l - 1) {
        b1 = 0;
        b2++;
      } 
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dj.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */