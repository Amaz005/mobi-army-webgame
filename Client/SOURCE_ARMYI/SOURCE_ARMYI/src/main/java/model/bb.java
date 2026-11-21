package model;

import coreLG.*;
import coreLG.ah;
import coreLG.bg;
import coreLG.bl;
import coreLG.bx;
import coreLG.f;
import coreLG.l;

import java.io.IOException;
import java.util.Vector;

public final class bb {
  private x b;
  
  private static bb c;
  
  private static boolean d = false;
  
  public static Object a = new Object();
  
  private static int e = 0;
  
  public static bb a() {
    if (c == null)
      c = new bb(); 
    return c;
  }
  
  public final void b() {
    this.b.d();
    cj.d("connect ok");
  }
  
  public final void c() {
    this.b.c();
    cj.d("connect fail");
  }
  
  public final void d() {
    this.b.e();
  }
  
  //thay thế tại cỗ này hàm a()
  public void a(az az) {
      if (az != null) {
        try {
          cj.d("msg= " + az.a);
          Label_6042: {
            switch (az.a) {
              case 4: {
                this.b.a(az.b().readUTF());
              }
              case 3: {
                cj.d("TOI DAY");
                System.currentTimeMillis();
                final ad a = ad.a();
                final int v = 0;
                final ad ad = a;
                cj.d("PING 42");
                final az az2 = new az(42);
                try {
                  az2.c().writeInt(v);
                }
                catch (IOException ex2) {}
                ad.a.a(az2);
                az2.d();
                (TerrainMidlet.e = new j()).b = az.b().readInt();
                TerrainMidlet.e.e = az.b().readInt();
                TerrainMidlet.e.c = az.b().readByte();
                TerrainMidlet.e.a(az.b().readInt());
                cj.d("TOI DAY 2");
                final Vector<dj> vector = new Vector<dj>();
                for (int i = 0; i < 22; ++i) {
                  cj.d("i=" + i);
                  vector.addElement(new dj((byte)i, az.b().readByte(), az.b().readShort()));
                }
                coreLG.l.a(vector);
                cj.d("TOI DAY 3");
                for (int j = 0; j < 9; ++j) {
                  if (j < 3) {
                    fa.c[j] = 1;
                    fa.d[j] = 0;
                  }
                  else {
                    fa.c[j] = az.b().readByte();
                    fa.d[j] = az.b().readShort() * 1000;
                  }
                }
                az.b().readUTF();
                ej.a = az.b().readUTF();
                el.p.e();
                el.a();
              }
              case 6: {
                final Vector<bw> vector2 = new Vector<bw>();
                while (az.b().available() > 0) {
                  final bw obj;
                  (obj = new bw()).a = az.b().readByte();
                  obj.b = az.b().readByte();
                  az.b().readByte();
                  obj.c = az.b().readByte();
                  vector2.addElement(obj);
                }
                el.b();
                el.m.a(vector2);
                el.m.b();
              }
              case 7: {
                if (el.h == el.p) {
                  return;
                }
                final Vector vector3 = new Vector<r>();
                final byte byte1 = az.b().readByte();
                while (az.b().available() > 0) {
                  final r obj2;
                  (obj2 = new r()).a = az.b().readByte();
                  obj2.b = az.b().readByte();
                  obj2.f = az.b().readByte();
                  obj2.c = az.b().readBoolean();
                  obj2.d = az.b().readInt();
                  az.b().readBoolean();
                  obj2.e = az.b().readUTF();
                  obj2.g = az.b().readByte();
                  vector3.addElement(obj2);
                }
                el.b();
                el.n.f = byte1;
                coreLG.f.a(vector3);
                el.n.b();
                cj.d("NhanM_BOARDLIST size: " + vector3.size());
              }
              case 8: {
                final int int1 = az.b().readInt();
                final int int2 = az.b().readInt();
                az.b().readByte();
                az.b().readByte();
                final Vector vector4 = new Vector<j>();
                while (az.b().available() > 0) {
                  final j obj3;
                  (obj3 = new j()).b = az.b().readInt();
                  if (obj3.b == -1) {
                    obj3.a = "";
                  }
                  else {
                    obj3.a = az.b().readUTF();
                    obj3.e = az.b().readInt();
                    obj3.k = az.b().readShort();
                    obj3.a(az.b().readInt());
                    obj3.c = az.b().readByte();
                    obj3.d = az.b().readBoolean();
                  }
                  vector4.addElement(obj3);
                }
                el.b();
                ci.S = false;
                el.p.a(int1, int2, vector4);
                for (int k = 0; k < vector4.size(); ++k) {
                  final j l;
                  if ((l = (j) vector4.elementAt(k)).b == int1) {
                    l.d = true;
                  }
                }
                el.p.b();
              }
              case 17: {
                this.b.b(az.b().readUTF());
              }
              case 12: {
                final j m = new j();
                final byte byte2 = az.b().readByte();
                m.b = az.b().readInt();
                m.a = az.b().readUTF();
                m.e = az.b().readInt();
                m.k = az.b().readShort();
                m.a(az.b().readInt());
                m.c = az.b().readByte();
                m.d = false;
                el.p.a(byte2, m);
                new StringBuffer(String.valueOf(m.a)).append(" v\u1eeba v\u00e0o b\u00e0n.").toString();
                cj.d("<< joinBoard ID " + m.b);
              }
              case 14: {
                final int int3 = az.b().readInt();
                cj.d("ID leave" + int3);
                final int int4 = az.b().readInt();
                el.p.c(int3);
                el.p.b(int4);
                if (el.h == el.j) {
                  for (int index = 0; index < el.p.d.size(); ++index) {
                    if (int3 == ((j)el.p.d.elementAt(index)).b) {
                      eb.b[index].aa = 5;
                      break;
                    }
                  }
                }
                ci.M.a(eb.f);
                synchronized (bb.a) {
                  bb.a.notifyAll();
                }
                // monitorexit(bb.a)
                cj.d("<< LeaveBoard ID : " + int3);
              }
              case 16: {
                this.b.a(az.b().readInt(), az.b().readBoolean());
              }
              case 19: {
                az.b().readShort();
                final int int5 = az.b().readInt();
                el.p.d(int5);
                cj.d("NHAN M - DAT TIEN CUOC: " + int5);
              }
              case 9: {
                this.b.a(az.b().readUTF(), az.b().readInt());
              }
              case 11: {
                az.b().readShort();
                final int int6 = az.b().readInt();
                this.b.a(int6);
                cj.d("NHAN M KICK id: " + int6);
              }
              case 30: {
                final byte byte3 = az.b().readByte();
                final Vector<j> vector5 = new Vector<j>();
                while (az.b().available() > 0) {
                  final j obj4;
                  (obj4 = new j()).b = az.b().readInt();
                  obj4.a = az.b().readUTF();
                  obj4.e = az.b().readInt();
                  obj4.c = az.b().readByte();
                  obj4.a(az.b().readInt());
                  obj4.h = az.b().readInt();
                  obj4.g = az.b().readInt();
                  obj4.d = true;
                  vector5.addElement(obj4);
                }
                this.b.a(byte3, vector5);
              }
              case 35: {
                this.b.a(az.b().readUTF(), az.b().readBoolean(), az.b().readUTF(), az.b().readUTF());
              }
              case 29: {
                final Vector<j> vector6 = new Vector<j>();
                while (az.b().available() > 0) {
                  final j obj5;
                  (obj5 = new j()).b = az.b().readInt();
                  obj5.a = az.b().readUTF();
                  obj5.e = az.b().readInt();
                  obj5.c = az.b().readByte();
                  obj5.d = (az.b().readByte() != 0);
                  obj5.h = az.b().readInt();
                  obj5.g = az.b().readInt();
                  vector6.addElement(obj5);
                }
                this.b.a(vector6);
              }
              case 36: {
                final Vector<j> vector7 = new Vector<j>();
                while (az.b().available() > 0) {
                  final j obj6;
                  (obj6 = new j()).b = az.b().readInt();
                  obj6.a = az.b().readUTF();
                  vector7.addElement(obj6);
                }
                this.b.c(vector7);
              }
              case 32: {
                this.b.a(az.b().readByte());
              }
              case 33: {
                this.b.b(az.b().readByte());
              }
              case 5: {
                System.out.println("CHAT TO");
                final bu bu;
                (bu = new bu()).a = az.b().readInt();
                bu.b = az.b().readUTF();
                bu.c = az.b().readUTF();
                this.b.a(bu);
              }
              case 38: {
                final bo bo;
                (bo = new bo()).a = az.b().readShort();
                final byte[] array = new byte[az.b().readShort()];
                az.b().read(array);
                bo.b = array;
                this.b.a(bo);
              }
              case 44: {
                final Vector<br> vector8 = new Vector<br>();
                while (az.b().available() > 0) {
                  final br obj7;
                  (obj7 = new br()).a = az.b().readUTF();
                  obj7.b = az.b().readUTF();
                  obj7.c = az.b().readUTF();
                  vector8.addElement(obj7);
                  cj.d(String.valueOf(obj7.a) + " " + obj7.b + " " + obj7.c);
                }
                this.b.d(vector8);
              }
              case 45: {
                this.b.d(az.b().readUTF());
              }
              case 46: {
                this.b.c(az.b().readUTF());
              }
              case 48: {
                this.b.a(az.b().readUTF(), az.b().readUTF());
              }
              case 47: {
                az.b().readUTF();
              }
              case 52: {
                final int int7 = az.b().readInt();
                cj.d(String.valueOf(int7) + " whoBonus");
                final int int8 = az.b().readInt();
                final int int9 = az.b().readInt();
                if (int7 == TerrainMidlet.e.b) {
                  TerrainMidlet.e.e = int9;
                }
                if (el.h == el.j) {
                  el.j.a(int8, int7);
                }
                cj.d("<< BonusMoney ID " + int7 + " Money " + int8);
              }
              case 10: {
                this.b.e(az.b().readUTF());
              }
              case 20: {
                cj.d("START ARMY");
                el.p.e.removeAllElements();
                final byte byte4 = az.b().readByte();
                final byte byte5 = az.b().readByte();
                cj.d("map ID = " + byte4);
                final short[] array2 = new short[8];
                final short[] array3 = new short[8];
                for (int n = 0; n < 8; ++n) {
                  array2[n] = az.b().readShort();
                  if (array2[n] != -1) {
                    array3[n] = az.b().readShort();
                  }
                  else {
                    array3[n] = -1;
                  }
                  cj.d("X= " + array2[n] + " Y= " + array3[n]);
                }
                if (!ci.S) {
                  el.j.a(byte4, byte5, array2, array3);
                  el.j.b();
                }
                else {
                  ej.a(byte4, byte5, array2, array3);
                }
                if (el.p.p[4] >= 0) {
                  final dj a2 = coreLG.l.a(12);
                  --a2.d;
                }
                if (el.p.p[5] >= 0) {
                  final dj a3 = coreLG.l.a(13);
                  --a3.d;
                }
                el.b();
                el.v.a = false;
                coreLG.bg.b();
                el.a(u.cs());
                bb.d = true;
              }
              case 93: {
                final byte byte6 = az.b().readByte();
                final short short1 = az.b().readShort();
                final short short2 = az.b().readShort();
                cj.d("FLY  x= " + short1 + " y= " + short2);
                eb.b(byte6, short1, short2);
                ci.M.a(byte6);
                if (el.h == el.j) {
                  synchronized (bb.a) {
                    bb.a.wait();
                    // monitorexit(bb.a)
                    return;
                  }
                }
              }
              case 21: {
                final byte byte7 = az.b().readByte();
                final short short3 = az.b().readShort();
                final short short4 = az.b().readShort();
                cj.d("MOVE");
                eb.a(byte7, short3, short4);
              }
              case 53: {
                cj.d("UPDATE XY");
                final byte byte8 = az.b().readByte();
                eb.c(byte8, az.b().readShort(), az.b().readShort());
                eb.b[byte8].ag = -1;
              }
              case 22:
              case 84: {
                cj.d("FIRE ARMY");
                final byte byte9 = az.b().readByte();
                final byte byte10 = az.b().readByte();
                final short short5 = az.b().readShort();
                final short short6 = az.b().readShort();
                final short short7 = az.b().readShort();
                byte byte11 = 0;
                if (byte10 == 17 || byte10 == 19) {
                  byte11 = az.b().readByte();
                }
                if (byte10 == 14 || byte10 == 40) {
                  coreLG.bg.b = az.b().readByte();
                  az.b().readByte();
                }
                if (byte10 == 44 || byte10 == 45 || byte10 == 47) {
                  coreLG.bg.b = az.b().readByte();
                  cj.d("ANGLE= " + coreLG.bg.b);
                }
                final byte byte12 = az.b().readByte();
                final int i2 = bg.k = az.b().readByte();
                cj.d("NBULL= " + i2);
                final short[][] array4 = new short[i2][];
                final short[][] array5 = new short[i2][];
                for (int n2 = 0; n2 < i2; ++n2) {
                  final short short8;
                  final short[] array6 = new short[short8 = az.b().readShort()];
                  final short[] array7 = new short[short8];
                  for (short n3 = 0; n3 < short8; ++n3) {
                    array6[n3] = az.b().readShort();
                    array7[n3] = az.b().readShort();
                  }
                  array4[n2] = array6;
                  array5[n2] = array7;
                }
                eb.b[byte9].a(byte9, short5, short6, byte10, array4, array5, byte12, byte11, short7);
                if (el.h == el.j) {
                  synchronized (bb.a) {
                    bb.a.wait();
                    // monitorexit(bb.a)
                    return;
                  }
                }
              }
              case 51: {
                final byte byte13 = az.b().readByte();
                final short short9 = az.b().readShort();
                final byte byte14 = az.b().readByte();
                eb.b[byte13].a(short9, byte14);
                System.out.println("Nhan Message: UPDATE HP: " + byte13 + " nextHP: " + short9 + " pixel= " + byte14);
              }
              case 24: {
                cj.d("NEXT TURN");
                final byte byte15 = az.b().readByte();
                coreLG.bl.b = false;
                eb.a(byte15);
                ci.N.e = 0;
                if (bb.d) {
                  bb.d = false;
                  el.b();
                }
              }
              case 25: {
                final byte byte16;
                ci.a(byte16 = az.b().readByte());
                System.out.println("Nhan Message: CHANGE WIND: " + byte16);
              }
              case 26: {
                final byte byte17 = az.b().readByte();
                final byte byte18 = az.b().readByte();
                cj.d("USED ITEM");
                eb.b[byte17].a(byte18, true, 0);
              }
              case 69: {
                final int int10 = az.b().readInt();
                final byte byte19 = az.b().readByte();
                el.b();
                if (int10 == TerrainMidlet.e.b) {
                  TerrainMidlet.e.c = byte19;
                }
                el.s.f();
                cj.d("<< change player " + byte19);
              }
              case 70: {}
              case 71: {
                this.b.a(az.b().readInt(), az.b().readByte());
              }
              case 50: {
                final byte byte20 = az.b().readByte();
                final byte byte21 = az.b().readByte();
                final int int11 = az.b().readInt();
                el.j.a(byte20, byte21, int11);
                el.p.r();
                el.p.A = 5;
                cj.d("<< team Win " + byte20 + " moneyBonus " + int11);
                synchronized (bb.a) {
                  bb.a.notifyAll();
                }
                // monitorexit(bb.a)
              }
              case 34: {
                if (az.b().readInt() != -1) {
                  final String utf = az.b().readUTF();
                  final int int12 = az.b().readInt();
                  final int int13 = az.b().readInt();
                  final int int14 = az.b().readInt();
                  final int int15 = az.b().readInt();
                  final int int16 = az.b().readInt();
                  final int int17 = az.b().readInt();
                  final int int18 = az.b().readInt();
                  final int int19 = az.b().readInt();
                  final j j2;
                  (j2 = new j()).a(int13);
                  el.a(String.valueOf(u.cD()) + ": " + utf + ". " + u.cE() + ": " + int12 + "~. Level: " + j2.l.a + "+" + j2.l.c + "%. " + u.r() + ": " + int14 + ". " + u.q() + ": " + (int15 + int17) + ". " + u.p() + ": " + int16 + " .Kill/Dead: " + int18 + "/" + int19);
                  return;
                }
                el.a(u.o());
              }
              case 72: {
                final byte byte22;
                final byte[] array8 = new byte[byte22 = az.b().readByte()];
                final byte[] array9 = new byte[byte22];
                for (byte b = 0; b < byte22; ++b) {
                  array8[b] = az.b().readByte();
                  array9[b] = az.b().readByte();
                }
                final int int20 = az.b().readInt();
                l.a(byte22, array8, array9, int20);
                cj.d("<< buy item n: " + byte22 + " money con lai " + int20);
              }
              case 74: {
                final byte byte23 = az.b().readByte();
                final int int21 = az.b().readInt();
                TerrainMidlet.e.e = int21;
                fa.c[byte23 + fa.b] = 1;
                el.b();
                cj.d("<< buy gun " + byte23 + fa.b + " money con lai " + int21);
              }
              case 42: {}
              case 63: {
                final String utf2 = az.b().readUTF();
                final String utf3 = az.b().readUTF();
                final String utf4 = az.b().readUTF();
                el.b(utf2, new at(this, utf3, utf4));
                cj.d("text: " + utf2 + "    data: " + utf3 + "    to: " + utf4);
              }
              case 75: {
                final byte byte24 = az.b().readByte();
                cj.d("Map ID = " + byte24);
                el.b();
                coreLG.bx.n = byte24;
                el.h = el.p;
                el.p.r();
              }
              case 64: {
                final byte byte25 = az.b().readByte();
                cj.d("Len= " + byte25);
                final byte[] array10 = new byte[byte25];
                az.b().read(array10, 0, byte25);
                az.b().read(new byte[byte25], 0, byte25);
                final byte byte26;
                final short[] array11 = new short[byte26 = az.b().readByte()];
                for (byte b2 = 0; b2 < byte26; ++b2) {
                  array11[b2] = az.b().readShort();
                }
                final byte byte27;
                final byte[] array12 = new byte[byte27 = az.b().readByte()];
                az.b().read(array12, 0, byte27);
                final byte byte28;
                final byte[] array13 = new byte[byte28 = az.b().readByte()];
                az.b().read(array13, 0, byte28);
                eb.a = az.b().readByte();
                final byte byte29;
                coreLG.bx.j = new byte[byte29 = az.b().readByte()];
                for (byte b3 = 0; b3 < byte29; ++b3) {
                  coreLG.bx.j[b3] = az.b().readByte();
                }
                coreLG.bx.k = new byte[byte29];
                for (byte b4 = 0; b4 < byte29; ++b4) {
                  coreLG.bx.k[b4] = az.b().readByte();
                  cj.d("BOSS ID= " + coreLG.bx.k[b4]);
                }
                ah.e = array10;
                coreLG.bl.aq = array11;
                coreLG.bl.ar = array11;
                fa.e = array12;
                fa.f = array13;
                el.s = new fa();
                el.m = new et();
              }
              case 76: {
                coreLG.bx.b = az.b().readByte();
                final byte byte30 = az.b().readByte();
                final String utf5 = az.b().readUTF();
                coreLG.bx.c = az.b().readByte();
                cj.d("boardID= " + byte30 + " boardName= " + utf5);
                f.a(byte30, utf5);
              }
              case 78: {
                if (az.b().readBoolean()) {
                  final byte byte31 = az.b().readByte();
                  final Vector<j> vector9 = new Vector<j>();
                  for (byte b5 = 0; b5 < byte31; ++b5) {
                    final j obj8;
                    (obj8 = new j()).a = az.b().readUTF();
                    obj8.b = az.b().readInt();
                    obj8.c = az.b().readByte();
                    obj8.e = az.b().readInt();
                    obj8.h = az.b().readInt();
                    obj8.g = az.b().readInt();
                    obj8.d = true;
                    vector9.addElement(obj8);
                  }
                  el.b();
                  x.b(vector9);
                  return;
                }
                cj.d("Someone invite ");
                el.a(az.b().readUTF(), new aw(this, az.b().readByte(), az.b().readByte(), az.b().readUTF()), new ax(this));
              }
              case 80: {
                final byte byte32;
                final coreLG.bl bl = eb.b[byte32 = az.b().readByte()];
                final boolean a4 = ci.K.a(bl.G, bl.H);
                final ad a5 = ad.a();
                final byte b6 = byte32;
                final boolean v2 = a4;
                final byte v3 = b6;
                final ad ad2 = a5;
                final az az3 = new az(80);
                try {
                  az3.c().writeByte(v3);
                  az3.c().writeBoolean(v2);
                }
                catch (IOException ex3) {}
                ad2.a.a(az3);
                az3.d();
              }
              case 94: {
                ad.a().a(el.p.p);
              }
              case 86: {
                this.b.a(az.b().readUTF(), az.b().readUTF(), az.b().readByte());
              }
              case 87: {
                final String utf6 = az.b().readUTF();
                cj.d("myName= " + utf6);
                TerrainMidlet.e.a = utf6;
              }
              case 88: {
                final byte byte33 = az.b().readByte();
                cj.d("len= " + byte33);
                et.b = new String[byte33];
                for (byte b7 = 0; b7 < byte33; ++b7) {
                  final String utf7 = az.b().readUTF();
                  az.b().readUTF();
                  et.b[b7] = utf7;
                }
              }
              case 89: {
                cj.d("SEND BOSS");
                final byte byte34;
                final short[] array14 = new short[byte34 = az.b().readByte()];
                final short[] array15 = new short[byte34];
                for (byte b8 = 0; b8 < byte34; ++b8) {
                  final j obj9;
                  (obj9 = new j()).b = az.b().readInt();
                  obj9.a = az.b().readUTF();
                  obj9.k = az.b().readShort();
                  obj9.c = az.b().readByte();
                  obj9.j = true;
                  array14[b8] = az.b().readShort();
                  array15[b8] = az.b().readShort();
                  el.p.e.addElement(obj9);
                }
                ci.L.b(array14, array15);
                cj.d("HAS SEND BOSS");
              }
              case 92: {
                final short short10;
                cv.j = new short[short10 = az.b().readShort()];
                for (short n4 = 0; n4 < short10; ++n4) {
                  cv.j[n4] = az.b().readShort();
                  cj.d("tile destroy= " + cv.j[n4]);
                }
              }
              case 90: {
                cj.d("NHAN FILE PACK");
                switch (az.b().readByte()) {
                  case 0: {
                    final byte byte35;
                    if ((byte35 = az.b().readByte()) != el.Q) {
                      final int unsignedShort;
                      final byte[] array16 = new byte[unsignedShort = az.b().readUnsignedShort()];
                      az.b().read(array16, 0, unsignedShort);
                      cv.i = array16;
                      el.a("tileversion", byte35);
                      el.a("tiledata", array16);
                      cj.d("LOAD PACK 1");
                    }
                    dl.f = 50;
                    dl.g = 75;
                    ad.a().a((byte)1, el.R);
                    return;
                  }
                  case 1: {
                    final byte byte36;
                    if ((byte36 = az.b().readByte()) != el.R) {
                      final int unsignedShort2;
                      final byte[] array17 = new byte[unsignedShort2 = az.b().readUnsignedShort()];
                      az.b().read(array17, 0, unsignedShort2);
                      coreLG.bx.z = array17;
                      bx.a();
                      el.a("iconversion", byte36);
                      el.a("icondata", array17);
                      cj.d("LOAD PACK 2");
                    }
                    dl.f = 75;
                    dl.g = 100;
                    ad.a().a((byte)3, el.R);
                    return;
                  }
                  case 2: {
                    dl.e = true;
                    el.c(u.b());
                    final byte byte37;
                    if ((byte37 = az.b().readByte()) != el.S) {
                      final int unsignedShort3;
                      final byte[] b9 = new byte[unsignedShort3 = az.b().readUnsignedShort()];
                      az.b().read(b9, 0, unsignedShort3);
                      el.a(b9);
                      el.a("valuesdata", b9);
                      el.a("valuesversion", byte37);
                      cj.d("LOAD PACK 3");
                    }
                    dl.f = 25;
                    dl.g = 50;
                    ad.a().a((byte)0, el.Q);
                    return;
                  }
                  case 3: {
                    final byte byte38;
                    if ((byte38 = az.b().readByte()) != el.T) {
                      final int unsignedShort4;
                      final byte[] array18 = new byte[unsignedShort4 = az.b().readUnsignedShort()];
                      az.b().read(array18, 0, unsignedShort4);
                      coreLG.bl.h = array18;
                      bl.a();
                      el.a("playerVersion", byte38);
                      el.a("playerdata", array18);
                      cj.d("LOAD PACK 4");
                    }
                    dl.f = 99;
                    dl.e = false;
                    el.b();
                    this.b.f();
                    return;
                  }
                  default: {
                    return;
                  }
                }
              }
              case 95: {
                cj.d("CAPTURE");
                eb.b[az.b().readByte()].a(az.b().readByte());
                if (el.h == el.j) {
                  synchronized (bb.a) {
                    bb.a.wait();
                    // monitorexit(bb.a)
                    return;
                  }
                }
              }
              case 96: {
                final byte byte39 = az.b().readByte();
                final byte byte40 = az.b().readByte();
                eb.b[byte40].aj = true;
                eb.b[byte40].ak = true;
                eb.b[byte39].ac = 3;
                if (el.h == el.j) {
                  synchronized (bb.a) {
                    bb.a.wait();
//                     monitorexit(bb.a)
                    return;
                  }
                }
              }
              case 97: {
                final Vector<ck> vector10 = new Vector<ck>();
                for (int n5 = 0; n5 < 10; ++n5) {
                  vector10.addElement(new ck(az.b().readByte(), az.b().readByte(), az.b().readInt()));
                }
                final byte byte41 = az.b().readByte();
                cj.d("LUCKY NUMB= " + byte41);
                el.b();
                el.G.a(vector10, byte41);
                break;
              }
            }
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
  }// hàm này cần thay thế
  
  public final void a(x paramx) {
    this.b = paramx;
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bb.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */