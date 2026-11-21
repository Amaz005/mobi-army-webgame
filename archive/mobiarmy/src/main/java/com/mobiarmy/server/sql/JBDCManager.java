package com.mobiarmy.server.sql;

import java.sql.*;
import com.mobiarmy.server.GameConfig;

public final class JBDCManager implements JBDCService {

    private Connection connection = null;

    // Constructor - tạo kết nối khi khởi tạo đối tượng
    public JBDCManager() {
        connection = getConnection();
        initTables();
    }

    private void initTables() {
        try (Statement stmt = connection.createStatement()) {
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `owned_mask` INT DEFAULT 7"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `char_levels` VARCHAR(255) DEFAULT '[1,1,1,0,0,0,0,0,0]'"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `item_counts` VARCHAR(255) DEFAULT '[20,20,20,20,20,20,20,20,20]'"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `selected_char` INT DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `killdead` VARCHAR(255) DEFAULT ''"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `itembag` TEXT"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `banbe` VARCHAR(255) DEFAULT ''"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `thongtin` TEXT"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `exp` INT DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `luong` SMALLINT DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `xu` BIGINT DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `nhanvat` TINYINT DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `superadmin` TINYINT(1) DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `online` TINYINT(1) DEFAULT 0"
                );
            } catch (SQLException ignore) {}
            try {
                stmt.executeUpdate(
                    "ALTER TABLE `account` ADD COLUMN `active` TINYINT(1) DEFAULT 1"
                );
            } catch (SQLException ignore) {}
        } catch (SQLException e) {
            System.err.println("initTables error: " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        try {
            // Tải driver JDBC của MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Xây dựng chuỗi kết nối từ cấu hình trong GameConfig
            String connectionString = String.format("jdbc:mysql://%s:%s/%s?characterEncoding=%s&user=%s&password=%s",
                     GameConfig.getDbHost(),
                     GameConfig.getDbPort(),
                     GameConfig.getDbName(),
                     GameConfig.getDbEncoding(),
                     GameConfig.getDbUser(),
                     GameConfig.getDbPass());

            // Kết nối tới cơ sở dữ liệu và trả về đối tượng Connection
            return DriverManager.getConnection(connectionString);

        } catch (SQLException exc) {
            System.err.println("SQLException occurred while trying to connect to database: " + exc.getMessage());
            exc.printStackTrace();  // In chi tiết stack trace để debug
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found on classpath.");
            e.printStackTrace();
        }
        return null;  // Trả về null nếu có lỗi trong quá trình kết nối
    }

    @Override
    public Statement getStatement() {
        try {
            // Kiểm tra nếu connection là null hoặc đã bị đóng
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not established or has been closed.");
                return null; // Trả về null nếu kết nối không hợp lệ
            }
            // Tạo Statement để thực hiện các truy vấn SQL
            return connection.createStatement();
        } catch (SQLException e) {
            System.err.println("SQLException occurred while creating statement: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getStaticConnection() {
        // Return the connection from the singleton instance used by Main.
        // This works because Main creates a single JBDCManager at startup.
        return getStaticInstance().connection;
    }

    private static volatile JBDCManager _staticInstance;

    private static JBDCManager getStaticInstance() {
        if (_staticInstance == null) {
            synchronized (JBDCManager.class) {
                if (_staticInstance == null) {
                    _staticInstance = new JBDCManager();
                }
            }
        }
        return _staticInstance;
    }

    // Kiểm tra xem kết nối có còn hoạt động không
    public boolean isConnected() {
        try {
            // Kiểm tra nếu connection là null hoặc đã bị đóng
            if (connection == null || connection.isClosed()) {
                return false;
            }

            // Thực hiện một truy vấn nhẹ để kiểm tra kết nối
            try (Statement stmt = connection.createStatement()) {
                stmt.executeQuery("SELECT 1");
            }
            return true;  // Nếu truy vấn thành công, kết nối còn hoạt động
        } catch (SQLException e) {
            System.err.println("Error checking database connection: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra nếu kết nối với cơ sở dữ liệu đã thành công
    public boolean isConnectedWithDB() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Đóng kết nối khi không sử dụng
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();  // Đóng kết nối
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
