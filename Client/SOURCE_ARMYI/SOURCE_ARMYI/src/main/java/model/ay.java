package model;

import coreLG.ci;
import coreLG.cv;
import coreLG.el;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class ay {
  private int[] g;
  
  private int[] h;
  
  private int[] i;
  
  private int[] j;
  
  private static Image k;
  
  private int[] l;
  
  private int m;
  
  private int n;
  
  public int a = 0;
  
  private boolean[] o;
  
  public int b = 99;
  
  public int c = 100;
  
  public int d = 15;
  
  public int e = 20;
  
  public int f = 7;
  
  public final void a(int paramInt) {
    this.n = paramInt;
    if (this.n == 0)
      this.m = el.a(150, 200); 
    if (this.n == 1)
      this.m = el.a(this.b, this.c); 
    if (k == null)
      try {
        k = Image.createImage("/tuyet.png");
      } catch (IOException iOException2) {
        IOException iOException1;
        (iOException1 = null).printStackTrace();
      }  
    this.g = new int[this.m];
    this.h = new int[this.m];
    this.i = new int[this.m];
    this.j = new int[this.m];
    this.l = new int[this.m];
    this.o = new boolean[this.m];
    for (paramInt = 0; paramInt < this.m; paramInt++) {
      this.g[paramInt] = el.a(-10, cv.a);
      this.h[paramInt] = el.a(-100, cv.b - this.a);
      this.i[paramInt] = 0;
      if (this.n == 0)
        this.j[paramInt] = el.a(1, 3); 
      if (this.n == 1)
        this.j[paramInt] = el.a(this.d, this.e); 
      this.l[paramInt] = el.a(1, 3);
      if (this.l[paramInt] == 2 && paramInt % 2 == 0)
        this.o[paramInt] = true; 
    } 
  }
  
  public final void a() {
    for (byte b = 0; b < this.m; b++) {
      this.h[b] = this.h[b] + this.j[b];
      if (this.n == 0)
        this.i[b] = ci.R >> 4; 
      if (this.n == 1)
        this.i[b] = (ci.R == 0) ? 1 : ((ci.R > 0) ? this.f : -this.f); 
      this.g[b] = this.g[b] + this.i[b];
      if (this.h[b] < -200 || this.h[b] > cv.b - this.a || this.g[b] > cv.a || this.g[b] < -10) {
        if (this.h[b] > cv.b - this.a);
        this.g[b] = el.a(-10, cv.a);
        this.h[b] = el.a(-100, cv.b - this.a);
      } 
    } 
  }
  
  public final void a(Graphics paramGraphics) {
    for (byte b = 0; b < this.m; b++) {
      if (this.l[b] == 2) {
        if (this.n == 0)
          paramGraphics.drawImage(k, this.g[b], this.h[b], 0); 
        if (this.n == 1) {
          paramGraphics.setColor(15987699);
          int i = Math.abs(ci.R);
          int j = ci.R;
          if (i == 0)
            paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + 1, this.h[b] + 4); 
          if (i > 0) {
            paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + ((j > 0) ? 4 : -4), this.h[b] + 4);
            paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + ((j > 0) ? 3 : -3), this.h[b] + 4);
          } 
        } 
      } 
    } 
  }
  
  public final void b(Graphics paramGraphics) {
    for (byte b = 0; b < this.m; b++) {
      if (this.n == 1) {
        paramGraphics.setColor(10921638);
        int i = Math.abs(ci.R);
        int j = ci.R;
        if (i == 0)
          paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + 1, this.h[b] + 2); 
        if (i > 0)
          paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + ((j > 0) ? 2 : -2), this.h[b] + 2); 
      } 
    } 
  }
  
  public final void c(Graphics paramGraphics) {
    paramGraphics.setColor(10742731);
    for (byte b = 0; b < this.m; b++) {
      if (this.l[b] != 2) {
        if (this.n == 0)
          paramGraphics.fillRect(this.g[b], this.h[b], 2, 2); 
        if (this.n == 1) {
          paramGraphics.setColor(11908790);
          int i = Math.abs(ci.R);
          int j = ci.R;
          if (i == 0)
            paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + 1, this.h[b] + 2); 
          if (i > 0)
            paramGraphics.drawLine(this.g[b], this.h[b], this.g[b] + ((j > 0) ? 2 : -2), this.h[b] + 2); 
        } 
      } 
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ay.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */