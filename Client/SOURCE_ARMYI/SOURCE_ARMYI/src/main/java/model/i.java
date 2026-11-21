package model;

import coreLG.ds;
import coreLG.el;
import model.bn;

import javax.microedition.lcdui.Graphics;

public final class i extends q {
  private String[] d;
  
  public final void a(String paramString, z paramz1, z paramz2, z paramz3) {
    this.d = bn.b.a(paramString, el.d - 40);
    this.a = paramz1;
    this.b = paramz2;
    this.c = paramz3;
  }
  
  public final void a(Graphics var1) {
    int var2 = 0;
    if (el.e < 200) {
      var2 += 10;
    }
  
    ds.a(8, el.e - 102, el.d - 16, 69, var1);
    var2 += el.e - 20 - 50 - (this.d.length * bn.b.a() >> 1);
    int var3 = 0;
  
    for(var2 = var2; var3 < this.d.length; var2 += bn.b.a()) {
      bn.b.a(var1, this.d[var3], el.g, var2, 2);
      ++var3;
    }
  
    super.a(var1);
  }
  
  public final void a() {
    super.a();
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.i.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */