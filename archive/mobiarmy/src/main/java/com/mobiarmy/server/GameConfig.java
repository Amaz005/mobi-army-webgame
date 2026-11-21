package com.mobiarmy.server;

import io.github.cdimascio.dotenv.Dotenv;

public class GameConfig {

    private Dotenv dotenv;

    // Các giá trị mặc định cho cấu hình nếu không tìm thấy trong .env
    private static String DB_NAME = "mobiarmy";
    private static String DB_PORT = "3306";
    private static String DB_USER = "root";
    private static String DB_PASS = "truong456";
    private static String DB_HOST = "localhost";
    private static String DB_ENCODING = "UTF-8";
    public static int PORT = 19149;

    public GameConfig() {
        dotenv = Dotenv.load();
    }

    public void init() {
        // Lấy các giá trị cấu hình từ .env hoặc sử dụng giá trị mặc định nếu không có
        try {
            PORT = Integer.parseInt(dotenv.get("PORT", String.valueOf(PORT)));
            DB_HOST = dotenv.get("DB_HOST", DB_HOST);
            DB_PORT = dotenv.get("DB_PORT", DB_PORT);
            DB_NAME = dotenv.get("DB_NAME", DB_NAME);
            DB_USER = dotenv.get("DB_USER", DB_USER);
            DB_PASS = dotenv.get("DB_PASS", DB_PASS);
            DB_ENCODING = dotenv.get("DB_ENCODING", DB_ENCODING);

            // Kiểm tra các giá trị cấu hình cần thiết
            if (DB_HOST == null || DB_PORT == null || DB_NAME == null || DB_USER == null || DB_PASS == null) {
                throw new IllegalArgumentException("Missing essential database configuration in .env file.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error loading game configuration: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // Dừng ứng dụng nếu không thể tải cấu hình
        }
    }

    // Getters (Optional, nếu bạn cần truy xuất thông tin cấu hình từ ngoài lớp này)
    public static String getDbHost() {
        return DB_HOST;
    }

    public static String getDbPort() {
        return DB_PORT;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPass() {
        return DB_PASS;
    }

    public static String getDbEncoding() {
        return DB_ENCODING;
    }

    public static int getPort() {
        return PORT;
    }
}
