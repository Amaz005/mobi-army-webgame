package com.mobiarmy.server.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {

    public byte command;

    private final byte[] rawData;
    private final boolean inputOnly;

    private ByteArrayOutputStream os;

    private DataOutputStream dos;

    private ByteArrayInputStream is;

    private DataInputStream dis;

    public Message() {
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(os);
        this.rawData = null;
        this.inputOnly = false;
    }

    public Message(int command) {
        this((byte) command);
    }

    public Message(byte command) {
        this.command = command;
        this.os = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(os);
        this.rawData = null;
        this.inputOnly = false;
    }

    public Message(byte command, byte[] data) {
        this.command = command;
        if (data == null) {
            this.rawData = new byte[0];
        } else {
            this.rawData = data.clone();
        }
        this.inputOnly = true;
    }

    public DataInputStream reader() {
        if (this.inputOnly) {
            // Lazy init the read stream from the cached raw data so each call returns
            // a fresh, unconsumed stream.
            this.is = new ByteArrayInputStream(this.rawData);
            this.dis = new DataInputStream(is);
        }
        return dis;
    }

    public DataOutputStream writer() {
        return dos;
    }

    public byte[] getData() {
        if (this.inputOnly) {
            // Return the cached payload directly — caller-safe, multiple-read safe.
            return this.rawData;
        }
        if (this.os == null) {
            return new byte[0];
        }
        return this.os.toByteArray();
    }

    public void cleanup() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
        } catch (IOException e) {
        }
    }

    public void dispose() {
        this.dis = null;
        this.is = null;
        this.dos = null;
        this.os = null;
    }
}
