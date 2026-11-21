package com.mobiarmy.server.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender implements Runnable {

    private Session session;
    private BlockingQueue<Message> sendingQueue;
    private DataOutputStream dos;

    public Sender(Session session, Socket socket) throws IOException {
        this.session = session;
        this.sendingQueue = new LinkedBlockingQueue<>();
        this.dos = new DataOutputStream(socket.getOutputStream());
    }

    public int getNumMessage() {
        return this.sendingQueue.size();
    }

    public void addMessage(Message message) {
        if (message != null) {
            sendingQueue.offer(message); // thread-safe
        }
    }

    @Override
    public void run() {
        try {
            while (session != null && session.connected) {
                // BlockingQueue sẽ chờ nếu chưa có message
                Message message = sendingQueue.take();
                doSendMessage(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // khôi phục trạng thái interrupt
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
            session.disconnect();
        } finally {
            close();
        }
    }

    private void doSendMessage(Message msg) throws IOException {
        byte[] data = msg.getData();
        if (session.connected) {
            dos.writeByte(writeKey(msg.command));
        } else {
            dos.writeByte(msg.command);
        }

        if (data != null) {
            int size = data.length;
            if (session.connected) {
                dos.writeByte(writeKey((byte) (size >> 8)));
                dos.writeByte(writeKey((byte) (size & 255)));
            } else {
                dos.writeShort(size);
            }
            if (session.connected) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = writeKey(data[i]);
                }
            }
            dos.write(data);
        } else {
            dos.writeShort(0);
        }
        dos.flush();
        msg.cleanup();
    }

    private byte writeKey(byte b) {
        byte i = (byte) ((Session.KEYS[session.curW++] & 255) ^ (b & 255));
        if (session.curW >= Session.KEYS.length) {
            session.curW %= Session.KEYS.length;
        }
        return i;
    }

    public void sendKey() {
        Message msg = new Message(-27);
        try {
            msg.writer().writeByte(Session.KEYS.length);
            msg.writer().writeByte(Session.KEYS[0]);
            for (int i = 1; i < Session.KEYS.length; i++) {
                msg.writer().writeByte(Session.KEYS[i] ^ Session.KEYS[i - 1]);
            }
            doSendMessage(msg);
            msg.cleanup();
            session.connected = true;
            session.sendThread.start();
        } catch (IOException e) {
            System.err.println("Error sending key: " + e.getMessage());
        }
    }

    void close() {
        try {
            if (this.dos != null) {
                this.dos.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing output stream: " + e.getMessage());
        } finally {
            this.dos = null;
            this.session = null;
            if (this.sendingQueue != null) {
                this.sendingQueue.clear();
            }
            this.sendingQueue = null;
        }
    }
}