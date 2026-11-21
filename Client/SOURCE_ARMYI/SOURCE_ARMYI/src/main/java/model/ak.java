package model;

import coreLG.el;
import model.ae;
import model.ag;
import model.as;

final class ak implements as {
  private final String[] a;
  
  private final String[] b;
  
  private final String c;
  
  ak(bp parambp, String[] paramArrayOfString1, String[] paramArrayOfString2, String paramString) {
    this.a = paramArrayOfString1;
    this.b = paramArrayOfString2;
    this.c = paramString;
  }
  
  public final void a() {
    if (el.u.d.c().equals("")) {
      el.a(u.S(), new al(this));
      return;
    } 
    this.a[0] = el.u.d.c();
    el.u.a(this.b[1], new ae(this, this.a, this.c), new ag(this), 1);
    bk bk = el.u;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ak.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */