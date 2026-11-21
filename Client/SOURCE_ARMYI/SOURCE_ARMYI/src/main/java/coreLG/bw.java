package coreLG;

import model.as;
import model.bk;
import model.u;

public class bw implements as {
  private final String[] a;
  
  private final String[] b;
  
  public bw(ej paramej, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    this.a = paramArrayOfString1;
    this.b = paramArrayOfString2;
  }
  
  public final void a() {
    if (el.u.d.c().equals("")) {
      el.a(u.aA(), new by(this));
      return;
    } 
    this.a[0] = el.u.d.c();
    el.u.a(this.b[1], new bz(this, this.a, this.b), new ey(this), 2);
    bk bk = el.u;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bw.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */