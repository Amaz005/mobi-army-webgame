package model;

import coreLG.TerrainMidlet;
import model.as;

import javax.microedition.io.ConnectionNotFoundException;

final class m implements as {
  private final String a;
  
  m(x paramx, String paramString) {
    this.a = paramString;
  }
  
  public final void a() {
    try {
      TerrainMidlet.b.platformRequest(this.a);
      TerrainMidlet.b.notifyDestroyed();
      return;
    } catch (ConnectionNotFoundException connectionNotFoundException) {
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.m.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */