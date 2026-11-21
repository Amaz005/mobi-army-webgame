package com.mobiarmy.network;

public interface ISession {
    boolean isConnected();
    void setHandler(IMessageHandler messageHandler);
    void sendMessage(Message message);
    void close();
}
