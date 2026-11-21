package model;

import coreLG.TerrainMidlet;
import coreLG.el;
import model.as;
import model.bd;
import model.k;
import model.n;

import javax.microedition.io.ConnectionNotFoundException;

final class o implements as {
  private final byte a;
  
  private final String b;
  
  o(x paramx, byte paramByte, String paramString) {
    this.a = paramByte;
    this.b = paramString;
  }
  
  public final void a() {
    n n = new n(this, this.b);
    if (this.a == 0) {
      if (el.x) {
        el.y.a(u.f(), null, new z(u.cG(), n), new z(u.bh(), new k(this, this.b)));
        return;
      } 
      el.y.a(u.f(), new z(u.cG(), n), new z("", n), new z(u.bh(), new bd(this, this.b)));
      return;
    } 
    if (this.a == 1)
      try {
        TerrainMidlet.b.platformRequest(this.b);
        TerrainMidlet.b.notifyDestroyed();
        return;
      } catch (ConnectionNotFoundException connectionNotFoundException) {
        connectionNotFoundException.printStackTrace();
        return;
      }  
    if (this.a == 2) {
      try {
        TerrainMidlet.b.platformRequest(this.b);
      } catch (ConnectionNotFoundException connectionNotFoundException) {
        connectionNotFoundException.printStackTrace();
      } 
      el.b();
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.o.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */