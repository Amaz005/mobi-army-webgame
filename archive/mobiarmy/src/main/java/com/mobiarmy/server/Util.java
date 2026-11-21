package com.mobiarmy.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp tiện ích cung cấp các phương thức hỗ trợ ngẫu nhiên và xử lý file.
 */
public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());
    private static final Random RAND = new Random();

    /**
     * Trả về giá trị ngẫu nhiên trong phạm vi [x1, x2].
     */
    public static int nextInt(int x1, int x2) {
        if (x1 > x2) {
            throw new IllegalArgumentException("x1 phải nhỏ hơn hoặc bằng x2");
        }
        return x1 + RAND.nextInt(x2 - x1 + 1);
    }

    /**
     * Trả về giá trị ngẫu nhiên trong phạm vi [0, max).
     */
    public static int nextInt(int max) {
        return RAND.nextInt(max);
    }

    /**
     * Trả về chỉ số dựa trên tỉ lệ phần trăm.
     * percents[i] là xác suất (trên 1000) để chọn i.
     */
    public static int nextInt(int[] percents) {
        int next = nextInt(1000);
        for (int i = 0; i < percents.length; i++) {
            if (next < percents[i]) {
                return i;
            }
            next -= percents[i];
        }
        return percents.length - 1; // Trả về phần tử cuối cùng nếu không khớp
    }

    /**
     * Đọc nội dung file và trả về dưới dạng byte[].
     */
    public static byte[] getFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            LOGGER.warning("File not found: " + path);
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            int read = fis.read(fileBytes);
            if (read != fileBytes.length) {
                LOGGER.warning("Could not read full file: " + path);
            }
            return fileBytes;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + path, e);
            return null;
        }
    }

    /**
     * Lưu dữ liệu vào file.
     */
    public static void saveFile(String path, byte[] data) {
        File file = new File(path);
        try {
            if (file.exists() && !file.delete()) {
                LOGGER.warning("Could not delete existing file: " + path);
            }
            if (!file.createNewFile()) {
                LOGGER.warning("Could not create new file: " + path);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(data);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving file: " + path, e);
        }
    }

    /**
     * Đọc toàn bộ nội dung file.
     */
    public static byte[] readAllFile(String path) {
        try {
            return Files.readAllBytes(new File(path).toPath());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error reading file: " + path, e);
            return new byte[0];
        }
    }
}
