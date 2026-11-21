package model;

import java.io.InputStream;

public final class ap {
  private static byte[] b;
  
  public static String a;
  
  public ap() {
    InputStream inputStream = getClass().getResourceAsStream("/agent.txt");
    try {
      b = new byte[inputStream.available()];
      inputStream.read(b);
      a = new String(b, "UTF-8");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ap.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */