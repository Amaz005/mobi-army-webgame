package coreLG;

import model.ac;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.y;

public final class cv {
  public static int a;
  
  public static int b;
  
  private static Vector k = new Vector();
  
  public static Vector c = new Vector();
  
  private static model.ac l;
  
  public static boolean d;
  
  private static byte m;
  
  int e;
  
  private bn n;
  
  public static byte f;
  
  public static String[] g;
  
  public static String[] h;
  
  public static byte[] i;
  
  public static short[] j;
  
  public void a(int paramInt) {
    d = false;
    l = null;
    this.e = paramInt;
    int j = paramInt;
    int i = j;
    byte[] arrayOfByte2 = null;
    byte b2;
    for (b2 = 0; b2 < c.size(); b2++) {
      em em;
      if ((em = (coreLG.em) c.elementAt(b2)).b == i)
        arrayOfByte2 = em.a; 
    } 
    byte[] arrayOfByte1 = arrayOfByte2;
    cj.d = new y(this.i);
    Hashtable hashtable = new Hashtable();
    b2 = 0;
    a = coreLG.cj.a(0, arrayOfByte2);
    b2 += 2;
    b = cj.a(2, arrayOfByte2);
    b2 += 2;
    byte b = arrayOfByte2[4];
    if (k.size() > 0)
      k.removeAllElements(); 
    int[] arrayOfInt = new int[b];
    b2++;
    byte b4;
    for (b4 = 0; b4 < b; b4++) {
      arrayOfInt[b4] = arrayOfByte1[b2];
      cj.d("land ID= " + arrayOfInt[b4]);
      if (!hashtable.containsKey((new StringBuffer(String.valueOf(arrayOfInt[b4]))).toString())) {
        hashtable.put((new StringBuffer(String.valueOf(arrayOfInt[b4]))).toString(), cj.d.a(String.valueOf(arrayOfInt[b4]) + ".png"));
        cj.d("land ID= " + arrayOfInt[b4]);
        bn bn1 = new bn(cj.a(b2 + 1, arrayOfByte1), cj.a(b2 + 3, arrayOfByte1), (Image)hashtable.get((new StringBuffer(String.valueOf(arrayOfInt[b4]))).toString()), !b(arrayOfInt[b4]));
        k.addElement(bn1);
      } else {
        bn bn1 = new bn(cj.a(b2 + 1, arrayOfByte1), cj.a(b2 + 3, arrayOfByte1), (Image)hashtable.get((new StringBuffer(String.valueOf(arrayOfInt[b4]))).toString()), !b(arrayOfInt[b4]));
        k.addElement(bn1);
      } 
      b2 += 5;
    } 
    cj.d = null;
    short[] arrayOfShort1 = new short[b4 = arrayOfByte1[b2]];
    short[] arrayOfShort2 = new short[b4];
    b2++;
    for (byte b3 = 0; b3 < b4; b3++) {
//      arrayOfShort1[b3] = (short)cj.a(b2, this);
//      arrayOfShort2[b3] = (short)cj.a(b2 + 2, this);
      
      arrayOfShort1[b3] = (short)cj.a(b2, arrayOfByte1);
      arrayOfShort2[b3] = (short)cj.a(b2 + 2, arrayOfByte1);
      b2 += 4;
    } 
    ac.f = b - ac.a.getHeight() + ac.b.getHeight() + 37;
    cj.d("MAPID= " + paramInt);
    for (byte b1 = 0; b1 < c.size(); b1++) {
//      em em = c.elementAt(b1);
      em em = (coreLG.em) c.elementAt(b1);
      if (paramInt == em.b) {
        if (paramInt == 36 || paramInt == 37 || paramInt == 38 || paramInt == 39) {
          l = new ac((byte)14);
        } else {
          l = new ac((byte)em.c);
        } 
        if (em.d != -1)
          l.d = em.d; 
        if (em.g != -1)
          l.e = em.g; 
        if (em.f != -1)
          ac.f += em.f; 
        if (em.e != -1) {
          m = (byte)em.e;
          d = true;
          return;
        } 
        break;
      } 
    } 
  }
  
  private static boolean b(int paramInt) {
    for (byte b = 0; b < j.length; b++) {
      if (paramInt == j[b])
        return true; 
    } 
    return false;
  }
  
  public static void a(bn parambn) {
    k.addElement(parambn);
  }
  
  public final boolean a(int paramInt1, int paramInt2) {
    for (byte b = 0; b < k.size(); b++) {
//      this.n = k.elementAt(b);
      this.n = (bn) k.elementAt(b);
      if (cj.a(paramInt1, paramInt2, this.n.e, this.n.f, this.n.c, this.n.d) && cj.e(this.n.a(paramInt1 - this.n.e, paramInt2 - this.n.f)))
        return true; 
    } 
    return false;
  }
  
  public static boolean a(int paramInt1, int paramInt2, byte paramByte) {
    if (ci.a == 2) {
      if (ci.O.size() > 3)
        return true; 
      if (ci.a == 1 && ci.O.size() > 6)
        return true; 
    } 
    if (m == 0) {
      if (paramInt2 >= ac.f + 48)
        switch (paramByte) {
          case 0:
            return true;
          case 1:
            return true;
          case 2:
            return true;
        }  
    } else if (m == 1) {
      if (paramInt2 >= ac.g)
        switch (paramByte) {
          case 0:
            ci.P.a(paramInt1, paramInt2, cj.b(-4, 4), cj.b(-11, -9), (byte)6);
            return true;
          case 1:
            ci.P.a(paramInt1, paramInt2, cj.b(-4, 4), cj.b(-14, -12), (byte)6);
            ci.P.a(paramInt1, paramInt2, cj.b(-6, 6), cj.b(-11, -9), (byte)6);
            return true;
          case 2:
            ci.P.a(paramInt1, paramInt2, cj.b(-4, 4), cj.b(-13, -11), (byte)6);
            ci.P.a(paramInt1, paramInt2, cj.b(-6, 6), cj.b(-11, -9), (byte)6);
            ci.P.a(paramInt1, paramInt2, cj.b(-2, 2), cj.b(-17, -15), (byte)6);
            return true;
        }  
    } else if (m == 2 && paramInt2 >= ac.g) {
      switch (paramByte) {
        case 0:
          ci.P.a(paramInt1, paramInt2, cj.b(-4, 4), cj.b(-11, -9), (byte)14);
          return true;
        case 1:
          ci.P.a(paramInt1, paramInt2, cj.b(-4, 4), cj.b(-14, -12), (byte)14);
          ci.P.a(paramInt1, paramInt2, cj.b(-6, 6), cj.b(-11, -9), (byte)14);
          return true;
        case 2:
          ci.P.a(paramInt1, paramInt2, cj.b(-4, 4), cj.b(-13, -11), (byte)14);
          ci.P.a(paramInt1, paramInt2, cj.b(-6, 6), cj.b(-11, -9), (byte)14);
          ci.P.a(paramInt1, paramInt2, cj.b(-2, 2), cj.b(-17, -15), (byte)14);
          return true;
      } 
    } 
    return false;
  }
  
  public final void b(int paramInt1, int paramInt2, byte paramByte) {
    int i = bn.a(paramByte);
    int j = bn.b(paramByte);
    byte b;
    for (b = 0; b < k.size(); b++) {
//      this.n = k.elementAt(b);
      this.n = (bn) k.elementAt(b);
      if (this.n.g && cj.a(paramInt1 - i / 2, paramInt2 - j / 2, i, j, this.n.e, this.n.f, this.n.c, this.n.d))
        ((bn)k.elementAt(b)).a(paramInt1, paramInt2, paramByte); 
    } 
    for (b = 0; b < eb.b.length; b++) {
      if (eb.b[b] != null) {
        if (cj.a((eb.b[b]).G, (eb.b[b]).H, paramInt1 - i / 2, paramInt2 - j / 2, i, j)) {
          if ((eb.b[b]).aa != 5 && (eb.b[b]).ag != 30)
            eb.b[b].c((paramInt1 > (eb.b[b]).G) ? 2 : 0); 
          (eb.b[b]).R = false;
          (eb.b[b]).S = true;
          (eb.b[b]).T = true;
        } 
        if ((eb.b[b]).P == 14)
          while (!a((eb.b[b]).G, (eb.b[b]).az)) {
            (eb.b[b]).az++;
            if ((eb.b[b]).az <= b)
              continue; 
            break;
          }  
      } 
    } 
  }
  
  public static void a() {
    if (l != null)
      l.a(); 
    for (byte b = 0; b < k.size(); b++) {
      if ((bn)k.elementAt(b) != null)
        ((bn)k.elementAt(b)).a(); 
    } 
  }
  
  public static void a(Graphics paramGraphics) {
    for (byte b = 0; b < k.size(); b++) {
      if ((bn)k.elementAt(b) != null)
        ((bn)k.elementAt(b)).a(paramGraphics); 
    } 
  }
  
  public static void b(Graphics paramGraphics) {
    if (l != null)
      l.a(paramGraphics); 
  }
  
  public static void c(Graphics paramGraphics) {
    if (l != null)
      l.a(m, paramGraphics); 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\cv.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */