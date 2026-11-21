package com.mobiarmy.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mobiarmy.server.model.map.SetCache;
import com.mobiarmy.server.network.Session;
import com.mobiarmy.server.service.SendPackData;
import com.mobiarmy.server.sql.JBDCManager;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final SessionManager manager = new SessionManager();

    private static volatile boolean running = true;
    private static ServerSocket serverSocket;
    private static ExecutorService clientPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        startServer();
        handleAdminPanel();
    }

    /**
     * Khởi động server, kiểm tra DB, khởi tạo cache và lắng nghe client.
     */
    private static void startServer() {
        JBDCManager jbdc = new JBDCManager();

        if (!jbdc.isConnected()) {
            LOGGER.severe("Failed to connect to database. Exiting server...");
            shutdown();
            return;
        }

        GameConfig config = new GameConfig();
        SetCache setCache = new SetCache(jbdc);
        SendPackData sendPackData = new SendPackData();

        config.init();
        setCache.init();

        try {
            serverSocket = new ServerSocket(GameConfig.PORT);
            LOGGER.log(Level.INFO, "Server started on port {0}", GameConfig.PORT);

            Thread acceptThread = new Thread(() -> acceptClients(jbdc, sendPackData));
            acceptThread.start();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server failed to start", e);
            shutdown();
        }
    }

    /**
     * Lắng nghe và chấp nhận kết nối từ client.
     */
    private static void acceptClients(JBDCManager jbdc, SendPackData sendPackData) {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                Session session = new Session(socket);
                session.jbdc = jbdc;
                session.sendPackData = sendPackData;
                manager.add(session);

                LOGGER.log(Level.INFO, "New session started: {0}", session.id);

            } catch (IOException e) {
                if (running) {
                    LOGGER.log(Level.WARNING, "Error accepting client connection", e);
                }
            }
        }
    }

    /**
     * Xử lý bảng điều khiển admin.
     */
    private static void handleAdminPanel() {
        try (Scanner scanner = new Scanner(System.in)) {
            LOGGER.info("Admin panel ready. Commands: exit, stats");

            while (running) {
                String input = scanner.nextLine().trim();
                switch (input) {
                    case "exit" -> {
                        LOGGER.info("Shutting down server...");
                        shutdown();
                    }
                    case "stats" -> {
                        LOGGER.log(Level.INFO, "Active sessions: {0}", manager.size());
                    }
                    default -> LOGGER.log(Level.WARNING, "Unknown command: {0}", input);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in admin panel", e);
        }
    }

    /**
     * Đóng server và giải phóng tài nguyên.
     */
    private static void shutdown() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            clientPool.shutdownNow();
            manager.closeAll();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error shutting down server", e);
        }
        System.exit(0);
    }
}