package com.mobiarmy.network;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Message {
    
    @FunctionalInterface
    public interface IOBlock {
        void execute() throws IOException;
    }

    @FunctionalInterface
    public interface IOWriter<T> {
        void write(Message msg, T item) throws IOException;
    }

    @FunctionalInterface
    public interface IOAction {
        void execute(Message msg) throws IOException;
    }

    @FunctionalInterface
    public interface Job {
        void execute();
    }

    private byte command;
    
    private ByteArrayOutputStream baos;
    private DataOutputStream dos;
    
    private ByteArrayInputStream bais;
    private DataInputStream dis;

    public Message(int command) {
        this((byte) command);
    }

    public Message(byte command) {
        this.command = command;
        this.baos = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.baos);
    }

    public Message(byte command, byte[] data) {
        this.command = command;
        this.baos = new ByteArrayOutputStream();
        this.dos = new DataOutputStream(this.baos);
        if (data != null) {
            this.bais = new ByteArrayInputStream(data);
            this.dis = new DataInputStream(this.bais);
        }
    }

    public byte getCommand() {
        return command;
    }

    public void setCommand(int v) {
        this.command = (byte) v;
    }

    public void setCommand(byte v) {
        this.command = v;
    }

    public byte[] getData() {
        return baos.toByteArray();
    }

    // -- Write - primitives --

    public Message sendByte(int v) throws IOException {
        dos.writeByte(v);
        return this;
    }

    public Message sendShort(int v) throws IOException {
        dos.writeShort(v);
        return this;
    }

    public Message sendInt(int v) throws IOException {
        dos.writeInt(v);
        return this;
    }

    public Message sendLong(long v) throws IOException {
        dos.writeLong(v);
        return this;
    }

    public Message sendBoolean(boolean v) throws IOException {
        dos.writeBoolean(v);
        return this;
    }

    public Message sendUTF(String v) throws IOException {
        if (v == null) v = "";
        byte[] bytes = v.getBytes("UTF-8");
        if (bytes.length > 0xFFFF) throw new IOException("UTF string too long");
        dos.writeShort(bytes.length);
        dos.write(bytes);
        return this;
    }

    public Message sendBytes(byte[] bytes) throws IOException {
        dos.write(bytes);
        return this;
    }

    public Message sendShorts(short[] shorts) throws IOException {
        for (short s : shorts) {
            dos.writeShort(s);
        }
        return this;
    }

    // -- Write - convenience --

    public Message sendFile(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        if (data.length > 0xFFFF) {
            throw new IOException("Pack file too large for short length: " + path);
        }
        dos.writeShort(data.length);
        dos.write(data);
        return this;
    }

    public Message apply(IOAction action) throws IOException {
        action.execute(this);
        return this;
    }

    public Message todo(Job job) {
        job.execute();
        return this;
    }

    public Message sendIf(boolean condition, IOBlock block) throws IOException {
        if (condition) block.execute();
        return this;
    }

    public Message sendTimes(int count, IOBlock block) throws IOException {
        for (int i = 0; i < count; i++) {
            block.execute();
        }
        return this;
    }

    public <T> Message sendEach(Collection<T> items, IOWriter<T> writer) throws IOException {
        for (T item : items) {
            writer.write(this, item);
        }
        return this;
    }

    public <T> Message sendEach(Collection<T> items, Predicate<T> filter, IOWriter<T> writer) throws IOException {
        for (T item : items) {
            if (filter.test(item)) {
                writer.write(this, item);
            }
        }
        return this;
    }

    // -- Read --

    public byte readByte() throws IOException {
        checkRead();
        return dis.readByte();
    }

    public int readUnsignedByte() throws IOException {
        checkRead();
        return dis.readUnsignedByte();
    }

    public short readShort() throws IOException {
        checkRead();
        return dis.readShort();
    }

    public int readUnsignedShort() throws IOException {
        checkRead();
        return dis.readUnsignedShort();
    }

    public int readInt() throws IOException {
        checkRead();
        return dis.readInt();
    }

    public long readLong() throws IOException {
        checkRead();
        return dis.readLong();
    }

    public boolean readBoolean() throws IOException {
        checkRead();
        return dis.readBoolean();
    }

    public String readUTF() throws IOException {
        checkRead();
        int len = dis.readUnsignedShort();
        byte[] buf = new byte[len];
        dis.readFully(buf);
        return new String(buf, "UTF-8");
    }

    public boolean isAvailable() {
        return bais != null && bais.available() > 0;
    }

    public int available() {
        return bais != null ? bais.available() : 0;
    }

    public boolean hasMore() {
        return bais != null && bais.available() > 0;
    }

    public DataInputStream reader() {
        return dis;
    }

    public void cleanup() {
        try {
            if (dis != null) dis.close();
            if (bais != null) bais.close();
            if (dos != null) dos.close();
            if (baos != null) baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkRead() {
        if (dis == null) {
            throw new IllegalStateException("Message has no read buffer (write-only message).");
        }
    }
}
