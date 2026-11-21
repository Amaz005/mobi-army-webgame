package coreLG;

import model.j;
import javax.microedition.lcdui.Graphics;

public class eb {
  public static int a;
  
  public static bl[] b;
  
  public static int[] c;
  
  public static int[] d;
  
  public static int[] e;
  
  public static byte f;
  
  public static short[] g;
  
  public static short[] h;
  
  private int k;
  
  private int l;
  
  public static int i;
  
  public static int j;
  
  public void a(short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
    this.k = 0;
    this.l = 0;
    byte b;
    for (b = 0; b < 8; b++) {
      coreLG.eb.b[b] = null;
      if (paramArrayOfshort1[b] != -1) {
        model.j j = (model.j) el.p.d.elementAt(b);
        boolean bool = (TerrainMidlet.e.b != j.b) ? true : false;
        if (!j.j) {
          coreLG.eb.b[b] = new bl(j.b, (byte)b, paramArrayOfshort1[b], paramArrayOfshort2[b], bool, (b % 2 == 0) ? 2 : 0, j.c);
        } else {
          coreLG.eb.b[b] = new dk(j.b, (byte)b, paramArrayOfshort1[b], paramArrayOfshort2[b], bool, (b % 2 == 0) ? 2 : 0, j.c);
        } 
        (coreLG.eb.b[b]).an = j.a;
        (coreLG.eb.b[b]).aD = j.f;
        (coreLG.eb.b[b]).ae = j.k;
        (coreLG.eb.b[b]).af = j.k;
        if (!bool) {
          (coreLG.eb.b[b]).J = el.p.g();
          ci.X = (byte)b;
        } 
      } 
      this.k++;
    } 
    cj.d("PLAYER COUNT= " + this.k);
    this.l = this.k;
    for (b = 0; b < 8; b++) {
      c[b] = -1;
      d[b] = -1;
      e[b] = b;
    } 
  }
  
  public void b(short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
    cj.d("BOSS NUMBER SEND= " + paramArrayOfshort1.length);
    cj.d("ALL COUNT = " + this.l);
    byte b;
    for (b = 0; b < paramArrayOfshort1.length; b++) {
      cj.d("HERE 1");
      coreLG.eb.b[b + this.l] = null;
      cj.d("HERE 2");
      if (paramArrayOfshort1[b] != -1) {
        model.j j = (model.j) el.p.e.elementAt(b);
        cj.d("HERE 3");
        boolean bool = (TerrainMidlet.e.b != j.b) ? true : false;
        coreLG.eb.b[b + this.l] = new dk(j.b, (byte)1, paramArrayOfshort1[b], paramArrayOfshort2[b], bool, 2, j.c);
        cj.d("HERE 4");
        (coreLG.eb.b[b + this.l]).an = j.a;
        (coreLG.eb.b[b + this.l]).ae = j.k;
        (coreLG.eb.b[b + this.l]).af = j.k;
        (coreLG.eb.b[b + this.l]).a = false;
        cj.d("HERE 5");
      } 
    } 
    cj.d("HERE 6");
    this.l += el.p.e.size();
    cj.d("HERE 7");
    el.p.e.removeAllElements();
    for (b = 0; b < coreLG.eb.b.length; b++) {
      if (coreLG.eb.b[b] != null)
        cj.d("model.i player= " + b);
    } 
  }
  
  public static void a() {
    for (byte b = 0; b < coreLG.eb.b.length; b++) {
      if (coreLG.eb.b[b] != null) {
        g[b] = (short)(coreLG.eb.b[b]).G;
        h[b] = (short)(coreLG.eb.b[b]).H;
      } else {
        g[b] = -1;
        h[b] = -1;
      } 
    } 
  }
  
  public static void a(Graphics paramGraphics) {
    for (byte b = 0; b < coreLG.eb.b.length; b++) {
      if (coreLG.eb.b[b] != null && (coreLG.eb.b[b]).aa != 5 && b != f && (coreLG.eb.b[f]).a != (coreLG.eb.b[b]).a && !(coreLG.eb.b[b]).ah) {
        boolean bool = true;
        int i = (coreLG.eb.b[b]).G;
        int j = (coreLG.eb.b[b]).H - 12;
        if (i < m.c + 5) {
          i = m.c + 5;
        } else if (i > m.c + ds.Z - 10) {
          i = m.c + ds.Z - 10;
        } else if (j > m.d + 20 && j < m.d + ds.aa - 30) {
          bool = false;
        } 
        if (j < m.d + 20) {
          j = m.d + 20;
        } else if (j > m.d + ds.aa - 30) {
          j = m.d + ds.aa - 30;
        } else if (i > m.c + 5 && i < m.c + ds.Z - 10) {
          bool = false;
        } 
        if (bool) {
          paramGraphics.setColor(16711680);
          paramGraphics.fillRect(i, j, 2, 2);
          if (el.c % 10 > 5)
            paramGraphics.drawRect(i - 2, j - 2, 5, 5); 
        } 
      } 
    } 
  }
  
  public static boolean b() {
    return (f == ci.X);
  }
  
  public static void a(byte paramByte) {
    cj.d("SET NEXT PLAYER " + paramByte);
    if (ci.V != "")
      return;
    (b[paramByte]).ad = true;
    if ((b[paramByte]).aa != 8) {
      (b[paramByte]).aa = 0;
      b[paramByte].e();
    }
    (b[paramByte]).O = 0;
    (b[paramByte]).K = -1;
    if ((b[paramByte]).L)
      (b[paramByte]).L = false;
    if ((b[paramByte]).P != 13)
      (b[paramByte]).I = true;
    for (byte b = 0; b < coreLG.eb.b.length; b++) {
      if (coreLG.eb.b[b] != null) {
        (coreLG.eb.b[b]).c = false;
        if ((coreLG.eb.b[b]).aa != 5)
          (coreLG.eb.b[b]).Q = false;
        bg.f = 0;
        if (b == ci.X) {
          coreLG.eb.b[b].g();
          if (!(coreLG.eb.b[b]).T) {
            model.ad.a().b((short)(coreLG.eb.b[b]).G, (short)(coreLG.eb.b[b]).H);
            (coreLG.eb.b[b]).T = true;
          }
        }
      }
    }
    // doan nay bi loi
//    dy dy;
//    dy = ci.Q.b = dy.a;
//    dy.e = true;
//    f = paramByte;
//    ci.M.a(paramByte);
//    cj.d("Next Player");
  
    final dy q;
    final dy dy = q = ci.Q;
    dy.b = q.a;
    q.e = true;
    f = paramByte;
    ci.M.a(paramByte);
    cj.d("Next Player");
  }
  
  public static void a(int paramInt, short paramShort1, short paramShort2) {
    (b[paramInt]).ao = paramShort1;
    (b[paramInt]).ap = paramShort2;
    if (paramInt == ci.X)
      b[paramInt].b(paramShort1, paramShort2); 
  }
  
  public static void b(int paramInt, short paramShort1, short paramShort2) {
    b[paramInt].a(paramShort1, paramShort2);
  }
  
  public static void c(int paramInt, short paramShort1, short paramShort2) {
    (b[paramInt]).G = paramShort1;
    (b[paramInt]).H = paramShort2;
    (b[paramInt]).ao = paramShort1;
    (b[paramInt]).am = paramShort1;
    (b[paramInt]).ap = paramShort2;
    if (paramInt == ci.X)
      b[paramInt].b(paramShort1, paramShort2); 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\eb.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */