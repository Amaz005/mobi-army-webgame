package model;

import coreLG.el;
import java.util.Vector;

public final class aq {
  public boolean a;
  
  public Vector b;
  
  public int c;
  
  public int d;
  
  private int f;
  
  private int g;
  
  public int e;
  
  public final void a(Vector paramVector) {
    this.b = paramVector;
    this.f = 0;
    this.g = 0;
    for (byte b = 0; b < paramVector.size(); b++) {
      z z = (model.z) paramVector.elementAt(b);
      int i;
      if ((i = bn.d.a(z.a)) > this.f)
        this.f = i; 
      this.g += 24;
    } 
    this.f += 10;
    if (this.f < 100)
      this.f = 100; 
    this.g += 4;
    this.d = el.e - this.g >> 1;
    this.e = el.e - 24;
    this.a = true;
    this.c = 0;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.aq.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */