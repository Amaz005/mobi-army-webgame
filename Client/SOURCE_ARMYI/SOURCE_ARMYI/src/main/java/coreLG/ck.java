package coreLG;

public final class ck {
  byte a;
  
  byte b;
  
  int c;
  
  public String d;
  
  public ck(byte paramByte1, byte paramByte2, int paramInt) {
    this.a = paramByte1;
    this.b = paramByte2;
    this.c = paramInt;
    if (paramByte1 == 0)
      this.d = "Xin chúc mừng, phần thưởng của bạn là " + paramInt + " item " + dj.a[paramByte2] + "."; 
    if (paramByte1 == 1)
      this.d = "Xin chúc mừng, phần thưởng của bạn là " + paramInt + "~."; 
    if (paramByte1 == 2)
      this.d = "Xin chúc mừng, phần thưởng của bạn là " + paramInt + "XP."; 
    if (paramByte1 == 3)
      this.d = "Chúc bạn may mắn lần sau"; 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\ck.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */