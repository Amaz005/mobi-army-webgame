package com.mobiarmy.server;

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

    public static void stop() {
        if (!running) return;
        running = false;
        
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
