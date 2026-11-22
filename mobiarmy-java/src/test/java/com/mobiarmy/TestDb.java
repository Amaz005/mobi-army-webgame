package com.mobiarmy;

import com.mobiarmy.data.SqlConnectionManager;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

/** Test helpers: a throwaway SQLite copy of dbarmy.sql, and a clock tests can move. */
public final class TestDb {
    private TestDb() {}

    /** Points SqlConnectionManager at a fresh SQLite database built from the canonical dump. */
    public static void fresh() throws Exception {
        File f = File.createTempFile("dbarmy-test", ".sqlite");
        f.deleteOnExit();
        SqlConnectionManager.useLocalDump("dbarmy.sql", f.getPath());
    }

    public static void exec(String sql, Object... params) throws Exception {
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) stmt.setObject(i + 1, params[i]);
            stmt.executeUpdate();
        }
    }

    public static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-11-22T10:00:00Z");

        public void advance(Duration d) {
            now = now.plus(d);
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public java.time.ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }
    }
}
