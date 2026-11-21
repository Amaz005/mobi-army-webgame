package coreLG;

import model.ad;
import model.as;
import model.bu;
import model.u;

final class ff implements as {
  private ab a;
  
  ff(ab paramab) {
    this.a = paramab;
  }
  
  public void a() {
    bu bu = (model.bu) this.a.a.elementAt(this.a.b);
    String str;
    if ((str = el.u.d.c()).length() == 0)
      return; 
    model.ad.a().a(bu.a, str);
    bu.d = true;
    el.a(u.aR());
    el.D = 100;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ff.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */