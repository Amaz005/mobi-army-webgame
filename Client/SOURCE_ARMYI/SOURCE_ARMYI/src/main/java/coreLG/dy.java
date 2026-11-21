package coreLG;

public class dy {
  public static dy instance;
  
  int a;
  
  int b;
  
  long c = this.d = System.currentTimeMillis();
  
  long d;
  
  boolean e = true;
  
  public static dy gI(){
    if (instance == null){
      instance = new dy();
    }
    return instance;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dy.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */