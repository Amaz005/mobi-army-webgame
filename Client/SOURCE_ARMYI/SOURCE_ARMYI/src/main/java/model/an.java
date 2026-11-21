package model;

import coreLG.TerrainMidlet;
import model.as;

import javax.microedition.io.ConnectionNotFoundException;

final class an implements as {
  private final String a;
  
  an(bp parambp, String paramString) {
    this.a = paramString;
  }
  
  public final void a() {
    try {
      TerrainMidlet.b.platformRequest(this.a);
      TerrainMidlet.b.notifyDestroyed();
      return;
    } catch (ConnectionNotFoundException connectionNotFoundException) {
      connectionNotFoundException.printStackTrace();
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.an.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */