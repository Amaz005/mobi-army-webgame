package coreLG;

import model.ad;
import model.as;
import model.bb;
import model.bi;
import model.j;
import java.io.InputStream;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.midlet.MIDlet;

import model.x;

public class TerrainMidlet extends MIDlet {
  public static byte a;
  
  private static el f;
  
  public static TerrainMidlet b;
  
  public static int c = 19149;
  
  public static String d = "27.0.14.69";
  
  public static j e;
  
  private static Player g;
  
  private static int h = -1;
  
  private static int i = 100;
  
  protected void destroyApp(boolean paramBoolean) {
    if (f != null) {
      f.c();
      f = null;
    } 
    notifyDestroyed();
  }
  
  protected void pauseApp() {
    notifyPaused();
  }
  
  protected void startApp() {
    if (f == null) {
      cj.a();
      f = new el();
    } 
    f.b = true;
    InputStream inputStream = getClass().getResourceAsStream("/provider.txt");
    try {
      byte[] arrayOfByte = new byte[inputStream.available()];
      inputStream.read(arrayOfByte);
      String str1;
      a = Byte.parseByte(str1 = new String(arrayOfByte, "UTF-8"));
      cj.d("PROVIDER= " + a);
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
    String str;
    if ((str = x.b()) != null)
      try {
        int i = str.indexOf(":");
        String str1 = str.substring(0, i);
        str = str.substring(i + 1);
        d = str1;
        c = Integer.parseInt(str);
      } catch (Exception exception) {} 
    bb.a().a(model.x.a());
    bi.a().a(bb.a());
    ad.a().a(model.bi.a());
    TerrainMidlet terrainMidlet = this;
    f.setFullScreenMode(true);
    Display.getDisplay(terrainMidlet).setCurrent((Displayable)f);
    b = this;
  }
  
  public static void a(String paramString1, String paramString2, model.as paramas1, as paramas2) {
    paramString2 = paramString2;
    (new Thread(new di(paramString2, paramString1, paramas1, paramas2))).start();
  }
  
  public static void a(int paramInt) {}
  
  public static void a() {
    try {
      g.start();
    } catch (Exception exception) {
    }
  }
  
  public static void b(int paramInt) {
    try {
      ((VolumeControl)g.getControl("VolumeControl")).setLevel(paramInt);
      i = paramInt;
    } catch (Exception exception) {
    }
  }
  
  public static void c(int paramInt) {
    if (0 == h)
      return; 
    try {
      if (g != null) {
        g.stop();
        g.deallocate();
        g = null;
        System.gc();
      } 
      (g = Manager.createPlayer("".getClass().getResourceAsStream("/" + Character.MIN_VALUE + ".mid"), "audio/midi")).realize();
      g.prefetch();
      g.setLoopCount(1);
      b(i);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.gc();
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\TerrainMidlet.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */