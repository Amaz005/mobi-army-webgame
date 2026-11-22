package com.mobiarmy.server;

import com.mobiarmy.portal.PortalServer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
    private static final int PORT = 19149; // Default port
    private static boolean running = false;
    private static ServerSocket serverSocket;
    private static PortalServer portal;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final AtomicInteger clientIdCounter = new AtomicInteger(1);
    
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        if (running) return;
        
        System.out.println("Starting MobiArmy Java Server...");
        
        try {
            ServerInit.init();
            
            // Re-initialize clients list
            ServerManager.clients = new CopyOnWriteArrayList<>();
            ServerManager.id = 0;
            ServerManager.numClients = 0;
            ServerManager.initRooms();
            ServerManager.start = true;
            startPortal();

            int portToUse = ServerManager.port > 0 ? ServerManager.port : PORT;
            serverSocket = new ServerSocket(portToUse);
            running = true;

            System.out.println("Server started on port " + portToUse);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setTcpNoDelay(true);
                    
                    if (ServerManager.numClients >= ServerManager.maxClients) {
                        System.out.println("Max clients reached, rejecting connection.");
                        clientSocket.close();
                        continue;
                    }

                    int newId = clientIdCounter.getAndIncrement();
                    ClientEntry clientEntry = new ClientEntry(clientSocket, newId);
                    ServerManager.clients.add(clientEntry);
                    ServerManager.numClients++;
                    
                    System.out.println("Client connected: " + newId + " from " + clientSocket.getRemoteSocketAddress());
                } catch (Exception e) {
                    if (running) {
                        System.err.println("Error accepting connection: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Server crashed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    /** Web portal API (specs/004-game-portal). A failure here must not stop the game server. */
    private static void startPortal() {
        try {
            portal = PortalServer.forGameServer(ServerManager.portalSessionIdleMinutes,
                    ServerManager.portalSecureCookie, ServerManager::onlineCount);
            portal.start(ServerManager.portalPort);
            System.out.println("Portal API started on port " + ServerManager.portalPort);
        } catch (Exception e) {
            System.err.println("Portal API failed to start on port " + ServerManager.portalPort + ": " + e.getMessage());
            e.printStackTrace();
            portal = null;
        }
    }

    public static void stop() {
        if (!running) return;
        running = false;
        if (portal != null) portal.stop();
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (ServerManager.clients != null) {
            for (ClientEntry client : ServerManager.clients) {
                try {
                    client.close();
                } catch (Exception ignored) {}
            }
            ServerManager.clients.clear();
            ServerManager.numClients = 0;
        }
        
        threadPool.shutdownNow();
        System.out.println("Server stopped.");
    }
}
