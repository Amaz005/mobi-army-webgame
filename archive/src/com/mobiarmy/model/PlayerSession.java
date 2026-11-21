package com.mobiarmy.model;

public class PlayerSession {

    public final Player player;
    public String ipAddress;
    public Room currentRoom;
    public long connectedAt;
    public long lastActivity;
    public boolean authenticated;
    public volatile Object clientHandler;

    public PlayerSession(Player player, String ipAddress) {
        this.player = player;
        this.ipAddress = ipAddress;
        this.connectedAt = System.currentTimeMillis();
        this.lastActivity = System.currentTimeMillis();
        this.authenticated = false;
        this.clientHandler = null;
    }

    public void updateActivity() {
        this.lastActivity = System.currentTimeMillis();
    }

    public void authenticate() {
        this.authenticated = true;
        updateActivity();
    }

    public void joinRoom(Room room) {
        this.currentRoom = room;
    }

    public void leaveRoom() {
        this.currentRoom = null;
    }

    public long getSessionDuration() {
        return System.currentTimeMillis() - connectedAt;
    }

    public void sendMessage(byte command, byte[] data) {
        if (clientHandler != null && clientHandler instanceof com.mobiarmy.server.NetworkServer.ClientHandler) {
            try {
                ((com.mobiarmy.server.NetworkServer.ClientHandler) clientHandler).sendMessage(command, data);
            } catch (java.io.IOException e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }
        }
    }
}
