package model;

import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

public final class v implements Runnable {
  private static Thread a;
  
  private static boolean b;
  
  private static int c;
  
  private static Player[] d;
  
  private static int e = -1;
  
  private static v f;
  
  public static void a() {
    if (f == null) {
      d = new Player[3];
      try {
        d[0] = Manager.createPlayer("".getClass().getResourceAsStream("/fire.wav"), "audio/model.x-wav");
        d[0].prefetch();
        d[1] = Manager.createPlayer("".getClass().getResourceAsStream("/shoot.wav"), "audio/model.x-wav");
        d[1].prefetch();
        d[2] = Manager.createPlayer("".getClass().getResourceAsStream("/item.wav"), "audio/model.x-wav");
        d[2].prefetch();
      } catch (IOException iOException2) {
        IOException iOException1;
        (iOException1 = null).printStackTrace();
      } catch (MediaException mediaException2) {
        MediaException mediaException1;
        (mediaException1 = null).printStackTrace();
      } 
      f = new v();
      b = true;
      (a = new Thread(f)).start();
    } 
  }
  
  public static void a(int paramInt) {
    e = paramInt;
  }
  
  public static void b(int paramInt) {
    c = paramInt;
  }
  
  public final void run() {
    while (b) {
      if (e > -1 && e < d.length)
        try {
          if (c > 0) {
            ((VolumeControl)d[e].getControl("VolumeControl")).setLevel(c * 20);
            d[e].setMediaTime(0L);
            d[e].start();
          } 
          e = -1;
        } catch (MediaException mediaException) {} 
      try {
        Thread.sleep(50L);
      } catch (InterruptedException interruptedException) {}
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.v.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */