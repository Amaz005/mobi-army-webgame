package model;

import coreLG.ci;
import coreLG.el;
import model.bm;
import model.bn;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class w extends bm {
  public static Image[] a;
  
  public int b;
  
  public w() {
    if (a == null)
      a = new Image[2]; 
    a[0] = ci.J[0];
    a[1] = ci.J[1];
  }
  
  public final void a(Graphics paramGraphics) {
    paramGraphics.translate(-paramGraphics.getTranslateX(), -paramGraphics.getTranslateY());
    paramGraphics.setClip(0, 0, el.d, el.e);
    paramGraphics.setColor(14279153);
    paramGraphics.fillRoundRect(el.d - 60, 5, 34, 20, 6, 6);
    paramGraphics.setColor(4682453);
    paramGraphics.fillRect(el.d - 60 + 2, 7, 30, 16);
    paramGraphics.drawImage(a[0], el.d - 60 + 4, 9, 0);
    model.bn.b.a(paramGraphics, "" + this.b, el.d - 60 + 28, 9, 2);
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.w.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */