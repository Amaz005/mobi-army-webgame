package coreLG;

import model.ad;
import model.c;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class bg {
  private static Image o = ci.h;
  
  public Vector a = new Vector();
  
  private byte p = 0;
  
  private int q;
  
  private int r;
  
  public static int b;
  
  private byte s;
  
  private byte t;
  
  private byte u = -1;
  
  private byte v = -1;
  
  private int w = 1;
  
  private int x = 0;
  
  private int y = 0;
  
  private boolean z = true;
  
  public static boolean c = false;
  
  private static boolean A;
  
  private static boolean B;
  
  private static boolean C;
  
  static boolean d;
  
  private static boolean D;
  
  private static boolean E;
  
  private static boolean F;
  
  private static boolean G;
  
  private static boolean H;
  
  public int e;
  
  private static int I = 20;
  
  public static int f = 0;
  
  private static int[] J = new int[I];
  
  private static int[] K = new int[I];
  
  private static int[] L = new int[I];
  
  private static int[] M = new int[I];
  
  private static int N = 20;
  
  static int g;
  
  static int h;
  
  private static int O;
  
  private static int P;
  
  public static Vector i = new Vector();
  
  public static boolean j = false;
  
  private static int Q;
  
  private static int R;
  
  private static int S;
  
  private int T;
  
  private static int U;
  
  private static int V;
  
  public static int k;
  
  public static boolean l;
  
  public static Vector m = new Vector();
  
  private short[][] W;
  
  private short[][] X;
  
  private int Y = 0;
  
  public static byte n;
  
  private static boolean Z;
  
  private boolean aa;
  
  public bg() {
    c = false;
    this.e = 0;
    this.p = 0;
  }
  
  public void a(byte paramByte1, byte paramByte2, short[][] paramArrayOfshort1, short[][] paramArrayOfshort2, byte paramByte3, byte paramByte4) {
    this.s = paramByte2;
    this.W = paramArrayOfshort1;
    this.X = paramArrayOfshort2;
    U = this.q;
    V = this.r;
    this.t = paramByte1;
    n = paramByte4;
    c = true;
    this.p = paramByte3;
    this.T = 1;
    this.Y = 0;
    j = false;
    this.aa = false;
    Z = false;
    if (paramByte2 == 43)
      Z = true; 
  }
  
  private void a(byte paramByte) {
    byte b;
    switch (paramByte) {
      case 0:
      case 32:
      case 34:
      case 35:
      case 37:
      case 41:
      case 42:
      case 45:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        this.Y++;
        return;
      case 43:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        this.Y++;
        return;
      case 1:
      case 10:
      case 11:
      case 33:
      case 44:
      case 47:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        this.Y++;
        return;
      case 2:
        for (b = 0; b < 3; b++)
          this.a.addElement(new ah(paramByte, this.W[b + this.Y], this.X[b + this.Y], this.t)); 
        this.Y += 3;
        return;
      case 3:
        this.a.addElement(new ah(paramByte, this.W[1], this.X[1], this.t));
        ci.U = false;
        return;
      case 4:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 5:
      case 36:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 6:
        for (b = 0; b < 3; b++)
          this.a.addElement(new ah(paramByte, this.W[b], this.X[b], this.t)); 
        return;
      case 7:
      case 31:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 8:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 9:
        for (b = 0; b < 4; b++)
          this.a.addElement(new ah(paramByte, this.W[b + this.Y], this.X[b + this.Y], this.t)); 
        this.Y += 4;
        return;
      case 16:
        this.a.addElement(new ah((byte)16, this.W[0], this.X[0], this.t));
        return;
      case 12:
        for (b = 1; b <= 6; b++)
          this.a.addElement(new ah(paramByte, this.W[b], this.X[b], this.t)); 
        return;
      case 14:
      case 40:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 15:
        this.a.addElement(new ah(paramByte, this.W[1], this.X[1], this.t));
        return;
      case 13:
      case 17:
      case 19:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        this.Y++;
        return;
      case 20:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        this.e++;
        this.Y++;
        return;
      case 18:
        if (!this.aa) {
          for (b = 0; b < 3; b++) {
            this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
            this.Y++;
          } 
          return;
        } 
        break;
      case 21:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        this.Y++;
        return;
      case 22:
        this.a.addElement(new ah(paramByte, this.W[this.Y], this.X[this.Y], this.t));
        return;
      case 23:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 24:
        for (b = 1; b <= 7; b++)
          this.a.addElement(new ah(paramByte, this.W[b], this.X[b], this.t)); 
        return;
      case 25:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 26:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 27:
        for (b = 1; b <= 4; b++)
          this.a.addElement(new ah(paramByte, this.W[b], this.X[b], this.t)); 
        return;
      case 28:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        return;
      case 29:
        for (b = 1; b <= 13; b++)
          this.a.addElement(new ah(paramByte, this.W[b], this.X[b], this.t)); 
        return;
      case 30:
        this.a.addElement(new ah(paramByte, this.W[0], this.X[0], this.t));
        break;
    } 
  }
  
  public void a() {
    try {
      byte b;
      for (b = 0; b < this.a.size(); b++)
        ((ah)this.a.elementAt(b)).a(); 
      if (!this.z) {
        if (!Z) {
          this.u = (byte)(this.u - 1);
          if (this.u == 0 && this.w > 0) {
            a(this.s);
            this.u = this.v;
            this.w--;
            if (this.w <= 1)
              this.z = true; 
          } 
        } else {
          bl bl = eb.b[this.t];
          if (this.W[this.Y][0] <= bl.G + 10 && this.W[this.Y][0] >= bl.G - 10) {
            a(this.s);
            this.w--;
            if (this.w <= 1)
              this.z = true; 
          } 
        } 
      } else if (this.a.size() == 0 && this.e <= 0 && this.p > 0 && !j) {
        System.out.println("CReateBullet nShoot: " + this.p);
        this.z = true;
        f = 0;
        byte b1;
        for (b1 = 0; b1 < I; b1++) {
          J[b1] = -1;
          K[b1] = -1;
          L[b1] = -1;
          M[b1] = -1;
        } 
        this.a(this.s);
        b1 = 0;
        switch (this.s) {
          case 0:
          case 32:
          case 40:
          case 41:
            this.e = 1;
            break;
          case 1:
            this.v = 5;
            this.z = false;
            this.e = 2;
            break;
          case 2:
            b1 = 1;
            this.e = 3;
            break;
          case 4:
            A = false;
            B = false;
            g = 400;
            h = N;
            this.e = 2;
            break;
          case 5:
          case 36:
            this.e = 1;
            break;
          case 6:
            b1 = 1;
            this.e = 3;
            break;
          case 7:
          case 31:
            this.e = 1;
            break;
          case 8:
            this.e = 1;
            break;
          case 9:
            this.e = 4;
            break;
          case 10:
            this.v = 5;
            this.z = false;
            this.e = 3;
            break;
          case 11:
            this.v = 3;
            this.z = false;
            this.e = 5;
            break;
          case 16:
            this.e = 7;
            break;
          case 14:
            this.e = 2;
            break;
          case 13:
            this.e = 1;
            break;
          case 19:
            this.e = 1;
            break;
          case 23:
            this.e = 8;
            break;
          case 26:
            this.e = 5;
            break;
          case 17:
            this.e = 1;
            break;
          case 28:
            this.e = 14;
            break;
          case 30:
            this.e = 1;
            break;
          case 21:
            this.e = 1;
            break;
          case 25:
            this.e = 1;
            break;
          case 22:
            this.e = 1;
            break;
          case 33:
            this.v = 5;
            this.z = false;
            this.e = 5;
            break;
          case 34:
            this.e = 1;
          case 35:
            this.e = 1;
            break;
          case 37:
            this.e = 1;
            break;
          case 42:
            this.e = 1;
            break;
          case 43:
            this.v = 11;
            this.z = false;
            this.e = 10;
            break;
          case 44:
            this.v = 3;
            this.z = false;
            this.e = 15;
            break;
          case 45:
            this.e = 1;
            break;
          case 47:
            this.v = 2;
            this.z = false;
            this.e = 5;
            break;
        } 
        if (this.s != 43)
          coreLG.ci.M.a((coreLG.ah)this.a.elementAt(b1));
        this.w = this.e;
        this.u = this.v;
        this.p = (byte)(this.p - 1);
        this.T++;
        if (this.T == this.p)
          this.T = 0; 
      } 
      if (A) {
        g += N;
        if (!B && g >= this.q - 165) {
          a((byte)3);
          coreLG.ci.M.a((coreLG.ah)this.a.elementAt(0));
          B = true;
        } 
        if (g >= cv.a)
          A = false; 
      } 
      if (C) {
        this.x++;
        if (this.x == 10)
          ci.M.a(O, 100); 
        if (this.x == 20) {
          this.x = 0;
          C = false;
          a((byte)15);
          coreLG.ci.M.c((coreLG.ah)this.a.elementAt(0));
        } 
      } 
      if (i.size() != 0) {
        d = true;
        for (b = 0; b < i.size(); b++)
          ((c)i.elementAt(b)).a(); 
      } else {
        d = false;
      } 
      if (D) {
        D = false;
        a((byte)18);
        coreLG.ci.M.a((coreLG.ah)this.a.elementAt(0));
      } 
      if (E) {
        E = false;
        a((byte)20);
        if (Q > -500 && R > -500)
          coreLG.ci.M.a((coreLG.ah)this.a.elementAt(this.a.size() - 1));
      } 
      if (F) {
        this.y++;
        if (this.y == 20) {
          this.y = 0;
          F = false;
          a((byte)24);
          coreLG.ci.M.b((coreLG.ah)this.a.elementAt(0));
        } 
      } 
      if (G) {
        G = false;
        a((byte)27);
        coreLG.ci.M.a((coreLG.ah)this.a.elementAt(0));
      } 
      if (H) {
        H = false;
        a((byte)29);
        coreLG.ci.M.d((coreLG.ah)this.a.elementAt(0));
      } 
      if (l && el.c % 3 == 0) {
        if (m.size() != 0) {
          coreLG.bp bp = (coreLG.bp) m.elementAt(0);
          coreLG.ci.P.a(bp.c, bp.d, bp.e, bp.f, 1);
          int i = bp.e + cj.b(-35, 35);
          int j = bp.f + cj.b(-10, 10);
          m.removeElement(bp);
          return;
        } 
        l = false;
        return;
      } 
    } catch (Exception exception2) {
      Exception exception1;
      (exception1 = null).printStackTrace();
      c();
    } 
  }
  
  public void a(Graphics paramGraphics) {
    byte b;
    for (b = 0; b < this.a.size(); b++)
      ((ah)this.a.elementAt(b)).a(paramGraphics); 
    if (A)
      paramGraphics.drawImage(o, g, h + el.c % 3, 3); 
    if (C && this.x > 5 && this.x < 20) {
      paramGraphics.setColor(16771821);
      paramGraphics.fillRect(O - 2, -100, 4, P + 100);
    } 
    if (d)
      for (b = 0; b < i.size(); b++)
        ((c)i.elementAt(b)).a(paramGraphics);  
  }
  
  public void a(coreLG.ah paramah, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte paramByte) {
    this.a.removeElement(paramah);
    if (paramah.d == 45) {
      c();
      return;
    } 
    ah ah1;
    if (this.a.size() > 0 && (ah1 = (ah) this.a.elementAt(0)).d != 19 && ah1.d != 43)
      if (ah1.d == 29) {
        ci.M.d(ah1);
      } else {
        ci.M.a(ah1);
      }  
    if (!coreLG.ah.a((int)paramah.d) || this.s == 14) {
      int i = ++f - 1;
      if (paramBoolean) {
        J[i] = paramInt1;
        K[i] = paramInt2;
        L[i] = paramInt3;
        M[i] = paramInt4;
      } else {
        J[i] = -1;
        K[i] = -1;
        L[i] = -1;
        M[i] = -1;
      } 
    } 
    if (coreLG.ah.a((int)paramah.d))
      if (paramBoolean) {
        this.q = paramInt1;
        this.r = paramInt2;
      } else {
        this.e = 1;
      }  
    if (this.s == 19) {
      this.q = U;
      this.r = V;
    } 
    this.e--;
    if (this.e == 0 && this.p == 0) {
      if (paramByte == ci.X)
        j = true; 
      if (this.p == 0)
        c(); 
      model.ad.a().a((byte)f, L, M);
    }
    coreLG.cj.d("REMOVE " + this.a.size() + " nBull= " + this.e + " nBoom= " + f);
  }
  
  public static void b() {
    i.removeAllElements();
  }
  
  private void c() {
    c = false;
    this.e = 0;
    this.p = 0;
    this.aa = true;
    if (d)
      for (byte b = 0; b < i.size(); b++) {
        model.c c;
        (c = (model.c) i.elementAt(b)).b--;
      }
    coreLG.cj.d("END SHOOT");
    coreLG.el.O = true;
    coreLG.el.P = 0;
  }
  
  public static void a(int paramInt1, int paramInt2) {
    A = true;
    g = paramInt1 - 400;
    h = paramInt2 - 320;
    ci.M.a(paramInt1 - 180, paramInt2 - 320);
    ci.U = false;
  }
  
  public static void b(int paramInt1, int paramInt2) {
    C = true;
    O = paramInt1;
    P = paramInt2;
    ci.M.a(paramInt1, paramInt2);
  }
  
  public static void c(int paramInt1, int paramInt2) {
    i.addElement(new model.c(paramInt1, paramInt2, 3));
  }
  
  public void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    D = true;
    paramInt1 = this.q - paramInt1;
    int i = this.r - paramInt2;
    cj.a(paramInt1, i);
  }
  
  public static void d(int paramInt1, int paramInt2) {
    E = true;
    Q = paramInt1;
    R = paramInt2;
  }
  
  public void e(int paramInt1, int paramInt2) {
    this.q = paramInt1;
    this.r = paramInt2 - 500;
    a((byte)12);
  }
  
  public static void b(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    G = true;
    S = (b < 90 && b > -90) ? 1 : -1;
  }
  
  public static void f(int paramInt1, int paramInt2) {
    F = true;
  }
  
  public static void g(int paramInt1, int paramInt2) {
    H = true;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bg.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */