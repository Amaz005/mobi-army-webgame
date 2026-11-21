package model;

import coreLG.ds;
import coreLG.el;
import model.as;
import model.bc;

import javax.microedition.lcdui.Graphics;

public final class bk extends q {
  private String[] e;
  
  public bc d = new bc();
  
  private as f;
  
  private as g;
  
  public bk() {
    this.d.a = 16;
    this.d.b = el.e - ds.ab - 44;
    this.d.c = el.d - 32;
    this.d.d = ds.ab + 2;
    this.d.e = true;
    this.c = this.d.h;
  }
  
  public final void a(String paramString, as paramas1, as paramas2, int paramInt) {
    this.d.a("");
    this.d.b(paramInt);
    this.e = bn.b.a(paramString, el.d - 40);
    this.f = paramas1;
    this.g = paramas2;
    this.a = new z(u.cI(), this.g);
    this.b = new z("OK", this.f);
  }
  
  public final void a(Graphics paramGraphics) {
    ds.a(8, el.e - 102, el.d - 16, 69, paramGraphics);
    int i = el.e - 35 - 50 - (this.e.length >> 1) * bn.b.a();
    byte b = 0;
    for (i = i; b < this.e.length; i += bn.b.a()) {
      bn.b.a(paramGraphics, this.e[b], el.g, i, 2);
      b++;
    } 
    this.d.a(paramGraphics);
    super.a(paramGraphics);
  }
  
  public final void a(int paramInt) {
    this.d.a(paramInt);
    super.a(paramInt);
  }
  
  public final void a() {
    this.d.b();
    super.a();
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bk.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */