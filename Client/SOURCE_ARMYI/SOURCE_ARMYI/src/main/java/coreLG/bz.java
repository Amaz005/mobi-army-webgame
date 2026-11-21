package coreLG;

import model.as;
import model.u;

final class bz implements as {
  private final String[] a;
  
  private final String[] b;
  
  bz(bw parambw, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    this.a = paramArrayOfString1;
    this.b = paramArrayOfString2;
  }
  
  public final void a() {
    if (el.u.d.c().equals("")) {
      el.a(u.aB(), new fg(this));
      return;
    } 
    this.a[1] = el.u.d.c();
    el.u.a(this.b[2], new fe(this, this.a), new ez(this), 2);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\bz.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */