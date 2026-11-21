package coreLG;

import model.am;
import model.bn;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import model.p;
import model.u;
import model.z;

public final class l extends ds {
  private static Image e = ci.F;
  
  private static Image f = ci.u;
  
  private static Image g = ci.t;
  
  private static Image h = ci.v;
  
  private static Image i = ci.x;
  
  private static Image j = ci.y;
  
  private static bi k;
  
  static Vector a = new Vector();
  
  boolean b;
  
  static int c;
  
  static int d;
  
  private z l;
  
  private model.p m = new p();
  
  private String n;
  
  public final void b() {
    super.b();
    a(u.l());
  }
  
  public l() {
    this.ac = new z(u.bJ(), new cg(this));
    this.l = new z(u.cH(), new cf(this));
    this.ae = this.l;
  }
  
  private void a(String paramString) {
    this.m.a(30, ds.Z - 30, ds.aa - 80, paramString);
  }
  
  public static void a(Vector paramVector) {
    int i = (a = paramVector).size();
    int j = i;
    int[] arrayOfInt1 = new int[i];
    for (byte b = 0; b < i; b++)
      arrayOfInt1[b] = (a(b)).b; 
    k = new bi(arrayOfInt1, 4, 5, 3);
    int[] arrayOfInt2 = new int[j];
    for (i = 0; i < j; i++)
      arrayOfInt2[i] = (a(i)).b; 
    bx.q = new bi(arrayOfInt2, 4, 5, 3);
  }
  
  public static dj a(int paramInt) {
    return (dj) a.elementAt(paramInt);
  }
  
  public static dj a() {
    return (dj) a.elementAt(k.b);
  }
  
  public static void e() {
    for (byte b = 0; b < a.size(); b++)
      (a(b)).g = 0; 
    d = 0;
  }
  
  public final void d() {
    if (!this.b) {
      k.a();
      if (el.H[5] && !this.b && !(a()).k) {
        a();
        if ((a()).d < 99) {
          this.b = true;
          if (99 - (a()).d < (a()).h) {
            c = 99 - (a()).d;
          } else {
            c = (a()).h;
          } 
          a(k.b, c);
        } else {
          el.a(u.ak());
        } 
      } 
    } else {
      if (el.H[4]) {
        if ((c -= (a()).h) < (a()).h)
          c = (a()).h; 
        a(k.b, c);
      } 
      if (el.H[6]) {
        if ((c += (a()).h) > 99 - (a()).d)
          c = 99 - (a()).d; 
        a(k.b, c);
      } 
      if (el.H[5])
        a((a()).b, (byte)c); 
    } 
    super.d();
    ds.t();
  }
  
  public final void c() {
    k.b();
    am.a();
  }
  
  public final void a(byte paramByte1, byte paramByte2) {
    a(paramByte1, paramByte2);
    if ((a(paramByte1)).e * paramByte2 > TerrainMidlet.e.e) {
      el.a(u.am());
      return;
    } 
    (a()).g = (byte)((a()).g + paramByte2);
    (a()).d = (byte)((a()).d + paramByte2);
    TerrainMidlet.e.e -= paramByte2 * (a()).e;
    a(u.al());
    if (this.b)
      this.b = false; 
  }
  
  private static void a(int paramInt1, int paramInt2) {
    d = 0;
    for (byte b = 0; b < a.size(); b++) {
      if (b == paramInt1) {
        int i = paramInt2 * (a(b)).e;
        d += i;
      } else if ((a(b)).g > 0) {
        int i = (a(b)).g * (a(b)).e;
        d += i;
      } 
    } 
  }
  
  public static void a(int[] paramArrayOfint) {
    byte b;
    for (b = 0; b < a.size(); b++)
      (a(b)).f = 0; 
    for (b = 0; b < paramArrayOfint.length; b++) {
      if (paramArrayOfint[b] >= 0)
        (a(paramArrayOfint[b])).f = (byte)((a(paramArrayOfint[b])).f + 1); 
    } 
  }
  
  public static int[] b(int[] paramArrayOfint) {
    paramArrayOfint = paramArrayOfint;
    if ((a(12)).d > 0) {
      paramArrayOfint[paramArrayOfint.length - 2] = -2;
    } else {
      paramArrayOfint[paramArrayOfint.length - 2] = -1;
    } 
    if ((a(13)).d > 0) {
      paramArrayOfint[paramArrayOfint.length - 1] = -2;
    } else {
      paramArrayOfint[paramArrayOfint.length - 1] = -1;
    } 
    for (byte b = 0; b < paramArrayOfint.length; b++) {
      if (paramArrayOfint[b] >= 0 && (a(paramArrayOfint[b])).d <= 0)
        paramArrayOfint[b] = -2; 
      if (b == paramArrayOfint.length - 1 && (a(13)).d <= 0) {
        paramArrayOfint[b] = -1;
      } else if (b == paramArrayOfint.length - 2 && (a(12)).d <= 0) {
        paramArrayOfint[b] = -1;
      } 
    } 
    return paramArrayOfint;
  }
  
  public static void a(byte paramByte, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt) {
    for (byte b = 0; b < paramByte; b++)
      (a(paramArrayOfbyte1[b])).d = paramArrayOfbyte2[b]; 
    TerrainMidlet.e.e = paramInt;
  }
  
  public final void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    model.am.a(paramGraphics);
    int i;
    for (i = 0; i <= el.d; i += 32)
      paramGraphics.drawImage(bx.h, i, el.e - 62, 0); 
    i = ds.Z - k.e >> 1;
    int j = ds.aa - k.f + 70 >> 1;
    a((byte)0, i - 5, j - 15, paramGraphics);
    a(i - 5, j + 114, paramGraphics);
    a(k, i, j, paramGraphics);
    this.n = !(a()).k ? (String.valueOf(model.u.bK()) + ": " + (a()).e + "~ / 1 " + u.bL()) : (String.valueOf(u.bK()) + ": " + u.bs());
    bn.b.a(paramGraphics, this.n, i + 4, j + 84, 0);
    bn.b.a(paramGraphics, ((a()).d > 0) ? (String.valueOf(u.br()) + ": " + (a()).d + " " + u.bL()) : "", i + 4, j + 98, 0);
    if (this.b) {
      Graphics graphics1 = paramGraphics;
      int k = ds.aa - coreLG.l.i.getHeight() >> 1;
      j = ds.Z - coreLG.l.i.getWidth() >> 1;
      i = coreLG.l.k.b;
      graphics1.drawImage(coreLG.l.i, j, k, 0);
      model.bn.b.a(graphics1, u.bq(), el.g, k + 7, 2);
      a(i).a(graphics1, j + 20, k + 25);
      bn.b.a(graphics1, String.valueOf(c) + " " + u.bL(), j + 70, k + 25, 0);
      bn.b.a(graphics1, String.valueOf(u.bK()) + ": " + (c * (a(i)).e) + "~", el.g, k + 52, 2);
      graphics1.drawRegion(bx.v[3], 0, 0, 13, 11, 4, j + 45 + el.c % 3, k + 27, 0);
      graphics1.drawRegion(bx.v[3], 0, 0, 13, 11, 7, j + 115 - el.c % 3, k + 27, 0);
    } 
    Graphics graphics = paramGraphics;
    j = ds.aa - 20;
    i = ds.Z - 5;
    graphics.drawImage(coreLG.l.j, i, j, 40);
    super.a(paramGraphics);
  }
  
  public static void a(bi parambi, int paramInt1, int paramInt2, Graphics paramGraphics) {
    paramGraphics.drawImage(e, paramInt1 - 5, paramInt2 - 2, 0);
    int[] arrayOfInt = new int[a.size()];
    boolean bool = false;
  }
  
  public static void a(byte paramByte, int paramInt1, int paramInt2, Graphics paramGraphics) {
    if (paramByte == 0) {
      paramGraphics.drawImage(f, paramInt1, paramInt2, 0);
      return;
    } 
    if (paramByte == 1)
      paramGraphics.drawImage(g, paramInt1, paramInt2, 0); 
  }
  
  public static void a(int paramInt1, int paramInt2, Graphics paramGraphics) {
    paramGraphics.drawImage(h, paramInt1, paramInt2, 0);
    bn.b.a(paramGraphics, "~" + TerrainMidlet.e.e, paramInt1 + 7, paramInt2 + 5, 0);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\l.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */