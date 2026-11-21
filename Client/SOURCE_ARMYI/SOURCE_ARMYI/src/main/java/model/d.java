package model;

import coreLG.ci;
import coreLG.cj;
import model.aa;

import java.util.Vector;

public final class d {
  public Vector a = new Vector();
  
  private boolean b = false;
  
  public final void a(int paramInt1, int paramInt2, byte paramByte) {
    if (this.a.size() < ((paramByte != 7 && paramByte != 13) ? 30 : 300))
      if (paramByte == 4) {
        if (cj.d(2) == 0) {
          this.a.addElement(new aa(paramInt1, paramInt2, paramByte));
          return;
        } 
      } else {
        this.a.addElement(new aa(paramInt1, paramInt2, paramByte));
      }  
  }
  
  public final void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    this.a.addElement(new aa(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5));
  }
  
  public final void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte paramByte) {
    byte b = 30;
    if (ci.a == 1) {
      b = 20;
    } else if (ci.a == 2) {
      b = 10;
    } 
    if (paramByte != 6 && paramByte != 8 && paramByte != 9 && paramByte != 12) {
      if (this.a.size() < (b << 1) / 3) {
        this.a.addElement(new aa(paramInt1, paramInt2, paramInt3, paramInt4, paramByte));
        return;
      } 
    } else if (this.a.size() < b) {
      this.a.addElement(new aa(paramInt1, paramInt2, paramInt3, paramInt4, paramByte));
    } 
  }
  
  public final void a(Object paramObject) {
    this.a.removeElement(paramObject);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.d.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */