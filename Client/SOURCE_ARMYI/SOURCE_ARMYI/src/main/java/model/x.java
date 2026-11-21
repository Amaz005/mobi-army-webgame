package model;

import coreLG.TerrainMidlet;
import coreLG.ab;
import coreLG.ds;
import coreLG.el;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

public final class x {
  private static x a = new x();
  
  private static boolean b;
  
  public static x a() {
    return a;
  }
  
  private static byte[] f(String var0) {
    try {
      RecordStore var3;
      byte[] var1 = (var3 = RecordStore.openRecordStore("vt" + var0, false)).getRecord(1);
      var3.closeRecordStore();
      return var1;
    } catch (Exception var2) {
      return null;
    }
  }
  
  
  public static String b() {
    byte[] arrayOfByte;
    return ((arrayOfByte = f("TLIP")) == null) ? null : new String(arrayOfByte);
  }
  
  public final void c() {
    if (!b) {
      try {
        String var5 = null;
        var5 = "http://teamobi.com/srvip/mobiarmy155.txt";
        HttpConnection var6;
        (var6 = (HttpConnection)Connector.open("http://teamobi.com/srvip/mobiarmy155.txt")).setRequestMethod("GET");
        var6.setRequestProperty("Content-Type", "//text plain");
        var6.setRequestProperty("Connection", "close");
        if (var6.getResponseCode() == 200) {
          String var1 = "";
          InputStream var2 = var6.openInputStream();
          int var7;
          if ((var7 = (int)var6.getLength()) != -1) {
            byte[] var8 = new byte[var7];
            var2.read(var8);
            var1 = new String(var8);
          }
          
          byte[] var9 = var1.getBytes();
          var5 = "TLIP";
          
          try {
            RecordStore var10;
            if ((var10 = RecordStore.openRecordStore("vt" + var5, true)).getNumRecords() > 0) {
              var10.setRecord(1, var9, 0, var9.length);
            } else {
              var10.addRecord(var9, 0, var9.length);
            }
            
            var10.closeRecordStore();
          } catch (Exception var3) {
          }
          
          var7 = var1.indexOf(":");
          String var11 = var1.substring(0, var7);
          var5 = var1.substring(var7 + 1);
          bi.a().a("socket://" + var11 + ":" + Integer.parseInt(var5));
          b = true;
          return;
        }
      } catch (IOException var4) {
        var4.printStackTrace();
      }
    }
    
    el.a(u.ag());
  }
  
  
  public final void d() {
    el.b(u.cs());
  }
  
  public final void e() {
    if (el.h != el.k) {
      el.a(u.ad());
      el.h = (ds)el.k;
    } 
  }
  
  public final void a(String paramString) {
    el.a(paramString);
  }
  
  public final void f() {
    TerrainMidlet.e.a = el.k.a.c();
    bx.a("caroun", el.k.a.c());
    bx.a("caropass", el.k.b.c());
    el.b();
    el.i.b();
  }
  
  public final void b(String paramString) {
    el.y.a(paramString, null, new z("OK", new a(this)), null);
    i i = el.y;
  }
  
  public final void a(int paramInt, boolean paramBoolean) {
    if (paramInt == TerrainMidlet.e.b)
      el.b(); 
    el.p.a(paramInt, paramBoolean);
  }
  
  public final void a(String paramString, int paramInt) {
    if (el.p.s()) {
      el.p.a(paramInt, paramString, 90);
      return;
    } 
    if (el.j.s())
      el.j.a(paramInt, paramString); 
  }
  
  public final void a(int paramInt) {
    el.p.c(paramInt);
    if (paramInt == TerrainMidlet.e.b) {
      el.y.a(u.t(), null, new z("OK", new b(this)), null);
      i i = el.y;
    } 
  }
  
  public final void a(int paramInt, Vector paramVector) {
    el.b();
    el.o.c = paramInt;
    el.o.a(1, paramVector);
    el.o.b();
  }
  
  public final void a(String paramString1, boolean paramBoolean, String paramString2, String paramString3) {
    if (!paramBoolean) {
      el.a(u.ac());
      return;
    } 
    TerrainMidlet.a(String.valueOf(paramString2) + paramString1 + " " + el.k.b.c(), "sms://" + paramString3, new e(this), new g(this));
  }
  
  public final void a(Vector paramVector) {
    el.b();
    el.o.a(2, paramVector);
    if (el.h == el.p) {
      el.o.a((ds)el.p);
      return;
    } 
    if (el.h == el.i)
      el.o.a((ds)el.i); 
  }
  
  public static void b(Vector paramVector) {
    el.b();
    el.o.b(3, paramVector);
    if (el.h == el.p) {
      el.o.a((ds)el.p);
      return;
    } 
    if (el.h == el.i)
      el.o.a((ds)el.i); 
  }
  
  public final void c(Vector paramVector) {
    if (paramVector.size() > 0)
      for (byte b = 0; b < paramVector.size(); b++) {
        j j;
        if ((j = (model.j) paramVector.elementAt(b)).a.equals(el.u.d.c())) {
          el.b(String.valueOf(u.X()) + " " + j.a + " " + u.Y());
          ad.a().e(j.b);
        } 
      }  
    el.a(u.Z());
  }
  
  public final void a(byte paramByte) {
    if (paramByte == 0) {
      el.a(u.aj());
      if (el.h == el.o && el.o.d == 2) {
        ad.a().d();
        return;
      } 
    } else {
      if (paramByte == 2) {
        el.a(u.ai());
        return;
      } 
      el.a(u.ah());
    } 
  }
  
  public final void b(byte paramByte) {
    if (paramByte == 0) {
      el.a(u.af());
      ad.a().d();
      return;
    } 
    el.a(u.ae());
  }
  
  public void a(bu var1) {
    if (el.h != el.q) {
      ++el.B.b;
      el.B.b();
    }
    
    coreLG.ab var2;
    (var2 = el.q).a.insertElementAt(var1, 0);
    var2.c = var2.a.size() * 40 - (el.e - 100);
  }
  
  public final void a(bo parambo) {
    bx.a(parambo);
    bx.a(parambo.a);
    Image.createImage(parambo.b, 0, parambo.b.length);
  }
  
  public final void d(Vector paramVector) {
    el.b();
    el.E.a(paramVector);
    el.E.b();
  }
  
  public final void c(String paramString) {
    el.C.a(paramString);
    el.C.b();
  }
  
  public final void d(String paramString) {
    el.a(paramString);
  }
  
  public final void a(String paramString1, String paramString2) {
    m m = new m(this, paramString2);
    el.y.a(paramString1, new z("Download", m), new z("", m), new z(u.cI(), new l(this)));
    i i = el.y;
  }
  
  public final void a(String paramString1, String paramString2, byte paramByte) {
    if (TerrainMidlet.e != null && TerrainMidlet.e.a != null)
      bn.a(paramString2, "@username", TerrainMidlet.e.a); 
    paramString2 = paramString2;
    o o = new o(this, paramByte, paramString2);
    el.y.a(paramString1, new z("OK", o), new z("", o), new z(u.bh(), new be(this)));
    i i = el.y;
  }
  
  public final void e(String paramString) {
    el.a(paramString);
  }
  
  public final void a(int paramInt, byte paramByte) {
    el.b();
    el.p.a(paramInt, paramByte);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.x.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */