package javax.microedition.io;

import java.io.IOException;

/** Interface only: Connector refuses http:// in the browser (research.md R2c), so nothing implements it. */
public interface HttpConnection extends ContentConnection {
    String GET = "GET", POST = "POST", HEAD = "HEAD";
    int HTTP_OK = 200;

    void setRequestMethod(String method) throws IOException;

    void setRequestProperty(String key, String value) throws IOException;

    int getResponseCode() throws IOException;

    String getHeaderField(String name) throws IOException;
}
