package model;

import coreLG.cj;
import model.ad;
import model.az;
import model.bo;

import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

public final class bx {
  private static Hashtable a = new Hashtable();
  
  public static bo a(short paramShort) {
    bo bo;
    if ((bo = (bo)a.get(new Short(paramShort))) != null)
      return bo; 
    if ((bo = b(paramShort)) != null) {
      a.put(new Short(paramShort), bo);
    } else {
      short s = paramShort;
      ad ad = model.ad.a();
      az az = new az(38);
      try {
        az.c().writeShort(s);
      } catch (IOException iOException) {}
      ad.a.a(az);
      az.d();
      (bo = new bo()).a = paramShort;
      a.put(new Short(paramShort), bo);
    } 
    return bo;
  }
  
  public static void a(bo parambo) {
    a("avi" + parambo.a, parambo.b);
  }
  
  private static bo b(short paramShort) {
    Image image = null;
    if ((image = cj.a("/av/" + paramShort + ".png")) != null) {
      bo bo;
      (bo = new bo()).a = paramShort;
      return bo;
    } 
    byte[] arrayOfByte = c("avi" + paramShort);
    try {
      image = Image.createImage(arrayOfByte, 0, arrayOfByte.length);
    } catch (Exception exception) {}
    if (image != null) {
      bo bo;
      (bo = new bo()).a = paramShort;
      return bo;
    } 
    return null;
  }
  
  private static byte[] c(String paramString) {
    byte[] arrayOfByte;
    try {
      RecordStore recordStore;
      arrayOfByte = (recordStore = RecordStore.openRecordStore(paramString, false)).getRecord(1);
      recordStore.closeRecordStore();
    } catch (Exception exception) {
      return null;
    } 
    return arrayOfByte;
  }
  
  private static void a(String paramString, byte[] paramArrayOfbyte) {
    try {
      RecordStore recordStore;
      if ((recordStore = RecordStore.openRecordStore(paramString, true)).getNumRecords() > 0) {
        recordStore.setRecord(1, paramArrayOfbyte, 0, paramArrayOfbyte.length);
      } else {
        recordStore.addRecord(paramArrayOfbyte, 0, paramArrayOfbyte.length);
      } 
      recordStore.closeRecordStore();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
  
  public static void a(String paramString1, String paramString2) {
    a(paramString1, paramString2.getBytes());
  }
  
  public static String a(String paramString) {
    byte[] arrayOfByte;
    return ((arrayOfByte = c(paramString)) == null) ? "" : new String(arrayOfByte);
  }
  
  public static void a(String paramString, int paramInt) {
    byte[] arrayOfByte;
    (arrayOfByte = new byte[1])[0] = (byte)paramInt;
    a(paramString, arrayOfByte);
  }
  
  public static int b(String paramString) {
    byte[] arrayOfByte;
    return ((arrayOfByte = c(paramString)) == null) ? 0 : arrayOfByte[0];
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bx.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */