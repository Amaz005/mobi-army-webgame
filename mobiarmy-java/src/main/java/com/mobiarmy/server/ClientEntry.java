package com.mobiarmy.server;

import com.mobiarmy.network.IMessageHandler;
import com.mobiarmy.network.ISession;
import com.mobiarmy.network.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.Timer;
import java.util.TimerTask;

public class ClientEntry implements ISession {
    private static final byte[] KEY = "bth.army2.ml".getBytes(StandardCharsets.US_ASCII);
    private static final Pattern ALNUM_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public Socket sc;
    private InputStream is;
    private OutputStream os;
    public int id;
    public User user;
    private IMessageHandler messageHandler;

    boolean connected;
    boolean login;
    /** Username reserved by command 35 on this connection; the next login with it creates the account. */
    private String pendingRegistration;
    private byte curR, curW;
    private AtomicInteger disposed = new AtomicInteger(0);

    private final BlockingQueue<Message> sendQueue = new LinkedBlockingQueue<>();
    private Thread collectorThread;
    private Thread senderThread;

    private Timer idleTimer;
    private Timer loginTimer;
    private static final long LOGIN_TIMEOUT_MS = 30000;

    public String platform;
    public String versionARM;
    public String IPAddress;

    public final Object obj = new Object();

    public ClientEntry(Socket sc, int id) {
        this.sc = sc;
        this.id = id;

        try {
            sc.setSoTimeout(200000);
            sc.setKeepAlive(true);
            this.is = sc.getInputStream();
            this.os = sc.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // We should set handler to a MessageHandler instance, wait for that class.
        setHandler(new MessageHandler(this));

        startLoginTimer();

        collectorThread = new Thread(this::collectorLoop);
        collectorThread.setDaemon(true);
        collectorThread.start();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void setHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void sendMessage(Message message) {
        sendQueue.offer(message);
    }

    private void senderLoop() {
        try {
            while (disposed.get() == 0 && os != null) {
                Message msg = sendQueue.take();
                if (disposed.get() != 0) break;
                doSendMessage(msg);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            closeMessage();
        }
    }

    private final Object sendLock = new Object();

    private void doSendMessage(Message m) {
        synchronized (sendLock) {
            if (disposed.get() != 0 || os == null) return;
            byte[] data = m.getData();
            try {
                int size = data != null ? data.length : 0;
                os.write(connected ? writeKey(m.getCommand()) : m.getCommand());
                if (connected) {
                    os.write(writeKey((byte) (size >> 8)));
                    os.write(writeKey((byte) (size & 0xFF)));
                    if (data != null && data.length > 0) {
                        for (int i = 0; i < data.length; i++) {
                            data[i] = writeKey(data[i]);
                        }
                    }
                } else {
                    writeBe16(size);
                }
                
                if (data != null && data.length > 0) {
                    os.write(data);
                }
                os.flush();
            } catch (Exception e) {
                closeMessage();
            }
        }
    }

    private void writeBe16(int value) throws Exception {
        os.write((value >> 8) & 0xFF);
        os.write(value & 0xFF);
    }

    private void collectorLoop() {
        try {
            while (disposed.get() == 0 && is != null) {
                Message message;
                try {
                    message = readMessage();
                } catch (Exception e) {
                    closeMessage();
                    break;
                }

                if (message == null) {
                    closeMessage();
                    break;
                }

                if (loginTimer != null) {
                    loginTimer.cancel();
                    loginTimer = null;
                }

                if (idleTimer != null) {
                    idleTimer.cancel();
                }
                idleTimer = new Timer();
                idleTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        closeMessage();
                    }
                }, 180000);

                try {
                    if (messageHandler != null) {
                        messageHandler.onMessage(message);
                    }
                } finally {
                    message.cleanup();
                }
            }
        } catch (Exception e) {
            closeMessage();
        }
    }

    private Message readMessage() throws Exception {
        int cmdByte = is.read();
        if (cmdByte < 0) return null;
        byte cmd = (byte) cmdByte;
        if (connected) cmd = readKey(cmd);

        int b1raw = is.read();
        int b2raw = is.read();
        if (b1raw < 0 || b2raw < 0) return null;

        int size = connected
                ? ((readKey((byte) b1raw) & 0xFF) << 8) | (readKey((byte) b2raw) & 0xFF)
                : (b1raw << 8) | b2raw;

        byte[] data = new byte[size];
        int byteRead = 0;
        while (byteRead < size) {
            int len = is.read(data, byteRead, size - byteRead);
            if (len <= 0) return null;
            byteRead += len;
        }

        if (connected) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readKey(data[i]);
            }
        }

        return new Message(cmd, data);
    }

    private byte readKey(byte b) {
        byte result = (byte) ((KEY[curR++] & 0xFF) ^ (b & 0xFF));
        if (curR >= KEY.length) curR %= KEY.length;
        return result;
    }

    private byte writeKey(byte b) {
        byte result = (byte) ((KEY[curW++] & 0xFF) ^ (b & 0xFF));
        if (curW >= KEY.length) curW %= KEY.length;
        return result;
    }

    @Override
    public void close() {
        if (!disposed.compareAndSet(0, 1)) return;
        
        // Try user.close() or equivalent
        // ServerManager.disconnect(this);
        
        cleanNetwork();
        user = null;
    }

    private void cleanNetwork() {
        curR = 0;
        curW = 0;
        connected = false;
        login = false;

        try { if (is != null) is.close(); } catch (Exception ignored) {}
        is = null;
        try { if (os != null) os.close(); } catch (Exception ignored) {}
        os = null;
        try { if (sc != null) sc.close(); } catch (Exception ignored) {}
        sc = null;

        if (idleTimer != null) { idleTimer.cancel(); idleTimer = null; }
        if (loginTimer != null) { loginTimer.cancel(); loginTimer = null; }
    }

    @Override
    public String toString() {
        return user != null ? user.toString() : "Client " + id;
    }

    public void hansakeMessage() {
        Message ms = new Message((byte) -27);
        try {
            ms.sendByte(KEY.length).sendByte(KEY[0]);
            for (int i = 1; i < KEY.length; i++) {
                ms.sendByte(KEY[i] ^ KEY[i - 1]);
            }
            doSendMessage(ms);
            connected = true;
            senderThread = new Thread(this::senderLoop);
            senderThread.setDaemon(true);
            senderThread.start();
        } catch (Exception ignored) {}
    }

    public void loginMessage(Message ms) {
        if (login) return;
        try {
            String userS = ms.readUTF().trim();
            String pass = ms.readUTF().trim();
            String version = ms.readUTF().trim();

            if (!ALNUM_PATTERN.matcher(userS).matches() || !ALNUM_PATTERN.matcher(pass).matches()) {
                sendMessage(new Message(4).sendUTF("Tài khoản hoặc mật khẩu không hợp lệ"));
                return;
            }

            System.out.println("Client: " + id + " name: " + userS + " pass: " + pass + " version: " + version);
            this.versionARM = version;

            if (pendingRegistration != null && pendingRegistration.equals(Registration.normalize(userS))) {
                String name = pendingRegistration;
                pendingRegistration = null;
                if (!Registration.isValidPassword(pass)) {
                    sendMessage(new Message(4).sendUTF("Mật khẩu chỉ gồm chữ và số, tối đa 50 ký tự."));
                    return;
                }
                if (!Registration.create(name, pass)) {
                    sendMessage(new Message(4).sendUTF("Tài khoản " + name + " đã tồn tại. Hãy chọn tên khác."));
                    return;
                }
                System.out.println("Client: " + id + " registered account " + name);
                userS = name;
            }

            User us = User.login(this, userS, pass);
            if (us != null) {
                System.out.println("Login Success!");
                login = true;
                if (loginTimer != null) {
                    loginTimer.cancel();
                    loginTimer = null;
                }
                user = us;
                // These methods need to be implemented in ServerManager if they aren't already.
                ServerManager.sendRoomInfo(user);
                ServerManager.sendMapCollisionInfo(user);
                ServerManager.sendNvData(user);
            } else {
                System.out.println("Login Failse!");
                login = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Closes the connection if nobody logs in within LOGIN_TIMEOUT_MS; restarting it gives more time. */
    private synchronized void startLoginTimer() {
        if (loginTimer != null) loginTimer.cancel();
        loginTimer = new Timer();
        loginTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!login) {
                    closeMessage();
                }
            }
        }, LOGIN_TIMEOUT_MS);
    }

    /**
     * Command 35 from the Register screen. The client sends only the username (the password went in
     * the original game's SMS), so this reserves the name for this connection; the next login on this
     * connection with that name creates the account with the password the player typed.
     */
    public void registerMessage(Message ms) {
        if (login) return;
        try {
            String name = Registration.normalize(ms.readUTF());
            if (!Registration.isValidUsername(name)) {
                sendMessage(new Message(4).sendUTF("Tên tài khoản chỉ gồm chữ và số, tối đa 20 ký tự."));
                return;
            }
            if (Registration.exists(name)) {
                sendMessage(new Message(4).sendUTF("Tài khoản " + name + " đã tồn tại. Hãy chọn tên khác."));
                return;
            }
            pendingRegistration = name;
            startLoginTimer();
            sendMessage(new Message(4).sendUTF("Tên " + name + " dùng được. Bấm Đăng nhập để hoàn tất đăng ký"
                    + " với mật khẩu bạn vừa nhập (chỉ gồm chữ và số)."));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sendMessage(new Message(4).sendUTF("Đăng ký thất bại, vui lòng thử lại sau."));
            } catch (Exception ignored) {}
        }
    }

    public void closeMessage() {
        if (messageHandler != null) messageHandler.onDisconnected();
        close();
    }

    public void regMessage(Message ms) {
        try {
            sendMessage(new Message((byte) 4).sendUTF("Vui lòng truy cập http://army2.ml để đăng ký"));
        } catch (Exception ignored) {}
    }
}
