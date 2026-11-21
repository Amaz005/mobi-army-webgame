package model;

import coreLG.ah;
import coreLG.bl;
import coreLG.ci;
import coreLG.cj;
import java.io.IOException;

public final class ad {
  bi a;
  
  private static ad b;
  
  public final void a(bi parambi) {
    this.a = parambi;
  }
  
  public static ad a() {
    if (b == null)
      b = new ad(); 
    return b;
  }
  
  public final void a(String paramString1, String paramString2, String paramString3) {
    az az = new az(1);
    try {
      az.c().writeUTF(paramString1);
      az.c().writeUTF(paramString2);
      az.c().writeUTF(paramString3);
      this.a.a(az);
      az.d();
    } catch (IOException iOException) {}
    cj.d("SendM_LOGIN");
  }
  
  public final void b() {
    az az = new az(6);
    this.a.a(az);
    az.d();
  }
  
  public final void a(byte paramByte) {
    az az = new az(7);
    try {
      az.c().writeByte(paramByte);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
    cj.d("SendM RequestBoardList BID: " + paramByte);
  }
  
  public final void a(byte paramByte1, byte paramByte2, String paramString) {
    az az = new az(8);
    try {
      az.c().writeByte(paramByte1);
      az.c().writeByte(paramByte2);
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void a(String paramString) {
    az az = new az(54);
    try {
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void c() {
    az az = new az(15);
    this.a.a(az);
    az.d();
    cj.d("GUI MESSAGE: LEAVE BOARD");
  }
  
  public final void a(boolean paramBoolean) {
    az az = new az(16);
    try {
      az.c().writeBoolean(paramBoolean);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void a(int paramInt) {
    az az = new az(19);
    try {
      az.c().writeInt(paramInt);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void b(String paramString) {
    az az = new az(18);
    try {
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void c(String paramString) {
    az az = new az(9);
    try {
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void b(int paramInt) {
    az az = new az(11);
    try {
      az.c().writeInt(paramInt);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
    cj.d("SendM Kick: ID" + paramInt);
  }
  
  public final void b(byte paramByte) {
    az az = new az(28);
    try {
      az.c().writeByte(paramByte);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void c(int paramInt) {
    az az = new az(31);
    try {
      az.c().writeByte(paramInt);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void d(int paramInt) {
    az az = new az(30);
    try {
      az.c().writeByte(paramInt);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void d(String paramString) {
    az az = new az(35);
    try {
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void d() {
    az az = new az(29);
    this.a.a(az);
    az.d();
  }
  
  public final void e(String paramString) {
    az az = new az(36);
    try {
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void e(int paramInt) {
    az az = new az(32);
    try {
      az.c().writeInt(paramInt);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void f(int paramInt) {
    az az = new az(33);
    try {
      az.c().writeInt(paramInt);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void a(int paramInt, String paramString) {
    az az = new az(5);
    try {
      az.c().writeInt(paramInt);
      az.c().writeUTF(paramString);
    } catch (IOException iOException) {}
    this.a.a(az);
    az.d();
  }
  
  public final void c(byte paramByte) {
    az az = new az(58);
    try {
      az.c().writeByte(paramByte);
    } catch (IOException iOException) {}
    this.a.a(az);
  }
  
  public final void e() {
    az az = new az(44);
    this.a.a(az);
  }
  
  public final void d(byte paramByte) {
    cj.d("send Training");
    az az = new az(83);
    try {
      az.c().writeByte(paramByte);
    } catch (IOException iOException2) {
      IOException iOException1;
      (iOException1 = null).printStackTrace();
    } 
    this.a.a(az);
    az.d();
  }
  
  public final void f() {
    az az = new az(20);
    this.a.a(az);
    az.d();
  }
  
  public final void e(byte paramByte) {
    az az = new az(75);
    try {
      az.c().writeByte(paramByte);
    } catch (IOException iOException2) {
      IOException iOException1;
      (iOException1 = null).printStackTrace();
    } 
    this.a.a(az);
    az.d();
  }
  
  public final void a(byte paramByte, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    az az = new az(79);
    try {
      az.c().writeByte(paramByte);
      for (byte b = 0; b < paramByte; b++) {
        cj.d("model.x= " + paramArrayOfint1[b]);
        az.c().writeInt(paramArrayOfint1[b]);
        az.c().writeInt(paramArrayOfint2[b]);
      } 
    } catch (Exception exception2) {
      Exception exception1;
      (exception1 = null).printStackTrace();
    } 
    this.a.a(az);
    az.d();
  }
  
  public final void a(short paramShort1, short paramShort2) {
    if (ci.S)
      return; 
    az az = new az(21);
    try {
      az.c().writeShort(paramShort1);
      az.c().writeShort(paramShort2);
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
    cj.d("GUI MOVE: ");
  }
  
  public final void a(byte paramByte1, short paramShort1, short paramShort2, short paramShort3, byte paramByte2, byte paramByte3, byte paramByte4) {
    az az = new az(84);
    try {
      az.c().writeByte(paramByte1);
      az.c().writeShort(paramShort1);
      az.c().writeShort(paramShort2);
      az.c().writeShort(paramShort3);
      az.c().writeByte(paramByte2);
      if (ah.b(paramByte1))
        az.c().writeByte(paramByte3); 
      az.c().writeByte(paramByte4);
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
    cj.d("SendWait_Fire bull: " + paramByte1 + " nShoot: " + paramByte4 + " force2= " + paramByte3);
  }
  
  public final void b(byte paramByte1, short paramShort1, short paramShort2, short paramShort3, byte paramByte2, byte paramByte3, byte paramByte4) {
    az az = new az(22);
    try {
      az.c().writeByte(paramByte1);
      az.c().writeShort(paramShort1);
      az.c().writeShort(paramShort2);
      az.c().writeShort(paramShort3);
      az.c().writeByte(paramByte2);
      if (ah.b(paramByte1))
        az.c().writeByte(paramByte3); 
      az.c().writeByte(paramByte4);
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
    cj.d("SendWait_Fire bull: " + paramByte1 + " nShoot: " + paramByte4 + " force2= " + paramByte3 + " Y= " + paramShort2);
  }
  
  public final void g() {
    if (ci.S)
      return; 
    cj.d("SHOOT RESULT");
    az az = new az(23);
    this.a.a(az);
    az.d();
  }
  
  public final void b(short paramShort1, short paramShort2) {
    if (ci.S)
      return; 
    if (!bl.b) {
      az az = new az(53);
      try {
        az.c().writeShort(paramShort1);
        az.c().writeShort(paramShort2);
      } catch (Exception exception2) {
        Exception exception1;
        (exception1 = null).printStackTrace();
      } 
      this.a.a(az);
      az.d();
    } 
  }
  
  public final void a(boolean paramBoolean, int paramInt) {
    az az = new az(78);
    try {
      az.c().writeBoolean(paramBoolean);
      az.c().writeInt(paramInt);
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
  }
  
  public final void h() {
    if (ci.S)
      return; 
    az az = new az(49);
    this.a.a(az);
    az.d();
    cj.d("Send SkipTurn");
  }
  
  public final void f(byte paramByte) {
    if (ci.S)
      return; 
    az az = new az(26);
    try {
      az.c().writeByte(paramByte);
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
    cj.d("SendM UseITEM " + paramByte);
  }
  
  public final void a(int[] paramArrayOfint) {
    az az = new az(68);
    try {
      for (byte b = 0; b < paramArrayOfint.length; b++)
        az.c().writeByte(paramArrayOfint[b]); 
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
    cj.d("SendM changeGun " + paramArrayOfint.length);
  }
  
  public final void g(byte paramByte) {
    az az = new az(69);
    try {
      az.c().writeByte(paramByte);
    } catch (Exception exception) {}
    this.a.a(az);
    az.d();
    cj.d("SendM chooseGun " + paramByte);
  }
  
  public final void i() {
    az az = new az(71);
    this.a.a(az);
    az.d();
  }
  
  public final void g(int paramInt) {
    az az = new az(56);
    try {
      az.c().writeByte(paramInt);
    } catch (Exception exception) {}
    this.a.a(az);
  }
  
  public final void h(int paramInt) {
    az az = new az(34);
    try {
      az.c().writeInt(paramInt);
    } catch (Exception exception) {}
    this.a.a(az);
  }
  
  public final void a(String paramString1, String paramString2) {
    az az = new az(81);
    try {
      az.c().writeUTF(paramString1);
      az.c().writeUTF(paramString2);
    } catch (Exception exception) {}
    this.a.a(az);
  }
  
  public final void a(byte paramByte, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    az az = new az(72);
    try {
      az.c().writeByte(paramByte);
      for (byte b = 0; b < paramByte; b++) {
        cj.d("item id= " + paramArrayOfbyte1[b]);
        az.c().writeByte(paramArrayOfbyte1[b]);
        az.c().writeByte(paramArrayOfbyte2[b]);
        cj.d("SendM_Mua_Item :  nItem: " + paramByte);
      } 
    } catch (Exception exception) {}
    this.a.a(az);
  }
  
  public final void h(byte paramByte) {
    az az = new az(74);
    try {
      az.c().writeByte(paramByte);
    } catch (Exception exception) {}
    this.a.a(az);
    cj.d("SendM BuyGun " + paramByte);
  }
  
  public final void a(byte paramByte, String paramString) {
    if (paramString == null)
      paramString = ""; 
    az az = new az(85);
    try {
      az.c().writeByte(paramByte);
      az.c().writeUTF(paramString);
    } catch (Exception exception2) {
      Exception exception1;
      (exception1 = null).printStackTrace();
    } 
    this.a.a(az);
  }
  
  public final void f(String paramString) {
    az az = new az(127);
    try {
      az.c().writeUTF(paramString);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    this.a.a(az);
  }
  
  public final void a(byte paramByte1, byte paramByte2) {
    az az = new az(90);
    try {
      az.c().writeByte(paramByte1);
      az.c().writeByte(paramByte2);
    } catch (Exception exception2) {
      Exception exception1;
      (exception1 = null).printStackTrace();
    } 
    this.a.a(az);
  }
  
  public final void i(int paramInt) {
    az az = new az(97);
    try {
      az.c().writeInt(paramInt);
    } catch (Exception exception2) {
      Exception exception1;
      (exception1 = null).printStackTrace();
    } 
    this.a.a(az);
    cj.d("SEND RULET");
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.ad.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */