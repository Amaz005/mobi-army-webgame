package coreLG;

import model.ad;
import model.as;
import model.bu;
import model.u;

final class ay implements as {
  private ab a;
  
  ay(ab paramab) {
    this.a = paramab;
  }
  
  public final void a() {
    bu bu;
    if ( this.a.b >= 0 && this.a.b < this.a.a.size() && (bu = (model.bu) this.a.a.elementAt(this.a.b)).a != TerrainMidlet.e.b) {
      ad.a().e(bu.a);
      el.b(String.valueOf(u.E()) + "...");
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.ay.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */