package coreLG;

public final class bi {
  int[] a;
  
  int b;
  
  private int l;
  
  int c;
  
  int d;
  
  private int m;
  
  private int n;
  
  int e;
  
  int f;
  
  int g;
  
  int h;
  
  int i;
  
  public boolean j;
  
  private static int o;
  
  static int k;
  
  private static int p;
  
  private static int q;
  
  public bi(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
    this.a = paramArrayOfint;
    this.l = 4;
    this.i = this.a.length;
    this.c = this.l / 2;
    this.g = 16;
    this.h = this.l + this.g;
    this.d = 5;
    this.m = this.a.length / this.d;
    this.e = this.d * (this.l + this.g);
    this.f = 3 * (this.l + this.g);
    this.n = this.l + this.g << 1;
    this.j = true;
  }
  
  public final void a() {
    if (el.H[2]) {
      ds.ai = false;
      this.b = a(this.i, this.b, this.d, this.m, (byte)0);
    } 
    if (el.H[8]) {
      ds.aj = false;
      this.b = a(this.i, this.b, this.d, this.m, (byte)1);
    } 
    if (el.H[4]) {
      ds.ak = false;
      this.b = a(this.i, this.b, this.d, this.m, (byte)2);
    } 
    if (el.H[6]) {
      ds.al = false;
      this.b = a(this.i, this.b, this.d, this.m, (byte)3);
    } 
    if (this.j) {
      int i = this.b;
      if ((i = i / this.d * (this.l + this.g)) > k + this.n) {
        o = i - this.n;
        return;
      } 
      if (i < k)
        o = i; 
    } 
  }
  
  public static int a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte paramByte) {
    switch (paramByte) {
      case 0:
        if (paramInt2 - paramInt3 >= 0) {
          paramInt2 -= paramInt3;
          break;
        } 
        paramInt3 = (paramInt1 % paramInt3 != 0) ? (paramInt3 * paramInt4) : (paramInt3 * (paramInt4 - 1));
        if (paramInt2 + paramInt3 < paramInt1)
          paramInt2 += paramInt3; 
        break;
      case 1:
        if (paramInt2 + paramInt3 < paramInt1) {
          paramInt2 += paramInt3;
          break;
        } 
        paramInt3 = (paramInt1 % paramInt3 != 0) ? (paramInt3 * paramInt4) : (paramInt3 * (paramInt4 - 1));
        if (paramInt2 - paramInt3 >= 0)
          paramInt2 -= paramInt3; 
        break;
      case 2:
        if ((paramInt2 - 1) / paramInt3 == paramInt2 / paramInt3 && paramInt2 - 1 >= 0) {
          paramInt2--;
          break;
        } 
        if (paramInt2 + paramInt3 - 1 < paramInt1) {
          paramInt2 += paramInt3 - 1;
          break;
        } 
        paramInt2 += paramInt1 % paramInt3 - 1;
        break;
      case 3:
        if ((paramInt2 + 1) / paramInt3 == paramInt2 / paramInt3 && paramInt2 + 1 < paramInt1) {
          paramInt2++;
          break;
        } 
        if ((paramInt2 - paramInt3 - 1) / paramInt3 == paramInt2 / paramInt3) {
          paramInt2 -= paramInt3 - 1;
          break;
        } 
        paramInt2 -= paramInt1 % paramInt3 - 1;
        break;
    } 
    return paramInt2;
  }
  
  public final void b() {
    if (this.j && k != o) {
      q = o - k << 2;
      p += q;
      k += p >> 4;
      p &= 0xF;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bi.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */