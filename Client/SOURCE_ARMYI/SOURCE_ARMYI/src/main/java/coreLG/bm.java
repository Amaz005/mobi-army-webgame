package coreLG;

import model.ad;
import model.as;
import model.bi;
import javax.microedition.io.ConnectionNotFoundException;

import model.u;

final class bm implements as {
  private ca a;
  
  bm(ca paramca) {
    this.a = paramca;
  }
  
  public final void a() {
    ca ca1;
    if (!(ca1 = this.a).a.p.equals(""))
      try {
        TerrainMidlet.b.platformRequest("tel:" + (ca1 = this.a).a.p);
        return;
      } catch (ConnectionNotFoundException connectionNotFoundException) {
        connectionNotFoundException.printStackTrace();
        return;
      }  
    if (!(bi.a()).c) {
      el.b(u.cr());
      dl.a();
    } else {
      el.b(u.cs());
    } 
    ad.a().a((byte)5, null);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bm.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */