package model;

import coreLG.ds;
import coreLG.el;
import java.util.Vector;

public final class bg {
  public boolean a;
  
  public Vector b;
  
  public int c;
  
  public int d;
  
  public int e;
  
  public int f;
  
  public int g;
  
  public int h;
  
  public static int i;
  
  public static int j;
  
  public static int k;
  
  public static int l;
  
  public static int m;
  
  public final void a(Vector paramVector, int paramInt) {
    this.b = paramVector;
    this.f = 0;
    this.g = 0;
    for (paramInt = 0; paramInt < paramVector.size(); paramInt++) {
      z z = (model.z) paramVector.elementAt(paramInt);
      int i;
      if ((i = bn.b.a(z.a)) > this.f)
        this.f = i; 
      this.g += ds.ab;
    } 
    this.f += 10;
    if (this.f < 100)
      this.f = 100; 
    this.g += 4;
    this.d = 2;
    this.e = el.e - 21 - this.g - 6;
    this.h = el.e - ds.ab;
    if (paramVector.size() > 7) {
      this.e = el.e - ds.ab * 7 - 31;
      this.g = ds.ab * 7 + 4;
    } 
    if (el.e < 200)
      this.e += 10; 
    this.a = true;
    this.c = 0;
    if ((m = this.b.size() * ds.ab - ds.ab * 7) < 0)
      m = 0; 
    i = 0;
    j = 0;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bg.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */