package coreLG;

import model.as;
import java.util.Vector;

import model.z;

final class dz implements as {
  final ek a;
  
  dz(ek paramek) {
    this.a = paramek;
  }
  
  public final void a() {
    Vector vector = new Vector();
    for (byte b = 0; b < 5; b++) {
      int i = this.a.c[b];
      model.z z = new z((new StringBuffer(String.valueOf(this.a.c[b]))).toString(), new dw(this, i));
      vector.addElement(z);
    } 
    el.v.a(vector, 0);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dz.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */