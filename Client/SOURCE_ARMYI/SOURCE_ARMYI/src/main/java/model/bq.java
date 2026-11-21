package model;

import coreLG.ci;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bq {
  private static int[] a = new int[4];
  
  private static Image b = ci.H;
  
  public static void a(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    a[0] = paramGraphics.getClipX();
    a[1] = paramGraphics.getClipY();
    a[2] = paramGraphics.getClipWidth();
    a[3] = paramGraphics.getClipHeight();
    int i = Math.max(paramInt1, a[0]);
    int j = Math.max(paramInt2, a[1]);
    int k = Math.min(paramInt1 + paramInt3, a[0] + a[2]) - i;
    int m = Math.min(paramInt2 + paramInt4, a[1] + a[3]) - j;
    if (k <= 0 || m <= 0)
      return; 
    paramGraphics.setClip(i, j, k, m);
    for (i = 0; i < paramInt3 / 120 + 1; i++) {
      for (j = 0; j < paramInt4 / 40 + 1; j++)
        paramGraphics.drawImage(b, paramInt1 + i * 120, paramInt2 + j * 40, 0); 
    } 
    paramGraphics.setClip(a[0], a[1], a[2], a[3]);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bq.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */