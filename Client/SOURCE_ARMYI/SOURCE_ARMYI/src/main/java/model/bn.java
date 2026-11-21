package model;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bn {
  public Image a;
  
  private final String j;
  
  private byte[] k;
  
  private int l;
  
  private int m;
  
  private int n;
  
  public static bn b = new bn(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđABCDEFGHIJKLMNOPQRSTUVWXYZĐ~", new byte[] { 
        4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 
        6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 
        8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 
        4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 
        7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 
        3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
        7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 
        8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 
        7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 
        10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 
        9, 7, 7, 8, 8, 6 }, 13, "fb.png", 0);
  
  public static bn c = new bn(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđABCDEFGHIJKLMNOPQRSTUVWXYZĐ~", new byte[] { 
        4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 
        6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 
        8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 
        4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 
        7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 
        3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
        7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 
        8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 
        7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 
        10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 
        9, 7, 7, 8, 8, 6 }, 15, "fb1.png", 0);
  
  public static bn d = new bn(" 0123456789.,:!?()-'/ABCDEFGHIJKLMNOPQRSTUVWXYZÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴĐ", new byte[] { 
        4, 8, 8, 8, 8, 8, 8, 8, 8, 8, 
        8, 4, 4, 4, 4, 8, 6, 6, 6, 3, 
        7, 10, 10, 10, 10, 8, 8, 10, 10, 5, 
        8, 9, 8, 13, 11, 10, 10, 10, 10, 10, 
        9, 10, 10, 13, 11, 11, 9, 10, 10, 10, 
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 
        10, 10, 10, 10, 8, 8, 8, 8, 8, 8, 
        8, 8, 8, 8, 8, 5, 5, 5, 5, 5, 
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 
        10, 10, 10, 10 }, 21, "fcg14m.png", 0);
  
  public static bn e = new bn("0123456789+-%$:abcdefghijklmnopqrstuvwxyz", new byte[] { 
        5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 
        5, 5, 7, 5, 3, 5, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 5, 5, 7, 6, 5, 
        5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 
        5 }, 8, "fs0.png", -1);
  
  public static bn f = new bn("0123456789+-%$:abcdefghijklmnopqrstuvwxyz", new byte[] { 
        5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 
        5, 5, 7, 5, 3, 5, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 5, 5, 7, 6, 5, 
        5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 
        5 }, 8, "fs1.png", -1);
  
  public static bn g = new bn(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", new byte[] { 
        2, 5, 4, 5, 5, 6, 5, 5, 5, 5, 
        5, 2, 3, 2, 2, 5, 4, 4, 6, 6, 
        6, 6, 4, 3, 8, 5, 5, 4, 5, 5, 
        4, 5, 5, 2, 3, 5, 2, 8, 5, 5, 
        5, 5, 4, 4, 3, 5, 6, 8, 4, 4, 
        4, 6, 5, 6, 6, 5, 5, 6, 6, 4, 
        4, 5, 5, 8, 7, 7, 5, 7, 6, 6, 
        6, 6, 6, 8, 6, 6, 5 }, 10, "tahoma9.png", 0);
  
  public static bn h = new bn(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđABCDEFGHIJKLMNOPQRSTUVWXYZĐ~", new byte[] { 
        3, 5, 3, 5, 5, 5, 5, 5, 5, 5, 
        5, 1, 1, 1, 1, 5, 3, 2, 5, 4, 
        5, 6, 2, 2, 9, 5, 5, 4, 5, 5, 
        3, 5, 5, 1, 1, 5, 1, 9, 5, 5, 
        5, 5, 3, 5, 2, 5, 4, 8, 4, 4, 
        4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 
        1, 2, 3, 1, 5, 5, 5, 5, 5, 5, 
        5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 
        6, 5, 5, 5, 5, 5, 7, 7, 7, 7, 
        7, 7, 4, 4, 4, 4, 4, 6, 6, 6, 
        7, 7, 6, 5, 7, 7, 1, 4, 7, 6, 
        7, 7, 7, 6, 7, 7, 6, 6, 7, 6, 
        8, 6, 6, 6, 7, 4 }, 14, "arialf.png", 1);
  
  public static bn i = new bn(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđABCDEFGHIJKLMNOPQRSTUVWXYZĐ~Á", new byte[] { 
        4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 
        6, 2, 2, 2, 2, 6, 4, 4, 6, 5, 
        6, 7, 4, 4, 10, 6, 6, 6, 6, 6, 
        4, 6, 6, 2, 2, 5, 2, 8, 6, 6, 
        6, 6, 4, 6, 3, 6, 6, 10, 6, 6, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 
        6, 6, 6, 6, 6, 6, 6, 6, 6, 3, 
        2, 3, 4, 2, 6, 6, 6, 6, 6, 6, 
        6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 
        7, 6, 6, 6, 6, 6, 8, 8, 8, 8, 
        8, 8, 6, 6, 6, 6, 6, 7, 8, 7, 
        7, 7, 6, 6, 8, 7, 2, 5, 7, 6, 
        8, 7, 8, 6, 8, 7, 7, 6, 7, 8, 
        11, 7, 8, 7, 7, 6, 7 }, 13, "arial11b.png", 0);
  
  private bn(String paramString1, byte[] paramArrayOfbyte, int paramInt1, String paramString2, int paramInt2) {
    y y = new y("/font/font");
    this.m = paramInt2;
    this.j = paramString1;
    this.k = paramArrayOfbyte;
    this.l = paramInt1;
    try {
      this.a = y.a(paramString2);
      this.n = this.a.getWidth();
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
  }
  
  public final void a(Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramString.length();
    if (paramInt3 == 0) {
      paramInt3 = paramInt1;
    } else if (paramInt3 == 1) {
      paramInt3 = paramInt1 - a(paramString);
    } else {
      paramInt3 = paramInt1 - (a(paramString) >> 1);
    } 
    for (byte b = 0; b < i; b++) {
      if ((paramInt1 = this.j.indexOf(paramString.charAt(b))) == -1)
        paramInt1 = 0; 
      if (paramInt1 > -1)
        paramGraphics.drawRegion(this.a, 0, paramInt1 * this.l, this.n, this.l, 0, paramInt3, paramInt2, 20); 
      paramInt3 += this.k[paramInt1] + this.m;
    } 
  }
  
  public final int a(String paramString) {
    int i = 0;
    for (byte b = 0; b < paramString.length(); b++) {
      int j;
      if ((j = this.j.indexOf(paramString.charAt(b))) == -1)
        j = 0; 
      i += this.k[j];
    } 
    return i;
  }
  
  public static String a(String paramString1, String paramString2, String paramString3) {
    StringBuffer stringBuffer = new StringBuffer();
    int i = paramString1.indexOf(paramString2);
    int j = 0;
    int k = paramString2.length();
    while (i != -1) {
      stringBuffer.append(paramString1.substring(j, i)).append(paramString3);
      j = i + k;
      i = paramString1.indexOf(paramString2, j);
    } 
    stringBuffer.append(paramString1.substring(j, paramString1.length()));
    return stringBuffer.toString();
  }
  
  public final String[] a(String paramString, int paramInt) {
    Vector vector = new Vector();
    int i;
    if ((i = paramString.length()) <= 1)
      return new String[] { paramString }; 
    String str = "";
    int j = 0;
    int k = 0;
    while (true) {
      if (a(str) < paramInt) {
        str = String.valueOf(str) + paramString.charAt(k);
        if (paramString.charAt(++k) != '\n')
          if (k >= i - 1) {
            k = i - 1;
          } else {
            continue;
          }  
      } 
      if (k != i - 1 && paramString.charAt(k + 1) != ' ') {
        int m = k;
        while (paramString.charAt(k + 1) != '\n' && (paramString.charAt(k + 1) != ' ' || paramString.charAt(k) == ' ') && k != j)
          k--; 
        if (k == j)
          k = m; 
      } 
      vector.addElement(paramString.substring(j, k + 1));
      if (k != i - 1) {
        for (j = k + 1; j != i - 1 && paramString.charAt(j) == ' '; j++);
        if (j != i - 1) {
          k = j;
          str = "";
          continue;
        } 
      } 
      break;
    } 
    String[] arrayOfString = new String[vector.size()];
    for (byte b = 0; b < vector.size(); b++)
      arrayOfString[b] = (String) vector.elementAt(b);
    return arrayOfString;
  }
  
  public final int a() {
    return this.l;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bn.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */