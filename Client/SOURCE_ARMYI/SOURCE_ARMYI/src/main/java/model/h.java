package model;

import coreLG.el;

import javax.microedition.lcdui.Graphics;

public final class h {
  private int a;
  
  private int b;
  
  private int c;
  
  private int d;
  
  private int e;
  
  private int f;
  
  private int g;
  
  private int h;
  
  private int i;
  
  private int j;
  
  private int k;
  
  private int l;
  
  private int m;
  
  private int n;
  
  private String[] o;
  
  private String[] p;
  
  private String[] q;
  
  private String[] r;
  
  private String[] s;
  
  private byte[] t;
  
  private int u = -1;
  
  private boolean v = false;
  
  private boolean w = false;
  
  private long x;
  
  public h() {
    if (el.e >= el.d) {
      el.t = false;
      this.d = el.e / 6 << 1;
      el.e -= this.d;
      this.a = 0;
      this.b = el.e;
      this.c = el.d;
      this.e = this.c / 4;
      this.f = this.d / 2;
      this.g = this.a;
      this.h = this.b;
      this.i = this.d / 3;
      this.j = this.c / 4;
      this.k = 4;
      this.l = 2;
      this.m = 4;
      this.n = 3;
      this.o = new String[] { "-", "Up", "ABC", "-", "$", "Down", "#", "OK" };
      this.p = new String[] { 
          ".,?!1", "abc2", "def3", model.u.cm(), "ghi4", "jkl5", "mno6", model.u.e(), "pqrs7", "tuv8",
          "wxyz9", "0" };
      this.q = new String[] { 
          ".,?!1", "ABC2", "DEF3", model.u.cm(), "GHI4", "JKL5", "MNO6", model.u.e(), "PQRS7", "TUV8",
          "WXYZ9", "0" };
      this.r = new String[] { 
          "1", "2", "3", model.u.cm(), "4", "5", "6", model.u.e(), "7", "8",
          "9", "0" };
      this.t = new byte[] { -6, -1, -7, -3, -2, -4, -5 };
    } else {
      el.t = true;
      this.c = el.d / 6 << 1;
      el.d -= this.c;
      this.b = 0;
      this.d = el.e;
      this.a = el.d;
      this.e = this.c / 2;
      this.f = this.d / 4;
      this.g = this.a;
      this.h = this.b;
      this.i = this.d / 4;
      this.j = this.c / 3;
      this.k = 2;
      this.l = 4;
      this.m = 3;
      this.n = 4;
      this.o = new String[] { "-", "OK", "ABC", "Top", "~", "Ă", "-", "Down" };
      this.p = new String[] { 
          ".,?!1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", model.u.e(),
          "0", model.u.cm() };
      this.q = new String[] { 
          ".,?!1", "ABC2", "DEF3", "GHI4", "JKL5", "MNO6", "PQRS7", "TUV8", "WXYZ9", model.u.e(),
          "0", model.u.cm() };
      this.r = new String[] { 
          "1", "2", "3", "4", "5", "6", "7", "8", "9", model.u.e(),
          "0", model.u.cm() };
      this.t = new byte[] { -7, -5, -1, -3, -4, -6, -2 };
    } 
    this.x = -1L;
    b();
  }
  
  public final void a() {
    if (this.v) {
      if (el.a(this.g, this.h, this.c, this.d)) {
        if (el.L) {
          int i = (el.J - this.g) / this.j;
          int j = (el.K - this.h) / this.i;
          this.u = j * this.m + i;
          j = this.u;
          h h1 = this;
          if (el.t && j < 9) {
            el.a.keyPressed(j + 49);
          } else if (!el.t && j % 4 != 3) {
            el.a.keyPressed(j + 49 - j / 4);
          } else {
            System.out.println("index: " + j);
            switch (j) {
              case 10:
                el.a.keyPressed(48);
                break;
              case 7:
              case 9:
                h1.v = false;
                break;
              case 3:
                if (el.p.u.e) {
                  el.p.u.h.b.a();
                  break;
                } 
                if (el.h.ae != null)
                  el.h.ae.b.a(); 
                break;
              case 11:
                if (el.t) {
                  if (el.p.u.e) {
                    el.p.u.h.b.a();
                    break;
                  } 
                  if (el.h.ae != null)
                    el.h.ae.b.a(); 
                  break;
                } 
                el.a.keyPressed(48);
                break;
            } 
          } 
          el.L = false;
        } 
        if (el.M && this.u != -1) {
          this.u = -1;
          el.M = false;
        } 
      } 
      return;
    } 
    if (this.w && el.M) {
      this.w = false;
      if (System.currentTimeMillis() / 10L - this.x > 40L) {
        b();
      } else {
        this.u = -1;
        this.v = true;
      } 
    } 
    if (el.a(this.a, this.b, this.c, this.d)) {
      if (el.L) {
        int i = (el.J - this.a) / this.e;
        int j = (el.K - this.b) / this.f;
        this.u = j * this.k + i;
        j = this.u;
        h h1 = this;
        if (j == 2) {
          h1.x = System.currentTimeMillis() / 10L;
          h1.w = true;
        } else {
          el.a.keyPressed(h1.t[j]);
        } 
        el.L = false;
      } 
      if (el.M && this.u != -1) {
        int i = this.u;
        h h1 = this;
        if (i != 2 && i < h1.t.length)
          el.a.keyReleased(h1.t[i]); 
        this.u = -1;
        el.M = false;
      } 
    } 
  }
  
  private void b() {
    switch (bc.f) {
      case 0:
      case 1:
        this.s = this.p;
        return;
      case 2:
        this.s = this.q;
        return;
      case 3:
        this.s = this.r;
        break;
    } 
  }
  
  public final void a(Graphics paramGraphics) {
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
    if (this.v) {
      paramGraphics = paramGraphics;
      paramGraphics.setClip(this.g, this.h, this.c, this.d);
      paramGraphics.setColor(15070459);
      paramGraphics.fillRect(this.g, this.h, this.c, this.d);
      paramGraphics.setColor(1);
      paramGraphics.drawRect(this.g, this.h, this.c - 1, this.d - 1);
      byte b1;
      for (b1 = 1; b1 < this.m; b1++)
        paramGraphics.fillRect(this.g + b1 * this.j, this.h, 1, this.d); 
      for (b1 = 1; b1 < this.n; b1++)
        paramGraphics.fillRect(this.g, this.h + b1 * this.i, this.c, 1); 
      for (b1 = 0; b1 < this.q.length; b1++) {
        paramGraphics.setClip(this.g + b1 % this.m * this.j, this.h + b1 / this.m * this.i - 5, this.j, this.i + 5);
        if (this.u == b1) {
          paramGraphics.setColor(16514186);
          paramGraphics.fillRect(this.g + b1 % this.m * this.j + 1, this.h + b1 / this.m * this.i + 1, this.j - 2, this.i - 2);
        } 
        bn.c.a(paramGraphics, this.s[b1], this.g + b1 % this.m * this.j + this.j / 2, this.h + b1 / this.m * this.i - 5 + this.i / 2, 2);
      } 
      return;
    } 
    paramGraphics = paramGraphics;
    paramGraphics.setClip(this.a, this.b, this.c, this.d);
    paramGraphics.setColor(15070459);
    paramGraphics.fillRect(this.g, this.h, this.c, this.d);
    paramGraphics.setColor(1);
    paramGraphics.drawRect(this.a, this.b, this.c - 1, this.d - 1);
    byte b;
    for (b = 1; b < this.k; b++)
      paramGraphics.fillRect(this.a + b * this.e, this.b, 1, this.d); 
    for (b = 1; b < this.l; b++)
      paramGraphics.fillRect(this.a, this.b + b * this.f, this.c, 1); 
    for (b = 0; b < this.o.length; b++) {
      if (this.u == b) {
        paramGraphics.setColor(16514186);
        paramGraphics.fillRect(this.a + b % this.k * this.e + 1, this.b + b / this.k * this.f + 1, this.e - 2, this.f - 2);
      } 
      if (this.o[b].equals("ABC")) {
        bn.c.a(paramGraphics, bc.g[bc.f], this.a + b % this.k * this.e + this.e / 2, this.b + b / this.k * this.f - 5 + this.f / 2, 2);
      } else if (this.o[b].equals("Up")) {
        paramGraphics.drawRegion(bn.c.a, 0, 300, 11, 15, 5, this.a + b % this.k * this.e + this.e / 2 - 5, this.b + b / this.k * this.f - 5 + this.f / 2, 20);
      } else if (this.o[b].equals("Down")) {
        paramGraphics.drawRegion(bn.c.a, 0, 300, 11, 15, 6, this.a + b % this.k * this.e + this.e / 2 - 7, this.b + b / this.k * this.f - 1 + this.f / 2, 20);
      } else {
        bn.c.a(paramGraphics, this.o[b], this.a + b % this.k * this.e + this.e / 2, this.b + b / this.k * this.f - 5 + this.f / 2, 2);
      } 
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.h.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */