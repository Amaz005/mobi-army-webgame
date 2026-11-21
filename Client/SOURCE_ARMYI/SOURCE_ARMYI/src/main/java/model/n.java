package model;

import coreLG.TerrainMidlet;
import coreLG.el;
import model.as;

import javax.microedition.io.ConnectionNotFoundException;

final class n implements as {
  private final String a;
  
  n(o paramo, String paramString) {
    this.a = paramString;
  }
  
  public final void a() {
    try {
      el.b();
      TerrainMidlet.b.platformRequest(this.a);
      TerrainMidlet.b.notifyDestroyed();
      return;
    } catch (ConnectionNotFoundException connectionNotFoundException) {
      connectionNotFoundException.printStackTrace();
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.n.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */