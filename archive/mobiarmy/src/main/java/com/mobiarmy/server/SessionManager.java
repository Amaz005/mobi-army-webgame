package com.mobiarmy.server;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mobiarmy.server.network.Message;
import com.mobiarmy.server.network.Session;

public class SessionManager {
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());

    // CopyOnWriteArrayList giúp thread-safe khi nhiều thread thêm/xóa session
    private final List<Session> sessions = new CopyOnWriteArrayList<>();

    /**
     * Thêm một session mới vào danh sách.
     */
    public void add(Session session) {
        if (session != null) {
            sessions.add(session);
            LOGGER.log(Level.INFO, "Session {0} added. Total sessions: {1}", 
                       new Object[]{session.id, sessions.size()});
        }
    }

    /**
     * Xóa một session khỏi danh sách.
     */
    public void remove(Session session) {
        if (session != null && sessions.remove(session)) {
            LOGGER.log(Level.INFO, "Session {0} removed. Total sessions: {1}", 
                       new Object[]{session.id, sessions.size()});
        }
    }

    /**
     * Lấy danh sách session hiện tại (read-only).
     */
    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Đếm số lượng session đang hoạt động.
     */
    public int size() {
        return sessions.size();
    }

    /**
     * Đóng tất cả session (dùng khi shutdown server).
     */
    public void closeAll() {
        LOGGER.info("Closing all sessions...");
        for (Session session : sessions) {
            try {
                session.disconnect();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing session " + session.id, e);
            }
        }
        sessions.clear();
    }

    /**
     * Tìm session theo ID.
     */
    public Session findById(int id) {
        for (Session session : sessions) {
            if (session.id == id) {
                return session;
            }
        }
        return null;
    }

    /**
     * Tìm session theo Player (nếu có).
     */
    public Session findByPlayer(String playerName) {
        for (Session session : sessions) {
            if (session.player != null && playerName.equals(session.player.getName())) {
                return session;
            }
        }
        return null;
    }

    /**
     * Gửi thông báo đến tất cả session.
     */
    public void broadcast(Message message) {
        for (Session session : sessions) {
            if (session.connected) {
                session.push(message);
            }
        }
        LOGGER.log(Level.INFO, "Broadcasted message to {0} sessions", sessions.size());
    }
}