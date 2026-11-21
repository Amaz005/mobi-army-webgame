package com.mobiarmy.server.network;

public interface IMessageHandle {
    public void onMessage(Message m);
    public void onConnected();
    public void onDisconnected();
}
