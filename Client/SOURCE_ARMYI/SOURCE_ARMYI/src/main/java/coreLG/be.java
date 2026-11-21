package coreLG;

import model.as;
import model.bk;
import model.bu;
import model.u;

final class be implements as {
  private ab a;
  
  be(ab paramab) {
    this.a = paramab;
  }
  
  public final void a() {
    if (this.a.b < 0 || this.a.b >= this.a.a.size())
      return; 
    if (el.D > 0) {
      el.a(model.u.aU());
      return;
    } 
    bu bu = (model.bu) this.a.a.elementAt(this.a.b);
    el.u.a(String.valueOf(u.aS()) + bu.b + ":", new ff(this.a), new fb(this.a), 0);
    bk bk = el.u;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\be.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */