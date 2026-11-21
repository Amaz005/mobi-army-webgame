package javax.wireless.messaging;

import java.io.IOException;
import javax.microedition.io.Connection;

/** Interface only: Connector refuses sms:// in the browser (research.md R9). */
public interface MessageConnection extends Connection {
    String TEXT_MESSAGE = "text", BINARY_MESSAGE = "binary";

    Message newMessage(String type);

    Message newMessage(String type, String address);

    void send(Message msg) throws IOException;
}
