package com.mobiarmy.server.model.map;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mobiarmy.server.Util;
import com.mobiarmy.server.model.map.MapData.MapDataEntry;
import com.mobiarmy.server.sql.JBDCService;
import com.mobiarmy.server.team.TeamImageOutput;

public class SetCache {

    private static final Logger LOGGER = Logger.getLogger(SetCache.class.getName());

    private static final String CACHE_DIRECTORY = "cache/";
    private static final String MAP_CACHE_FILE = CACHE_DIRECTORY + "valuesdata";
    private static final String ICON_CACHE_FILE = CACHE_DIRECTORY + "icondata";
    private static final String PLAYER_CACHE_FILE = CACHE_DIRECTORY + "playerdata";
    private static final String TILE_CACHE_FILE = CACHE_DIRECTORY + "tiledata";

    public SetCache(JBDCService jbdc) {
        loadMapDataFromDatabase(jbdc);
    }

    /**
     * Initialize all cache types.
     */
    public void init() {
        initMapCache();
        initImageCache("res/player", PLAYER_CACHE_FILE, "player images");
        initImageCache("res/map/icon", ICON_CACHE_FILE, "map icons");
        initImageCache("res/effect/hole", TILE_CACHE_FILE, "tile icons");
    }

    /**
     * Initialize map cache.
     */
    private void initMapCache() {
        if (MapData.entrys.isEmpty()) {
            LOGGER.warning("No map entries available to cache.");
            return;
        }

        createCacheDirectory();

        try (ByteArrayOutputStream bas = new ByteArrayOutputStream();
             DataOutputStream ds = new DataOutputStream(bas)) {

            ds.writeByte(MapData.entrys.size());
            LOGGER.log(Level.INFO, "Init map entries: {0}", MapData.entrys.size());

            for (MapDataEntry entry : MapData.entrys) {
                writeMapData(ds, entry);
            }

            Util.saveFile(MAP_CACHE_FILE, bas.toByteArray());
            LOGGER.info("Map cache saved successfully to: " + MAP_CACHE_FILE);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while initializing map cache", e);
        }
    }

    private void writeMapData(DataOutputStream ds, MapDataEntry entry) throws IOException {
        ds.writeByte(entry.id);
        ds.writeShort(entry.data.length);
        ds.write(entry.data);
        ds.writeShort(entry.bg);
        ds.writeShort(entry.mapAddY);
        ds.writeShort(entry.bullEffShower);
        ds.writeShort(entry.inWaterAddY);
        ds.writeShort(entry.cl2AddY);
        ds.writeUTF(entry.name);
        ds.writeUTF(entry.file);

        LOGGER.info(() -> String.format("   - id=%d name=%s file=%s", entry.id, entry.name, entry.file));
    }

    /**
     * Generic method to initialize image caches.
     */
    private void initImageCache(String resourcePath, String cacheFile, String description) {
        try {
            LOGGER.log(Level.INFO, "Cache {0}...", description);
            File dir = new File(resourcePath);
            if (!dir.exists()) {
                throw new IOException("Folder not found: " + resourcePath);
            }

            TeamImageOutput tos = new TeamImageOutput();
            for (File file : dir.listFiles()) {
                tos.addFile(file.getName(), file.getPath());
            }

            Util.saveFile(cacheFile, tos.output());
            LOGGER.log(Level.INFO, "{0} cached successfully to: {1}", new Object[]{description, cacheFile});

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while caching " + description, e);
        }
    }

    /**
     * Create cache directory if it doesn't exist.
     */
    private void createCacheDirectory() {
        Path cacheDir = Paths.get(CACHE_DIRECTORY);
        if (!Files.exists(cacheDir)) {
            try {
                Files.createDirectories(cacheDir);
                LOGGER.info("Created cache directory: " + CACHE_DIRECTORY);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create cache directory", e);
            }
        }
    }

    /**
     * Load map data from the database.
     */
    private void loadMapDataFromDatabase(JBDCService jbdc) {
        if (jbdc == null) return;

        try (ResultSet res = jbdc.getStatement().executeQuery("SELECT * FROM `map`;")) {
            MapData.entrys = new ArrayList<>();
            MapData.brickEntrys = new ArrayList<>();

            while (res.next()) {
                MapDataEntry entry = new MapDataEntry();
                entry.id = (byte) (res.getByte("id") - 1);
                entry.name = res.getString("name");
                entry.file = res.getString("file");

                if (entry.id == 27) {
                    entry.data = new byte[0];
                } else {
                    entry.data = Util.getFile("res/map/" + entry.file);
                    if (entry.data == null) {
                        LOGGER.log(Level.WARNING, "Failed to load map data for: {0}", entry.name);
                        continue;
                    }
                }

                entry.bg = res.getShort("bg");
                entry.mapAddY = res.getShort("mapAddY");
                entry.bullEffShower = res.getShort("bullEffShower");
                entry.inWaterAddY = res.getShort("inWaterAddY");
                entry.cl2AddY = res.getShort("cl2AddY");

                MapData.entrys.add(entry);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading map data from database", e);
        }
    }
}
