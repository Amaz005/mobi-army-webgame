package com.mobiarmy.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnectionManager {
    public enum Mode { LocalSqlite, MySql }

    private static Mode mode = Mode.LocalSqlite;

    private static String mySqlConnectionString;
    private static String sqliteConnectionString;

    public static void configure(String host, String database, String user, String password) {
        mySqlConnectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=false", 
                                              host, database, user, password);
    }

    public static void useMySql() {
        if (mySqlConnectionString == null) {
            throw new IllegalStateException("SqlConnectionManager.configure(...) must be called before useMySql().");
        }
        mode = Mode.MySql;
        System.out.println("[SqlConnectionManager] Switched to MySQL.");
    }

    public static void useLocalDump(String dumpSqlPath) throws Exception {
        useLocalDump(dumpSqlPath, "local_dbarmy.sqlite");
    }

    public static void useLocalDump(String dumpSqlPath, String sqliteFilePath) throws Exception {
        File dumpFile = new File(dumpSqlPath);
        if (!dumpFile.exists()) {
            throw new java.io.FileNotFoundException("SQL dump not found: " + dumpSqlPath);
        }

        File sqliteFile = new File(sqliteFilePath);
        if (sqliteFile.exists()) {
            sqliteFile.delete();
        }

        sqliteConnectionString = "jdbc:sqlite:" + sqliteFilePath;

        try (Connection conn = DriverManager.getConnection(sqliteConnectionString)) {
            try (Statement pragma = conn.createStatement()) {
                pragma.execute("PRAGMA journal_mode=WAL; PRAGMA synchronous=NORMAL; PRAGMA busy_timeout=5000;");
            }
            SqlDumpLoader.loadIntoSqlite(dumpSqlPath, conn);
        }

        mode = Mode.LocalSqlite;
        System.out.println("[SqlConnectionManager] Running offline against a local SQLite build of " + dumpSqlPath + " (WAL mode).");
    }

    public static Connection createConnection() throws SQLException {
        if (mode == Mode.MySql) {
            if (mySqlConnectionString == null) {
                throw new IllegalStateException("SqlConnectionManager.configure(...) must be called before createConnection() in MySQL mode.");
            }
            return DriverManager.getConnection(mySqlConnectionString);
        }

        if (sqliteConnectionString == null) {
            throw new IllegalStateException("SqlConnectionManager.useLocalDump(...) must be called before createConnection() in local mode.");
        }
        
        Connection conn = DriverManager.getConnection(sqliteConnectionString);
        try (Statement pragma = conn.createStatement()) {
            pragma.execute("PRAGMA busy_timeout=5000;");
        }
        return conn;
    }
}
