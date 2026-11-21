package coreLG;

import model.ad;
import model.as;
import model.j;
import model.u;

final class al implements as {
  private bx a;
  
  al(bx parambx) {
    this.a = parambx;
  }
  
  public final void a() {
    try {
      int i;
      if ((i = Integer.parseInt(el.u.d.c())) < 0)
        return; 
      el.b();
      if (i > TerrainMidlet.e.e) {
        el.a(String.valueOf(u.ar()) + TerrainMidlet.e.e);
        return;
      } 
      ad.a().a(i);
      for (i = 0; i < this.a.d.size(); i++)
        ((j)this.a.d.elementAt(i)).d = false;
      return;
    } catch (Exception exception) {
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\al.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */