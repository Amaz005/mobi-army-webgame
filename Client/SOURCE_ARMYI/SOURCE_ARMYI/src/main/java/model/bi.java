package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.io.SocketConnection;

public final class bi {
  private static bi k = new bi();
  
  private DataOutputStream l;
  
  public DataInputStream a;
  
  public bb b;
  
  private SocketConnection m;
  
  public boolean c;
  
  public boolean d;
  
  private final bv n = new bv(this);
  
  private Thread o;
  
  public Thread e;
  
  private int p;
  
  public int f;
  
  boolean g;
  
  public byte[] h = null;
  
  private byte q;
  
  private byte r;
  
  long i;
  
  public static boolean j;
  
  public static bi a() {
    return k;
  }
  
  public final boolean b() {
    return this.c;
  }
  
  public final void a(bb parambb) {
    this.b = parambb;
  }
  
  public final void a(String paramString) {
    if (this.c || this.d)
      return; 
    this.g = false;
    this.m = null;
    this.o = new Thread(new bt(this, paramString));
    this.o.start();
  }
  
  public final void a(az paramaz) {
    this.n.a(paramaz);
  }
  
  private synchronized void b(az paramaz) {
    byte[] arrayOfByte = paramaz.a();
    try {
      byte b;
      if (this.g) {
        b = a(paramaz.a);
        this.l.writeByte(b);
      } else {
        this.l.writeByte(paramaz.a);
      } 
      if (arrayOfByte != null) {
        int i = arrayOfByte.length;
        if (this.g) {
          byte b1 = a((byte)(i >> 8));
          this.l.writeByte(b1);
          i = a((byte)i);
          this.l.writeByte(i);
        } else {
          this.l.writeShort(i);
        } 
        if (this.g)
          for (byte b1 = 0; b1 < arrayOfByte.length; b1++)
            arrayOfByte[b1] = a(arrayOfByte[b1]);  
        this.l.write(arrayOfByte);
        this.p += 5 + arrayOfByte.length;
      } else {
        this.l.writeShort(0);
        this.p += 5;
      } 
      this.l.flush();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private byte a(byte paramByte) {
    this.r = (byte)(this.r + 1);
    paramByte = (byte)(this.h[this.r] & 0xFF ^ paramByte & 0xFF);
    if (this.r >= this.h.length)
      this.r = (byte)(this.r % this.h.length); 
    return paramByte;
  }
  
  public final void c() {
    d();
  }
  
  private void d() {
    this.h = null;
    this.q = 0;
    this.r = 0;
    try {
      this.c = false;
      this.d = false;
      if (this.m != null) {
        this.m.close();
        this.m = null;
      } 
      if (this.l != null) {
        this.l.close();
        this.l = null;
      } 
      if (this.a != null) {
        this.a.close();
        this.a = null;
      } 
      this.e = null;
      System.gc();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  static SocketConnection a(bi parambi) {
    return parambi.m;
  }
  
  static void a(bi parambi, SocketConnection paramSocketConnection) {
    parambi.m = paramSocketConnection;
  }
  
  static void a(bi parambi, DataOutputStream paramDataOutputStream) {
    parambi.l = paramDataOutputStream;
  }
  
  static bv b(bi parambi) {
    return parambi.n;
  }
  
  static void a(bi parambi, az paramaz) {
    parambi.b(paramaz);
  }
  
  static void c(bi parambi) {
    parambi.d();
  }
  
  static byte a(bi parambi, byte paramByte) {
    paramByte = paramByte;
    parambi.q = (byte)(parambi.q + 1);
    paramByte = (byte)((parambi = parambi).h[parambi.q] & 0xFF ^ paramByte & 0xFF);
    if (parambi.q >= parambi.h.length)
      parambi.q = (byte)(parambi.q % parambi.h.length); 
    return paramByte;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bi.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */