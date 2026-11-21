package com.mobiarmy.manager;

import com.mobiarmy.model.Player;
import com.mobiarmy.model.Room;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.Collection;

public class RoomManager {

    private static RoomManager instance;
    private final Map<Integer, Room> roomsById;
    private int nextRoomId;

    private RoomManager() {
        this.roomsById = new ConcurrentHashMap<>();
        this.nextRoomId = 1;
        initializeDefaultRooms();
    }

    public static synchronized RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    private void initializeDefaultRooms() {
        createRoom("Khu Tap Luan", 8, 1000, (byte) 1);
        createRoom("Khu Co Ban", 8, 500, (byte) 2);
        createRoom("Khu Nang Cao", 4, 2000, (byte) 3);
        createRoom("Khu Thi Dau", 8, 5000, (byte) 4);
        createRoom("Phong Thu Chan", 4, 1000, (byte) 1);
        createRoom("Khu Vo Dich", 8, 10000, (byte) 5);
        for (int i = 0; i < 14; i++) {
            createRoom("Khu vuc " + (i + 7), 8, 1000, (byte) 1);
        }
    }

    public Room createRoom(String name, int maxPlayers, int betXu, byte mapId) {
        int id = nextRoomId++;
        Room room = new Room(id, name);
        room.maxPlayers = maxPlayers;
        room.betXu = betXu;
        room.mapId = mapId;
        roomsById.put(id, room);
        return room;
    }

    public Room getRoomById(int id) {
        return roomsById.get(id);
    }

    public boolean joinRoom(Player player, int roomId) {
        Room room = roomsById.get(roomId);
        if (room == null) {
            return false;
        }
        return room.join(player);
    }

    public boolean leaveRoom(Player player) {
        if (player.session != null && player.session.currentRoom != null) {
            Room room = player.session.currentRoom;
            boolean result = room.leave(player);
            if (result) {
                player.session.leaveRoom();
            }
            return result;
        }
        return false;
    }

    public int getRoomCount() {
        return roomsById.size();
    }

    public Collection<Room> getAllRooms() {
        return roomsById.values();
    }

    public int getTotalPlayersInRooms() {
        return roomsById.values().stream()
                .mapToInt(r -> r.players.size())
                .sum();
    }

    public String getRoomList() {
        return roomsById.values().stream()
                .map(r -> "Room[" + r.id + "] " + r.name + " (" + r.currentPlayers + "/" + r.maxPlayers + ")")
                .collect(Collectors.joining(", "));
    }

    public void removeRoom(int roomId) {
        roomsById.remove(roomId);
    }
}
