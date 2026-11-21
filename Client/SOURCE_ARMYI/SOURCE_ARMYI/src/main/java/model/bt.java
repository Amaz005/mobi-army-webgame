package model;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

final class bt implements Runnable {
  private final String b;
  
  final bi a;
  
  bt(bi parambi, String paramString) {
    this.a = parambi;
    this.b = paramString;
  }
  
  public final void run() {
    bi.j = false;
    (new Thread(new ba(this))).start();
    this.a.d = true;
    Thread.currentThread().setPriority(1);
    this.a.c = true;
    try {
      String str = this.b;
      bt bt1 = this;
      System.out.println(">" + str + "<");
      bi.a(bt1.a, (SocketConnection)Connector.open(str));
      bi.a(bt1.a, bi.a(bt1.a).openDataOutputStream());
      bt1.a.a = bi.a(bt1.a).openDataInputStream();
      (new Thread(bi.b(bt1.a))).start();
      bt1.a.e = new Thread(new bf(bt1.a));
      bt1.a.e.start();
      bt1.a.i = System.currentTimeMillis();
      bi.a(bt1.a, new az((byte)-27));
      bt1.a.d = false;
      this.a.b.b();
      return;
    } catch (Exception exception) {
      try {
        Thread.sleep(500L);
      } catch (InterruptedException interruptedException) {}
      if (bi.j)
        return; 
      System.out.println("cannot connect to " + this.b);
      if (this.a.b != null) {
        this.a.c();
        this.a.b.c();
      } 
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bt.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */