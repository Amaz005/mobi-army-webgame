package com.mobiarmy.server.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Reader implements Runnable {

    private DataInputStream dis;
    private Session session;

    public Reader(Session session, Socket socket) {
        try {
            this.session = session;
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message msg = readMessage();
                session.lastTimeReadMessage = System.currentTimeMillis();
                System.out.println("[Reader] recv cmd=" + (msg.command & 0xff) + " (0x" + String.format("%02X", msg.command & 0xff) + ") data_len=" + msg.getData().length);
                if (!session.connected && msg.command != ICommand.HANDSAKE) {
                    System.out.println("[Reader] dropping cmd=" + (msg.command & 0xff) + " (no handshake yet)");
                    msg.cleanup();
                    continue;
                }
                session.controller.onMessage(msg);
                msg.cleanup();
            }
        } catch (Exception ex) {
            System.out.println("[Reader] exception: " + ex.getClass().getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        // Client.gI().kickSession(session);
        session.disconnect();
        session.controller.onDisconnected();
        try {
            close();
        } catch (IOException e) {

        }
    }

    private Message readMessage() throws Exception {
        long st = System.currentTimeMillis();
        byte cmd = dis.readByte();
        if (session.connected) {
            cmd = readKey(cmd);
        }
        int size;
        if (session.connected) {
            byte b1 = dis.readByte();
            byte b2 = dis.readByte();
            size = (readKey(b1) & 255) << 8 | readKey(b2) & 255;
        } else {
            size = dis.readUnsignedShort();
        }
        if (size > 1024) {
            throw new IOException("Data too big");
        }
        byte data[] = new byte[size];
        int len = 0;
        int byteRead = 0;
        while (len != -1 && byteRead < size) {
            len = dis.read(data, byteRead, size - byteRead);
            if (len > 0) {
                byteRead += len;
            }
        }
        if (session.connected) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readKey(data[i]);
            }
        }
        if (session.logCheck) {
            System.out.println("Time read message: " + (System.currentTimeMillis() - st) + " ms");
        }
        return new Message(cmd, data);
    }

    private byte readKey(byte b) {
        byte i = (byte) ((Session.KEYS[session.curR++] & 255) ^ (b & 255));
        if (session.curR >= Session.KEYS.length) {
            session.curR %= Session.KEYS.length;
        }
        return i;
    }

    void close() throws IOException {
        if (this.dis != null) {
            this.dis.close();
        }
        this.dis = null;
        this.session = null;
    }
}
