package com.mobiarmy.server.model.map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class MapData {

    public static final class MapDataEntry {
        public byte id;
        public String name;
        public String file;
        public byte[] data;
        public short bg;
        public short mapAddY;
        public short cl2AddY;
        public short inWaterAddY;
        public short bullEffShower;
        public short[] XPlayer;
        public short[] YPlayer;
    }

    public static final class MapBrickEntry {
        public int id;
        public int[] dat;
        public int Width;
        public int Height;

        MapBrickEntry(int id, int[] dat, int W, int H) {
            this.id = id;
            this.dat = dat;
            this.Width = W;
            this.Height = H;
        }
    }

    // Khởi tạo danh sách nếu chưa có
    public static ArrayList<MapDataEntry> entrys = new ArrayList<>();
    public static ArrayList<MapBrickEntry> brickEntrys = new ArrayList<>();
    
    // Sử dụng HashSet để tối ưu việc tìm kiếm các id không va chạm
    public static final HashSet<Short> idNotColision = new HashSet<>(Set.of((short)70, (short)71, (short)73, (short)74, (short)75, (short)77, (short)78, (short)79, (short)97));

    /**
     * Kiểm tra xem id có thuộc danh sách các đối tượng không va chạm không.
     */
    public static boolean isNotColision(int id) {
        return idNotColision.contains((short) id);
    }

    /**
     * Lấy thông tin về MapBrickEntry từ id.
     */
    public static MapBrickEntry getMapBrickEntry(int id) {
        return brickEntrys.stream()
                .filter(me -> me.id == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Tải MapBrick từ file ảnh.
     */
    public static void loadMapBrick(int id) {
        System.out.println("Load Map Brick id=" + id);
        try {
            File mapBrickFile = new File("res/icon/map/" + id + ".png");
            
            // Kiểm tra nếu file tồn tại
            if (!mapBrickFile.exists()) {
                System.out.println("Map Brick file not found: " + mapBrickFile.getPath());
                return;
            }

            BufferedImage img = ImageIO.read(mapBrickFile);
            int W = img.getWidth();
            int H = img.getHeight();
            int[] argb = new int[W * H];
            img.getRGB(0, 0, W, H, argb, 0, W);

            // Thêm MapBrickEntry vào danh sách
            MapBrickEntry me = new MapBrickEntry(id, argb, W, H);
            brickEntrys.add(me);
        } catch (IOException e) {
            System.err.println("Error loading Map Brick with id " + id);
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra xem MapBrick có tồn tại không.
     */
    public static boolean existsMapBrick(int id) {
        return brickEntrys.stream().anyMatch(me -> me.id == id);
    }
}
