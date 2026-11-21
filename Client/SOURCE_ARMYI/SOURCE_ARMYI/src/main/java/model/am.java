package model;

import coreLG.bx;
import coreLG.el;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class am {
  public static Image a;
  
  public static Image[] b;
  
  private static int[] c;
  
  private static int[] d = new int[] { 0, el.g };
  
  private static int[] e;
  
  static {
    c = new int[] { 30, 60 };
    e = new int[] { 2, 1 };
    try {
      a = bx.y;
      b = bx.w;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static void a() {
    for(int var0 = 0; var0 < 2; ++var0) {
      int[] var10000 = d;
      var10000[var0] += e[var0];
      if (d[var0] > el.d) {
        d[var0] = -b[var0].getWidth();
      }
    }
    
  }
  
  public static void a(Graphics var0) {
    var0.drawImage(a, 30, 40, 0);
    
    for(int var1 = 1; var1 >= 0; --var1) {
      var0.drawImage(b[var1], d[var1], c[var1], 0);
    }
    
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.am.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */