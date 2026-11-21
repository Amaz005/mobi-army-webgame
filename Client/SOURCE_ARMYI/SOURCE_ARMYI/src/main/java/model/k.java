package model;

import coreLG.TerrainMidlet;
import coreLG.el;
import model.as;

import javax.microedition.io.ConnectionNotFoundException;

final class k implements as {
  private final String a;
  
  k(o paramo, String paramString) {
    this.a = paramString;
  }
  
  public final void a() {
    el.b();
    try {
      TerrainMidlet.b.platformRequest(this.a);
      return;
    } catch (ConnectionNotFoundException connectionNotFoundException) {
      connectionNotFoundException.printStackTrace();
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.k.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */