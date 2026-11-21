package coreLG;

import java.util.Random;
import javax.microedition.lcdui.Graphics;

public final class dk extends bl {
  private int[] aH = new int[6];
  
  private int[] aI = new int[6];
  
  private boolean aJ = false;
  
  private int aK;
  
  private static int aL;
  
  public static int aF;
  
  private int aM;
  
  private int aN;
  
  private int aO;
  
  private int aP;
  
  private int aQ = 8;
  
  private boolean aR;
  
  private int aS;
  
  private int aT;
  
  private int aU = 0;
  
  private int aV = 0;
  
  private int aW = 0;
  
  private int aX;
  
  private int aY;
  
  private int aZ;
  
  private Random ba = new Random();
  
  private int bb;
  
  public static int aG;
  
  public dk(int paramInt1, byte paramByte1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, byte paramByte2) {
    super(paramInt1, paramByte1, paramInt2, paramInt3, paramBoolean, paramInt4, paramByte2);
    paramInt1 = 6;
    paramInt1 = 5;
    int var1000 = 5 + this.ba.nextInt(1);
    this.bb = 18;
    if (this.P == 9);
    if (this.P == 11 || this.P == 12)
      this.D = true;
    if (this.P == 14 || this.P == 15 || this.P == 17 || this.P == 16 || this.P == 19 || this.P == 18 || this.P == 20)
      this.i = true;
    if (this.P == 20)
      for (byte b = 0; b < 6; b++) {
        if (b == 0) {
          this.aH[b] = cj.b(20, el.d / 3);
          this.aI[b] = cj.b(this.H - 44, this.H);
        } else {
          this.aH[b] = this.aH[b - 1] + 25 + cj.b(0, cv.a / 3 - 20);
          this.aI[b] = cj.b(this.H - 44, this.H);
        }
      }
  }
  
  public final void d() {
    super.d();
  }
  
  private void a(Graphics paramGraphics, int paramInt) {
    if (this.aa != 5 && this.ae > 0) {
      paramGraphics.setColor(16777215);
      paramGraphics.fillRect(this.G - 15 + this.aM, this.H + 5 - paramInt + this.aN, 25, 4);
      if (this.aC > 16) {
        paramGraphics.setColor(65280);
      } else if (this.aC > 8) {
        paramGraphics.setColor(16744512);
      } else {
        paramGraphics.setColor(16711680);
      }
      if (this.aC > 25)
        this.aC = 25;
      paramGraphics.fillRect(this.G - 15 + this.aM, this.H + 5 - paramInt + this.aN, this.aC, 4);
      paramGraphics.setColor(0);
      paramGraphics.drawRect(this.G - 15 + this.aM, this.H + 5 - paramInt + this.aN, 25, 4);
    }
  }
  
  public void a(Graphics paramGraphics) {
    if (!this.e)
      return;
    if (this.P == 11) {
      byte b;
      if (this.Z == 2) {
        b = 0;
        if (this.Y == 1) {
          this.aU = 15;
          this.aV = 0;
        }
        if (this.Y == 2) {
          this.aU = 15;
          this.aV = -5;
        }
        if (this.Y == 3) {
          this.aU = 7;
          this.aV = -12;
        }
        this.aW = 4;
      } else {
        b = 2;
        if (this.Y == 1) {
          this.aU = -15;
          this.aV = 0;
        }
        if (this.Y == 2) {
          this.aU = -15;
          this.aV = -5;
        }
        if (this.Y == 3) {
          this.aU = -7;
          this.aV = -12;
        }
        this.aW = -4;
      }
      if (this.Z == 0)
        this.aA = -this.aA;
      int i = this.G - this.aW + ((this.Y == 1 || this.Y == 2) ? this.aA : 0);
      int j = this.H - 10 + this.aS + ((this.Y == 2 || this.Y == 3) ? this.aB : 0);
      int k = this.G + this.aU + ((this.Y == 1 || this.Y == 2) ? this.aA : 0);
      int m = this.H - 15 + this.aS + this.aV + ((this.Y == 2 || this.Y == 3) ? this.aB : 0);
      paramGraphics.drawRegion(bl.as, 0, 30 * this.E, 42, 30, b, i, j, 33);
      paramGraphics.drawRegion(bl.at, 0, 25 * this.F, 44, 25, b, this.G, this.H, 33);
      if (this.Y == 0 || this.Y == 6)
        this.Y = 1;
      this.C.a(this.Y, k, m, this.Z, 33, paramGraphics);
      a(paramGraphics, 50);
      b(paramGraphics);
      return;
    }
    if (this.P == 13) {
      if (this.aa == 5) {
        paramGraphics.drawImage(bl.n, this.G, this.H, 33);
      } else {
        if (this.Y > 3)
          this.Y = 1;
        paramGraphics.drawImage(bl.l, this.G, this.H, 33);
        int b = (this.Y != 3) ? 3 : 33;
        if (this.Y == 2 || this.Y == 3) {
          this.aU = -15;
        } else {
          this.aU = -20;
        }
        this.C.a(this.Y, this.G + this.aU, this.H - 22 + this.aS, 0, b, paramGraphics);
        paramGraphics.drawImage(bl.m, this.G - 1, this.H - 10 + this.aS, 33);
        this.C.a(this.Y, this.G, this.H - 20 + this.aS, 0, b, paramGraphics);
        if (this.g) {
          this.aY++;
          if (this.aY == 10)
            this.aY = 0;
          if (this.aY <= 5) {
            this.aX = 0;
          } else {
            this.aX = 1;
          }
          paramGraphics.drawRegion(bl.x, 0, 10 * this.aX, 5, 10, 0, this.G - 10, this.H, 17);
          paramGraphics.drawRegion(bl.x, 0, 10 * this.aX, 5, 10, 0, this.G + 10, this.H, 17);
          paramGraphics.drawRegion(bl.x, 0, 10 * this.aX, 5, 10, 0, this.G - 15, this.H, 17);
          paramGraphics.drawRegion(bl.x, 0, 10 * this.aX, 5, 10, 0, this.G + 15, this.H, 17);
        }
      }
      a(paramGraphics, 60);
      b(paramGraphics);
      return;
    }
    if (this.P == 14) {
      if (this.aa == 8) {
        paramGraphics.drawRegion(bl.y, 0, 46, 51, 46, 0, this.G + this.aM, this.H + this.aN, 33);
      } else if (this.aa == 5) {
        paramGraphics.drawRegion(bl.y, 0, 92, 51, 46, 0, this.G + this.aM, this.H + this.aN, 33);
      } else {
        paramGraphics.drawRegion(bl.y, 0, 0, 51, 46, 0, this.G + this.aM, this.H + this.aN, 33);
      }
      if (this.aa != 5) {
        if (el.c % 3 == 0) {
          paramGraphics.drawRegion(bl.z, 0, 0, 16, 11, 0, this.G + this.aM, this.H + this.aN - 5, 17);
        } else {
          paramGraphics.drawRegion(bl.z, 0, 11, 16, 11, 0, this.G + this.aM, this.H + this.aN - 5, 17);
        }
        if (this.ay) {
          paramGraphics.setColor(149905);
          for (byte b = 0; b < this.aQ; b++) {
            if (b < this.aQ / 2 + this.aQ / 3 && b > this.aQ / 2 - this.aQ / 3) {
              paramGraphics.setColor(16777215);
            } else {
              paramGraphics.setColor(5880763);
            }
            paramGraphics.drawLine(this.G - this.aQ / 2 + b + this.aM, this.H + this.aN, this.G - this.aQ / 2 + b + this.aM, this.az);
          }
          if (this.aQ == 0)
            this.aR = true;
          if (this.aQ == 8)
            this.aR = false;
          if (el.c % 2 == 0)
            if (!this.aR) {
              this.aQ--;
            } else {
              this.aQ++;
            }
        }
      }
      a(paramGraphics, 55);
      b(paramGraphics);
      return;
    }
    if (this.P == 15) {
      paramGraphics.drawImage(bl.o, this.G, this.H + aL, 33);
      paramGraphics.drawRegion(bl.r, 0, this.aK * 23, 4, 23, 0, this.G - 15, this.H - 15 + aL, 33);
      if (this.aK == 0) {
        this.aK = 1;
      } else if (this.aK == 1) {
        this.aK = 0;
      }
      if (el.c % 2 == 0) {
        paramGraphics.drawImage(bl.s, this.G + 5, this.H - 27 + aL, 33);
        paramGraphics.drawImage(bl.s, this.G + 37, this.H - 27 + aL, 33);
      }
      paramGraphics.drawImage(bl.v, this.G - 5, this.H - 43 + aL, 33);
      paramGraphics.drawRegion(bl.r, 0, this.aK * 23, 4, 23, 0, this.G, this.H - 46 + aL, 33);
      int[] arrayOfInt1 = { this.G, this.G - 30, this.G + 20 };
      int[] arrayOfInt2 = { this.H - 20, this.H - 35, this.H - 25 };
      if (this.aa == 5)
        for (byte b = 0; b < 3; b++)
          paramGraphics.drawRegion(bl.w, 0, b * 12, 14, 12, 0, arrayOfInt1[b], arrayOfInt2[b] + aL, 0);
      return;
    }
    if (this.P == 16) {
      if (aF <= this.G + 50 && aF >= this.G - 50) {
        this.Y = 1;
      } else if (aF > this.G + 50) {
        this.Y = 0;
        this.Z = 0;
      } else if (aF < this.G - 50) {
        this.Y = 0;
        this.Z = 2;
      }
      if (this.Y > 1)
        this.Y = 0;
      int b = (this.aa == 5) ? 0 : aL;
      paramGraphics.drawImage(bl.p, this.G, this.H - 7 + b, 33);
      this.C.a(this.Y, this.G + 2, this.H + b - 7, this.Z, 3, paramGraphics);
      a(paramGraphics, -7);
      b(paramGraphics);
      return;
    }
    if (this.P == 19) {
      if (this.aa == 5) {
        this.C.a(2, this.G, this.H + aL, 0, 33, paramGraphics);
      } else if (el.c % 2 == 0) {
        this.C.a(0, this.G, this.H + aL, 0, 33, paramGraphics);
      } else {
        this.C.a(1, this.G, this.H + aL, 0, 33, paramGraphics);
      }
      a(paramGraphics, 15);
      b(paramGraphics);
      return;
    }
    if (this.P == 17) {
      int b = (this.aa == 5) ? 0 : aL;
      paramGraphics.drawImage(bl.t, this.G, this.H + b, 33);
      a(paramGraphics, -7);
      b(paramGraphics);
      return;
    }
    if (this.P == 18) {
      int b = (this.aa == 5) ? 0 : aL;
      paramGraphics.drawImage(bl.u, this.G, this.H + b, 33);
      a(paramGraphics, 30);
      b(paramGraphics);
      return;
    }
    if (this.P == 20) {
      paramGraphics.setColor(8026746);
      byte b;
      for (b = 0; b < 3; b++)
        paramGraphics.drawLine(0, this.aw + b * 22 - 44, cv.a, this.aw + b * 22 - 44);
      for (b = 0; b < 3; b++)
        paramGraphics.drawImage(bl.B, this.aH[b], this.aI[b], 3);
      if (this.ac == 0) {
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 1, this.G + 1, this.H, 33);
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 0, this.G, this.H - 22, 33);
      }
      if (this.ac == 1) {
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 3, this.G, this.H, 33);
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 2, this.G, this.H - 22, 33);
      }
      if (this.ac == 2) {
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 4, this.G, this.H, 40);
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 5, this.G + 22, this.H, 40);
      }
      if (this.ac == 3) {
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 6, this.G, this.H, 40);
        paramGraphics.drawRegion(bl.A, 0, this.ab * 22, 41, 22, 7, this.G + 22, this.H, 40);
      }
      if (this.au) {
        paramGraphics.setColor(5395026);
        paramGraphics.fillRect(this.av, this.aw - 41, 1, this.H - this.aw);
      }
      a(paramGraphics, 48);
      b(paramGraphics);
      return;
    }
    if (this.P == 12 && (this.f || this.g)) {
      this.aY++;
      if (this.aY == 10)
        this.aY = 0;
      if (this.aY <= 5) {
        this.aX = 0;
      } else {
        this.aX = 1;
      }
      paramGraphics.drawRegion(bl.x, 0, 10 * this.aX, 5, 10, 0, this.G - 5, this.H + 2, 17);
      paramGraphics.drawRegion(bl.x, 0, 10 * this.aX, 5, 10, 0, this.G + 5, this.H + 2, 17);
    }
    super.a(paramGraphics);
  }
  
  private void h() {
    this.aZ++;
    if (this.aZ == 10)
      this.aZ = 0; 
    if (this.aZ < 5) {
      this.Y = 1;
      return;
    } 
    this.Y = 2;
  }
  
  public void c() {
    super.c();
    int var1;
    switch(super.P) {
      case 9:
      case 10:
        if (super.aa == 5) {
          super.Y = 3;
          return;
        }
      
        if (super.al && !super.D) {
          if (super.ao < super.G && !super.I) {
            this.b(0);
            this.h();
            return;
          }
        
          if (super.ao > super.G && !super.I) {
            this.b(2);
            this.h();
            return;
          }
        
          if (super.ao == super.G && super.ap != super.H && super.aa == 0 && !super.I) {
            super.H = super.ap;
          }
        }
      
        if (super.c) {
          if (super.P == 10) {
            this.aJ = true;
          }
        } else {
          this.aJ = false;
        }
      
        if (this.aJ) {
          super.Y = 4;
        } else {
          ++this.aZ;
          if (this.aZ == 10) {
            this.aZ = 0;
          }
        
          if (this.aZ >= 5) {
            super.Y = 1;
            return;
          }
        
          super.Y = 0;
        }
        break;
      case 11:
        if (super.aa == 5) {
          super.F = 2;
          super.E = 1;
          super.Y = 0;
          return;
        }
      
        if (super.aA < 0) {
          super.aA += 2;
        }
      
        if (super.aA > 0) {
          super.aA = 0;
        }
      
        if (super.aB > 0) {
          --super.aB;
        }
      
        if (super.aB < 0) {
          super.aB = 0;
        }
      
        if (super.aa != 4) {
          ++this.aT;
          if (this.aT == 10) {
            this.aT = 0;
          }
        
          if (this.aT == 5) {
            this.aS = 1;
          }
        
          if (this.aT == 0) {
            this.aS = 0;
          }
        }
      
        if (super.aa == 4) {
          this.aT = 0;
          this.aS = 0;
        }
      
        if (super.F != 0) {
          if (super.G < super.k) {
            super.Z = 2;
            return;
          }
        
          super.Z = 0;
          return;
        }
        break;
      case 12:
        if (super.g) {
          super.H -= this.bb;
          --this.bb;
          if (this.bb == 0) {
            super.g = false;
            this.bb = 18;
            super.I = true;
            return;
          }
        }
        break;
      case 13:
        if (super.aa != 4) {
          ++this.aT;
          if (this.aT == 10) {
            this.aT = 0;
          }
        
          if (this.aT == 5) {
            this.aS = 1;
          }
        
          if (this.aT == 0) {
            this.aS = 0;
          }
        }
      
        if (super.g) {
          super.H -= this.bb;
          --this.bb;
          if (this.bb == 0) {
            super.I = true;
          }
        }
      
        if (super.g && ci.K.a(super.G, super.H)) {
          super.j = true;
          this.bb = 18;
          super.g = false;
        }
      
        if (super.j) {
          super.j = false;
        
          for(var1 = 0; var1 < 4; ++var1) {
            ci.P.a(super.G - var1 * 20, super.H, cj.d(4 + super.V / 3), cj.b(-8 - super.V / 3, -5 - super.V / 3), (byte)3);
            ci.P.a(super.G + var1 * 20, super.H, cj.d(4 + super.V / 3), cj.b(-8 - super.V / 3, -5 - super.V / 3), (byte)3);
          }
        
          coreLG.m.b = 3;
          return;
        }
        break;
      case 14:
        if (super.ax) {
          this.a((int)10);
          return;
        }
      
        if (super.aa != 4 && super.aa != 5) {
          if (el.c % 2 == 0) {
            ++this.aO;
            if (this.aO == 20) {
              this.aO = 0;
            }
          
            if (this.aO < 10) {
              ++this.aM;
            } else {
              --this.aM;
            }
          
            ++this.aP;
            if (this.aP == 10) {
              this.aP = 0;
            }
          
            if (this.aP < 5) {
              --this.aN;
              return;
            }
          
            ++this.aN;
            return;
          }
          break;
        }
      
        this.aM = 0;
        this.aN = 0;
        return;
      case 15:
        if (super.aa == 5) {
          ++super.H;
          super.I = false;
          if (coreLG.el.c % 5 == 0) {
            new model.bl(cj.b(super.G - 50, super.G + 50), coreLG.cj.b(super.H - 40, super.H), (byte)0);
          }
        }
      
        if (super.ax) {
          this.a((int)10);
          aL = 0;
          if (super.ag == 43) {
            aG += 2;
            ci.M.a(super.G, super.H + aG);
          }
        } else {
          aG = 0;
          aL = eb.j;
        }
      
        for(var1 = 0; var1 < eb.b.length; ++var1) {
          if (eb.b[var1] != null) {
            if (eb.b[var1].aa != 5) {
              if (eb.b[var1].P == 16) {
                eb.b[var1].G = super.G + 51;
                eb.b[var1].H = super.H + 10;
              }
            
              if (eb.b[var1].P == 17) {
                eb.b[var1].G = super.G - 10;
                eb.b[var1].H = super.H + 30;
              }
            
              if (eb.b[var1].P == 18) {
                eb.b[var1].G = super.G - 60;
                eb.b[var1].H = super.H - 13;
              }
            }
          
            if (eb.b[var1].P == 19) {
              eb.b[var1].G = super.G + 53;
              eb.b[var1].H = super.H - 24;
            }
          }
        }
      
        return;
      case 16:
      case 17:
      case 18:
      case 19:
      default:
        break;
      case 20:
        label235: {
          super.aE = 5;
          if (super.ax) {
            ++this.aZ;
            if (this.aZ == 4) {
              this.aZ = 0;
            }
          
            if (this.aZ >= 2) {
              super.ab = 1;
              break label235;
            }
          } else {
            if (super.aa == 5) {
              super.ab = 3;
              break label235;
            }
          
            if (super.aa == 8) {
              super.ab = 2;
              break label235;
            }
          }
        
          super.ab = 0;
        }
      
        if (super.ax) {
          this.a((int)8);
        }
      
        if (super.au) {
          this.b();
        }
    }
  
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\dk.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */