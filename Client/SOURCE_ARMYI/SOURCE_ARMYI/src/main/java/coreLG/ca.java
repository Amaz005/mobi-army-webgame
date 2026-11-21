package coreLG;

import model.as;
import java.util.Vector;

import model.u;
import model.z;

final class ca implements as {
  final dl a;
  
  ca(dl paramdl) {
    this.a = paramdl;
  }
  
  public final void a() {
    Vector vector = new Vector();
    vector.addElement(new model.z(u.ck(), new cb(this)));
    vector.addElement(new z(model.u.i(), new cc(this)));
    vector.addElement(new z("Gọi Hotline", new bm(this)));
    vector.addElement(new z(model.u.cG(), new bk(this)));
    el.v.a(vector, 0);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ca.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */