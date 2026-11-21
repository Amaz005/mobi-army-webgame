package model;

import coreLG.cj;
import javax.microedition.lcdui.Graphics;

public final class bc {
  public int a;
  
  public int b;
  
  public int c;
  
  public int d;
  
  public boolean e;
  
  private boolean i = false;
  
  private boolean j = true;
  
  private static int k = 2;
  
  private static final int[] l = new int[] { 18, 14, 11, 9, 6, 4, 2 };
  
  private static int m = 0;
  
  private static String[] n = new String[] { 
      " 0", ".,@?!_1\"/$-():*+<=>;%&~#%^&*{}[];'/1", "abc2áàảãạâấầẩẫậăắằẳẵặ2", "def3đéèẻẽẹêếềểễệ3", "ghi4íìỉĩị4", "jkl5", "mno6óòỏõọôốồổỗộơớờởỡợ6", "pqrs7", "tuv8úùủũụưứừửữự8", "wxyz9ýỳỷỹỵ9", 
      "*", "#" };
  
  private static String[] o = new String[] { 
      "0", "1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", 
      "0", "0" };
  
  private String p = "";
  
  private String q = "";
  
  private String r = "";
  
  private int s = 0;
  
  private int t = 0;
  
  private int u = 500;
  
  private int v = 0;
  
  private int w = -1984;
  
  private int x = 0;
  
  private int y = 0;
  
  private int z = 10;
  
  private int A = 0;
  
  private static boolean B;
  
  private static int C;
  
  public static int f = 0;
  
  public static final String[] g = new String[] { "abc", "Abc", "ABC", "123" };
  
  private static int D = 11;
  
  private static bn E = bn.i;
  
  public z h;
  
  public bc() {
    this.p = "";
    m = E.a() + 1;
    this.h = new z(model.u.cm(), new f(this));
    C = E.a("ABC") + 5;
  }
  
  public final void a() {
    cj.d("CLEAR");
    if (this.s > 0 && this.p.length() > 0) {
      this.p = String.valueOf(this.p.substring(0, this.s - 1)) + this.p.substring(this.s, this.p.length());
      this.s--;
      d();
      e();
    } 
  }
  
  private void d() {
    if (this.A == 2) {
      this.r = this.q;
    } else {
      this.r = this.p;
    } 
    if (this.v < 0 && E.a(this.r) + this.v < this.c - 4 - 13 - C)
      this.v = this.c - 10 - C - E.a(this.r); 
    if (this.v + E.a(this.r.substring(0, this.s)) <= 0) {
      this.v = -E.a(this.r.substring(0, this.s));
      this.v += 40;
    } else if (this.v + E.a(this.r.substring(0, this.s)) >= this.c - 12 - C) {
      this.v = this.c - 10 - C - E.a(this.r.substring(0, this.s)) - 8;
    } 
    if (this.v > 0)
      this.v = 0; 
  }
  
  private void c(int paramInt) {
    if ((this.A == 2 || this.A == 3) && (paramInt < 48 || paramInt > 57) && (paramInt < 65 || paramInt > 90) && (paramInt < 97 || paramInt > 122))
      return;
    if (this.p.length() < this.u) {
      String str = String.valueOf(this.p.substring(0, this.s)) + (char)paramInt;
      if (this.s < this.p.length())
        str = String.valueOf(str) + this.p.substring(this.s, this.p.length());
      this.p = str;
      this.s++;
      e();
      d();
    }
  }
  
  public final boolean a(int paramInt) {
    if (paramInt == 8 || paramInt == -8 || paramInt == 204) {
      a();
      return true;
    }
    if (paramInt >= 65 && paramInt <= 122) {
      B = true;
      C = 0;
    }
    if (B) {
      if (paramInt == 45) {
        if (paramInt == this.w && this.x < l[k]) {
          this.p = String.valueOf(this.p.substring(0, this.s - 1)) + '_';
          this.r = this.p;
          e();
          d();
          this.w = -1984;
          return false;
        }
        this.w = 45;
      }
      if (paramInt >= 32) {
        c(paramInt);
        return false;
      }
    }
    if (paramInt == D) {
      if (++f > 3)
        f = 0;
      this.x = 1;
      this.w = paramInt;
      return false;
    }
    if (paramInt == 42)
      paramInt = 58;
    if (paramInt == 35)
      paramInt = 59;
    if (paramInt >= 48 && paramInt <= 59) {
      if (this.A == 0 || this.A == 2 || this.A == 3) {
        String[] arrayOfString;
        String str;
        paramInt = paramInt;
        if (this.A == 2 || this.A == 3) {
          arrayOfString = o;
        } else {
          arrayOfString = n;
        }
        if (paramInt == this.w) {
          this.y = (this.y + 1) % arrayOfString[paramInt - 48].length();
          char c = arrayOfString[paramInt - 48].charAt(this.y);
          if (f == 0) {
            c = Character.toLowerCase(c);
          } else if (f == 1) {
            c = Character.toUpperCase(c);
          } else if (f == 2) {
            c = Character.toUpperCase(c);
          } else {
            c = arrayOfString[paramInt - 48].charAt(arrayOfString[paramInt - 48].length() - 1);
          }
          str = String.valueOf(this.p.substring(0, this.s - 1)) + c;
          if (this.s < this.p.length())
            str = String.valueOf(str) + this.p.substring(this.s, this.p.length());
          this.p = str;
          this.x = l[k];
          e();
        } else if (this.p.length() < this.u) {
          if (f == 1 && this.w != -1984)
            f = 0;
          this.y = 0;
          char c = arrayOfString[paramInt - 48].charAt(this.y);
          if (f == 0) {
            c = Character.toLowerCase(c);
          } else if (f == 1) {
            c = Character.toUpperCase(c);
          } else if (f == 2) {
            c = Character.toUpperCase(c);
          } else {
            c = arrayOfString[paramInt - 48].charAt(arrayOfString[paramInt - 48].length() - 1);
          }
          str = String.valueOf(this.p.substring(0, this.s)) + c;
          if (this.s < this.p.length())
            str = String.valueOf(str) + this.p.substring(this.s, this.p.length());
          this.p = str;
          this.x = l[k];
          this.s++;
          e();
          d();
        }
        this.w = paramInt;
      } else if (this.A == 1) {
        c(paramInt);
        this.x = 1;
      }
    } else {
      this.y = 0;
      this.w = -1984;
      if (paramInt == 14) {
        if (this.s > 0) {
          this.s--;
          d();
          this.z = 10;
          return false;
        }
      } else if (paramInt == 15) {
        if (this.s < this.p.length()) {
          this.s++;
          d();
          this.z = 10;
          return false;
        }
      } else {
        if (paramInt == 19) {
          a();
          return false;
        }
        this.w = paramInt;
      }
    }
    return true;
  }
  
  public final void a(Graphics paramGraphics) {
    bc bc2;
    boolean bool = (bc2 = this).e;
    if (this.A == 2) {
      this.r = this.q;
    } else {
      this.r = this.p;
    } 
    paramGraphics.setColor(4694994);
    if (bool && this.j) {
      paramGraphics.setColor(16775305);
      paramGraphics.fillRect(this.a + 1, this.b + 1, this.c - 1, this.d - 1);
      paramGraphics.setColor(4694994);
    } 
    paramGraphics.drawRect(this.a + 1, this.b + 1, this.c - 2, this.d - 2);
    paramGraphics.setClip(this.a + 3, this.b + 1, this.c - C - 8, this.d - 4);
    paramGraphics.setColor(0);
    E.a(paramGraphics, this.r, 4 + this.v + this.a, this.b + (this.d - E.a()) / 2 + 1, 0);
    bc bc1;
    if ((bc1 = this).e && this.x == 0 && (this.z > 0 || this.t / 5 % 2 == 0)) {
      paramGraphics.setColor(0);
      paramGraphics.fillRect(4 + this.v + this.a + E.a(this.r.substring(0, this.s)) - 1 + 1, this.b + (this.d - m) / 2 + 1, 1, m);
    } 
  }
  
  private void e() {
    if (this.A == 2) {
      this.q = "";
      for (byte b = 0; b < this.p.length(); b++)
        this.q = String.valueOf(this.q) + "*"; 
      if (this.x > 0 && this.s > 0)
        this.q = String.valueOf(this.q.substring(0, this.s - 1)) + this.p.charAt(this.s - 1) + this.q.substring(this.s, this.q.length()); 
    } 
  }
  
  public final void b() {
    this.t++;
    if (this.x > 0) {
      this.x--;
      if (this.x == 0) {
        this.y = 0;
        if (f == 1 && this.w != D)
          f = 0; 
        this.w = -1984;
        e();
      } 
    } 
    if (this.z > 0)
      this.z--; 
  }
  
  public final String c() {
    return this.p;
  }
  
  public final void a(String paramString) {
    if (paramString == null)
      return; 
    this.w = -1984;
    this.x = 0;
    this.y = 0;
    this.p = paramString;
    this.r = paramString;
    e();
    this.s = paramString.length();
    d();
  }
  
  public final void b(int paramInt) {
    this.A = paramInt;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bc.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */