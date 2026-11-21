package com.mobiarmy.manager;

import com.mobiarmy.model.Player;
import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Database instance;
    private final String dataDir;
    private final Map<String, PlayerData> playerCache;

    private Database() {
        this.dataDir = "player_data";
        this.playerCache = new HashMap<>();
        initDataDir();
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void initDataDir() {
        try {
            Files.createDirectories(Paths.get(dataDir));
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }

    public void savePlayer(Player player) {
        PlayerData data = new PlayerData();
        data.username = player.username;
        data.password = player.password;
        data.xu = player.xu;
        data.exp = player.exp;
        data.selectedChar = player.selectedChar;
        data.level = player.level;
        data.blood = player.blood;
        data.attack = player.attack;
        data.defense = player.defense;
        data.kill = player.kill;
        data.death = player.death;
        data.charLevels = player.charLevels;
        data.itemCounts = player.itemCounts;

        String filename = dataDir + File.separator + sanitizeFilename(player.username) + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Failed to save player " + player.username + ": " + e.getMessage());
        }
    }

    public Player loadPlayer(String username) {
        String filename = dataDir + File.separator + sanitizeFilename(username) + ".dat";
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            PlayerData data = (PlayerData) ois.readObject();
            Player player = new Player(data.username, data.password);
            player.xu = data.xu;
            player.exp = data.exp;
            player.selectedChar = data.selectedChar;
            player.level = data.level;
            player.blood = data.blood;
            player.attack = data.attack;
            player.defense = data.defense;
            player.kill = data.kill;
            player.death = data.death;
            if (data.charLevels != null) player.charLevels = data.charLevels;
            if (data.itemCounts != null) player.itemCounts = data.itemCounts;
            player.updateStats();
            return player;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load player " + username + ": " + e.getMessage());
            return null;
        }
    }

    public boolean playerExists(String username) {
        String filename = dataDir + File.separator + sanitizeFilename(username) + ".dat";
        return new File(filename).exists();
    }

    private String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public void saveAll() {
        for (PlayerData data : playerCache.values()) {
            Player player = new Player(data.username, data.password);
            player.xu = data.xu;
            player.exp = data.exp;
            player.selectedChar = data.selectedChar;
            player.level = data.level;
            player.blood = data.blood;
            player.attack = data.attack;
            player.defense = data.defense;
            player.kill = data.kill;
            player.death = data.death;
            savePlayer(player);
        }
    }

    private static class PlayerData implements Serializable {
        private static final long serialVersionUID = 1L;
        String username;
        String password;
        int xu;
        int exp;
        int selectedChar;
        int level;
        int blood;
        int attack;
        int defense;
        int kill;
        int death;
        int[] charLevels;
        int[] itemCounts;
    }
}
