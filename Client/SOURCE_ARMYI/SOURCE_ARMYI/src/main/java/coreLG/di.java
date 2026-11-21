package coreLG;

import model.as;
import javax.microedition.io.Connector;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

final class di implements Runnable {
  private final String a;
  
  private final String b;
  
  private final as c;
  
  private final as d;
  
  di(String paramString1, String paramString2, as paramas1, as paramas2) {
    this.a = paramString1;
    this.b = paramString2;
    this.c = paramas1;
    this.d = paramas2;
  }
  
  public final void run() {
    try {
      MessageConnection messageConnection = null;
      TextMessage textMessage;
      (textMessage = (TextMessage)(messageConnection = (MessageConnection)Connector.open(this.a)).newMessage("text")).setAddress(this.a);
      textMessage.setPayloadText(this.b);
      messageConnection.send((Message)textMessage);
      this.c.a();
      return;
    } catch (Exception exception) {
      this.d.a();
      return;
    } 
  }
}


/* Location:              D:\Downloads\jar\Army165.jar!\coreLG\di.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */