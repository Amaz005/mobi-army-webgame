package model;

import model.az;
import model.bi;

import java.io.IOException;
import java.util.Vector;

public class bv implements Runnable {
  private final Vector a;
  
  private bi b;
  
  public bv(bi parambi) {
    this.b = parambi;
    this.a = new Vector();
  }
  
  public final void a(az paramaz) {
    this.a.addElement(paramaz);
  }
  
  public void run() {
    try {
      while(this.b.c) {
        if (this.b.g) {
          while(this.a.size() > 0) {
            az var1 = (az)this.a.elementAt(0);
            this.a.removeElementAt(0);
            bi.a(this.b, var1);
          }
        }
        Thread.sleep(10L);
      }
    
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bv.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */