package com.mobiarmy.server;

import com.mobiarmy.manager.*;
import com.mobiarmy.model.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkServer {

    private static final byte[] ENCRYPTION_KEY = "bth.army2.ml".getBytes();
    private static final byte MAP_COLLISION_VERSION = 1;
    private static final byte NV_VERSION = 1;
    private static final byte ICON_VERSION = 1;
    private static final byte VALUES_VERSION = 1;
    private static final byte PLAYER_VERSION = 1;

    private ServerSocket serverSocket;
    private final int port;
    private final ExecutorService threadPool;
    private volatile boolean running;
    private final List<ClientHandler> clients;
    private final AtomicInteger connectedCount;
    private final AtomicInteger totalConnections;
    private volatile long startTime;

    public NetworkServer(int port) {
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool();
        this.clients = new CopyOnWriteArrayList<>();
        this.running = false;
        this.connectedCount = new AtomicInteger(0);
        this.totalConnections = new AtomicInteger(0);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        startTime = System.currentTimeMillis();

        System.out.println("============================================");
        System.out.println("  MobiArmy Server v1.6.5 - Khoi dong...");
        System.out.println("============================================");
        System.out.println("  Port: " + port);
        System.out.println("  Khoi tao du lieu...");
        DataManager.getInstance();
        RoomManager.getInstance();
        PlayerManager.getInstance();
        System.out.println("  Da khoi tao xong!");
        System.out.println("  Cho ket noi tu client...");
        System.out.println("============================================");
        System.out.println();

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setTcpNoDelay(true);
                clientSocket.setSoTimeout(0);

                String clientIP = clientSocket.getInetAddress().getHostAddress();
                int connId = totalConnections.incrementAndGet();

                System.out.println("[CONNECT] Client #" + connId + " tu " + clientIP + " da ket noi.");
                System.out.println("  Tong ket noi hien tai: " + connectedCount.incrementAndGet());

                ClientHandler handler = new ClientHandler(clientSocket, connId, this);
                clients.add(handler);
                threadPool.execute(handler);
            } catch (IOException e) {
                if (running) {
                    System.err.println("Loi chap nhan ket noi: " + e.getMessage());
                }
            }
        }
    }

    public void stop() {
        running = false;
        System.out.println("\n[DANG DUNG] Server dang tat...");
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler client : clients) {
                client.disconnect();
            }
            threadPool.shutdown();
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
            Database.getInstance().saveAll();
        } catch (Exception e) {
            System.err.println("Loi khi dung server: " + e.getMessage());
        }
        printShutdownStats();
    }

    private void printShutdownStats() {
        long uptime = System.currentTimeMillis() - startTime;
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        System.out.println("\n============================================");
        System.out.println("  Server da tat.");
        System.out.println("  Tong ket noi: " + totalConnections.get());
        System.out.println("  Thoi gian chay: " + minutes + " phut");
        System.out.println("============================================");
    }

    void removeClient(ClientHandler handler) {
        clients.remove(handler);
        connectedCount.decrementAndGet();
    }

    public int getOnlineCount() {
        return connectedCount.get();
    }

    public boolean isRunning() {
        return running;
    }

    // ============================================================
    // INNER CLASS: ClientHandler
    // ============================================================
    public static class ClientHandler implements Runnable {

        private final Socket socket;
        private final int connectionId;
        private final NetworkServer server;
        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean connected = true;
        private PlayerSession session;
        private volatile long lastPingTime;

        public ClientHandler(Socket socket, int connectionId, NetworkServer server) {
            this.socket = socket;
            this.connectionId = connectionId;
            this.server = server;
            this.lastPingTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                socket.setSoTimeout(5000);

                byte handshakeCmd = dis.readByte();
                System.out.println("[#" + connectionId + "] Byte dau tien nhan: " + handshakeCmd + " (0x" + String.format("%02X", handshakeCmd) + ")");
                if (handshakeCmd == -27) {
                    sendHandshake();
                    encryptionActive = true;
                    System.out.println("[#" + connectionId + "] Da gui encryption key, encryption bat");
                } else {
                    System.out.println("[#" + connectionId + "] Lenh dau tien khong phai key request: " + handshakeCmd);
                }

                socket.setSoTimeout(0);

                while (connected) {
                    ServerMessage message = readMessage();
                    if (message == null) break;
                    System.out.println("[#" + connectionId + "] Da nhan msg: cmd=" + message.command + " size=" + message.data.length);
                    if (message.data.length > 0) {
                        StringBuilder hex = new StringBuilder();
                        for (byte b : message.data) hex.append(String.format("%02X ", b & 0xff));
                        System.out.println("[#" + connectionId + "] Data hex: " + hex);
                    }
                    processMessage(message);
                }
            } catch (EOFException e) {
                System.out.println("[DISCONNECT#" + connectionId + "] Client dong ket noi (EOF).");
            } catch (SocketException e) {
                System.out.println("[DISCONNECT#" + connectionId + "] Loi socket: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("[ERROR#" + connectionId + "] Loi doc: " + e.getMessage());
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private void sendHandshake() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(baos);
            dataOut.writeByte(ENCRYPTION_KEY.length);
            dataOut.writeByte(ENCRYPTION_KEY[0]);
            for (int i = 1; i < ENCRYPTION_KEY.length; i++) {
                dataOut.writeByte(ENCRYPTION_KEY[i] ^ ENCRYPTION_KEY[i - 1]);
            }
            dataOut.flush();

            byte[] keyData = baos.toByteArray();
            dos.writeByte(-27);
            dos.writeShort(keyData.length);
            dos.write(keyData);
            dos.flush();
        }

        private byte[] key = ENCRYPTION_KEY;
        private byte curR = 0, curW = 0;
        private boolean encryptionActive = false;

        private ServerMessage readMessage() throws IOException {
            try {
                byte rawCmd = dis.readByte();
                byte cmd = rawCmd;
                if (encryptionActive) {
                    cmd = readKey(rawCmd);
                }

                int size;
                if (encryptionActive) {
                    byte b1 = dis.readByte();
                    byte b2 = dis.readByte();
                    size = (readKey(b1) & 0xff) << 8 | (readKey(b2) & 0xff);
                } else {
                    size = dis.readUnsignedShort();
                }

                byte[] data = new byte[size];
                if (size > 0) {
                    dis.readFully(data);
                }

                if (encryptionActive && size > 0) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = readKey(data[i]);
                    }
                }

                return new ServerMessage(cmd, data);
            } catch (EOFException e) {
                return null;
            }
        }

        private void processMessage(ServerMessage message) {
            System.out.println("[MSG#" + connectionId + "] cmd=" + message.command
                    + (session != null && session.player != null
                        ? " user=" + session.player.username : ""));

            try {
                switch (message.command) {
                    case 1:
                        handleLoginRequest(message);
                        break;
                    case 2:
                        handleGameAction(message);
                        break;
                    case 5:
                        handleCharSelect(message);
                        break;
                    case 6:
                        handleRoomListRequest(message);
                        break;
                    case 7:
                        handleBoardListRequest(message);
                        break;
                    case 8:
                        handleJoinRoom(message);
                        break;
                    case 9:
                        handleChat(message);
                        break;
                    case 11:
                        handleKick(message);
                        break;
                    case 12:
                        handleReady(message);
                        break;
                    case 15:
                        handleLeaveRoom(message);
                        break;
                    case 16:
                        handleReady(message);
                        break;
                    case 20:
                        handleStartGame(message);
                        break;
                    case 22:
                        handleShoot(message);
                        break;
                    case 26:
                        handleUseItem(message);
                        break;
                    case 28:
                        handleQuickJoin(message);
                        break;
                    case 29:
                        handlePlayerInfo(message);
                        break;
                    case 34:
                        handlePlayerInfo(message);
                        break;
                    case 42:
                    case 58:
                    case 127:
                        handlePing(message);
                        break;
                    case 54:
                        handleSetRoomName(message);
                        break;
                    case 56:
                        handleSetMaxPlayers(message);
                        break;
                    case 64:
                        handleNVDataRequest(message);
                        break;
                    case 69:
                        handleCharSelect(message);
                        break;
                    case 72:
                        handleBuyItem(message);
                        break;
                    case 74:
                        handleBuyChar(message);
                        break;
                    case 75:
                        handleSelectMap(message);
                        break;
                    case 77:
                        handleNapThe(message);
                        break;
                    case 83:
                        handleStartArmy(message);
                        break;
                    case 88:
                        handleEntryListRequest(message);
                        break;
                    case 90:
                        handleDataPackRequest(message);
                        break;
                    case 92:
                        handleMapCollisionRequest(message);
                        break;
                    case 97:
                        handleLuckyBox(message);
                        break;
                    case 102:
                        handleEquipItem(message);
                        break;
                    case 110:
                        handleQuaySo(message);
                        break;
                    default:
                        System.out.println("[MSG#" + connectionId + "] Lenh chua xu ly: cmd=" + message.command);
                }
            } catch (Exception e) {
                System.err.println("[MSG#" + connectionId + "] Loi xu ly cmd=" + message.command + ": " + e.getMessage());
            }
        }

        // ============================================================
        // LOGIN
        // ============================================================
        private void handleLoginRequest(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);

            String username = reader.readUTF().trim();
            String password = reader.readUTF().trim();
            String version = reader.readUTF().trim();

            System.out.println("[LOGIN#" + connectionId + "] user=" + username + " ver=" + version);

            if (username.isEmpty() || password.isEmpty()) {
                sendError((byte) 4, "Tai khoan hoac mat khau khong hop le!");
                return;
            }

            PlayerManager pm = PlayerManager.getInstance();
            if (pm.getPlayerByUsername(username) == null) {
                pm.registerPlayer(username, password);
                System.out.println("[LOGIN#" + connectionId + "] Da dang ky: " + username);
            }

            Database db = Database.getInstance();
            Player savedPlayer = db.loadPlayer(username);
            if (savedPlayer != null) {
                savedPlayer.session = null;
                pm.registerPlayer(username, password);
                Player p = pm.getPlayerByUsername(username);
                p.xu = savedPlayer.xu;
                p.exp = savedPlayer.exp;
                p.selectedChar = savedPlayer.selectedChar;
                p.level = savedPlayer.level;
                p.blood = savedPlayer.blood;
                p.attack = savedPlayer.attack;
                p.defense = savedPlayer.defense;
                p.kill = savedPlayer.kill;
                p.death = savedPlayer.death;
                p.charLevels = savedPlayer.charLevels;
                p.itemCounts = savedPlayer.itemCounts;
                p.updateStats();
            }

            session = pm.loginPlayer(username, password, socket.getInetAddress().getHostAddress());
            if (session == null) {
                sendError((byte) 4, "Dang nhap that bai!");
                return;
            }
            session.clientHandler = this;
            encryptionActive = true;

            System.out.println("[LOGIN#" + connectionId + "] Thanh cong! PlayerID=" + session.player.id);

            sendLoginSuccess();
        }

        private void sendLoginSuccess() throws IOException {
            sendNVData();
            sendRoomTypes();
            sendAreaNames();
            sendMapCollision();
            handleRoomListRequest(new ServerMessage((byte) 6, new byte[0]));
            sendUpdateXu();
        }

        private void sendUpdateXu() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeInt(session.player.xu);
            writer.writeInt(0);
            writer.flush();
            sendMessage((byte) 105, baos.toByteArray());
        }

        private void sendNVData() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            byte numNV = 8;
            writer.writeByte(numNV);
            for (int i = 0; i < numNV; i++) {
                writer.writeByte(30 + i);
            }

            writer.writeByte(numNV);
            for (int i = 0; i < numNV; i++) {
                writer.writeShort(150);
            }

            writer.writeByte(numNV);
            for (int i = 0; i < numNV; i++) {
                writer.writeByte(5);
            }

            writer.writeByte(numNV);
            for (int i = 0; i < numNV; i++) {
                writer.writeByte(10);
            }

            writer.writeByte((byte) 100);

            writer.writeByte((byte) 10);
            for (int i = 0; i < 10; i++) {
                writer.writeByte((byte) (30 + i));
            }

            for (int i = 0; i < 10; i++) {
                writer.writeByte((byte) (12 + i));
            }

            writer.writeByte((byte) 100);
            writer.flush();
            sendMessage((byte) 64, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui NV data (cmd=64)");
        }

        private void sendRoomTypes() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            writer.writeByte(7);
            String[] vietNames = {"PHONG SO CAP", "PHONG TRUNG CAP", "PHONG VIP", "PHONG DAU TRUONG", "PHONG TU DO", "PHONG DAU TRUM", "PHONG DAU DOI"};
            String[] engNames = {"NEWBIE ROOM", "INTERMEDIATE", "VIP ROOM", "ARENA", "FREEDOM ROOM", "BOSS BATTLE", "CLAN BATTLE"};
            for (int i = 0; i < 7; i++) {
                writer.writeUTF(vietNames[i]);
                writer.writeUTF(engNames[i]);
            }
            writer.flush();
            sendMessage((byte) 88, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui room types (cmd=88)");
        }

        private void sendAreaNames() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            String[] areaNames = {"Bom", "Nhen may", "Nguoi may", "T-rex", "UFO", "Khi cay", "Nhen doc", "Ma", "Truom lien hoan"};
            writer.writeByte(areaNames.length);
            for (int i = 0; i < areaNames.length; i++) {
                writer.writeByte(i);
                writer.writeUTF("Phong " + i + ": " + areaNames[i]);
                writer.writeByte(i);
            }
            writer.flush();
            sendMessage((byte) -19, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui area names (cmd=-19)");
        }

        private void sendMapCollision() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            short[] noCollision = {0, 1, 2, 3, 4, 5};
            writer.writeShort(noCollision.length);
            for (short id : noCollision) {
                writer.writeShort(id);
            }
            writer.flush();
            sendMessage((byte) 92, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui map collision (cmd=92)");
        }

        private void sendError(byte cmd, String message) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeUTF(message);
            writer.flush();
            sendMessage(cmd, baos.toByteArray());
        }

        // ============================================================
        // ROOM LIST (cmd=6)
        // ============================================================
        private void handleRoomListRequest(ServerMessage message) throws IOException {
            System.out.println("[MSG#" + connectionId + "] Room list request");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            RoomManager rm = RoomManager.getInstance();
            int count = 0;
            for (Room room : rm.getAllRooms()) {
                if (count >= 30) break;
                writer.writeByte(room.id);
                writer.writeByte(room.currentPlayers >= room.maxPlayers ? (byte) 0 : (byte) 1);
                writer.writeByte(0);
                writer.writeByte((byte) room.mapId);
                count++;
            }

            writer.flush();
            sendMessage((byte) 6, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui room list (cmd=6), count=" + count);
        }

        // ============================================================
        // BOARD LIST (cmd=7)
        // ============================================================
        private void handleBoardListRequest(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);

            byte roomId = 0;
            if (message.data.length > 0) {
                roomId = reader.readByte();
            }

            System.out.println("[MSG#" + connectionId + "] Board list request (cmd=7) roomId=" + roomId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            RoomManager rm = RoomManager.getInstance();
            int count = 0;
            for (Room room : rm.getAllRooms()) {
                writer.writeByte(room.id);
                writer.writeByte((byte) room.currentPlayers);
                writer.writeByte((byte) room.maxPlayers);
                writer.writeBoolean(room.hasPassword);
                writer.writeInt(room.betXu);
                writer.writeBoolean(false);
                writer.writeUTF(room.name);
                writer.writeByte(0);
                count++;
                if (count >= 50) break;
            }

            writer.flush();
            sendMessage((byte) 7, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui " + count + " boards (cmd=7)");
        }

        // ============================================================
        // JOIN ROOM (cmd=8)
        // ============================================================
        private void handleJoinRoom(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);

            byte roomNumber = reader.readByte();
            byte areaNumber = reader.readByte();
            String password = "";
            if (message.data.length > 2) {
                password = reader.readUTF();
            }

            System.out.println("[ROOM#" + connectionId + "] Vao phong: room=" + roomNumber + " area=" + areaNumber);

            RoomManager rm = RoomManager.getInstance();
            Room room = rm.getRoomById(roomNumber);

            if (room == null) {
                sendError((byte) 45, "Phong khong ton tai!");
                return;
            }

            if (room.hasPassword && !password.isEmpty()) {
                String enteredPass = "";
                try {
                    ByteArrayInputStream pbis = new ByteArrayInputStream(
                        Arrays.copyOfRange(message.data, 2, message.data.length));
                    DataInputStream pbisReader = new DataInputStream(pbis);
                    enteredPass = pbisReader.readUTF();
                } catch (Exception e) {
                }
                if (!enteredPass.equals(password)) {
                    sendError((byte) 45, "Sai mat khau!");
                    return;
                }
            }

            if (room.currentPlayers >= room.maxPlayers) {
                sendError((byte) 45, "Phong da day!");
                return;
            }

            if (session != null && session.player != null) {
                room.join(session.player);
                session.joinRoom(room);
                session.player.currentCharId = (byte) session.player.selectedChar;
                session.player.ready = false;
            }

            System.out.println("[ROOM#" + connectionId + "] Vao phong thanh cong");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(roomNumber);
            writer.flush();
            sendMessage((byte) 7, baos.toByteArray());

            sendRoomPlayers(room);
        }

        private void sendRoomPlayers(Room room) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            for (Player p : room.players) {
                writer.writeInt(p.id);
                writer.writeUTF(p.displayName);
                writer.writeByte(0);
                writer.writeByte(p.currentCharId);
                writer.writeBoolean(p.ready);
            }

            byte[] data = baos.toByteArray();
            for (Player p : room.players) {
                if (p.session != null) {
                    p.session.sendMessage((byte) 8, data);
                }
            }
        }

        // ============================================================
        // CHAT (cmd=9)
        // ============================================================
        private void handleChat(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            int targetId = reader.readInt();
            String text = reader.readUTF();

            System.out.println("[CHAT#" + connectionId + "] [" + session.player.username + "]: " + text);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeInt(session.player.id);
            writer.writeUTF(session.player.displayName);
            writer.writeUTF(text);
            byte[] data = baos.toByteArray();

            if (session != null && session.currentRoom != null) {
                for (Player p : session.currentRoom.players) {
                    if (p.session != null) {
                        p.session.sendMessage((byte) 9, data);
                    }
                }
            } else {
                sendMessage((byte) 9, data);
            }
        }

        // ============================================================
        // LEAVE ROOM (cmd=15)
        // ============================================================
        private void handleLeaveRoom(ServerMessage message) throws IOException {
            System.out.println("[ROOM#" + connectionId + "] Roi phong");

            if (session != null && session.player != null && session.currentRoom != null) {
                Room room = session.currentRoom;
                Player leavingPlayer = session.player;

                room.leave(leavingPlayer);
                session.leaveRoom();

                sendRoomPlayers(room);

                if (room.players.isEmpty()) {
                    RoomManager.getInstance().removeRoom(room.id);
                }
            }
        }

        // ============================================================
        // READY (cmd=12, cmd=16)
        // ============================================================
        private void handleReady(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);

            boolean ready = false;
            if (message.data.length > 0) {
                ready = reader.readBoolean();
            }

            System.out.println("[READY#" + connectionId + "] " + session.player.username + " ready=" + ready);

            if (session != null && session.player != null && session.currentRoom != null) {
                Room room = session.currentRoom;
                session.player.ready = ready;
                room.setReady(session.player, ready);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream writer = new DataOutputStream(baos);
                writer.writeInt(session.player.id);
                writer.writeBoolean(ready);
                byte[] data = baos.toByteArray();

                for (Player p : room.players) {
                    if (p.session != null) {
                        p.session.sendMessage((byte) 12, data);
                    }
                }

                checkAutoStart(room);
            }
        }

        private void checkAutoStart(Room room) throws IOException {
            if (room.state != Room.RoomState.WAITING) return;
            if (room.readyPlayers.size() < room.players.size()) return;
            if (room.players.size() < 1) return;

            room.startGame();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(room.mapId);
            writer.writeByte(0);

            for (int i = 0; i < 8; i++) {
                if (i < room.players.size()) {
                    Player p = room.players.get(i);
                    writer.writeShort(p.posX);
                    writer.writeShort(p.posY);
                } else {
                    writer.writeShort(-1);
                    writer.writeShort(-1);
                }
            }
            writer.flush();

            for (Player p : room.players) {
                if (p.session != null) {
                    p.session.sendMessage((byte) 20, baos.toByteArray());
                }
            }
            System.out.println("[ROOM] Phong " + room.id + " bat dau!");
        }

        // ============================================================
        // KICK (cmd=11)
        // ============================================================
        private void handleKick(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            int playerId = reader.readInt();

            if (session == null || session.currentRoom == null) return;

            Room room = session.currentRoom;
            Player target = null;
            for (Player p : room.players) {
                if (p.id == playerId) {
                    target = p;
                    break;
                }
            }

            if (target != null && target.session != null) {
                ByteArrayOutputStream kickNotif = new ByteArrayOutputStream();
                DataOutputStream w = new DataOutputStream(kickNotif);
                w.writeShort(0);
                w.writeInt(target.id);
                w.flush();
                target.session.sendMessage((byte) 11, kickNotif.toByteArray());
            }

            if (target != null) {
                room.leave(target);
                if (target.session != null) target.session.leaveRoom();
                sendRoomPlayers(room);
            }
        }

        // ============================================================
        // START GAME (cmd=20)
        // ============================================================
        private void handleStartGame(ServerMessage message) throws IOException {
            if (session != null && session.currentRoom != null) {
                checkAutoStart(session.currentRoom);
            }
        }

        // ============================================================
        // QUICK JOIN (cmd=28)
        // ============================================================
        private void handleQuickJoin(ServerMessage message) throws IOException {
            System.out.println("[MSG#" + connectionId + "] Choi ngay (cmd=28)");

            RoomManager rm = RoomManager.getInstance();
            Room found = null;
            for (Room room : rm.getAllRooms()) {
                if (room.currentPlayers < room.maxPlayers) {
                    found = room;
                    break;
                }
            }

            if (found == null) {
                found = rm.createRoom("Phong " + System.currentTimeMillis(), 8, 1000, (byte) 1);
            }

            if (session != null && session.player != null) {
                found.join(session.player);
                session.joinRoom(found);
                session.player.currentCharId = (byte) session.player.selectedChar;
                session.player.ready = false;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(found.id);
            writer.flush();
            sendMessage((byte) 7, baos.toByteArray());
            sendRoomPlayers(found);
        }

        // ============================================================
        // CHARACTER SELECT (cmd=5, cmd=69)
        // ============================================================
        private void handleCharSelect(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);

            byte charId = reader.readByte();

            System.out.println("[CHAR#" + connectionId + "] Chon nhan vat id=" + charId);

            if (session != null && session.player != null) {
                session.player.currentCharId = charId;
                session.player.selectedChar = charId;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(charId);
            writer.writeInt(session != null ? session.player.attack : 200);
            writer.writeInt(session != null ? session.player.defense : 400);
            writer.writeInt(session != null ? session.player.blood : 320);
            writer.flush();
            sendMessage((byte) 5, baos.toByteArray());
        }

        // ============================================================
        // GAME ACTION (cmd=2)
        // ============================================================
        private void handleGameAction(ServerMessage message) throws IOException {
            if (session == null || session.player == null) return;

            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            int actionType = bais.read();

            switch (actionType) {
                case 1: {
                    int x = bais.read() | (bais.read() << 8);
                    int y = bais.read() | (bais.read() << 8);
                    session.player.posX = x;
                    session.player.posY = y;

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    DataOutputStream w = new DataOutputStream(out);
                    w.writeInt(session.player.id);
                    w.writeByte(1);
                    w.writeShort(x);
                    w.writeShort(y);
                    broadcastGameAction((byte) 2, out.toByteArray());
                    break;
                }
                case 2: {
                    int targetId = bais.read() | (bais.read() << 8) | (bais.read() << 16) | (bais.read() << 24);
                    int damage = session.player.attack;

                    Player target = findPlayerInRoom(targetId);
                    if (target != null) {
                        target.takeDamage(damage);

                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        DataOutputStream w = new DataOutputStream(out);
                        w.writeInt(session.player.id);
                        w.writeByte(2);
                        w.writeInt(targetId);
                        w.writeInt(target.hp);
                        w.writeBoolean(!target.isAlive());
                        broadcastGameAction((byte) 2, out.toByteArray());

                        if (!target.isAlive()) {
                            session.player.kill++;
                            target.death++;
                            session.player.addExp(50);
                        }
                    }
                    break;
                }
                case 5: {
                    session.player.respawn();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    DataOutputStream w = new DataOutputStream(out);
                    w.writeInt(session.player.id);
                    w.writeByte(5);
                    w.writeShort(session.player.posX);
                    w.writeShort(session.player.posY);
                    w.writeInt(session.player.hp);
                    broadcastGameAction((byte) 2, out.toByteArray());
                    break;
                }
            }
        }

        // ============================================================
        // SHOOT (cmd=22)
        // ============================================================
        private void handleShoot(ServerMessage message) throws IOException {
            if (session == null || session.currentRoom == null) return;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream w = new DataOutputStream(out);
            w.writeInt(session.player.id);
            w.write(message.data);
            broadcastGameAction((byte) 2, out.toByteArray());
        }

        // ============================================================
        // USE ITEM (cmd=26)
        // ============================================================
        private void handleUseItem(ServerMessage message) throws IOException {
            if (session == null || session.currentRoom == null) return;

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream w = new DataOutputStream(out);
            w.writeInt(session.player.id);
            w.write(message.data);
            broadcastGameAction((byte) 2, out.toByteArray());
        }

        // ============================================================
        // BUY ITEM (cmd=72)
        // ============================================================
        private void handleBuyItem(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            byte itemType = reader.readByte();
            int itemId = reader.readByte() & 0xFF;

            System.out.println("[SHOP#" + connectionId + "] Mua item: type=" + itemType + " id=" + itemId);

            if (session == null || session.player == null) {
                sendError((byte) 45, "Chua dang nhap!");
                return;
            }

            int price = 1000;
            if (session.player.xu < price) {
                sendError((byte) 45, "Khong du xu!");
                return;
            }

            session.player.spendXu(price);
            int idx = itemId % session.player.itemCounts.length;
            session.player.itemCounts[idx]++;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeInt(session.player.xu);
            writer.flush();
            sendMessage((byte) 105, baos.toByteArray());
            sendUpdateXu();
        }

        // ============================================================
        // BUY CHAR (cmd=74)
        // ============================================================
        private void handleBuyChar(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            byte charId = reader.readByte();

            System.out.println("[SHOP#" + connectionId + "] Mua nhan vat: " + charId);
        }

        // ============================================================
        // SELECT MAP (cmd=75)
        // ============================================================
        private void handleSelectMap(ServerMessage message) throws IOException {
            if (session == null || session.currentRoom == null) return;

            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream dis = new DataInputStream(bais);
            byte mapId = dis.readByte();

            session.currentRoom.mapId = mapId;
            System.out.println("[ROOM#" + connectionId + "] Chon map=" + mapId);
        }

        // ============================================================
        // SET ROOM NAME (cmd=54)
        // ============================================================
        private void handleSetRoomName(ServerMessage message) throws IOException {
            if (session == null || session.currentRoom == null) return;

            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream dis = new DataInputStream(bais);
            String name = dis.readUTF();
            session.currentRoom.name = name;
            System.out.println("[ROOM#" + connectionId + "] Dat ten phong: " + name);
        }

        // ============================================================
        // SET MAX PLAYERS (cmd=56)
        // ============================================================
        private void handleSetMaxPlayers(ServerMessage message) throws IOException {
            if (session == null || session.currentRoom == null) return;

            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream dis = new DataInputStream(bais);
            byte max = dis.readByte();
            session.currentRoom.maxPlayers = max;
            System.out.println("[ROOM#" + connectionId + "] Dat so nguoi toi da: " + max);
        }

        // ============================================================
        // NAPT THE (cmd=77)
        // ============================================================
        private void handleNapThe(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            String type = reader.readUTF().trim();
            String seri = reader.readUTF().trim();
            String maso = reader.readUTF().trim();

            System.out.println("[NAPTHE#" + connectionId + "] type=" + type + " seri=" + seri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeUTF("The cao: " + type + " (seri: " + seri + ")");
            writer.flush();
            sendMessage((byte) 45, baos.toByteArray());
        }

        // ============================================================
        // START ARMY / TRAINING (cmd=83)
        // ============================================================
        private void handleStartArmy(ServerMessage message) throws IOException {
            System.out.println("[MSG#" + connectionId + "] Bat dau luyen tap (cmd=83)");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(1);
            writer.writeByte(0);
            for (int i = 0; i < 8; i++) {
                writer.writeShort(100 + i * 10);
                writer.writeShort(200);
            }
            writer.flush();
            sendMessage((byte) 20, baos.toByteArray());
        }

        // ============================================================
        // NV DATA REQUEST (cmd=64)
        // ============================================================
        private void handleNVDataRequest(ServerMessage message) throws IOException {
            sendNVData();
        }

        // ============================================================
        // PLAYER INFO (cmd=29, cmd=34)
        // ============================================================
        private void handlePlayerInfo(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            int playerId = -1;
            if (message.data.length >= 4) {
                playerId = reader.readInt();
            }

            Player target = null;
            if (playerId > 0) {
                for (PlayerSession s : PlayerManager.getInstance().getAllSessions()) {
                    if (s.player.id == playerId) {
                        target = s.player;
                        break;
                    }
                }
            }
            if (target == null && session != null) {
                target = session.player;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            if (target == null) {
                writer.writeInt(-1);
            } else {
                writer.writeInt(target.id);
                writer.writeUTF(target.displayName);
                writer.writeInt(target.power);
                writer.writeInt(target.level);
                writer.writeInt(target.hp);
                writer.writeInt(target.attack);
                writer.writeInt(target.defense);
                writer.writeInt(target.extraAttack);
                writer.writeInt(target.kill);
                writer.writeInt(target.death);
            }
            writer.flush();
            sendMessage((byte) 34, baos.toByteArray());
        }

        // ============================================================
        // ENTRY LIST (cmd=88)
        // ============================================================
        private void handleEntryListRequest(ServerMessage message) throws IOException {
            sendRoomTypes();
        }

        // ============================================================
        // MAP COLLISION (cmd=92)
        // ============================================================
        private void handleMapCollisionRequest(ServerMessage message) throws IOException {
            sendMapCollision();
        }

        // ============================================================
        // DATA PACK (cmd=90)
        // ============================================================
        private void handleDataPackRequest(ServerMessage message) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.data);
            DataInputStream reader = new DataInputStream(bais);
            byte packType = reader.readByte();
            byte clientVersion = reader.readByte();

            System.out.println("[MSG#" + connectionId + "] Data pack request: type=" + packType + " clientVer=" + clientVersion);

            String filePath = null;
            byte serverVersion = 1;

            switch (packType) {
                case 1: filePath = "data/icon.pack"; serverVersion = ICON_VERSION; break;
                case 2: filePath = "data/values.pack"; serverVersion = VALUES_VERSION; break;
                case 3: filePath = "data/player.pack"; serverVersion = PLAYER_VERSION; break;
                default:
                    System.out.println("[MSG#" + connectionId + "] Pack type khong ho tro: " + packType);
                    return;
            }

            if (clientVersion == serverVersion) {
                ByteArrayOutputStream empty = new ByteArrayOutputStream();
                DataOutputStream w = new DataOutputStream(empty);
                w.writeByte(packType);
                w.writeByte(serverVersion);
                w.writeShort(0);
                w.flush();
                sendMessage((byte) 90, empty.toByteArray());
                return;
            }

            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.err.println("[MSG#" + connectionId + "] Pack file not found: " + filePath);
                return;
            }

            byte[] fileBytes = Files.readAllBytes(path);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);
            writer.writeByte(packType);
            writer.writeByte(serverVersion);
            writer.writeShort((short) fileBytes.length);
            writer.write(fileBytes);
            writer.flush();
            sendMessage((byte) 90, baos.toByteArray());
            System.out.println("[MSG#" + connectionId + "] Da gui pack type=" + packType + " (" + fileBytes.length + " bytes)");
        }

        // ============================================================
        // LUCKY BOX (cmd=97)
        // ============================================================
        private void handleLuckyBox(ServerMessage message) throws IOException {
            System.out.println("[MSG#" + connectionId + "] Vong quay (cmd=97)");

            Random rand = new Random();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(baos);

            for (int i = 0; i < 10; i++) {
                writer.writeByte(i);
                writer.writeByte(i);
                writer.writeInt(rand.nextInt(100));
            }
            writer.writeInt(rand.nextInt(10));
            writer.flush();
            sendMessage((byte) 97, baos.toByteArray());
        }

        // ============================================================
        // QUAY SO (cmd=110)
        // ============================================================
        private void handleQuaySo(ServerMessage message) throws IOException {
            sendError((byte) 45, "Dang cap nhat!");
        }

        // ============================================================
        // EQUIP ITEM (cmd=102)
        // ============================================================
        private void handleEquipItem(ServerMessage message) throws IOException {
            System.out.println("[MSG#" + connectionId + "] Trang bi (cmd=102)");
        }

        // ============================================================
        // PING (cmd=42, 58, 127)
        // ============================================================
        private void handlePing(ServerMessage message) throws IOException {
            sendMessage((byte) 42, new byte[0]);
            lastPingTime = System.currentTimeMillis();
        }

        // ============================================================
        // BROADCAST HELPERS
        // ============================================================
        private void broadcastGameAction(byte cmd, byte[] data) throws IOException {
            if (session == null || session.currentRoom == null) return;
            for (Player p : session.currentRoom.players) {
                if (p.session != null && p != session.player) {
                    p.session.sendMessage(cmd, data);
                }
            }
        }

        private Player findPlayerInRoom(int playerId) {
            if (session == null || session.currentRoom == null) return null;
            for (Player p : session.currentRoom.players) {
                if (p.id == playerId) return p;
            }
            return null;
        }

        // ============================================================
        // SEND MESSAGE
        // ============================================================
        public void sendMessage(byte command, byte[] data) throws IOException {
            synchronized (dos) {
                byte[] encryptedData = null;
                int size = data != null ? data.length : 0;

                if (encryptionActive && size > 0) {
                    encryptedData = new byte[size];
                    for (int i = 0; i < size; i++) {
                        encryptedData[i] = writeKey(data[i]);
                    }
                }

                if (command == 90) {
                    dos.writeByte(encryptionActive ? writeKey(command) : command);

                    dos.write(encryptedData != null ? encryptedData : data);
                } else {
                    dos.writeByte(encryptionActive ? writeKey(command) : command);

                    if (encryptionActive) {
                        dos.writeByte(writeKey((byte) (size >> 8)));
                        dos.writeByte(writeKey((byte) (size & 0xFF)));
                    } else {
                        dos.writeShort(size);
                    }

                    if (size > 0) {
                        dos.write(encryptedData != null ? encryptedData : data);
                    }
                }
                dos.flush();
            }
        }

        // ============================================================
        // ENCRYPTION
        // ============================================================
        private byte readKey(byte b) {
            byte result = (byte) ((key[curR++] & 0xff) ^ (b & 0xff));
            if (curR >= key.length) curR %= key.length;
            return result;
        }

        private byte writeKey(byte b) {
            byte result = (byte) ((key[curW++] & 0xff) ^ (b & 0xff));
            if (curW >= key.length) curW %= key.length;
            return result;
        }

        // ============================================================
        // DISCONNECT
        // ============================================================
        public void disconnect() {
            if (!connected) return;
            connected = false;
            server.removeClient(this);

            if (session != null && session.player != null) {
                System.out.println("[DISCONNECT#" + connectionId + "] " + session.player.username);

                if (session.currentRoom != null) {
                    Room room = session.currentRoom;
                    room.leave(session.player);
                    session.leaveRoom();

                    if (!room.players.isEmpty()) {
                        try {
                            sendRoomPlayers(room);
                        } catch (IOException e) {
                        }
                    } else {
                        RoomManager.getInstance().removeRoom(room.id);
                    }
                }

                Database.getInstance().savePlayer(session.player);
                PlayerManager.getInstance().removeSession(session);
            } else {
                System.out.println("[DISCONNECT#" + connectionId + "] Da ngat ket noi.");
            }

            try {
                if (socket != null) socket.close();
                if (dis != null) dis.close();
                if (dos != null) dos.close();
            } catch (IOException e) {
            }

            key = null;
            dis = null;
            dos = null;
            curR = 0;
            curW = 0;
            session = null;
        }
    }

    // ============================================================
    // INNER CLASS: ServerMessage
    // ============================================================
    static class ServerMessage {
        byte command;
        byte[] data;

        ServerMessage(byte command, byte[] data) {
            this.command = command;
            this.data = data != null ? data : new byte[0];
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        int port = 19149;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Port khong hop le, su dung mac dinh: 19149");
            }
        }

        System.out.println("\n============================================");
        System.out.println("  MobiArmy Server v1.6.5");
        System.out.println("  Port: " + port);
        System.out.println("============================================\n");

        final NetworkServer server = new NetworkServer(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n[Nhan lenh tat] Dang dung server...");
            server.stop();
        }));

        Thread adminThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("[ADMIN] Nhap lenh (go 'help' de xem danh sach):");
            while (server.isRunning()) {
                try {
                    if (System.in.available() > 0) {
                        String cmd = scanner.nextLine().trim().toLowerCase();
                        handleAdminCommand(cmd, server);
                    }
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            }
            scanner.close();
        }, "AdminConsole");
        adminThread.setDaemon(true);
        adminThread.start();

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Khong the khoi dong server tren port " + port + ": " + e.getMessage());
            System.exit(1);
        }
    }

    private static void handleAdminCommand(String cmd, NetworkServer server) {
        if (cmd.isEmpty()) return;

        switch (cmd) {
            case "help":
                System.out.println("\n--- Lenh Admin ---");
                System.out.println("  online    - So nguoi online");
                System.out.println("  players   - Danh sach nguoi choi");
                System.out.println("  rooms     - Danh sach phong");
                System.out.println("  save      - Luu du lieu");
                System.out.println("  stop      - Dung server");
                System.out.println("------------------\n");
                break;
            case "online":
                System.out.println("Online: " + PlayerManager.getInstance().getOnlineCount());
                break;
            case "players":
                System.out.println("Nguoi choi: " + PlayerManager.getInstance().getOnlinePlayerList());
                break;
            case "rooms":
                System.out.println("Phong: " + RoomManager.getInstance().getRoomList());
                break;
            case "save":
                Database.getInstance().saveAll();
                System.out.println("Da luu!");
                break;
            case "stop":
            case "quit":
                System.out.println("Dang dung...");
                server.stop();
                System.exit(0);
                break;
            default:
                System.out.println("Lenh khong ton tai. Go 'help'.");
        }
    }

    public Collection<PlayerSession> getAllSessions() {
        return PlayerManager.getInstance().getAllSessions();
    }

    public Collection<Room> getAllRooms() {
        return RoomManager.getInstance().getAllRooms();
    }
}
