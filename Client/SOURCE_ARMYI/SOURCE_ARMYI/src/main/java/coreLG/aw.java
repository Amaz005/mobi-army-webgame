package coreLG;

import model.ad;
import model.as;
import model.u;

final class aw implements as {
  private bx a;
  
  aw(bx parambx) {
    this.a = parambx;
  }
  
  public final void a() {
    if (bx.f(this.a) == 0) {
      if (this.a.p[bx.g(this.a)] == -1) {
        el.a(model.u.x());
        return;
      } 
      l.a(this.a.p);
      dj dj = l.a(bx.q.b);
      if (bx.h(this.a) && !dj.j) {
        if (dj.d > 0 || dj.k) {
          if (dj.f < dj.i) {
            this.a.p[bx.g(this.a)] = dj.b;
            bx.a(this.a, false);
            bx.i(this.a);
            return;
          } 
          el.a(String.valueOf(model.u.w()) + dj.i + u.v());
          return;
        } 
        el.a(model.u.u());
        return;
      } 
      bx.a(this.a, true);
      return;
    } 
    if (bx.f(this.a) == 1) {
      if (bx.j(this.a)) {
        el.b(model.u.cs());
        bx.b(this.a, false);
        if (bx.k(this.a) == 3 && (l.a(11)).d == 0)
          return; 
        ad.a().g((byte)bx.k(this.a));
        this.a.t.c();
        return;
      } 
      bx.b(this.a, true);
      bx.a(this.a, this.a.r - 45, this.a.s - 22);
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\aw.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */