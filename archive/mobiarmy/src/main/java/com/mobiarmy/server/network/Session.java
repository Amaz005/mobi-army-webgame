package com.mobiarmy.server.network;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import com.mobiarmy.server.Player;
import com.mobiarmy.server.service.SendPackData;
import com.mobiarmy.server.sql.JBDCManager;

public class Session {

    private static final int TIME_WAIT_READ_MESSAGE = 180000;

    public boolean logCheck;

    private static int baseId = 0;
    public int id;

    public boolean isLock = false;

    public byte timeWait = 50;

    public boolean connected;

    static final byte[] KEYS = UUID.randomUUID().toString().getBytes();
    byte curR, curW;

    private Socket socket;
    Thread sendThread;
    Thread receiveThread;
    private Reader collector;
    private Sender sender;
    public MessageHandle controller;
    public JBDCManager jbdc;
    public Player player;
    public SendPackData sendPackData;

    public byte zoomLevel;
    public boolean isSetClientType;

    public long lastTimeLogout;
    public boolean loginSuccess, joinedGame, dataLoadFailed;

    public long lastTimeReadMessage;

    public String version;
    public boolean strRequire;

    public int getNumOfMessages() {
        return this.sender.getNumMessage();
    }

    public Session(Socket socket) {
        try {
            socket.setTcpNoDelay(true);
            this.id = baseId++;
            this.socket = socket;
            this.controller = new MessageHandle(this);
            this.sender = new Sender(this, socket);
            this.sendThread = new Thread((this.sender), "Send ");
            this.receiveThread = new Thread((this.collector = new Reader(this, socket)), "Receive");
            this.receiveThread.start();
        } catch (IOException e) {
        }
    }

    public void push(Message sending) {
        if (this.sender != null) {
            sender.addMessage(sending);
        }
    }

    public void disconnect() {
        if (connected) {
            connected = false;
            curR = 0;
            curW = 0;
            try {
                if (this.sender != null) {
                    this.sender.close();
                }
                if (this.collector != null) {
                    this.collector.close();
                }
                if (socket != null) {
                    socket.close();
                }
                this.socket = null;
                this.sender = null;
                this.collector = null;
                this.sendThread = null;
                this.receiveThread = null;
            } catch (IOException e) {
            }
        }
    }

    public void sendSessionKey() {
        this.sender.sendKey();
    }

}
