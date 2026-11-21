package coreLG;

import model.am;
import model.bn;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import model.z;

public final class ek extends ds {
  private z d = new z("Quay", new ea(this));
  
  private z e = new z(model.u.cH(), new dv(this));
  
  private z f = new model.z("Mức tiền", new dz(this));
  
  private int g = el.d / 2 - 85;
  
  private int h = el.e / 2 - 85;
  
  private int i = 170;
  
  private int j = 170;
  
  private int k;
  
  private int l = this.i / 2;
  
  private int m;
  
  private int n = 0;
  
  private int o = 0;
  
  private int p = 0;
  
  private int q;
  
  public int a;
  
  public int b;
  
  private Vector r = new Vector();
  
  private int[] s = new int[] { 239, 203, 167, 131, 95, 59, 23, 311, 275 };
  
  private int[] t = new int[] { 16315529, 9621318, 504643, 701933, 27571, 16315529, 9621318, 504643, 701933, 27571 };
  
  private boolean u;
  
  private boolean v;
  
  private boolean w;
  
  private boolean x;
  
  private static Image y;
  
  final int[] c = new int[] { 200, 500, 1000, 1500, 2000 };
  
  private int z = 250;
  
  private int A = 290;
  
  private int B = 270;
  
  private int C;
  
  private int D;
  
  private int E;
  
  static {
    try {
      y = Image.createImage("/gui/xoay.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    }
  }
  
  public ek() {
    this.ad = this.d;
    this.ae = this.e;
    this.ac = this.f;
    if (this.h + this.i > el.e - ds.af)
      this.h = 5; 
  }
  
  public final void a(int paramInt) {
    el.b("Bạn có muốn chơi quay số với giá " + paramInt + "~?", new dx(this, paramInt));
  }
  
  public final void a(ds paramds) {
    ds.ah = paramds;
    this.a = 200;
    this.b = TerrainMidlet.e.e;
    b();
  }
  
  public final void a(Vector paramVector, int paramInt) {
    this.r = paramVector;
    this.q = paramInt;
    this.ad = null;
    this.ae = null;
    this.ac = null;
    this.u = true;
    this.k = 0;
  }
  
  public final void a(Graphics paramGraphics) {
    ds.b(paramGraphics);
    model.bn.g.a(paramGraphics, "1.6.5", el.d - 2, 2, 1);
    am.a(paramGraphics);
    int i;
    for (i = 0; i <= el.d; i += 32)
      paramGraphics.drawImage(bx.h, i, el.e - 62, 0); 
    paramGraphics.setColor(5526612);
    paramGraphics.fillArc(this.g - 4, this.h - 4, this.i + 8, this.j + 8, 0, 360);
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    i = 0;
    j = 0;
    k = 0;
    m = 0;
    n = 0;
    i1 = 0;
    i = (2 * this.l / 8 - 5) * cj.b(cj.c(this.z)) >> 10;
    j = -((2 * this.l / 8 - 5) * cj.a(cj.c(this.z))) >> 10;
    k = (2 * this.l / 8 - 5) * cj.b(cj.c(this.A)) >> 10;
    m = -((2 * this.l / 8 - 5) * cj.a(cj.c(this.A))) >> 10;
    n = 3 * this.l / 5 * cj.b(cj.c(this.B)) >> 10;
    i1 = -(3 * this.l / 5 * cj.a(cj.c(this.B))) >> 10;
    i = this.g + this.i / 2 + i;
    j = this.h + this.i / 2 + j;
    k = this.g + this.i / 2 + k;
    m = this.h + this.i / 2 + m;
    n = this.g + this.i / 2 + n;
    i1 = this.h + this.i / 2 + i1;
    for (byte b = 0; b < 10; b++) {
      if (this.k > 360)
        this.k -= 360; 
      int i2 = this.k + b * 36 + 18;
      int i3 = this.k + b * 36;
      int i4 = 4 * this.l / 5 * cj.b(cj.c(i2)) >> 10;
      i2 = -(4 * this.l / 5 * cj.a(cj.c(i2))) >> 10;
      i4 = this.g + this.i / 2 + i4;
      i2 = this.h + this.i / 2 + i2 - model.bn.c.a() / 2;
      i3 = 4 * this.l / 5 * cj.b(cj.c(i3)) >> 10;
      paramGraphics.setColor(this.t[b]);
      paramGraphics.fillArc(this.g, this.h, this.i, this.j, this.k + b * 36, 36);
      if (this.v || this.w) {
        int b1 = (this.o >= 10) ? 15 : (this.o + 1);
        if (i3 <= 0 && i3 + b1 >= 0)
          this.x = true; 
      } 
      if (this.r.size() != 0) {
        int i5 = i2;
        i4 = i4;
        Graphics graphics = paramGraphics;
        ck ck1;
        ck ck2;
        if ((ck1 = ck2 = (ck) this.r.elementAt(b)).a == 0) {
          graphics.drawRegion(ci.n, 0, ck1.b + 2 << 4, 16, 16, 0, i4, i5, 3);
          model.bn.c.a(graphics, (new StringBuffer(String.valueOf(ck1.c))).toString(), i4, i5 + 10, 3);
        } 
        if (ck1.a == 1)
          bn.c.a(graphics, String.valueOf(ck1.c) + "~", i4, i5, 3); 
        if (ck1.a == 2)
          bn.c.a(graphics, String.valueOf(ck1.c) + "XP", i4, i5, 3); 
        if (ck1.a == 3);
      } 
    } 
    paramGraphics.setColor(16610328);
    paramGraphics.fillArc(this.g + this.l - this.l / 4, this.h + this.l - this.l / 4, 2 * this.l / 4, 2 * this.l / 4, 0, 360);
    paramGraphics.fillTriangle(i, j, k, m, n, i1);
    paramGraphics.drawImage(y, this.g + this.l, this.h + this.l, 3);
    if (el.e >= 200)
      model.bn.c.a(paramGraphics, String.valueOf(this.b) + "~", 5, el.e - ds.af - 20, 0);
    super.a(paramGraphics);
  }
  
  public void d() {
    super.d();
  }
  
  public void c() {
    if (this.u) {
      this.m++;
      if (this.m == 20) {
        this.v = true;
        this.u = false;
        this.m = 0;
        int i = this.q;
        this.C = Math.abs(this.s[i]);
        this.D = this.C + ((i != 7) ? 26 : 13);
        this.E = cj.b(15, 18);
      } 
    } 
    if (this.v) {
      if (this.o == 19) {
        if ((this.k >= this.C && this.k <= this.D) || ((this.q == 7 && ((this.k >= this.C && this.k <= this.D) || (this.k >= 347 && this.k <= 360))))) {
          this.o = 19;
          this.p = 0;
          this.v = false;
          this.w = true;
        } else {
          this.k += 19;
        } 
      } else {
        this.k += this.o;
        if (this.o != this.E || el.c % 3 == 0)
          this.o++;
      } 
    } 
    if (this.w) {
      if (this.p < 2850) {
        this.p += this.o;
        this.k += this.o;
        this.n++;
        if (this.n == 15) {
          this.n = 0;
          this.o--;
        } 
      } else {
        int b = (this.m < 15) ? 2 : 4;
        if (el.c % b == 0) {
          this.m++;
          this.k++;
        } 
        if (this.m == 30) {
          this.m = 0;
          this.p = 0;
          this.o = 0;
          this.w = false;
          this.ad = this.d;
          this.ae = this.e;
          this.ac = this.f;
          ck ck = (coreLG.ck) this.r.elementAt(this.q);
          this.b = TerrainMidlet.e.e;
          el.a(ck.d);
        } 
      } 
    } 
    if (this.x) {
      this.x = false;
      if (this.p < 2850) {
        int i;
        if ((i = this.o + 1) < 3)
          i = 3; 
        this.z += i;
        if (this.z > 260)
          this.z = 260;
        this.A += i;
        if (this.A >= 300)
          this.A = 300;
        this.B += i;
        if (this.B >= 280)
          this.B = 280;
      } 
    } 
    if (this.z != 250)
      this.z -= 2; 
    if (this.z < 250)
      this.z = 250; 
    if (this.A != 290)
      this.A -= 2; 
    if (this.A < 290)
      this.A = 290; 
    if (this.B != 270)
      this.B -= 2; 
    if (this.B < 270)
      this.B = 270; 
    am.a();
    super.c();
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ek.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */