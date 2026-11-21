package model;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Image;

public final class y {
  private String[] a;
  
  private int[] b;
  
  private int[] c;
  
  private byte[] d;
  
  private int e;
  
  private int f;
  
  private String g;
  
  private byte[] code = new byte[] {
      78, 103, 117, 121, 101, 110, 86, 97, 110, 77, 
      105, 110, 104 };
  
  private int i = this.code.length;
  
  private DataInputStream j;
  
  public y(byte[] paramArrayOfbyte) {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramArrayOfbyte);
    this.j = new DataInputStream(byteArrayInputStream);
    int i = 0;
    int j = 0;
    this.f = 0;
    try {
      int k = this.j.readUnsignedByte();
      this.f++;
      this.a = new String[this.e];
      this.b = new int[this.e];
      this.c = new int[this.e];
      for (byte b = 0; b < this.e; b++) {
        int m;
        byte[] arrayOfByte = new byte[m = k = this.j.readByte()];
        this.j.read(arrayOfByte);
        this.a(arrayOfByte);
        this.a[b] = new String(arrayOfByte);
        this.b[b] = i;
        int n = this.j.readUnsignedShort();
        i += this.c[b];
        j += this.c[b];
        this.f += m + 3;
      } 
      this.d = new byte[j];
      this.j.readFully(this.d);
      this.a(this.d);
    } catch (IOException iOException) {}
    this.a();
  }
  
  public y(String paramString) {
    int i = 0;
    int j = 0;
    this.g = paramString;
    this.f = 0;
    this.j = new DataInputStream(this.getClass().getResourceAsStream(this.g));
    try {
      int k = this.j.readUnsignedByte();
      this.f++;
      this.a = new String[this.e];
      this.b = new int[this.e];
      this.c = new int[this.e];
      for (byte b = 0; b < this.e; b++) {
        int m;
        byte[] arrayOfByte = new byte[m = k = this.j.readByte()];
        this.j.read(arrayOfByte);
        a(arrayOfByte);
        this.a[b] = new String(arrayOfByte);
        this.b[b] = i;
        int n = this.j.readUnsignedShort();
        i += this.c[b];
        j += this.c[b];
        this.f += m + 3;
      } 
      this.d = new byte[j];
      this.j.readFully(this.d);
      a(this.d);
    } catch (IOException iOException) {}
    a();
  }
  
  private void a(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    for (byte b = 0; b < i; b++)
      paramArrayOfbyte[b] = (byte)(paramArrayOfbyte[b] ^ this.code[b % this.i]);
  }
  
  private void a() {
    try {
      if (this.j != null) {
        this.j.close();
      }
    } catch (IOException iOException) {}
  }
  
  public final Image a(String paramString) {
    for (byte b = 0; b < this.e; b++) {
      if (this.a[b].compareTo(paramString) == 0)
        return Image.createImage(this.d, this.b[b], this.c[b]); 
    } 
    System.out.println("File '" + paramString + "' not found!");
    return null;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.y.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */