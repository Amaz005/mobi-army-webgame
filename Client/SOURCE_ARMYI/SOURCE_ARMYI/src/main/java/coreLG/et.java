package coreLG;

import model.am;
import model.bn;
import model.bw;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.u;
import model.z;

public final class et extends ds {
  private int d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h;
  
  private int i = 0;
  
  protected int a;
  
  public static String[] b = null;
  
  private static Image j = ci.C;
  
  private static Image k = ci.D;
  
  private static Image l = ci.E;
  
  public Vector c;
  
  public et() {
    this.ad = new model.z(u.cK(), new dr(this));
    this.ac = new model.z(u.cJ(), new dq(this));
    this.ae = new z(u.cI(), new dp(this));
  }
  
  protected final void a() {
    byte b;
    if ((b = ((bw)this.c.elementAt(this.a)).a) == -1)
      return; 
    model.ad.a().a(b);
    el.b(u.cs());
    coreLG.f.e = 0;
    coreLG.f.d = 0;
    coreLG.f.c = 0;
    bx.c = ((bw)this.c.elementAt(this.a)).c;
  }
  
  public final void b() {
    super.b();
  }
  
  public final void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    am.a(paramGraphics);
    for (byte b1 = 0; b1 <= el.d; b1 += 32)
      paramGraphics.drawImage(bx.h, b1, el.e - 62, 0); 
    Graphics graphics = paramGraphics;
    et et1 = this;
    graphics.translate(0, 5);
    graphics.setClip(0, 0, el.d, el.e - 15 - ds.ab + ((el.d < 150) ? 10 : 0));
    graphics.translate(0, -et1.e);
    int i = 0;
    for (byte b2 = 0; b2 < et1.c.size(); b2++) {
      if (b2 == et1.a) {
        graphics.setColor(16777014);
        graphics.fillRect(1, i + 3, el.d - 2, ds.ab);
      } 
      bw bw;
      if ((bw = (model.bw) et1.c.elementAt(b2)).a == -1) {
        for (byte b = 0; b <= el.d / k.getWidth(); b++)
          graphics.drawImage(k, b * k.getWidth(), i + 2, 0); 
        bn.d.a(graphics, b[bw.c], 10, i + 3, 0);
        i += ds.ab + 3;
      } else {
        graphics.drawImage(l, 10, i + 6, 0);
        model.bn.c.a(graphics, String.valueOf(u.cL()) + " " + bw.a, 30, i + 5, 0);
        if (bw.b >= 0 && bw.b <= 2)
          graphics.drawRegion(j, 0, bw.b * 7, 7, 7, 0, el.d - 20, i + 9, 0); 
        i += ds.ab;
      } 
    } 
    super.a(paramGraphics);
  }
  
  public final void d() {
    super.d();
    boolean bool = false;
    if (el.H[8]) {
      el.H[8] = false;
      ds.aj = false;
      this.a++;
      if (this.a >= this.c.size())
        this.a = 1; 
      if (((bw)this.c.elementAt(this.a)).a == -1)
        this.a++; 
      if (this.a >= this.c.size())
        this.a = 1; 
      bool = true;
    } else if (el.H[2]) {
      el.H[2] = false;
      ds.ai = false;
      this.a--;
      if (this.a < 0)
        this.a = this.c.size() - 1; 
      if (((bw)this.c.elementAt(this.a)).a == -1)
        this.a--; 
      if (this.a < 0)
        this.a = this.c.size() - 1; 
      bool = true;
    } 
    if (bool) {
      this.d = this.a * ds.ab - (el.e - 35 - ds.ab) / 2;
      if (this.d > this.h)
        this.d = this.h; 
      if (this.d < 0)
        this.d = 0; 
      if (this.a == this.c.size() - 1 || this.a == 0)
        this.e = this.d; 
    } 
  }
  
  public final void c() {
    model.am.a();
    et et1;
    if ((et1 = this).e != et1.d) {
      et1.g = et1.d - et1.e << 2;
      et1.f += et1.g;
      et1.e += et1.f >> 4;
      et1.f &= 0xF;
    } 
    if (Math.abs(this.d - this.e) < 15 && this.e < 0)
      this.d = 0; 
    if (Math.abs(this.d - this.e) < 10 && this.e > this.h)
      this.d = this.h; 
  }
  
  public final void a(Vector paramVector) {
    this.c = new Vector();
    byte b = -1;
    Vector vector;
    a(vector = paramVector, 0, vector.size() - 1);
    for (byte b1 = 0; b1 < paramVector.size(); b1++) {
      bw bw1;
      byte b2 = (bw1 = (bw) paramVector.elementAt(b1)).c;
      if (b < b2) {
        this.c.addElement(new bw((byte)-1, (byte)0, (byte)0, (byte)b2));
        b = b2;
      } 
      int i;
      bw bw2;
      for (i = this.c.size() - 1; i > 0 && (bw2 = (bw) this.c.elementAt(i)).a != -1 && bw2.a > bw1.a; i--);
      this.c.insertElementAt(bw1, i + 1);
    }
    this.e = this.d = 0;
    this.a = 1;
    this.h = this.c.size() * ds.ab - el.e - 60;
    if (this.h < 0)
      this.h = 0;
  }
  
  private static void a(Vector paramVector, int paramInt1, int paramInt2) {
    if (paramInt2 - paramInt1 <= 0)
      return; 
    byte b = ((bw)paramVector.elementAt(paramInt2)).c;
    byte b1 = b;
    int j = paramInt2;
    int i = paramInt1;
    Vector vector = paramVector;
    i--;
    int k = j;
    while (true) {
      if (((bw)vector.elementAt(++i)).c >= b1) {
        do {
        
        } while (k > 0 && ((bw)vector.elementAt(--k)).c > b1);
        if (i < k) {
          b(vector, i, k);
          continue;
        } 
        b(vector, i, j);
        int m = i;
        a(paramVector, paramInt1, m - 1);
        a(paramVector, m + 1, paramInt2);
        return;
      } 
    } 
  }
  
  private static void b(Vector paramVector, int paramInt1, int paramInt2) {
    Object object = paramVector.elementAt(paramInt2);
    paramVector.setElementAt(paramVector.elementAt(paramInt1), paramInt2);
    paramVector.setElementAt(object, paramInt1);
  }
  
  static void a(et paramet) {
    model.ad.a().b();
    el.b(u.cs());
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\et.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */