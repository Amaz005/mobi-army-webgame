package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class  az {
  public byte a;
  
  private ByteArrayOutputStream b = null;
  
  private DataOutputStream c = null;
  
  private ByteArrayInputStream d = null;
  
  private DataInputStream e = null;
  
  public az(int paramInt) {
    this.a = (byte)paramInt;
    this.b = new ByteArrayOutputStream();
    this.c = new DataOutputStream(this.b);
  }
  
  public az() {}
  
  public az(byte paramByte) {
    this.a = -27;
    this.b = new ByteArrayOutputStream();
    this.c = new DataOutputStream(this.b);
  }
  
  public az(byte paramByte, byte[] paramArrayOfbyte) {
    this.a = paramByte;
    this.d = new ByteArrayInputStream(paramArrayOfbyte);
    this.e = new DataInputStream(this.d);
  }
  
  public byte[] a() {
    return this.b.toByteArray();
  }
  
  public DataInputStream b() {
    return this.e;
  }
  
  public DataOutputStream c() {
    return this.c;
  }
  
  public void d() {
    try {
      if (this.e != null)
        this.e.close(); 
      if (this.c != null) {
        this.c.close();
        return;
      } 
    } catch (IOException iOException) {}
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.az.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */