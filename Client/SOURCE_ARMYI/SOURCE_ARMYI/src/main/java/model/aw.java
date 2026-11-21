package model;

import coreLG.bx;
import coreLG.f;
import model.ad;
import model.as;

final class aw implements as {
  private final byte a;
  
  private final byte b;
  
  private final String c;
  
  aw(bb parambb, byte paramByte1, byte paramByte2, String paramString) {
    this.a = paramByte1;
    this.b = paramByte2;
    this.c = paramString;
  }
  
  public final void a() {
    bx.b = this.a;
    f.a(this.b, this.c);
    ad.a().a(this.a, this.b, "");
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.aw.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */