package model;

import coreLG.bl;
import coreLG.bx;
import coreLG.ci;
import coreLG.cv;
import coreLG.ds;
import coreLG.el;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class ac {
  private static Image h;
  
  private static Image i;
  
  private static Image j;
  
  public static Image a;
  
  public static Image b;
  
  private static Image k;
  
  private static Image l;
  
  private static Image m;
  
  private static Image n;
  
  private static Image o;
  
  private static Image p;
  
  private static Image q;
  
  private static Image r;
  
  private static Image s;
  
  private static Image t;
  
  private static Image u;
  
  private static Image v;
  
  private static Image w;
  
  private static Image x;
  
  private static Image y;
  
  private static Image z;
  
  private static Image A;
  
  private static Image B;
  
  private static Image C;
  
  private static Image D;
  
  private static Image E;
  
  private static Image F;
  
  private static Image G;
  
  private static Image[] H;
  
  private static Image I;
  
  private static Image J;
  
  private static Image K;
  
  public static byte c;
  
  public int d = 0;
  
  public int e = 130;
  
  private s L;
  
  private static int M;
  
  private static int N;
  
  private static int O;
  
  private static int P;
  
  public static int f;
  
  public static int g;
  
  private int[] Q = new int[] { 52, 110, 250 };
  
  private int[] R = new int[] { 100, 180, 150 };
  
  private int[] S = new int[] { 45, 40, 50 };
  
  private int[] T = new int[] { 100, 200, 300 };
  
  private int[] U = new int[] { 80, 50, 100 };
  
  private static int V;
  
  private ay W;
  
  private boolean X;
  
  private int Y;
  
  static {
    y filePack;
    (filePack = new y("/map/model.bg")).a("rock1.png");
    filePack.a("rock2.png");
    filePack.a("cobafilePack.png");
    bx.x = filePack.a("cloud1.png");
    filePack.a("longga.png");
    j = filePack.a("cl2.png");
    h = filePack.a("sun0.png");
    a = filePack.a("wts.png");
    b = filePack.a("inWater.png");
    l = filePack.a("co.png");
    i = filePack.a("halongkaka.png");
    k = filePack.a("canhdong.png");
    n = filePack.a("rungRam.png");
    o = filePack.a("bang.png");
    p = filePack.a("back1.png");
    q = filePack.a("back2.png");
    r = filePack.a("back3.png");
    s = filePack.a("back4.png");
    t = filePack.a("back5.png");
    u = filePack.a("back6.png");
    v = filePack.a("back7.png");
    w = filePack.a("back8.png");
    x = filePack.a("back9.png");
    y = filePack.a("back10.png");
    z = filePack.a("back11.png");
    A = filePack.a("back12.png");
    B = filePack.a("back13.png");
    C = filePack.a("back14.png");
    D = filePack.a("back15.png");
    try {
      E = Image.createImage("/map/b1.png");
      F = Image.createImage("/map/b2.png");
      G = Image.createImage("/map/b3.png");
    } catch (IOException iOException2) {
      iOException2.printStackTrace();
    } 
    bl.B = filePack.a("web.png");
    m = filePack.a("co2.png");
    I = filePack.a("rock_up.png");
    J = filePack.a("map_spider_lafilePackout.png");
    K = filePack.a("rock_down.png");
    (H = new Image[3])[0] = filePack.a("mafilePack1.png");
    H[1] = filePack.a("mafilePack2.png");
    H[2] = filePack.a("mafilePack3.png");
    bx.y = filePack.a("sun0.png");
    bx.w = new Image[2];
    for (byte b = 0; b < 2; b++)
      bx.w[b] = filePack.a("cl" + (b + 1) + ".png");
    V = 10;
    (new int[11])[0] = 1;
    (new int[11])[1] = 1;
    (new int[11])[2] = 10;
    (new int[11])[3] = 10;
    (new int[11])[4] = 20;
    (new int[11])[5] = 20;
    (new int[11])[6] = 30;
    (new int[11])[7] = 30;
    (new int[11])[8] = 40;
    (new int[11])[9] = 40;
    (new int[11])[10] = 40;
  }
  
  public ac(byte paramByte) {
    M = ds.aa - 135;
    c = paramByte;
    this.L = new s(a, 24, 24);
    g = cv.b - l.getHeight();
    N = ds.Z / 2;
    switch (paramByte) {
      case 0:
        O = ds.Z / 128;
        return;
      case 11:
        O = ds.Z / 123;
        P = 1;
        return;
      case 1:
        O = ds.Z / 72;
        return;
      case 3:
        O = ds.Z / 72;
        return;
      case 2:
        O = ds.Z / 64;
        return;
      case 4:
        O = ds.Z / 241;
        P = ds.Z / 226;
        return;
      case 5:
        O = ds.Z / 241;
        return;
      case 6:
      case 10:
        O = ds.Z / 238;
        P = ds.Z / 225;
        return;
      case 8:
        O = ds.Z / 219;
        P = ds.Z / 210;
        return;
      case 7:
        O = ds.Z / 219;
        P = ds.Z / 218;
        return;
      case 12:
        O = ds.Z / 108;
        P = ds.Z / 108;
        this.W = new ay();
        this.W.b = 300;
        this.W.c = 400;
        this.W.d = 5;
        this.W.e = 7;
        this.W.f = 3;
        this.W.a = 150;
        this.W.a(1);
        this.W.a = -50;
        return;
      case 13:
        O = ds.Z / 219;
        P = ds.Z / 218;
        return;
      case 14:
        O = ds.Z / 219;
        P = ds.Z / 218;
        break;
    } 
  }
  
  public final void a() {
    if (ci.a != 2 && c != 3) {
      ac ac1 = this;
      for (byte b = 0; b < ac1.Q.length; b++) {
        ac1.Q[b] = ac1.Q[b] - (coreLG.m.c - coreLG.m.e) * ac1.S[b] / 100;
        ac1.R[b] = ac1.R[b] - (coreLG.m.d - coreLG.m.f) * ac1.S[b] / 100;
        ac1.T[b] = ac1.T[b] - (coreLG.m.c - coreLG.m.e) * ac1.S[b] / 100;
        ac1.U[b] = ac1.U[b] - (coreLG.m.d - coreLG.m.f) * ac1.S[b] / 100;
      } 
    } 
    if (this.W != null)
      this.W.a(); 
  }
  
  public final void a(Graphics var1) {
    Graphics var2;
    label337: {
      var2 = var1;
      byte var8 = c;
      var1.translate(-var1.getTranslateX(), -var1.getTranslateY() + 1);
      int var3;
      int var4;
      int var5;
      int var6;
      int var9;
      switch(var8) {
        case 0:
          var1.setColor(8831994);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a == 2) {
            var1.setColor(8438010);
            var1.fillRect(0, 0, ds.Z, ds.aa);
            break label337;
          }
        
          var9 = (coreLG.m.c >> 2) % 128;
          var3 = ds.aa - (coreLG.m.d + ds.aa >> 2) + 49;
          var1.setColor(2002158);
          var1.fillRect(0, var3, ds.Z, ds.aa - var3);
        
          for(var4 = 0; var4 <= O + 1; ++var4) {
            var2.drawImage(i, -var9 + (var4 << 7), var3, 6);
          }
        
          var2.drawImage(h, N, var3 - 100, 0);
          var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          break;
        case 1:
          var1.setColor(8180459);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a == 2) {
            break label337;
          }
        
          var9 = (coreLG.m.c >> 2) % 64;
          var3 = ds.aa - (coreLG.m.d + ds.aa >> 2) + 170;
        
          for(var4 = 0; var4 <= O + 1; ++var4) {
            var2.drawImage(k, -var9 + (var4 << 6), var3, 36);
          }
        
          var2.drawImage(h, N, var3 - 200, 0);
          var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          break;
        case 2:
          var1.setColor(8180459);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 2) % 64;
            var3 = ds.aa - (coreLG.m.d + ds.aa >> 2) + 150 + this.d;
          
            for(var4 = 0; var4 <= O + 1; ++var4) {
              var2.drawImage(o, -var9 + (var4 << 6), var3, 36);
            }
          
            var2.drawImage(h, N, var3 - 200, 0);
            var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
            this.b(var2);
            var2.setColor(18797);
            var2.fillRect(0, var3, el.d, 500);
          }
          break label337;
        case 3:
          var1.setColor(8711932);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 2) % 72;
            var3 = ds.aa - (coreLG.m.d + ds.aa >> 2) + this.d;
          
            for(var4 = 0; var4 <= O + 1; ++var4) {
              var2.drawImage(n, -var9 + var4 * 72, var3, 36);
            }
          }
          break label337;
        case 4:
          var1.setColor(14282750);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a == 2) {
            break label337;
          }
        
          var9 = (coreLG.m.c >> 3) % 241;
          var3 = (coreLG.m.c >> 2) % 226;
          var4 = ds.aa - (coreLG.m.d + ds.aa >> 2) + this.d;
        
          for(var5 = 0; var5 <= O + 1; ++var5) {
            var2.drawImage(q, -var9 + var5 * 241, var4, 36);
          }
        
          for(var5 = 0; var5 <= P + 1; ++var5) {
            var2.drawImage(p, -var3 + var5 * 226, var4, 36);
          }
        
          var2.setColor(11255025);
          var2.fillRect(0, var4, el.d, 100);
          var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          break;
        case 5:
          var1.setColor(16775152);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 3) % 241;
            var3 = (coreLG.m.c >> 2) % 241;
            var4 = ds.aa - coreLG.m.d / 2 + this.d;
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(s, -var9 + var5 * 241, var4, 36);
            }
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(r, -var3 + var5 * 241, var4, 36);
            }
          
            var2.setColor(12373247);
            var2.fillRect(0, var4, el.d, 100);
          }
          break label337;
        case 6:
          var1.setColor(16706268);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a == 2) {
            break label337;
          }
        
          var9 = (coreLG.m.c >> 3) % 238;
          var3 = (coreLG.m.c >> 2) % 225;
          var4 = ds.aa - coreLG.m.d / 2 + this.d;
        
          for(var5 = 0; var5 <= O + 1; ++var5) {
            var2.drawImage(u, -var9 + var5 * 238, var4, 36);
          }
        
          for(var5 = 0; var5 <= O + 1; ++var5) {
            var2.drawImage(t, -var3 + var5 * 225, var4, 36);
          }
        
          var2.setColor(12373247);
          var2.fillRect(0, var4, el.d, 600);
          var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          break;
        case 7:
          var1.setColor(15267327);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 3) % 219;
            var3 = (coreLG.m.c >> 2) % 218;
            var4 = ds.aa - coreLG.m.d / 3 + this.d - 100;
            var5 = ds.aa - coreLG.m.d / 2 + this.d;
          
            for(var6 = 0; var6 <= O + 1; ++var6) {
              var2.drawImage(w, -var9 + var6 * 219, var4, 36);
            }
          
            for(var6 = 0; var6 <= O + 1; ++var6) {
              var2.drawImage(w, -var9 + var6 * 219 + 30, var4 + 150, 36);
            }
          
            for(var6 = 0; var6 <= O + 1; ++var6) {
              var2.drawImage(v, -var3 + var6 * 218, var5, 36);
            }
          }
          break label337;
        case 8:
          var1.setColor(16706268);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 3) % 219;
            var3 = (coreLG.m.c >> 2) % 210;
            var4 = ds.aa - coreLG.m.d / 2 + this.d;
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(x, -var9 + var5 * 219, var4, 36);
            }
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(y, -var3 + var5 * 210, var4, 36);
            }
          
            var2.setColor(12373247);
            var2.fillRect(0, var4, el.d, 100);
            var1 = var2;
            ac var10 = this;
          
            for(var3 = 0; var3 < H.length; ++var3) {
              var1.drawImage(H[var3], var10.T[var3], var10.U[var3] + var10.e, 0);
            }
          
            var1.drawImage(H[0], var10.T[0] + 30, var10.U[0] + var10.e - 30, 0);
          }
          break label337;
        case 9:
          var1.setColor(16752448);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a == 2) {
            break label337;
          }
        
          var9 = (coreLG.m.c >> 3) % 238;
          var3 = (coreLG.m.c >> 2) % 225;
          var4 = ds.aa - coreLG.m.d / 2 + this.d;
        
          for(var5 = 0; var5 <= O + 1; ++var5) {
            var2.drawImage(u, -var9 + var5 * 238, var4, 36);
          }
        
          for(var5 = 0; var5 <= O + 1; ++var5) {
            var2.drawImage(t, -var3 + var5 * 225, var4, 36);
          }
        
          var2.setColor(12373247);
          var2.fillRect(0, var4, el.d, 100);
          var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          break;
        case 10:
          var1.setColor(7106965);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 3) % 238;
            var3 = (coreLG.m.c >> 2) % 225;
            var4 = ds.aa - coreLG.m.d / 2 + this.d;
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(A, -var9 + var5 * 238, var4, 36);
            }
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(z, -var3 + var5 * 225, var4, 36);
            }
          
            var2.setColor(4936293);
            var2.fillRect(0, var4, el.d, 600);
            var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          }
          break label337;
        case 11:
          var1.setColor(13039359);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 3) % 123;
            var3 = ds.aa - (coreLG.m.d + ds.aa >> 2) + this.d;
            var1.setColor(9296639);
            var1.fillRect(0, var3, ds.Z, ds.aa - var3);
          
            for(var4 = 0; var4 <= O + 1; ++var4) {
              var2.drawImage(B, -var9 + var4 * 123, var3, 6);
            }
          
            var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          }
          break label337;
        case 12:
          if (el.c % 200 == 0) {
            this.X = true;
          }
        
          var1.setColor(530454);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 4) % 108;
            var3 = (coreLG.m.c >> 3) % 108;
            var4 = ds.aa - (coreLG.m.d + ds.aa >> 2) + this.d;
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(D, -var9 + var5 * 108, var4, 6);
            }
          
            if (this.X) {
              ++this.Y;
              if (this.Y == 10) {
                this.Y = 0;
                this.X = false;
              }
            
              if (this.Y % 2 == 0) {
                var2.setColor(16777215);
                var2.fillRect(0, var4 - 60, el.d, 130);
              }
            }
          
            if (this.W != null) {
              var2.setClip(0, var4 - 50, 1000, 120);
              this.W.b(var2);
              var2.setClip(0, 0, 2000, 2000);
            }
          
            var2.setClip(0, var4 - 50, 1000, 100);
            var2.setClip(0, 0, 2000, 2000);
          
            for(var5 = 0; var5 <= O + 1; ++var5) {
              var2.drawImage(C, -var3 + var5 * 108, var4, 6);
            }
          
            var2.translate(-var2.getTranslateX(), -var2.getTranslateY() + 1);
          }
          break label337;
        case 13:
          var1.setColor(8229794);
          var1.fillRect(0, 0, ds.Z, ds.aa);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 2) % 144;
            var3 = (coreLG.m.c >> 2) % 176;
            var4 = (coreLG.m.c >> 3) % 69;
            var5 = ds.aa - coreLG.m.d / 3 + this.d;
            var6 = ds.aa - coreLG.m.d / 4 + this.d - 80;
          
            int var7;
            for(var7 = 0; var7 <= el.d / 69 + 1; ++var7) {
              var2.drawImage(J, -var4 + var7 * 69, var6, 36);
            }
          
            var2.setColor(9728620);
            var2.fillRect(0, var6, el.d, 300);
          
            for(var7 = 0; var7 <= el.d / 144 + 1; ++var7) {
              var2.drawImage(K, -var9 + var7 * 144, var5, 36);
            }
          
            var2.setColor(3223857);
            var2.fillRect(0, -500, el.d, -coreLG.m.d / 3 + 120 + 500 - 88);
          
            for(var7 = 0; var7 <= el.d / 176 + 1; ++var7) {
              var2.drawImage(I, -var3 + var7 * 176, -coreLG.m.d / 3 + 120, 36);
            }
          
            var2.setColor(4996403);
            var2.fillRect(0, var5, el.d, 300);
          }
          break label337;
        case 14:
          var1.setColor(14282750);
          var1.fillRect(0, 0, el.d, el.e);
          if (ci.a != 2) {
            var9 = (coreLG.m.c >> 3) % 219;
            var3 = (coreLG.m.c >> 2) % 218;
            var4 = ds.aa - coreLG.m.d / 3 + this.d - 100;
            var5 = ds.aa - coreLG.m.d / 3 + this.d - 30;
          
            for(var6 = 0; var6 <= O + 1; ++var6) {
              var2.drawImage(E, -var9 + var6 * 219, var4, 36);
            }
          
            for(var6 = 0; var6 <= O + 1; ++var6) {
              var2.drawImage(F, -var9 + var6 * 219 + 30, var4 + 150, 36);
            }
          
            for(var6 = 0; var6 <= O + 1; ++var6) {
              var2.drawImage(G, -var3 + var6 * 218, var5, 36);
            }
          }
          break;
        default:
          break label337;
      }
    
      this.b(var2);
    }
  
    var2.translate(-coreLG.m.c, -coreLG.m.d);
  }
  
  private void b(Graphics paramGraphics) {
    for (byte b = 0; b < this.Q.length; b++)
      paramGraphics.drawImage(j, this.Q[b], this.R[b], 0); 
  }
  
  public final void a(byte paramByte, Graphics paramGraphics) {
    int i;
    int j = 0;
    int k = 0;
    if (paramByte == 0) {
      j = coreLG.m.c / 24;
      k = (coreLG.m.c + ds.Z) / 24;
      for (i = j; i <= k; i++) {
        this.L.a((el.c % 8 > 4) ? 0 : 1, i * 24, f - 12 + b.getHeight() / 2, 0, 0, paramGraphics);
        paramGraphics.drawImage(b, i * 24, f - 12 + 24 + b.getHeight() / 2, 0);
      } 
      return;
    } 
    if (paramByte == 1) {
      j = coreLG.m.c / 64;
      k = (coreLG.m.c + ds.Z) / 64;
      for (i = j; i <= k; i++)
        paramGraphics.drawImage(l, i << 6, g, 0); 
      return;
    } 
    if (paramByte == 2) {
      j = coreLG.m.c / 64;
      k = (coreLG.m.c + ds.Z) / 64;
      for (i = j; i <= k; i++)
        paramGraphics.drawImage(m, i << 6, g, 0); 
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ac.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */