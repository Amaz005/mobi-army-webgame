package coreLG;

public class cn {
  int a;
  
  int b;
  
  int c;
  
  int d;
  
  int e;
  
  final dl f;
  
  public cn(dl paramdl, int paramInt1, int paramInt2, int paramInt3) {
    this.f = paramdl;
    this.d = paramInt3;
    this.a = paramInt1;
    this.b = paramInt2;
    this.e = (int)(System.currentTimeMillis() % (paramInt3 + 1) % 2L) + 1;
    this.c = paramInt3 % 2;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\cn.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */