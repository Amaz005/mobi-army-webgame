package coreLG;

import model.as;
import java.util.Vector;
import model.u;
import model.z;

public class an implements as {
  private ab a;
  
  public an(ab paramab) {
    this.a = paramab;
  }
  
  public void a() {
    Vector vector = new Vector();
    vector.addElement(new z(model.u.cm(), new av(this.a)));
    vector.addElement(new z(model.u.az(), new coreLG.as(this.a)));
    vector.addElement(new z(model.u.ay(), new ba(this.a)));
    vector.addElement(new model.z(u.ba(), new ay(this.a)));
    el.v.a(vector, 0);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\an.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */