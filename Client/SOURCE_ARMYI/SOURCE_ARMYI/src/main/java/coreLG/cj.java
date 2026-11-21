package coreLG;

import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

import model.y;

public class cj {
  public static Random a = new Random();
  
  private static short[] e = new short[] { 
      0, 18, 36, 54, 71, 89, 107, 125, 143, 160, 
      178, 195, 213, 230, 248, 265, 282, 299, 316, 333, 
      350, 367, 384, 400, 416, 433, 449, 465, 481, 496, 
      512, 527, 543, 558, 573, 587, 602, 616, 630, 644, 
      658, 672, 685, 698, 711, 724, 737, 749, 761, 773, 
      784, 796, 807, 818, 828, 839, 849, 859, 868, 878, 
      887, 896, 904, 912, 920, 928, 935, 943, 949, 956, 
      962, 968, 974, 979, 984, 989, 994, 998, 1002, 1005, 
      1008, 1011, 1014, 1016, 1018, 1020, 1022, 1023, 1023, 1024, 
      1024 };
  
  private static short[] f;
  
  private static int[] g;
  
  public static Image b;
  
  public static Image c;
  
  public static y d;
  
  static {
    try {
      b = Image.createImage("/iconmenu.png");
      c = Image.createImage("/iconcam.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    }
  }
  
  public static void a() {
    f = new short[91];
    g = new int[91];
    for (byte b = 0; b <= 90; b++) {
      f[b] = e[90 - b];
      if (f[b] == 0) {
        g[b] = Integer.MAX_VALUE;
      } else {
        g[b] = (e[b] << 10) / f[b];
      } 
    } 
  }
  
  public static Image a(String paramString) {
    Image image = null;
    try {
      image = Image.createImage(paramString);
    } catch (Exception exception) {}
    if (image == null)
      System.out.println("Load image '" + paramString + "': ERROR!!!"); 
    return image;
  }
  
  public static int a(int paramInt) {
    return ((paramInt = c(paramInt)) >= 0 && paramInt < 90) ? e[paramInt] : ((paramInt >= 90 && paramInt < 180) ? e[180 - paramInt] : ((paramInt >= 180 && paramInt < 270) ? -e[paramInt - 180] : -e[360 - paramInt]));
  }
  
  public static int b(int paramInt) {
    return ((paramInt = c(paramInt)) >= 0 && paramInt < 90) ? f[paramInt] : ((paramInt >= 90 && paramInt < 180) ? -f[180 - paramInt] : ((paramInt >= 180 && paramInt < 270) ? -f[paramInt - 180] : f[360 - paramInt]));
  }
  
  public static int a(int var0, int var1) {
    int var10000;
    int var2;
    if (var0 != 0) {
      var2 = Math.abs((var1 << 10) / var0);
    
      label44: {
        for(int var3 = 0; var3 <= 90; ++var3) {
          if (g[var3] >= var2) {
            var10000 = var3;
            break label44;
          }
        }
      
        var10000 = 0;
      }
    
      var2 = var10000;
      if (var1 >= 0 && var0 < 0) {
        var2 = 180 - var2;
      }
    
      if (var1 < 0 && var0 < 0) {
        var2 += 180;
      }
    
      if (var1 >= 0 || var0 < 0) {
        return var2;
      }
    
      var10000 = 360 - var2;
    } else {
      var10000 = var1 > 0 ? 90 : 270;
    }
  
    var2 = var10000;
    return var2;
  }
  
  public static int c(int paramInt) {
    if (paramInt >= 360)
      paramInt -= 360; 
    if (paramInt < 0)
      paramInt += 360; 
    return paramInt;
  }
  
  public static int b(int paramInt1, int paramInt2) {
    return paramInt1 + a.nextInt(paramInt2 - paramInt1);
  }
  
  public static int d(int paramInt) {
    return a.nextInt() % paramInt;
  }
  
  public static boolean e(int paramInt) {
    return (paramInt >> 24 != 0);
  }
  
  public static boolean a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    return !(paramInt1 < paramInt3 || paramInt1 >= paramInt3 + paramInt5 || paramInt2 < paramInt4 || paramInt2 >= paramInt4 + paramInt6);
  }
  
  public static boolean a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
    return (paramInt1 + paramInt3 >= paramInt5 && paramInt1 <= paramInt5 + paramInt7 && paramInt2 + paramInt4 >= paramInt6 && paramInt2 <= paramInt6 + paramInt8);
  }
  
  public static byte[] b(String paramString) {
    try {
      RecordStore recordStore;
      byte[] arrayOfByte = (recordStore = RecordStore.openRecordStore(paramString, false)).getRecord(1);
      recordStore.closeRecordStore();
      return arrayOfByte;
    } catch (Exception exception) {
      System.out.println("rms '" + paramString + "' not found!");
      return null;
    } 
  }
  
  public static void a(String paramString, int paramInt) {
    try {
      a(new byte[] { (byte)paramInt }, paramString);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public static int c(String paramString) {
    byte[] arrayOfByte;
    return ((arrayOfByte = b(paramString)) == null) ? -1 : arrayOfByte[0];
  }
  
  public static void a(byte[] paramArrayOfbyte, String paramString) {
    try {
      RecordStore recordStore;
      if ((recordStore = RecordStore.openRecordStore(paramString, true)).getNumRecords() == 0) {
        recordStore.addRecord(paramArrayOfbyte, 0, paramArrayOfbyte.length);
      } else {
        recordStore.setRecord(1, paramArrayOfbyte, 0, paramArrayOfbyte.length);
      } 
      recordStore.closeRecordStore();
      return;
    } catch (Exception exception) {
      System.out.println("save rms '" + paramString + "' error!");
      return;
    } 
  }
  
  public static int a(int paramInt, byte[] paramArrayOfbyte) {
    byte b;
    return ((b = paramArrayOfbyte[paramInt]) & 0xFF) << 8 | (b = paramArrayOfbyte[paramInt + 1]) & 0xFF;
  }
  
  public static void d(String paramString) {
    System.out.println(paramString);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\cj.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */