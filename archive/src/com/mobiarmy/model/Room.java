package com.mobiarmy.model;

import java.util.concurrent.CopyOnWriteArrayList;

public class Room {

    public final int id;
    public String name;
    public int currentPlayers;
    public int maxPlayers;
    public boolean hasPassword;
    public int betXu;
    public byte mapId;
    public RoomState state;
    public final CopyOnWriteArrayList<Player> players;
    public final CopyOnWriteArrayList<Player> readyPlayers;

    public static final byte STATE_WAITING = 0;
    public static final byte STATE_STARTING = 1;
    public static final byte STATE_PLAYING = 2;
    public static final byte STATE_ENDED = 3;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.currentPlayers = 0;
        this.maxPlayers = 8;
        this.hasPassword = false;
        this.betXu = 1000;
        this.mapId = 1;
        this.state = RoomState.WAITING;
        this.players = new CopyOnWriteArrayList<>();
        this.readyPlayers = new CopyOnWriteArrayList<>();
    }

    public boolean join(Player player) {
        if (players.size() >= maxPlayers) {
            return false;
        }
        if (!players.contains(player)) {
            players.add(player);
            currentPlayers = players.size();
            return true;
        }
        return false;
    }

    public boolean leave(Player player) {
        if (players.remove(player)) {
            readyPlayers.remove(player);
            currentPlayers = players.size();
            return true;
        }
        return false;
    }

    public void setReady(Player player, boolean ready) {
        if (ready) {
            if (!readyPlayers.contains(player)) {
                readyPlayers.add(player);
            }
        } else {
            readyPlayers.remove(player);
        }
        player.ready = ready;
    }

    public boolean canStart() {
        return readyPlayers.size() >= 1 && state == RoomState.WAITING;
    }

    public void startGame() {
        this.state = RoomState.PLAYING;
        for (Player p : players) {
            p.hp = p.maxHp;
            p.posX = 100;
            p.posY = 200;
        }
    }

    public void endGame() {
        this.state = RoomState.ENDED;
        for (Player p : players) {
            p.ready = false;
        }
        readyPlayers.clear();
    }

    public enum RoomState {
        WAITING,
        STARTING,
        PLAYING,
        ENDED
    }
}
