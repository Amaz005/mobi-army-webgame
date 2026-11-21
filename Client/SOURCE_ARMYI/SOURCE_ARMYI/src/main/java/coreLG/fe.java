package coreLG;

import model.ad;
import model.as;
import model.u;

final class fe implements as {
  private final String[] a;
  
  fe(bz parambz, String[] paramArrayOfString) {
    this.a = paramArrayOfString;
  }
  
  public final void a() {
    if (el.u.d.c().equals("")) {
      el.a(u.aC(), new fd(this));
      return;
    } 
    this.a[2] = el.u.d.c();
    if (this.a[2].equals(this.a[1])) {
      ad.a().a(this.a[0], this.a[1]);
      el.b();
      el.a(model.u.cs());
      return;
    } 
    el.a(model.u.aD());
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\fe.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */