package coreLG;

import model.ad;
import model.as;

final class cf implements as {
  private l a;
  
  cf(l paraml) {
    this.a = paraml;
  }
  
  public final void a() {
    if (this.a.b) {
      this.a.b = false;
      return;
    } 
    if (l.d > 0) {
      byte b1 = 0;
      byte[] arrayOfByte1 = new byte[l.a.size()];
      byte[] arrayOfByte2 = new byte[l.a.size()];
      for (byte b2 = 0; b2 < l.a.size(); b2++) {
        if ((l.a(b2)).g > 0) {
          arrayOfByte1[b1] = (l.a(b2)).b;
          arrayOfByte2[b1] = (l.a(b2)).g;
          b1++;
        } 
      } 
      if (b1 > 0) {
        ad.a().a((byte)b1, arrayOfByte1, arrayOfByte2);
        el.b();
      } 
    } 
    el.i.b();
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\cf.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */