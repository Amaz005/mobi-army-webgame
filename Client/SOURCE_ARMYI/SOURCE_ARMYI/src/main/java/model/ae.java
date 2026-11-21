package model;

import coreLG.cj;
import coreLG.el;
import model.ad;
import model.as;

import java.io.IOException;

final class ae implements as {
  private final String[] a;
  
  private final String b;
  
  ae(ak paramak, String[] paramArrayOfString, String paramString) {
    this.a = paramArrayOfString;
    this.b = paramString;
  }
  
  public final void a() {
    if (el.u.d.c().equals("")) {
      el.a(u.R(), (as)(new af(this)));
    } else {
      this.a[1] = el.u.d.c();
      cj.d("String card= " + this.b);
      ad var10000 = ad.a();
      String var10002 = this.a[0];
      String var3 = this.a[1];
      String var2 = var10002;
      String var1 = this.b;
      ad var6 = var10000;
      az var4 = new az(77);
    
      try {
        var4.c().writeUTF(var1);
        var4.c().writeUTF(var2);
        var4.c().writeUTF(var3);
      } catch (IOException var5) {
        var5.printStackTrace();
      }
    
      var6.a.a(var4);
      var4.d();
      el.a(u.cs());
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ae.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */