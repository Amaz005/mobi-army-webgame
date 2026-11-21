package coreLG;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import model.s;
import model.y;

public class bl {
  public boolean a;
  
  private boolean aF;
  
  private static Image aG;
  
  public static boolean b;
  
  public boolean c;
  
  public static boolean d = false;
  
  private static Image[] aH = new Image[20];
  
  public boolean e = true;
  
  public boolean f = false;
  
  public boolean g = false;
  
  private static y aI;
  
  public static byte[] h;
  
  public boolean i;
  
  public boolean j;
  
  public int k;
  
  private static Image aJ;
  
  public static Image l;
  
  public static Image m;
  
  public static Image n;
  
  public static Image o;
  
  public static Image p;
  
  public static Image q;
  
  public static Image r;
  
  public static Image s;
  
  public static Image t;
  
  private static Image aK;
  
  public static Image u;
  
  public static Image v;
  
  private static Image aL;
  
  public static Image w;
  
  public static Image x;
  
  public static Image y;
  
  public static Image z;
  
  public static Image A;
  
  public static Image B;
  
  s C;
  
  public boolean D;
  
  int E = 0;
  
  int F = 0;
  
  public int G;
  
  public int H;
  
  boolean I;
  
  public int[] J;
  
  public int K = -1;
  
  public boolean L;
  
  private boolean aM;
  
  public int M = 0;
  
  public int N = 0;
  
  public int O = 0;
  
  private byte aN;
  
  byte P = 0;
  
  public boolean Q;
  
  public boolean R;
  
  boolean S;
  
  public boolean T;
  
  private boolean aO = false;
  
  public boolean U = false;
  
  byte V = 0;
  
  byte W = 0;
  
  private int aP = 30;
  
  private int aQ = 30;
  
  int X = 0;
  
  private int aR = 0;
  
  private int aS;
  
  private int aT;
  
  int Y = 5;
  
  private int aU;
  
  int Z = 2;
  
  private int aV = this.Z;
  
  public byte aa = 0;
  
  int ab;
  
  public int ac = 1;
  
  boolean ad = false;
  
  private boolean aW = false;
  
  public int ae;
  
  public int af;
  
  private String aX;
  
  public byte ag = 0;
  
  private byte aY;
  
  public boolean ah;
  
  public boolean ai;
  
  public boolean aj;
  
  public boolean ak;
  
  private int aZ;
  
  boolean al;
  
  short am;
  
  private int ba;
  
  private int bb;
  
  String an;
  
  private int bc = 0;
  
  private int bd = 1;
  
  int ao;
  
  int ap;
  
  public static short[] aq = null;
  
  public static short[] ar = null;
  
  private static Image be;
  
  public static Image as;
  
  public static Image at;
  
  private int bf = 100;
  
  boolean au = false;
  
  private byte bg = -1;
  
  private boolean bh;
  
  private boolean bi;
  
  int av;
  
  int aw;
  
  private int bj;
  
  private int bk;
  
  private int bl;
  
  private int bm;
  
  boolean ax = false;
  
  private int bn;
  
  private int bo;
  
  boolean ay;
  
  int az;
  
  private int bp = 0;
  
  public int aA;
  
  public int aB;
  
  private short[][] bq;
  
  private short[][] br;
  
  int aC = 25;
  
  private boolean bs = false;
  
  private int bt = 0;
  
  private int bu;
  
  public byte aD;
  
  byte aE = 1;
  
  private static int bv;
  
  private static int bw;
  
  private static int bx;
  
  private static int by;
  
  private static int bz;
  
  public static void a() {
    aI = new model.y(h);
    aG = ci.l;
    aH[0] = aI.a("yellowP.png");
    aH[1] = aI.a("cuteGirl.png");
    aH[2] = aI.a("greenP.png");
    aH[3] = aI.a("conKhiP.png");
    aH[4] = aI.a("rocketer.png");
    aH[5] = aI.a("robot.png");
    aH[6] = aI.a("ga.png");
    aH[7] = aI.a("tazz.png");
    aH[8] = aI.a("apache.png");
    aH[9] = aI.a("bosssmall.png");
    aH[10] = aI.a("bossbig.png");
    be = aI.a("bug_gun.png");
    as = aI.a("bug_body.png");
    at = aI.a("bug_leg.png");
    x = aI.a("fire.png");
    aJ = aI.a("arm.png");
    l = aI.a("leg.png");
    m = aI.a("body.png");
    n = aI.a("body_injured.png");
    aH[12] = aI.a("bossrobot.png");
    y = aI.a("UFO.png");
    z = aI.a("UFOFire.png");
    o = aI.a("khicau.png");
    p = aI.a("gun.png");
    q = aI.a("bomb.png");
    r = aI.a("fan1.png");
    s = aI.a("fan2.png");
    t = aI.a("gunbig.png");
    aI.a("mainGun.png");
    aK = aI.a("eye.png");
    u = aI.a("back_fan.png");
    v = aI.a("front_fan.png");
    aL = aI.a("miniballoon.png");
    w = aI.a("injured.png");
    A = aI.a("spider.png");
    aH[16] = aI.a("mainGun.png");
    aH[19] = aK;
    aH[11] = be;
    aH[13] = aJ;
    aI = null;
  }
  
  public bl(int paramInt1, byte paramByte1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, byte paramByte2) {
    this.aN = paramByte1;
    this.al = paramBoolean;
    this.Z = paramInt4;
    this.P = paramByte2;
    this.X = (paramByte2 == 9 || paramByte2 == 10 || this.P == 14 || this.P == 17 || this.P == 15 || this.P == 16 || this.P == 19 || this.P == 18 || this.P == 20) ? 0 : ((paramInt4 == 2) ? ((aq[this.P] < 0) ? 0 : aq[this.P]) : (180 - aq[this.P]));
    this.a = (this.aN % 2 == 0);
    if (this.P == 3) {
      this.C = new s(aH[this.P], 30, 32);
    } else if (this.P == 6) {
      this.C = new s(aH[this.P], 29, 24);
    } else if (this.P == 7) {
      this.C = new model.s(aH[this.P], 32, 32);
    } else if (this.P == 9) {
      this.C = new model.s(aH[this.P], 18, 19);
    } else if (this.P == 10) {
      this.C = new model.s(aH[this.P], 30, 28);
    } else if (this.P == 12) {
      this.C = new s(aH[this.P], 24, 30);
    } else if (this.P == 11) {
      this.C = new s(aH[this.P], 15, 15);
    } else if (this.P == 13) {
      this.C = new s(aH[this.P], 35, 40);
    } else if (this.P != 14) {
      if (this.P == 16) {
        this.C = new model.s(aH[this.P], 21, 20);
      } else if (this.P != 15 && this.P != 17) {
        if (this.P == 19) {
          this.C = new s(aH[this.P], 16, 8);
        } else if (this.P != 18 && this.P != 20) {
          this.C = new model.s(aH[this.P], 24, 24);
        } 
      } 
    } 
    this.I = true;
    this.S = false;
    this.T = true;
    this.G = paramInt2;
    this.H = paramInt3;
    this.av = paramInt2;
    this.aw = paramInt3;
    this.ba = paramInt2;
    this.bb = paramInt3;
    this.ao = this.G;
    this.ap = this.H;
    this.am = (short)this.G;
    this.K = -1;
    this.J = new int[coreLG.bx.o];
    this.ae = this.af;
    this.aj = false;
  }
  
  public final void a(byte paramByte) {
    this.au = true;
    this.bg = paramByte;
    this.bi = true;
    this.av = this.G;
    this.aw = this.H;
  }
  
  public final void b() {
    if (this.au) {
      this.I = false;
      this.ac = 3;
      if (this.bi) {
        this.H += 6;
        if (this.H >= (eb.b[this.bg]).H - 24) {
          (eb.b[this.bg]).H -= 24;
          this.bi = false;
          this.bh = true;
        } 
      } 
      if (this.bh) {
        this.H -= 6;
        this.H += 24;
        (eb.b[this.bg]).ap = this.H + 24;
        if (this.H <= this.aw) {
          this.H = this.aw;
          this.ap = this.aw;
          this.bh = false;
          this.au = false;
          el.O = true;
        } 
      } 
    } 
  }
  
  private static bp c(int paramInt1, int paramInt2) {
    int i = paramInt2 * cj.b(cj.c(paramInt1)) >> 10;
    paramInt1 = -(paramInt2 * cj.a(cj.c(paramInt1))) >> 10;
    return new bp(i, paramInt1);
  }
  
  public final void a(int paramInt1, int paramInt2) {
    this.ax = true;
    this.bj = paramInt1;
    this.bk = paramInt2;
    this.bl = this.G;
    this.bm = this.H;
    if (this.bj > this.bl) {
      this.ac = 1;
      return;
    } 
    this.ac = 0;
  }
  
  public final void a(int paramInt) {
    if (this.G >= cv.a) {
      this.G = -100;
      this.bj = this.bl;
      this.bk = this.bm;
      dk.aG = 0;
    } 
    int i = this.bj - this.G;
    int j = this.H - this.bk;
    i = cj.a(i, j);
    this.bn = (c(i, paramInt)).a;
    this.bo = (c(i, paramInt)).b;
    this.G += this.bn;
    this.H += this.bo;
    if (this.G < this.bj + paramInt / 2 && this.G >= this.bj - paramInt / 2 && this.H < this.bk + paramInt / 2 && this.H >= this.bk - paramInt / 2) {
      this.G = this.bj;
      this.H = this.bk;
      this.az = this.bk;
      while (!ci.K.a(this.G, this.az) && this.az < cv.b)
        this.az++; 
      this.ag = -1;
      this.ay = true;
      this.ax = false;
      el.O = true;
    } 
  }
  
  public void c() {
    switch (this.aa) {
      case 8:
        this.aU++;
        if (this.aU > 25) {
          if (this.ae <= 0) {
            this.k();
            break;
          } 
          this.aa = 0;
          this.e();
          this.aU = 0;
        } 
        break;
      case 5:
        if (this.bf <= 0) {
          ci.P.a(this.G, this.H - 8, (byte)5);
          this.bf = 100;
          break;
        } 
        this.bf--;
        break;
      case 7:
        this.aU++;
        if (this.aU > 4) {
          this.Y++;
          if (this.Y == 10)
            this.Y = 8;
          this.aU = 0;
        } 
        break;
    } 
    this.aS = this.aQ * cj.b(this.X) >> 10;
    this.aT = this.aQ * cj.a(this.X) >> 10;
    if (this.al && !this.i && !this.D) {
      if (this.ao < this.G && !this.I) {
        b(0);
      } else if (this.ao > this.G && !this.I) {
        b(2);
      } 
      if (this.ao == this.G && this.ap != this.H && this.aa == 0 && !this.I)
        this.H = this.ap; 
    } 
    if (this.S && !coreLG.bg.c) {
      this.I = true;
      this.S = false;
      this.R = true;
    } 
    if (this.I)
      if ((this.i || this.au) && this.aa != 5) {
        this.I = false;
        this.R = false;
      } else {
        if (this.H > cv.b + 200) {
          b = false;
          if (this.R) {
            this.Q = true;
            this.R = false;
            if (this.aa != 5 && ci.X == this.aN && !this.aW) {
              model.ad.a().b((short)this.G, (short)this.H);
              this.aW = true;
            } 
            this.I = false;
          } 
          this.ap = this.H;
          if (this.aa != 1)
            this.b(this.G, this.H);
        } else {
          this.bc += this.bd;
          int i = Math.abs((i = this.H + this.bc) - this.H);
          for (byte b = 0; b <= i; b++) {
            if (ci.K.a(this.G, this.H)) {
              this.bc = 0;
              this.I = false;
              if (this.R) {
                this.Q = true;
                this.R = false;
                if ((eb.b[eb.f]).P == 13)
                  this.j = true;
              } 
              this.ap = this.H;
              if (this.aa != 1)
                this.b(this.G, this.H);
              if (this.aN == ci.X) {
                if (this.aa != 1) {
                  if (!coreLG.bg.c) {
                    if (this.T) {
                      if (this.K == 21) {
                        eb.c(this.aN, (short)this.G, (short)this.H);
                        // Byte code: goto -> 782
                      } 
                      // Byte code: goto -> 782
                    } 
                    // Byte code: goto -> 782
                  } 
                  // Byte code: goto -> 782
                } 
                // Byte code: goto -> 782
              } 
              // Byte code: goto -> 782
            } 
            this.H++;
          } 
        } 
        if (cv.d && !this.aF && cv.a(this.G, this.H, (byte)2))
          this.aF = true;
      }  
    if (!this.L &&  this.P != 13) {
      if (this.Z == 0 && this.X > 180 - aq[this.P])
        this.X = 180 - aq[this.P];
      if (this.Z == 2 && this.X < aq[this.P])
        this.X = aq[this.P];
    } 
    if (this.ak) {
      this.aZ++;
      if (this.aZ == 20) {
        this.aZ = 0;
        this.ak = false;
        el.O = true;
      } 
      if (el.c % 2 == 0);
    } 
  }
  
  private void h() {
    if (this.Z == 0) {
      if (this.X > 180 - aq[this.P])
        this.X = 180 - aq[this.P]; 
      if (this.X < 91)
        this.X = 91; 
    } 
    if (this.Z == 2) {
      if (this.X < aq[this.P])
        this.X = aq[this.P]; 
      if (this.X > 89)
        this.X = 89; 
    } 
  }
  
  private void i() {
    if (this.L) {
      if (this.Z == 0)
        if (this.K == 18) {
          if (this.X > 225)
            this.X = 225; 
          if (this.X < 180)
            this.X = 180; 
        } else if (this.K == 20) {
          if (this.X != 91) {
            this.X = 91;
            this.Y = 3;
          } 
        } else if (this.K == 21) {
          this.X = -269;
        } else {
          h();
        }  
      if (this.Z == 2)
        if (this.K == 18) {
          if (this.X > 0)
            this.X = 0; 
          if (this.X < -45) {
            this.X = -45;
            return;
          } 
        } else if (this.K == 20) {
          if (this.X != 89) {
            this.X = 89;
            this.Y = 3;
            return;
          } 
        } else {
          if (this.K == 21) {
            this.X = -89;
            return;
          } 
          h();
        }  
      return;
    } 
    h();
  }
  
  public void d() {
    if (el.z == null) {
      if ((!this.al && this.ad || ci.S) && this.aa != 5) {
        if (!ds.ag) {
          if (el.I[2] && !this.I) {
            if (this.Z == 2) {
              this.l();
            } else {
              this.m();
            }
          } else if (!el.I[8]) {
            this.aR = 1;
          }
        } else if (!el.I[8]) {
          this.aR = 1;
        }
      
        if (!ds.ag) {
          if (el.I[8] && !this.I) {
            if (this.Z == 0) {
              this.l();
            } else {
              this.m();
            }
          } else if (!el.I[2]) {
            this.aR = 1;
          }
        } else if (!el.I[2]) {
          this.aR = 1;
        }
      
        if (!ds.ag && el.I[4] && !this.I && !el.I[6] && !el.I[5]) {
          this.b(0);
        }
      
        if (!ds.ag && el.I[6] && !this.I && !el.I[4] && !el.I[5]) {
          this.b(2);
        }
      
        if (el.I[5] && !this.I && !el.I[4] && !el.I[6]) {
          el.H[12] = false;
          el.H[13] = false;
          if (this.aa != 3 && this.V > 1) {
            this.aa = 3;
            this.ag = coreLG.ah.a(this.P);
            if (coreLG.ah.b((int)this.ag) && !this.L) {
              this.U = true;
            } else if (this.aM) {
              this.U = coreLG.ah.b((int)this.ag);
            } else {
              this.U = false;
            }
          
            ci.Q.e = false;
            System.out.println("READY SHOOT");
          } else if (!this.U) {
            ++this.V;
            if (this.V >= this.aP) {
              this.j();
              if (coreLG.ah.b((int)this.ag)) {
                this.U = true;
              }
            
              ds.t();
            }
          } else if (!this.aO) {
            ++this.V;
            if (this.V >= this.aP) {
              this.aO = true;
            }
          } else {
            ++this.W;
            if (this.W >= this.aP) {
              this.j();
              this.aO = false;
              ds.t();
            }
          }
        } else if (!this.U) {
          if (this.V > 1 && this.aa == 3) {
            this.aa = 2;
            this.j();
            ds.t();
          } else {
            this.V = 0;
          }
        } else if (this.aa == 3) {
          if (!this.aO) {
            this.aO = true;
          } else if (this.W > 1) {
            this.aa = 2;
            this.j();
            this.aO = false;
            ds.t();
          }
        } else {
          this.V = 0;
          this.W = 0;
        }
      
        if (this.aa == 1 && (!el.I[4] && !el.I[6] || el.I[4] && el.I[6])) {
          cj.d("REALESAS");
          this.aa = 0;
          this.e();
          if (this.am != this.G) {
            cj.d("INPUT x= " + (short)this.G + " y= " + (short)this.H);
            model.ad.a().a((short)this.G, (short)this.H);
            StringBuffer var10000 = new StringBuffer("change location= ");
            int var3 = this.H;
            int var2 = this.G;
            boolean var1 = true;
            int var4 = var3;
            var3 = 0;
          
            boolean var10001;
            while(true) {
              if (var3 >= 60) {
                var10001 = true;
                break;
              }
            
              if (!ci.K.a(var2, var4)) {
                var10001 = false;
                break;
              }
            
              ++var4;
              ++var3;
            }
          
            cj.d(var10000.append(!var10001).toString());
          }
        
          this.am = (short)this.G;
        }
      }
    
    }
  }
  
  private void j() {
    label69: {
      this.ad = false;
      b = true;
      this.c = true;
      this.aY = 1;
      d = true;
      cj.d("GUN= " + this.P);
      this.ag = coreLG.ah.a(this.P);
      if (this.K == 2) {
        this.aY = (byte)(this.aY << 1);
        this.K = -1;
      } else {
        if (this.K == 1) {
          this.K = -1;
          this.a((byte)5, (byte)1);
          break label69;
        }
      
        if (this.K == 8) {
          this.K = -1;
          this.a((byte)4, (byte)1);
          break label69;
        }
      
        if (this.K == 11) {
          this.K = -1;
          this.a((byte)16, (byte)1);
          break label69;
        }
      
        if (this.K == 6) {
          this.K = -1;
          this.a((byte)6, (byte)1);
          break label69;
        }
      
        if (this.K == 7) {
          this.K = -1;
          this.a((byte)7, (byte)1);
          break label69;
        }
      
        if (this.K == 8) {
          this.K = -1;
          this.a((byte)4, (byte)1);
          break label69;
        }
      
        if (this.K == 9) {
          this.K = -1;
          this.a((byte)8, (byte)1);
          break label69;
        }
      
        if (this.K == 14) {
          this.K = -1;
          this.a((byte)14, (byte)1);
          break label69;
        }
      
        if (this.K == 15) {
          this.K = -1;
          this.a((byte)13, (byte)1);
          break label69;
        }
      
        if (this.K == 16) {
          this.K = -1;
          this.a((byte)22, (byte)1);
          break label69;
        }
      
        if (this.K == 17) {
          this.K = -1;
          this.a((byte)26, (byte)1);
          break label69;
        }
      
        if (this.K == 18) {
          this.K = -1;
          this.a((byte)25, (byte)1);
          break label69;
        }
      
        if (this.K == 19) {
          this.K = -1;
          this.a((byte)23, (byte)1);
          break label69;
        }
      
        if (this.K == 20) {
          this.K = -1;
          this.a((byte)28, (byte)1);
          break label69;
        }
      
        if (this.K == 21) {
          this.a((byte)30, (byte)1);
          break label69;
        }
      }
    
      this.a(this.ag, this.aY);
    }
  
    cj.d("EEEE");
    aq[this.P] = ar[this.P];
  }
  
  public final void a(byte paramByte1, short paramShort1, short paramShort2, byte paramByte2, short[][] paramArrayOfshort1, short[][] paramArrayOfshort2, byte paramByte3, byte paramByte4, short paramShort3) {
    this.c = true;
    b = true;
    this.ay = false;
    if (eb.b() && this.aN == ci.X)
      this.ad = false; 
    this.G = this.ao = this.am = paramShort1;
    this.H = this.ap = paramShort2;
    cj.d("ASDASDASD=" + paramArrayOfshort1.length);
    b(this.G, this.H);
    if (paramByte2 != 36)
      if (paramArrayOfshort1[0][0] > paramShort1) {
        this.Z = 2;
      } else {
        this.Z = 0;
      }  
    this.ag = paramByte2;
    this.X = paramShort3;
    e();
    ci.N.a(paramByte1, paramByte2, paramArrayOfshort1, paramArrayOfshort2, paramByte3, paramByte4);
    this.aa = 4;
    if (paramByte2 == 35)
      this.g = true; 
    if (paramByte2 == 43) {
      this.bq = paramArrayOfshort1;
      this.br = paramArrayOfshort2;
      eb.b(paramByte1, (short)(cv.a + 100), paramArrayOfshort2[0][0]);
    } 
    if (paramByte2 == 44)
      dk.aF = paramArrayOfshort1[0][(paramArrayOfshort1[0]).length - 1]; 
    if (paramByte2 == 47)
      try {
        short s1 =  this.bq[2][3];
        paramShort1 = this.br[2][3];
        int i;
        switch (i = model.aa.a(this.G, this.H, s1, paramShort1)) {
          case 3:
            this.ac = 3;
            return;
          case 2:
            this.ac = 2;
            return;
          case 0:
            this.ac = 0;
            return;
          case 1:
            this.ac = 1;
            break;
        } 
      } catch (Exception exception) {}
  }
  
  private void a(byte paramByte1, byte paramByte2) {
    if (ci.S) {
      model.ad.a().a(paramByte1, (short)this.G, (short)this.H, (short)this.X, this.V, this.W, paramByte2);
    } else {
      model.ad.a().b(paramByte1, (short)this.G, (short)this.H, (short)this.X, this.V, this.W, paramByte2);
    } 
    this.M = this.V;
    this.N = this.W;
  }
  
  public final void a(int paramInt1, boolean paramBoolean, int paramInt2) {
    if (!paramBoolean) {
      if (this.L) {
        coreLG.cj.d("ISUSEDITEM");
        return;
      } 
      model.ad.a().f((byte)paramInt1);
      this.J[paramInt2] = -1;
      this.L = true;
      return;
    } 
    switch (paramInt1) {
      case 4:
        this.aM = true;
        this.ah = true;
        break;
      case 3:
        this.aM = true;
        this.ai = true;
        if (!this.ah);
        break;
      case 5:
        this.aM = true;
        break;
      case 0:
      case 2:
        this.aM = true;
        break;
      case 18:
        if (this.Z == 2)
          this.X = 0; 
        if (this.Z == 0)
          this.X = 180; 
        this.aM = false;
        break;
      case 20:
        this.X = 89;
        this.aM = false;
        break;
      case 21:
        this.X = -89;
        this.aM = false;
        break;
      case 10:
        this.aM = true;
        break;
      case 1:
      case 6:
      case 7:
      case 8:
      case 9:
      case 11:
      case 14:
      case 15:
      case 16:
      case 17:
      case 19:
        this.aM = false;
        break;
    } 
    this.K = paramInt1;
    this.aa = 0;
    if (!this.ah);
    if (this.aN == ci.X)
      --coreLG.l.a(paramInt1).d;//doan code nay them vao thay doan code cmt duoi
//      (l.a(paramInt1)).d = (byte)((l.a(paramInt1)).d - 1);
    model.v.a(2);
    this.V = 0;
    this.W = 0;
    this.aO = false;
  }
  
  public void a(Graphics paramGraphics) {
    if (!this.e)
      return; 
    if (!ci.V.equals("") || !this.ah || this.aN == ci.X || this.a == (eb.b[ci.X]).a) {
      this.c(paramGraphics);
      this.d(paramGraphics);
      this.e(paramGraphics);
    } 
    this.b(paramGraphics);
    if (eb.b() && ci.X == this.aN && this.aa != 8 && !(this instanceof dk))
      paramGraphics.drawImage(aG, this.G + this.aS, this.H - this.aT - 11, 3);
  }
  
  private void c(Graphics paramGraphics) {
    int i = this.Z;
    if (this.aa == 8)
      i = this.aV; 
    if (this.P == 13)
      return; 
    if (this.P == 14)
      return; 
    if (this.P == 15)
      return; 
    if (this.P == 17)
      return; 
    if (this.P == 19)
      return; 
    if (this.P == 20)
      return; 
    this.C.a(this.Y, this.G, this.H, i, 33, paramGraphics);
    if (this.aj && el.c % 5 == 0);
  }
  
  private void d(Graphics paramGraphics) {
    if (!ci.V.equals(""))
      return; 
    if (this.P == 13)
      return; 
    if (this.aa != 5 && this.ae > 0) {
      paramGraphics.setColor(16777215);
      paramGraphics.fillRect(this.G - 15, this.H + 5 - 37, 25, 4);
      if (this.aC > 16) {
        paramGraphics.setColor(65280);
      } else if (this.aC > 8) {
        paramGraphics.setColor(16744512);
      } else {
        paramGraphics.setColor(16711680);
      } 
      if (this.aC > 25)
        this.aC = 25; 
      paramGraphics.fillRect(this.G - 15, this.H + 5 - 37, this.aC, 4);
      paramGraphics.setColor(0);
      paramGraphics.drawRect(this.G - 15, this.H + 5 - 37, 25, 4);
    } 
  }
  
  private void e(Graphics paramGraphics) {
    if (!ci.V.equals(""))
      return; 
    if (this instanceof dk)
      return; 
    if (eb.f == this.aN) {
      if (el.c % 10 > 5)
        (this.a ? model.bn.e : model.bn.f).a(paramGraphics, this.an, this.G, this.H - 41, 2);
    } else {
      (this.a ? model.bn.e : model.bn.f).a(paramGraphics, this.an, this.G, this.H - 41, 2);
    } 
    coreLG.bx.a(this.aD, this.G, this.H - 24, 3, paramGraphics);
  }
  
  public final void a(int paramInt, byte paramByte) {
    cj.d("UPDATE HP");
    boolean bool = false;
    this.bs = true;
    this.aC = paramByte;
    if (paramInt < this.ae) {
      this.bt = this.ae - paramInt;
      ci.M.a(this.G, this.H);
    } else {
      this.bt = paramInt - this.ae;
      bool = true;
    } 
    if (this.aX == "") {
      this.aX = String.valueOf(this.aX) + this.bt;
    } else {
      this.aX = String.valueOf(this.aX) + "-" + this.bt;
    } 
    this.bu = 25;
    this.ae = paramInt;
    if (paramInt <= 0)
      k(); 
    if (bool)
      return; 
    ci.Q.b += 2;
    el.P = 0;
    el.O = true;
    if (el.h == el.j)
      synchronized (model.bb.a) {
        try {
          model.bb.a.wait();
        } catch (InterruptedException interruptedException2) {
          interruptedException2.printStackTrace();
        } 
        /* monitor exit ThisExpression{ObjectType{java/lang/Object}} */
        return;
      }  
  }
  
  private void k() {
    this.aa = 5;
    this.ae = 0;
    this.Y = 7;
    if (this.ah)
      this.ah = false; 
    if (this.ai)
      this.ai = false; 
    if (this.i) {
      if (this.P == 19)
        return; 
      if (this.P == 15)
        return; 
      this.I = true;
    } 
    synchronized (model.bb.a) {
      model.bb.a.notify();
      /* monitor exit ThisExpression{ObjectType{java/lang/Object}} */
      return;
    } 
  }
  
  final void b(Graphics paramGraphics) {
    if (this.bs) {
      paramGraphics.setColor(16711680);
      model.bn.d.a(paramGraphics, this.aX, this.G, this.H - this.bu, 3);
      this.bu++;
      if (this.bu > 50) {
        this.bs = false;
        this.aX = "";
      } 
    } 
  }
  
  public final void b(int paramInt) {
    if (this.aa == 0 || this.aa == 2 || this.aa == 8) {
      this.aa = 1;
      this.Y = 4;
    } 
    if (this.aa != 3)
      if (paramInt == 0) {
        if (this.Z != 0)
          this.X = 180 - this.X; 
        this.Z = 0;
      } else if (paramInt == 2) {
        if (this.Z != 2)
          this.X = 180 - this.X; 
        this.Z = 2;
      }  
    if (this.aa == 1) {
      if (!this.R)
        this.R = true; 
      if (cv.d && ci.O.size() == 0)
        cv.a(this.G, this.H, (byte)0); 
      byte b = this.aE;
      if (this.ai)
        b = 2; 
      if (!this.al) {
        if (this.O > 40)
          return; 
        this.O++;
      } 
      this.aU++;
      if (this.aU > 2) {
        this.Y++;
        if (this.Y == 6)
          this.Y = 4;
        this.aU = 0;
      } 
      if (paramInt == 0) {
        this.G -= b;
      } else if (paramInt == 2) {
        this.G += b;
      } 
      this.I = true;
      if (ci.K.a(this.G, this.H - 5)) {
        if (!this.al)
          this.O--; 
        if (paramInt == 0) {
          this.G += b;
        } else if (paramInt == 2) {
          this.G -= b;
        } 
        this.I = false;
        if (this.al && this.G == this.ao && this.H != this.ap) {
          this.H = this.ap;
          return;
        } 
      } else {
        for (paramInt = 4; paramInt >= 0; paramInt--) {
          if (ci.K.a(this.G, this.H - paramInt)) {
            this.H -= paramInt;
            this.ap = this.H;
            this.I = false;
            if (this.al && this.G == this.ao && this.H != this.ap)
              this.H = this.ap; 
            return;
          } 
        } 
      } 
    } 
  }
  
  private void l() {
    if (this.aa == 0 || this.aa == 1)
      this.aa = 2; 
    if (this.aa == 2) {
      this.X += this.aR;
      if (this.aR < 15)
        this.aR += 2; 
      i();
      e();
    } 
  }
  
  private void m() {
    if (this.aa == 0 || this.aa == 1)
      this.aa = 2; 
    if (this.aa == 2) {
      this.X -= this.aR;
      if (this.aR < 15)
        this.aR += 2; 
      i();
      e();
    } 
  }
  
  public final void e() {
    if (this.X < 255 && this.X > 90) {
      this.Z = 0;
      if (this.X <= 195) {
        if (this.X > 165) {
          this.Y = 1;
          return;
        } 
        if (this.X > 115) {
          this.Y = 2;
          return;
        } 
        this.Y = 3;
        return;
      } 
    } else {
      this.Z = 2;
      if (this.X > 65) {
        this.Y = 3;
        return;
      } 
      if (this.X > 15) {
        this.Y = 2;
        return;
      } 
      if (this.X > -15) {
        this.Y = 1;
        return;
      } 
    } 
    this.Y = 0;
  }
  
  public final void c(int paramInt) {
    if (this.aa == 8)
      return; 
    if (this.aa == 5)
      return; 
    this.aa = 8;
    this.aU = 0;
    this.Y = 6;
    this.aV = paramInt;
  }
  
  public final void f() {
    this.Y = 8;
    this.aa = 7;
  }
  
  public static void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Graphics paramGraphics) {
    Graphics graphics;
    int i;
    int j;
    int k = (paramInt1 == 3) ? 32 : 24;
    if (paramInt1 == 7)
      k = 32; 
    if (paramInt1 == 10)
      k = 28; 
    if (paramInt1 == 12) {
      k = 30;
      paramInt2 = 0;
    } 
    if (paramInt1 == 11) {
      paramInt3 = paramInt4;
      paramInt2 = paramInt3;
      graphics = paramGraphics;
      if (++bw == 10)
        bw = 0; 
      if (bw == 5)
        bv = 1; 
      if (bw == 0)
        bv = 0; 
      paramInt4 = paramInt2 + 4;
      paramInt5 = paramInt3 - 10 + bv;
      j = paramInt2 - 15;
      k = paramInt3 - 15 + bv;
      graphics.drawRegion(as, 0, 0, 42, 30, 2, paramInt4, paramInt5, 33);
      graphics.drawRegion(at, 0, 0, 44, 25, 2, paramInt2, paramInt3, 33);
      graphics.drawRegion(be, 0, 15, 15, 15, 0, j, k, 33);
      return;
    } 
    if (paramInt1 == 13) {
      paramInt3 = paramInt4;
      paramInt2 = paramInt3;
      if (++bw == 10)
        bw = 0; 
      if (bw == 5)
        bv = 1; 
      if (bw == 0)
        bv = 0; 
      paramGraphics.drawImage(l, paramInt2, paramInt3, 33);
      bx = -20;
      paramGraphics.drawRegion(aJ, 0, 0, 35, 40, 0, paramInt2 + bx, paramInt3 - 22 + bv, 3);
      paramGraphics.drawImage(m, paramInt2 - 1, paramInt3 - 10 + bv, 33);
      paramGraphics.drawRegion(aJ, 0, 0, 35, 40, 0, paramInt2, paramInt3 - 20 + bv, 3);
      return;
    } 
    if (paramInt1 == 14) {
      paramInt3 = paramInt4;
      paramInt2 = paramInt3;
      paramGraphics.drawRegion(y, 0, 0, 51, 46, 0, paramInt2, paramInt3, 33);
      if (el.c % 3 == 0) {
        paramGraphics.drawRegion(z, 0, 0, 16, 11, 0, paramInt2, paramInt3 + -5, 17);
        return;
      }
      paramGraphics.drawRegion(z, 0, 11, 16, 11, 0, paramInt2, paramInt3 - 5, 17);
      return;
    } 
    if (paramInt1 == 15) {
      paramInt3 = paramInt4;
      paramInt2 = paramInt3;
      if (++by == 20)
        by = 0; 
      if (by <= 10) {
        bz = -1;
      } else {
        bz = 0;
      }
      paramGraphics.drawImage(aL, paramInt2, paramInt3 + bz, 33);
      return;
    }
    paramGraphics.drawRegion(aH[paramInt1], 0, paramInt2 * k, aH[paramInt1].getWidth(), k, paramInt5, paramInt3, paramInt4, 33);
  }
  
  public final void b(int paramInt1, int paramInt2) {
    if (this.aN == ci.X) {
      this.ba = paramInt1;
      this.bb = paramInt2;
    } 
  }
  
  public final void g() {
    if (this.G != this.ba)
      this.G = this.ba; 
    if (this.H != this.bb)
      this.H = this.bb; 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\model.bl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */