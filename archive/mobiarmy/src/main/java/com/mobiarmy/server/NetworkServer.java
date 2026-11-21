package com.mobiarmy.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.Files;


public class NetworkServer {
    private ServerSocket serverSocket;
    private final int port;
    private final ExecutorService threadPool;
    private volatile boolean running;
    private final List<ClientHandler> clients;

    public NetworkServer(int port) {
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool();
        this.clients = new CopyOnWriteArrayList<>();
        this.running = false;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("Server started on port " + port);

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                threadPool.execute(handler);
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler client : clients) {
                client.disconnect();
            }
            threadPool.shutdown();
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {
        private final Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;
        private byte[] key;
        private byte curR, curW;
        private boolean keyComplete = false;
        private boolean connected;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.connected = true;
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                // === WAIT FOR KEY REQUEST ===
                byte cmd = dis.readByte();
                if (cmd == -27) {
                    int size = 0;
                    // Một số client (như Session_ME) chỉ gửi -27 mà không có data
                    if (dis.available() >= 2) {
                        size = dis.readUnsignedShort();
                        if (size > 0) {
                            byte[] data = new byte[size];
                            dis.readFully(data);
                        }
                    }
                    System.out.println("Key request received (cmd=-27, size=" + size + ")");
                    sendKey();
                } else {
                    System.out.println("Unexpected first command: " + cmd);
                }

                // === MAIN MESSAGE LOOP ===
                while (true) {
                    ServerMessage message = readMessage();
                    if (message != null) {
                        processMessage(message);
                    } else {
                        break;
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected");
            } catch (IOException e) {
                if (connected) {
                    System.out.println("Client disconnected due to network error: " + e.getMessage());
                }
            } finally {
                disconnect();
            }
        }

        private void sendKey() throws IOException {
            // Generate a random encryption key
            Random random = new Random();
            int keySize = 8 + random.nextInt(8); // 8-15 bytes
            key = new byte[keySize];

            // Generate random key
            byte[] originalKey = new byte[keySize];
            for (int i = 0; i < keySize; i++) {
                originalKey[i] = (byte) random.nextInt(256);
            }

            // XOR encode the key for transmission
            byte[] encodedKey = new byte[keySize];
            encodedKey[0] = originalKey[0];
            for (int i = 1; i < keySize; i++) {
                encodedKey[i] = (byte) (originalKey[i] ^ originalKey[i - 1]);
            }

            // Store the original key for encryption
            key = originalKey;
            curR = 0;
            curW = 0;

            // Send key message
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(baos);
            dataOut.writeByte(keySize);
            dataOut.write(encodedKey);

            byte[] keyData = baos.toByteArray();

            // Send unencrypted key message
            dos.writeByte(-27);
            dos.writeShort(keyData.length);
            dos.write(keyData);
            dos.flush();

            keyComplete = true;
            System.out.println("Encryption key sent. Key size: " + keySize);
        }

        private ServerMessage readMessage() throws IOException {
            try {
                byte cmd = dis.readByte();

                // --- Sửa 1: Nếu client gửi yêu cầu key mới (-27), reset trạng thái ---
                if (cmd == (byte) -27) {
                    keyComplete = false;
                }

                if (keyComplete) {
                    cmd = readKey(cmd);
                }

                int size;
                if (keyComplete) {
                    byte b1 = dis.readByte();
                    byte b2 = dis.readByte();
                    size = (readKey(b1) & 0xff) << 8 | (readKey(b2) & 0xff);
                } else {
                    size = dis.readUnsignedShort();
                }

                byte[] data = new byte[size];
                dis.readFully(data);

                if (keyComplete) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = readKey(data[i]);
                    }
                }

                return new ServerMessage(cmd, data);
            } catch (EOFException e) {
                // --- Sửa 2: Ngắt kết nối sạch khi client đóng ---
                System.out.println("Client disconnected (EOF)");
                return null;
            } catch (SocketException e) {
                // --- Sửa 3: Reset session để lần sau client reconnect được ---
                System.out.println("Client socket reset: " + e.getMessage());
                keyComplete = false;
                return null;
            }
        }

        private void processMessage(ServerMessage message) {
            System.out
                    .println("Received message - Command: " + message.command + ", Data size: " + message.data.length);

            try {
                switch (message.command) {
                    case 1:
                        handleLoginRequest(message);
                        handleCommand64(message);
                        // handleCommand88(message);
                        break;
                    case 2:
                        handleGameAction(message);
                        break;
                    case 83:
                        handleCommand80(message);
                        handleCommand20(message);
                        break;
                    case 6:
                        handleCommand6(message);
                        break;
                    case 7:
                        handleCommand7(message);
                        // handleCommand76(message);
                        break;
                    case 8:
                        // handleCommand12(message);
                        handleCommand8(message);
                        // handleCommand75(message);
                        // handleCommand10(message);
                        // handleCommand12(message);
                        break;
                    case 29:
                        handleCommand29(message);
                        break;
                    case 30:
                        handleCommand30(message);
                        break;
                    case 34:
                        handleCommand34(message);
                        break;
                    case 42:
                    case 58:
                    case 127:
                        handleCommand42(message);
                    case 90:
                        if (version.equals("1.6.5"))
                            handleCommand90(message);
                        break;
                    default:
                        System.out.println("Unknown command: " + message.command);
                        sendMessage((byte) message.command, "Server received your message".getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String version = "";

        private void handleLoginRequest(ServerMessage message) throws IOException {
            System.out.println("Processing login request (sending command 3)");

            // Đọc thông tin login
            DataInputStream reader = new DataInputStream(new ByteArrayInputStream(message.data));
            String username = reader.readUTF();
            String password = reader.readUTF();
            String ver = reader.readUTF();
            version = ver;
            System.out.println("Login attempt - Username: " + username + " pwd: " + password + " ver: " + ver);

            // Gói phản hồi command 3
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            writer.writeInt(10002); // d.b// iddb
            writer.writeInt(20000); // d.e// xu
            writer.writeByte(0); // d.c // nhân vật đang chọn
            writer.writeInt(250000); // d.a(...)//exp

            for (int i = 0; i < 22; i++) {
                writer.writeByte(20); // byte var144
                writer.writeShort(1000 + i); // short var133
            }

            for (int i = 0; i < 9; i++) {
                if (i < 3) {
                    // Client không đọc phần này, bỏ qua
                } else {
                    writer.writeByte(0); // es.c[var]
                    writer.writeShort(1); // es.d[var] / 1000
                }
            }

            writer.writeUTF("TAI GAME");
            writer.writeUTF("");

            writer.flush();

            sendMessage((byte) 3, baos.toByteArray());
            System.out.println("Sent command 3 response to client");
        }

        private void handleCommand42(ServerMessage message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            sendMessage((byte) 42, baos.toByteArray());
            System.out.println("Responded to command 42 (pong/tick)");
        }

        private void handleCommand64(ServerMessage message) throws IOException {
            System.out.println("Processing command 64 request (sendNVdata)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            // --- var85: độ dài 2 mảng đầu ---
            byte len85 = 9;
            writer.writeByte(len85);

            // --- var86: mảng byte đầu tiên ---
            byte[] var86 = new byte[len85];
            for (int i = 0; i < len85; i++)
                var86[i] = (byte) (i + 1);
            writer.write(var86);

            // --- var87: mảng byte thứ hai ---
            byte[] var87 = new byte[len85];
            for (int i = 0; i < len85; i++)
                var87[i] = (byte) (10 + i);
            writer.write(var87);

            // --- var88: số lượng short (mảng var89) ---
            byte len88 = 9;
            writer.writeByte(len88);
            short[] var89 = new short[len88];
            for (int i = 0; i < len88; i++) {
                var89[i] = (short) (100 + i);
                writer.writeShort(var89[i]);
            }

            // --- var158: độ dài mảng var91 ---
            byte len91 = 9;
            writer.writeByte(len91);
            byte[] var91 = new byte[len91];
            for (int i = 0; i < len91; i++)
                var91[i] = (byte) (20 + i);
            writer.write(var91);

            // --- var92: độ dài mảng var93 ---
            byte len93 = 9;
            writer.writeByte(len93);

            byte[] var93 = new byte[len93];
            for (int i = 0; i < len93; i++)
                var93[i] = (byte) (30 + i);
            writer.write(var93);

            // --- var94: số lượng đối tượng ee ---
            byte len94 = 9;
            writer.writeByte(len94);
            for (int i = 0; i < len94; i++) {
                byte var96 = (byte) (i + 1); // id
                short dataLen = 4;
                byte[] var98 = new byte[dataLen];
                for (int j = 0; j < dataLen; j++)
                    var98[j] = (byte) (40 + j + i);

                writer.writeByte(var96);
                writer.writeShort(dataLen);
                writer.write(var98);

                System.out.println("ee[" + i + "] id=" + var96 + " len=" + dataLen);
            }

            writer.flush();
            sendMessage((byte) 64, baos.toByteArray());
            System.out.println("Sent command 64 (sendNVdata) successfully");
        }

        private void handleCommand75(ServerMessage message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            writer.writeByte((byte) 3);
            writer.flush();

            sendMessage((byte) 75, baos.toByteArray());
        }

        private void handleCommand76(ServerMessage message) throws IOException {
            System.out.println("Processing command 76 request (sending board info)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            byte bw_b = 1; // trạng thái hoặc mode hiện tại
            byte boardId = 5; // ID của bảng / phòng
            String boardName = "TrainingGround"; // tên bảng

            writer.writeByte(bw_b);
            writer.writeByte(boardId);
            writer.writeUTF(boardName);
            writer.flush();

            sendMessage((byte) 76, baos.toByteArray());
            System.out.println(
                    "Sent command 76 response: bw.b=" + bw_b + ", boardID=" + boardId + ", name='" + boardName + "'");
        }

        private void handleCommand80(ServerMessage message) throws IOException {
            System.out.println("Processing command 80 request");

            DataInputStream reader = new DataInputStream(new ByteArrayInputStream(message.data));
            byte receivedByte = 0;
            if (message.data.length > 0) {
                receivedByte = reader.readByte();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(receivedByte); // client đọc bằng readByte()
            writer.writeBoolean(false); // client đọc bằng readBoolean()
            writer.flush();

            sendMessage((byte) 80, baos.toByteArray());
            System.out.println("Sent command 80 response: byte=" + receivedByte + ", bool=true");
        }

        private void handleCommand88(ServerMessage message) throws IOException {
            System.out.println("Processing command 88 request");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            byte len = 3; // số lượng chuỗi
            writer.writeByte(len);

            for (int i = 0; i < len; i++) {
                String name = "Entry_" + i;
                String desc = "Description_" + i;

                writer.writeUTF(name);
                writer.writeUTF(desc);

                System.out.println("88[" + i + "] name=" + name + " desc=" + desc);
            }

            writer.flush();
            sendMessage((byte) 88, baos.toByteArray());
            System.out.println("Sent command 88 response with " + len + " entries");
        }

        private void handleCommand90(ServerMessage message) throws IOException {
            System.out.println("Processing command 90 request (sending data pack)");

            DataInputStream reader = new DataInputStream(new ByteArrayInputStream(message.data));
            byte packType = reader.readByte(); // 0..3
            System.out.println("Requested pack type: " + packType);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            writer.writeByte(packType); // gói phản hồi cùng mã loại

            // Đường dẫn file thực tế
            String filePath = null;
            byte version = 1; // có thể tăng version nếu thay đổi file

            switch (packType) {
                case 0:
                    filePath = "data/tile.pack";
                    version = 10; // tileversion
                    break;
                case 1:
                    filePath = "data/icon.pack";
                    version = 8; // iconversion
                    break;
                case 2:
                    filePath = "data/values.pack";
                    version = 5; // valuesversion
                    break;
                case 3:
                    filePath = "data/player.pack";
                    version = 7; // playerversion
                    break;
                default:
                    System.out.println("Unknown pack type: " + packType);
                    return;
            }

            File f = new File(filePath);
            if (!f.exists()) {
                System.err.println("Pack file not found: " + filePath);
                return;
            }

            // Đọc toàn bộ file
            byte[] fileBytes = Files.readAllBytes(f.toPath());
            int len = fileBytes.length;

            // Ghi dữ liệu xuống buffer
            writer.writeByte(version);
            writer.writeShort(len);
            writer.write(fileBytes);

            writer.flush();

            // Gửi cho client
            sendMessage((byte) 90, baos.toByteArray());
            System.out.println("Sent pack type=" + packType + " (" + len + " bytes, version=" + version + ")");
        }

        private void handleCommand6(ServerMessage message) throws IOException {
            System.out.println("Processing command 6 request (sending room list)");

            // Ví dụ: server gửi danh sách 5 phòng
            int roomCount = 15;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            for (int i = 0; i < roomCount; i++) {
                byte a = (byte) i; // bt.a
                byte b = (byte) (10 + i); // bt.b (ví dụ số lượng người)
                byte skip = 5; // byte bỏ qua
                byte c = (byte) (1 + i); // bt.c (ví dụ trạng thái phòng)

                writer.writeByte(a);
                writer.writeByte(b);
                writer.writeByte(skip);
                writer.writeByte(c);

                System.out.println("Room " + i + " -> a=" + a + ", b=" + b + ", c=" + c);
            }

            writer.flush();

            // Gửi về client command = 6
            sendMessage((byte) 6, baos.toByteArray());
            System.out.println("Sent command 6 with " + roomCount + " rooms");
        }

        private void handleCommand7(ServerMessage message) throws IOException {
            System.out.println("Processing command 7 request (sending board list)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            // Byte đầu tiên (ed.m.e)
            byte eValue = 0; // ví dụ giá trị trạng thái bảng
            writer.writeByte(eValue);

            // Ví dụ: gửi danh sách 3 phần tử q
            int boardCount = 20;
            for (int i = 0; i < boardCount; i++) {
                byte a = (byte) i; // q.a
                byte b = (byte) 1; // q.b//
                byte f = (byte) 8; // q.f
                boolean c = false; // q.c
                int d = 1000; // q.d
                boolean skip = false; // boolean bỏ qua
                String e = "Khu vực " + i; // q.e
                byte g = (byte) 0; // q.g // gắn cờ 0 hoặc 1

                writer.writeByte(a);
                writer.writeByte(b);// số nguoi hiện tai
                writer.writeByte(f);// max so nguoi
                writer.writeBoolean(c);// dat mat khau
                writer.writeInt(d);// xu dat cuoc
                writer.writeBoolean(skip);
                writer.writeUTF(e);
                writer.writeByte(g);

                System.out.println("Board[" + i + "] -> a=" + a + ", b=" + b + ", f=" + f + ", c=" + c + ", d=" + d
                        + ", e=" + e + ", g=" + g);
            }

            writer.flush();

            // Gửi về client command = 7
            sendMessage((byte) 7, baos.toByteArray());
            System.out.println("Sent command 7 response with " + boardCount + " items");
        }

        private void handleCommand8(ServerMessage message) throws IOException {
            System.out.println("Processing command 8 request (sending entity list)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            // Hai giá trị int đầu tiên
            int var3 = 0; // 0=chủ hoặc 1=ok
            int var112 = -1; // tiền
            writer.writeInt(var3);
            writer.writeInt(var112);

            // Hai byte bỏ qua
            writer.writeByte(0);
            writer.writeByte(0);

            // Danh sách đối tượng i (ví dụ 3 phần tử)
            int player = 4;
            for (int i = 0; i < player; i++) {
                int b = i++; // i.b //iddb
                writer.writeInt(b);

                if (b == -1) {
                    // Không ghi thêm dữ liệu
                } else {
                    String name = "PlayerBot" + b;
                    int e = 0;// bỏ qua chỗ này
                    int value = 1;// level
                    byte c = (byte) i;// nhân vật
                    boolean d = true;// sẵn sàng

                    writer.writeUTF(name); // i.a
                    writer.writeInt(e); // i.e
                    writer.writeInt(value); // i.a(value)
                    writer.writeByte(c); // i.c// nhân vật đang sử dụng
                    writer.writeBoolean(d); // i.d

                    System.out.println("Entity[" + i + "] -> b=" + b + ", a=" + name + ", e=" + e + ", value=" + value
                            + ", c=" + c + ", d=" + d);
                }
            }

            writer.flush();

            sendMessage((byte) 8, baos.toByteArray());
            System.out.println("Sent command 8 response with " + player + " entities");
        }

        private void handleCommand9(ServerMessage message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            int value = 1234;
            String name = "PlayerJoin";
            writer.writeInt(value);
            writer.writeUTF(name);
            writer.flush();

            sendMessage((byte) 9, baos.toByteArray());
            System.out.println("Sent command 9: name=" + name + ", value=" + value);
        }

        private void handleCommand10(ServerMessage message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            String text = "abc";
            writer.writeUTF(text);
            writer.flush();

            sendMessage((byte) 10, baos.toByteArray());
            System.out.println("Sent command 10: message=" + text);
        }

        private void handleCommand11(ServerMessage message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            short unk = 0;
            int kickId = 999;
            writer.writeShort(unk);
            writer.writeInt(kickId);
            writer.flush();

            sendMessage((byte) 11, baos.toByteArray());
            System.out.println("Sent command 11: kick id=" + kickId);
        }

        private void handleCommand12(ServerMessage message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            byte boardId = 1;
            int playerId = 4321;
            String playerName = "HeroJoined";
            int e = 200;
            int val = 100;
            byte c = 1;

            writer.writeByte(boardId);
            writer.writeInt(playerId);
            writer.writeUTF(playerName);
            writer.writeInt(e);
            writer.writeInt(val);
            writer.writeByte(c);
            writer.flush();

            sendMessage((byte) 12, baos.toByteArray());
            System.out.println("Sent command 12: player=" + playerName + ", id=" + playerId);
        }

        private void handleCommand29(ServerMessage message) throws IOException {
            System.out.println("Processing command 29 request (sending item list)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            int itemCount = 4;
            for (int i = 0; i < itemCount; i++) {
                int b = 100 + i;
                String name = "PlayerBot" + b;
                int e = 1000 + i;
                byte c = (byte) (i % 4);
                boolean d = (i % 2 == 0);
                int h = 2000 + i;
                int g = 3000 + i;

                writer.writeInt(b);
                writer.writeUTF(name);
                writer.writeInt(e);
                writer.writeByte(c);
                writer.writeByte(d ? 1 : 0);
                writer.writeInt(h);
                writer.writeInt(g);

                System.out.println("Item[" + i + "] -> b=" + b + ", name=" + name + ", e=" + e + ", c=" + c + ", d=" + d
                        + ", h=" + h + ", g=" + g);
            }

            writer.flush();
            sendMessage((byte) 29, baos.toByteArray());
            System.out.println("Sent command 29 response with " + itemCount + " items");
        }

        private void handleCommand30(ServerMessage message) throws IOException {
            System.out.println("Processing command 30 request (sending categorized items)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            byte category = 1; // var125
            writer.writeByte(category);

            int itemCount = 3;
            for (int i = 0; i < itemCount; i++) {
                int b = 500 + i;
                String name = "PlayerBot" + b;
                int e = 2000 + i;
                byte c = (byte) (i + 1);
                int extra = 9000 + i;
                int h = 4000 + i;
                int g = 5000 + i;

                writer.writeInt(b);
                writer.writeUTF(name);
                writer.writeInt(e);
                writer.writeByte(c);
                writer.writeInt(extra); // a(var1.b().readInt())
                writer.writeInt(h);
                writer.writeInt(g);

                // var132.d = true
                System.out.println("Category[" + i + "] -> b=" + b + ", name=" + name + ", e=" + e + ", c=" + c
                        + ", extra=" + extra + ", h=" + h + ", g=" + g);
            }

            writer.flush();
            sendMessage((byte) 30, baos.toByteArray());
            System.out.println("Sent command 30 response with " + itemCount + " categorized items");
        }

        private void handleCommand34(ServerMessage message) throws IOException {
            System.out.println("Processing command 34 request (sending player info)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            boolean sendValid = true; // đổi thành false để test nhánh -1

            if (!sendValid) {
                // Gửi gói báo lỗi (-1)
                writer.writeInt(-1);
                sendMessage((byte) 34, baos.toByteArray());
                System.out.println("Sent command 34 response: invalid player (-1)");
                return;
            }

            // --- Gói hợp lệ ---
            int id = 1234;
            String playerName = "buinhulac";
            int var151 = 1500; // ví dụ: sức mạnh
            int var152 = 5; // level (truyền vào i.a())
            int var78 = 320; // máu
            int var153 = 200; // tấn công
            int var80 = 400; // phòng thủ
            int var154 = 50; // cộng thêm tấn công
            int var155 = 30; // số kill
            int var156 = 10; // số chết

            writer.writeInt(id);
            writer.writeUTF(playerName);
            writer.writeInt(var151);
            writer.writeInt(var152);
            writer.writeInt(var78);
            writer.writeInt(var153);
            writer.writeInt(var80);
            writer.writeInt(var154);
            writer.writeInt(var155);
            writer.writeInt(var156);

            writer.flush();

            sendMessage((byte) 34, baos.toByteArray());
            System.out.println("Sent command 34 response for player '" + playerName + "'");
        }

        private void handleCommand20(ServerMessage message) throws IOException {
            System.out.println("Processing command 20 request (START ARMY)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            byte mapId = 3; // var111
            byte stageId = 1; // var125

            writer.writeByte(mapId);
            writer.writeByte(stageId);

            // 8 cặp short X, Y
            short[] x = new short[8];
            short[] y = new short[8];

            for (int i = 0; i < 8; i++) {
                // ví dụ: 1 nửa có vị trí hợp lệ, nửa còn lại rỗng
                if (i < 4) {
                    x[i] = (short) (100 + i * 10);
                    y[i] = (short) (200 + i * 10);
                } else {
                    x[i] = -1;
                    y[i] = -1;
                }

                writer.writeShort(x[i]);
                if (x[i] != -1) {
                    writer.writeShort(y[i]);
                }

                System.out.println("Slot[" + i + "] -> X=" + x[i] + " Y=" + y[i]);
            }

            writer.flush();

            sendMessage((byte) 20, baos.toByteArray());
            System.out.println("Sent command 20 (START ARMY) with map=" + mapId + ", stage=" + stageId);
        }

        private void handleGameAction(ServerMessage message) throws IOException {
            System.out.println("Processing game action");
            sendMessage((byte) 2, "Action processed".getBytes());
        }

        public void sendMessage(byte command, byte[] data) throws IOException {
            synchronized (dos) {
                if (keyComplete) {
                    dos.writeByte(writeKey(command));
                } else {
                    dos.writeByte(command);
                }

                int size = data != null ? data.length : 0;

                if (keyComplete) {
                    dos.writeByte(writeKey((byte) (size >> 8)));
                    dos.writeByte(writeKey((byte) (size & 0xFF)));
                } else {
                    dos.writeShort(size);
                }

                if (data != null && size > 0) {
                    byte[] encryptedData = data.clone();
                    if (keyComplete) {
                        for (int i = 0; i < encryptedData.length; i++) {
                            encryptedData[i] = writeKey(encryptedData[i]);
                        }
                    }
                    dos.write(encryptedData);
                }

                dos.flush();
            }
        }

        private byte readKey(byte b) {
            byte i = (byte) ((key[curR++] & 0xff) ^ (b & 0xff));
            if (curR >= key.length) {
                curR %= key.length;
            }
            return i;
        }

        private byte writeKey(byte b) {
            byte i = (byte) ((key[curW++] & 0xff) ^ (b & 0xff));
            if (curW >= key.length) {
                curW %= key.length;
            }
            return i;
        }

        public void disconnect() {
            this.connected = false;
            clients.remove(this);
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                if (dis != null)
                    dis.close();
                if (dos != null)
                    dis.close();
                key = null;
                dis = null;
                dos = null;
                curR = 0;
                curW = 0;
                System.gc();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected");
        }
    }

    static class ServerMessage {
        byte command;
        byte[] data;

        ServerMessage(byte command, byte[] data) {
            this.command = command;
            this.data = data;
        }
    }
}