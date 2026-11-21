package coreLG;

import model.bn;
import javax.microedition.lcdui.Graphics;
import model.u;
import model.z;

public class dc extends ds {
  private int b;
  
  private int c;
  
  private int d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h;
  
  private int i;
  
  private int j;
  
  private static int k = cv.f;
  
  private static int l = 0;
  
  private static int[] m = new int[cv.f];
  
  private static int n;
  
  private static int o = 100;
  
  private static int p = 80;
  
  private static int q = 50;
  
  private static int[] r;
  
  private int s;
  
  public boolean a;
  
  public void b() {
    this.s = !this.a ? (cv.f - bx.j.length) : bx.j.length;
    k = this.s;
    this.g = el.d / 90;
    this.h = (el.d - this.g * 90 >> 1) + 40;
    ci.M = new m();
    int i;
    for (i = 0; i < k; i++)
      m[i] = i * o; 
    n = ds.Z >> 1;
    l = k / 2;
    this.j = (n - m[l]) / 2;
    if (r == null) {
      (new int[3])[1] = 1;
      (new int[3])[2] = 1;
      r = new int[] { -50, 100, 190 };
      (new int[3])[0] = -20;
      (new int[3])[2] = 130;
    } 
    this.ad = new z(model.u.cF(), new dt(this));
    this.ae = new z(u.cI(), new du(this));
    this.j = el.e - 5 + ds.af;
    this.i = this.s / this.g;
    if (this.s % this.g != 0)
      this.i++;
    i = this.i * 57 + 40;
    this.f = i - this.j;
    if (this.f < 0)
      this.f = 0;
    this.e();
    super.b();
  }
  
  protected void a() {
    coreLG.el.b(u.D());
    int b1 = this.a ? (cv.f - bx.j.length) : 0;
    if (l >= this.s)
      return; 
    byte b;
    if ((b = (byte)(l + b1)) == -1)
      return; 
    model.ad.a().e(b);
    coreLG.el.c(model.u.cs());
  }
  
  public void a(Graphics paramGraphics) {
    coreLG.ds.b(paramGraphics);
    Graphics graphics = paramGraphics;
    graphics.setClip(0, ds.af, el.d, el.e - ds.af);
    graphics.translate(0, -this.c);
    int b1 = this.a ? (cv.f - bx.j.length) : 0;
    for (byte b2 = 0; b2 < this.s; b2++) {
      int i = b2 % this.g;
      int j = b2 / this.g;
      i = i * 90 + this.h;
      j = j * 57 + 60;
      if (b2 == l) {
        graphics.setColor(3684546);
        graphics.fillRect(i - (p >> 1) - 5, j - (q >> 1) - 5, p + 10, q + 10);
      } 
      graphics.setColor(16777215);
      graphics.fillRect(i - (p >> 1) + 1, j - (q >> 1) + 1, p - 2, q - 2);
      try {
        if (coreLG.bx.f[b2 + b1] != null) {
          graphics.drawImage(bx.f[b2 + b1], i, j, 3);
          graphics.drawImage(bx.i, i, j, 3);
        } 
      } catch (Exception exception) {}
    } 
    graphics.setClip(0, 0, 1000, 1000);
    graphics.translate(-graphics.getTranslateX(), -graphics.getTranslateY());
    graphics.setColor(1133755);
    graphics.fillRect(0, 0, el.d, coreLG.ds.af + 1);
    model.bn.c.a(graphics, String.valueOf(l + 1) + ". " + cv.g[l + b1], coreLG.ds.Z >> 1, 2, 2);
    super.a(paramGraphics);
  }
  
  private void e() {
    int i = l / this.g * 57 + 60 - el.e / 2 - ds.af;
    this.b = i;
    if (this.b > this.f)
      this.b = this.f; 
    if (this.b < 0)
      this.b = 0; 
    if (l == this.s - 1 || l == 0)
      this.c = this.b; 
  }
  
  public void d() {
    super.d();
    if (el.H[2]) {
      if ((l -= this.g) < 0)
        l = this.s - 1; 
      this.e();
    } 
    if (el.H[8]) {
      if ((l += this.g) > this.s - 1)
        l = 0; 
      this.e();
    } 
    if (el.H[4]) {
      if (--l < 0)
        l = k - 1; 
      this.j = (n - m[l]) / 2;
      this.e();
    } 
    if (el.H[6]) {
      if (++l >= k)
        l = 0; 
      this.j = (n - m[l]) / 2;
      this.e();
    }
    coreLG.ds.t();
  }
  
  public void c() {
    if (this.c != this.b) {
      this.e = this.b - this.c << 2;
      this.d += this.e;
      this.c += this.d >> 4;
      this.d &= 0xF;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dc.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */