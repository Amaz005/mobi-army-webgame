package model;

public class ba implements Runnable {
  private bt a;
  
  public ba(bt parambt) {
    this.a = parambt;
  }
  
  public void run() {
    try {
      Thread.sleep(20000L);
    } catch (InterruptedException var3) {
    }
  
    if (this.a.a.d) {
      try {
        bi.a(this.a.a).close();
      } catch (Exception var2) {
      }
    
      System.out.println("TIMEOUT");
      bi.j = true;
      this.a.a.d = false;
      this.a.a.c = false;
      this.a.a.b.c();
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ba.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */