package coreLG;

import model.ab;
import model.ad;
import model.aq;
import model.as;
import model.az;
import model.bb;
import model.bg;
import model.bk;
import model.bm;
import model.bn;
import model.bp;
import model.bs;
import model.h;
import model.i;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import model.q;
import model.w;
import model.z;

public final class el extends Canvas implements Runnable {
  private Thread U;
  
  public static el a;
  
  boolean b = true;
  
  public static int c;
  
  public static int d;
  
  public static int e;
  
  public static int f;
  
  public static int g;
  
  public static ds h;
  
  private static ae V;
  
  public static ej i;
  
  public static ci j;
  
  public static dl k;
  
  public static dc l;
  
  public static et m;
  
  public static f n;
  
  public static dd o;
  
  public static bx p;
  
  public static coreLG.ab q;
  
  public static l r;
  
  public static fa s;
  
  public static boolean t;
  
  private static boolean W;
  
  public static bk u;
  
  public static bg v = new bg();
  
  static aq w = new model.aq();
  
  private static boolean X;
  
  public static boolean x;
  
  public static model.i y = new i();
  
  public static q z;
  
  public static Vector A = new Vector();
  
  public static w B;
  
  public static bs C;
  
  public static int D;
  
  public static model.bp E;
  
  public static model.ab F;
  
  public static ek G;
  
  private h Y;
  
  public static boolean[] H = new boolean[14];
  
  private static boolean[] Z = new boolean[14];
  
  public static boolean[] I = new boolean[14];
  
  public static int J;
  
  public static int K;
  
  public static boolean L;
  
  public static boolean M;
  
  public static Image N;
  
  public static boolean O;
  
  public static int P;
  
  public static byte Q;
  
  public static byte R;
  
  public static byte S;
  
  public static byte T;
  
  private static Random aa = new Random();
  
  public el() {
    a = this;
    X = (getKeyCode(8) == -20);
    x = (System.getProperty("microedition.platform").indexOf("RIM") == 0);
    a(false);
    model.v.a();
    this.U = new Thread(this);
    this.U.start();
  }
  
  private void a(boolean paramBoolean) {
    setFullScreenMode(true);
    d = getWidth();
    e = getHeight();
    ds.Z = d;
    ds.aa = e;
    if (paramBoolean) {
      this.Y = new h();
    } else {
      this.Y = null;
    } 
    f = e / 2;
    g = d / 2;
    V = new ae();
    i = new ej();
    k = new dl();
    n = new f();
    o = new dd();
    p = new bx();
    E = new bp();
    F = new model.ab();
    q = new coreLG.ab();
    C = new model.bs();
    r = new l();
    j = new ci();
    u = new model.bk();
    B = new model.w();
    G = new ek();
    k.b();
  }
  
  public final int getGameAction(int paramInt) {
    return getGameAction(paramInt);
  }
  
  public final void run() {
    while (this.b) {
      if (++c > 10000)
        c = 0; 
      long l2 = System.currentTimeMillis();
      try {
        if (v.a) {
          bg bg1 = v;
          if (H[2]) {
            bg1.c--;
            if (bg1.c < 0)
              bg1.c = bg1.b.size() - 1; 
          } else if (H[8]) {
            bg1.c++;
            if (bg1.c > bg1.b.size() - 1)
              bg1.c = 0; 
          } else if (H[5] || H[12]) {
            bg1.a = false;
            if (bg1.b.size() > 0)
              ((model.z)bg1.b.elementAt(bg1.c)).b.a();
          } else if (H[13]) {
            bg1.a = false;
          } 
          if ((bg.i = bg1.c * ds.ab - 2 * ds.ab) > bg.m)
            bg.i = bg.m; 
          if (bg.i < 0)
            bg.i = 0; 
          if (bg1.c == bg1.b.size() - 1 || bg1.c == 0)
            bg.j = bg.i; 
          ds.t();
        } else if (w.a) {
          aq aq1 = w;
          if (H[2]) {
            aq1.c--;
            if (aq1.c < 0)
              aq1.c = aq1.b.size() - 1; 
          } else if (H[8]) {
            aq1.c++;
            if (aq1.c > aq1.b.size() - 1)
              aq1.c = 0; 
          } else if (H[5] || H[12] || H[13]) {
            aq1.a = false;
            if (aq1.b.size() > 0)
              ((model.z)aq1.b.elementAt(aq1.c)).b.a();
          } 
          ds.t();
        } 
        if (z != null) {
          z.a();
        } else {
          h.d();
        } 
        el el1 = this;
        if (D > 0)
          D--; 
        for (byte b = 0; b < A.size(); b++)
          ((bm)A.elementAt(b)).a(); 
        if (v.a) {
          bg bg1 = v;
          if (bg.j != bg.i) {
            model.bg.l = bg.i - bg.j << 2;
            bg.k += bg.l;
            bg.j += bg.k >> 4;
            bg.k &= 0xF;
          } 
          if (bg1.h > bg1.e) {
            int j;
            if ((j = bg1.h - bg1.e >> 1) < 1)
              j = 1; 
            bg1.h -= j;
          } 
          bg1.h = bg1.e;
          if (Math.abs(bg.i - bg.j) < 15 && bg.j < 0)
            bg.i = 0; 
          if (Math.abs(model.bg.i - bg.j) < 10 && bg.j > bg.m)
            bg.i = bg.m; 
        } else if (w.a) {
          aq aq1;
          if ((aq1 = w).e > aq1.d) {
            int j;
            if ((j = aq1.e - aq1.d >> 1) < 1)
              j = 1; 
            aq1.e -= j;
          } 
          aq1.e = aq1.d;
        } 
        if (el1.Y != null)
          el1.Y.a(); 
        h.c();
        if (O && ++P == 20) {
          P = 0;
          O = false;
          synchronized (bb.a) {
            bb.a.notify();
          } 
        } 
        repaint();
        serviceRepaints();
      } catch (Exception exception2) {
        Exception exception1;
        (exception1 = null).printStackTrace();
      } 
      long l1 = System.currentTimeMillis() - l2;
      try {
        if (l1 < 60L) {
          Thread.sleep(60L - l1);
          continue;
        } 
        Thread.sleep(1L);
      } catch (InterruptedException interruptedException) {}
    } 
  }
  
  protected final void paint(Graphics paramGraphics) {
    h.a(paramGraphics);
    if (z != null)
      z.a(paramGraphics); 
    if (v.a) {
      Graphics graphics = paramGraphics;
      bg bg1 = v;
      graphics.translate(-graphics.getTranslateX(), -graphics.getTranslateY());
      ds.a(bg1.d - 2, bg1.e - 6, bg1.f, bg1.g + 12, graphics);
      graphics.translate(bg1.d + 5, bg1.h + 2);
      graphics.setClip(-5, 0, bg1.f, bg1.g);
      graphics.translate(0, -bg.j);
      for (byte b1 = 0; b1 < bg1.b.size(); b1++) {
        graphics.setColor(0);
        if (b1 == bg1.c) {
          graphics.setColor(15772161);
          graphics.fillRect(-3, b1 * ds.ab, bg1.f - 8, ds.ab);
        } 
        model.bn.b.a(graphics, ((z)bg1.b.elementAt(b1)).a, 0, 3 + b1 * ds.ab, 0);
      } 
      graphics.translate(-graphics.getTranslateX(), -graphics.getTranslateY());
      graphics.setClip(-1000, -1000, 2000, 2000);
      model.bn.b.a(graphics, model.u.cF(), 5, e - bn.b.a() - 3, 0);
      model.bn.b.a(graphics, model.u.bh(), d - 5, e - model.bn.b.a() - 3, 1);
    } else if (w.a) {
      Graphics graphics = paramGraphics;
      aq aq1 = w;
      graphics.translate(-graphics.getTranslateX(), -graphics.getTranslateY());
      graphics.setClip(0, 0, d, e);
      for (byte b1 = 0; b1 < aq1.b.size(); b1++) {
        graphics.setColor(0);
        if (b1 == aq1.c) {
          graphics.setColor(16777014);
          graphics.fillRect(0, aq1.d + b1 * 24 - 1, d, 24);
        } 
        model.bn.d.a(graphics, ((z)aq1.b.elementAt(b1)).a, g, aq1.d + b1 * 24, 2);
      } 
    } 
    for (byte b = 0; b < A.size(); b++)
      ((bm)A.elementAt(b)).a(paramGraphics); 
    if (this.Y != null)
      this.Y.a(paramGraphics); 
  }
  
  public final void keyPressed(int paramInt) {
    if (X) {
      switch (getGameAction(paramInt)) {
        case 1:
          I[2] = true;
          H[2] = true;
          return;
        case 6:
          I[8] = true;
          H[8] = true;
          return;
        case 2:
          I[4] = true;
          H[4] = true;
          return;
        case 5:
          I[6] = true;
          H[6] = true;
          return;
        case 8:
          I[5] = true;
          H[5] = true;
          return;
      } 
      if (paramInt == -21) {
        paramInt = -6;
      } else if (paramInt == -22) {
        paramInt = -7;
      } 
    } else if (x) {
      switch (paramInt) {
        case 1:
          I[2] = true;
          H[2] = true;
          return;
        case 6:
          I[8] = true;
          H[8] = true;
          return;
        case 2:
          I[4] = true;
          H[4] = true;
          return;
        case 5:
          I[6] = true;
          H[6] = true;
          return;
        case -8:
          I[5] = true;
          H[5] = true;
          return;
      } 
    } 
    int j = paramInt;
    if (z != null) {
      z.a(j);
    } else {
      v.getClass();
    } 
    h.e(j);
    switch (j) {
      case 42:
        I[10] = true;
        H[10] = true;
        return;
      case 35:
        I[11] = true;
        H[11] = true;
        return;
      case -21:
      case -6:
        I[12] = true;
        H[12] = true;
        return;
      case -22:
      case -7:
        I[13] = true;
        H[13] = true;
        return;
      case -5:
      case 10:
        I[5] = true;
        H[5] = true;
        return;
      case -38:
      case -1:
        I[2] = true;
        H[2] = true;
        return;
      case -39:
      case -2:
        I[8] = true;
        H[8] = true;
        return;
      case -3:
        I[4] = true;
        H[4] = true;
        return;
      case -4:
        I[6] = true;
        H[6] = true;
        break;
    } 
  }
  
  public final void keyReleased(int paramInt) {
    if (X) {
      switch (getGameAction(paramInt)) {
        case 1:
          I[2] = false;
          return;
        case 6:
          I[8] = false;
          return;
        case 2:
          I[4] = false;
          return;
        case 5:
          I[6] = false;
          return;
        case 8:
          I[5] = false;
          return;
      } 
      if (paramInt == -21) {
        paramInt = -6;
      } else if (paramInt == -22) {
        paramInt = -7;
      } 
    } else if (x) {
      switch (paramInt) {
        case 1:
          I[2] = false;
          return;
        case 6:
          I[8] = false;
          return;
        case 2:
          I[4] = false;
          return;
        case 5:
          I[6] = false;
          return;
        case -8:
          I[5] = false;
          return;
      } 
      if (paramInt == 27)
        paramInt = -7; 
    } 
    int j;
    switch (j = paramInt) {
      case 42:
        I[10] = false;
        Z[10] = true;
        return;
      case 35:
        I[11] = false;
        Z[11] = true;
        return;
      case -21:
      case -6:
        I[12] = false;
        Z[12] = true;
        return;
      case -22:
      case -7:
        I[13] = false;
        Z[13] = true;
        return;
      case -5:
      case 10:
        I[5] = false;
        Z[5] = true;
        return;
      case -38:
      case -1:
        I[2] = false;
        return;
      case -39:
      case -2:
        I[8] = false;
        return;
      case -3:
        I[4] = false;
        return;
      case -4:
        I[6] = false;
        break;
    } 
  }
  
  protected final void pointerDragged(int paramInt1, int paramInt2) {
    J = paramInt1;
    K = paramInt2;
  }
  
  protected final void pointerPressed(int paramInt1, int paramInt2) {
    L = true;
    M = false;
    J = paramInt1;
    K = paramInt2;
  }
  
  public static void a() {
    String str;
    if ((Q = (byte)cj.c(str = "tileversion")) == -1)
      Q = 0; 
    if ((R = (byte)cj.c(str = "iconversion")) == -1)
      R = 0; 
    if ((S = (byte)cj.c(str = "valuesversion")) == -1)
      S = 0; 
    if ((T = (byte)cj.c(str = "playerVersion")) == -1)
      T = 0; 
    model.ad.a().a((byte)2, S);
    if (cj.b(str = "valuesdata") != null) {
      a(cj.b(str = "valuesdata"));
    } else {
      dl.e = true;
    } 
    if (cj.b(str = "icondata") != null) {
      bx.z = cj.b(str = "icondata");
      bx.a();
    } 
    if (cj.b(str = "tiledata") != null)
      cv.i = cj.b(str = "tiledata"); 
    if (cj.b(str = "playerdata") != null) {
      bl.h = cj.b(str = "playerdata");
      bl.a();
    } 
  }
  
  public static void a(byte[] paramArrayOfbyte) {
    az az = new az((byte)90, paramArrayOfbyte);
    try {
      byte b = az.b().readByte();
      cv.g = new String[b];
      cv.h = new String[b];
      for (byte b1 = 0; b1 < b; b1++) {
        byte b2 = az.b().readByte();
        short s;
        byte[] arrayOfByte = new byte[s = az.b().readShort()];
        az.b().read(arrayOfByte, 0, s);
        short[] arrayOfShort = new short[5];
        for (byte b3 = 0; b3 < 5; b3++)
          arrayOfShort[b3] = az.b().readShort(); 
        cv.g[b1] = az.b().readUTF();
        cv.h[b1] = az.b().readUTF();
        em em = new em(arrayOfByte, b2, arrayOfShort);
        cv.c.addElement(em);
      } 
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public static void a(String paramString, byte[] paramArrayOfbyte) {
    cj.a(paramArrayOfbyte, paramString);
  }
  
  public static void a(String paramString, byte paramByte) {
    cj.a(paramString, paramByte);
  }
  
  protected final void pointerReleased(int paramInt1, int paramInt2) {
    L = false;
    M = true;
    J = paramInt1;
    K = paramInt2;
    if (h == k && k.h) {
      if (a(g - 20, 0, 40, 40) && (h == V || h == k)) {
        if (!W) {
          W = true;
          a(true);
          return;
        } 
        W = false;
        a(false);
      } 
    } 
  }
  
  public static boolean a(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return (!L && !M) ? false : ((J >= paramInt1 && J <= paramInt1 + paramInt3 && K >= paramInt2 && K <= paramInt2 + paramInt4));
  }
  
  public static void a(String paramString) {
    y.a(paramString, null, new model.z("OK", new bf()), null);
    z = (q)y;
  }
  
  public static void a(String paramString, as paramas) {
    y.a(paramString, null, new z("OK", new ar(paramas)), null);
    z = (q)y;
  }
  
  public static void b(String paramString, as paramas) {
    y.a(paramString, new z(model.u.bi(), paramas), new model.z("", paramas), new z(model.u.bh(), new au()));
    z = (q)y;
  }
  
  public static void a(String paramString, as paramas1, as paramas2) {
    y.a(paramString, new z(model.u.bi(), paramas1), new model.z("", paramas1), new z(model.u.bh(), paramas2));
    z = (q)y;
  }
  
  public static void b(String paramString) {
    y.a(paramString, null, new z("Cancel", new ax()), null);
    z = (q)y;
  }
  
  public static int a(int paramInt1, int paramInt2) {
    return paramInt1 + aa.nextInt(paramInt2 - paramInt1);
  }
  
  public static void c(String paramString) {
    y.a(paramString, null, null, null);
    z = (q)y;
  }
  
  public static void b() {
    z = null;
  }
  
  public final void c() {
    this.b = false;
    if (j != null)
      ad.a().c(); 
  }
  
  static {
    try {
      N = Image.createImage("/bpa.png");
    } catch (IOException iOException2) {
      IOException iOException1;
      (iOException1 = null).printStackTrace();
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\el.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */