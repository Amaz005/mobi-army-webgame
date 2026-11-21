package model;

final class bf implements Runnable {
  private bi a;
  
  bf(bi parambi) {
    this.a = parambi;
  }
  
  public void run() {
    while(true) {
      try {
        if (this.a.b()) {
          bf var1 = this;
          byte var2 = this.a.a.readByte();
          if (this.a.g) {
            var2 = bi.a(this.a, var2);
          }
        
          int var3;
          if (this.a.g) {
            byte var4 = this.a.a.readByte();
            byte var5 = this.a.a.readByte();
            var3 = (bi.a(this.a, var4) & 255) << 8 | bi.a(this.a, var5) & 255;
          } else {
            var3 = this.a.a.readUnsignedShort();
          }
        
          byte[] var11 = new byte[var3];
          int var14 = 0;
          int var6 = 0;
        
          while(var14 != -1 && var6 < var3) {
            if ((var14 = var1.a.a.read(var11, var6, var3 - var6)) > 0) {
              var6 += var14;
            }
          }
        
          bi var10000 = var1.a;
          var10000.f += var6 + 5;
          if (var1.a.g) {
            for(var3 = 0; var3 < var11.length; ++var3) {
              var11[var3] = bi.a(var1.a, var11[var3]);
            }
          }
        
          az var9 = new az(var2, var11);
        
          try {
            if (var9.a != -27) {
              this.a.b.a(var9);
              continue;
            }
          
            az var10 = var9;
            var1 = this;
            byte var12 = var9.b().readByte();
            this.a.h = new byte[var12];
          
            int var13;
            for(var13 = 0; var13 < var12; ++var13) {
              var1.a.h[var13] = var10.b().readByte();
            }
          
            for(var13 = 0; var13 < var1.a.h.length - 1; ++var13) {
              byte[] var15 = var1.a.h;
              var15[var13 + 1] ^= var1.a.h[var13];
            }
          
            var1.a.g = true;
          } catch (Exception var7) {
            var7.printStackTrace();
          }
          continue;
        }
      } catch (Exception var8) {
      }
    
      if (this.a.c) {
        if (this.a.b != null) {
          if (System.currentTimeMillis() - this.a.i > 500L) {
            this.a.b.d();
          } else {
            this.a.b.c();
          }
        }
      
        if (bi.a(this.a) != null) {
          bi.c(this.a);
        }
      }
    
      return;
    }
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\model.bf.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */