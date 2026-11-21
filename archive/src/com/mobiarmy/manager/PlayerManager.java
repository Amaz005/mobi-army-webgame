package com.mobiarmy.manager;

import com.mobiarmy.model.Player;
import com.mobiarmy.model.PlayerSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.Collection;

public class PlayerManager {

    private static PlayerManager instance;
    private final Map<String, Player> playersByUsername;
    private final Map<Integer, PlayerSession> sessionsByPlayerId;
    private final Map<String, PlayerSession> sessionsByUsername;

    private PlayerManager() {
        this.playersByUsername = new ConcurrentHashMap<>();
        this.sessionsByPlayerId = new ConcurrentHashMap<>();
        this.sessionsByUsername = new ConcurrentHashMap<>();
    }

    public static synchronized PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public Player registerPlayer(String username, String password) {
        if (playersByUsername.containsKey(username)) {
            return playersByUsername.get(username);
        }
        Player player = new Player(username, password);
        playersByUsername.put(username, player);
        return player;
    }

    public Player getPlayerByUsername(String username) {
        return playersByUsername.get(username);
    }

    public PlayerSession loginPlayer(String username, String password, String ipAddress) {
        Player player = playersByUsername.get(username);
        if (player == null) {
            player = registerPlayer(username, password);
        }
        if (!player.password.equals(password)) {
            return null;
        }
        PlayerSession session = new PlayerSession(player, ipAddress);
        session.authenticate();
        player.session = session;
        sessionsByPlayerId.put(player.id, session);
        sessionsByUsername.put(username, session);
        return session;
    }

    public void addSession(PlayerSession session) {
        sessionsByPlayerId.put(session.player.id, session);
        sessionsByUsername.put(session.player.username, session);
        session.player.session = session;
    }

    public void removeSession(PlayerSession session) {
        sessionsByPlayerId.remove(session.player.id);
        sessionsByUsername.remove(session.player.username);
        session.player.session = null;
    }

    public PlayerSession getSessionByUsername(String username) {
        return sessionsByUsername.get(username);
    }

    public PlayerSession getSessionByPlayerId(int playerId) {
        return sessionsByPlayerId.get(playerId);
    }

    public int getOnlineCount() {
        return sessionsByPlayerId.size();
    }

    public Collection<PlayerSession> getAllSessions() {
        return sessionsByPlayerId.values();
    }

    public String getOnlinePlayerList() {
        return sessionsByPlayerId.values().stream()
                .map(s -> s.player.username + " (ID:" + s.player.id + ")")
                .collect(Collectors.joining(", "));
    }

    public void broadcast(String message) {
        for (PlayerSession session : sessionsByPlayerId.values()) {
            System.out.println("[Broadcast to " + session.player.username + "]: " + message);
        }
    }

    public boolean isUsernameOnline(String username) {
        return sessionsByUsername.containsKey(username);
    }
}
