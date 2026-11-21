package coreLG;

import model.as;
import model.bk;
import model.j;
import model.u;

public class bu implements as {
  private dd a;
  
  public bu(dd paramdd) {
    this.a = paramdd;
  }
  
  public void a() {
    if (this.a.b < 0 || this.a.b >= this.a.a.size())
      return; 
    if (el.D > 0) {
      el.a(u.aU());
      return;
    } 
    j j = (model.j) this.a.a.elementAt(this.a.b);
    el.u.a(u.aS() + j.a + ":", new bq(this.a), new br(this.a), 0);
    bk bk = el.u;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bu.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */