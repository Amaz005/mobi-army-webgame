package com.mobiarmy.portal;

import com.mobiarmy.data.SqlConnectionManager;
import com.mobiarmy.server.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Top-10 MobiArmy players by experience, read-only, cached for 60 s (data-model.md › Ranking entry).
 * Only the in-game name and ranked values are read; superadmins are left out.
 */
public final class Ranking {
    /** data-model.md › Ranking entry.rank, .name, .level, .exp */
    public record Entry(int rank, String name, int level, int exp) {}

    public record Snapshot(List<Entry> entries, Instant takenAt) {}

    static final Duration TTL = Duration.ofSeconds(60);
    private static final String SQL =
            "SELECT username, exp FROM account WHERE superadmin = 0 ORDER BY exp DESC, id ASC LIMIT 10";

    private final Clock clock;
    private Snapshot cached;

    public Ranking(Clock clock) {
        this.clock = clock;
    }

    public synchronized Snapshot top() throws Exception {
        Instant now = clock.instant();
        if (cached != null && now.isBefore(cached.takenAt().plus(TTL))) return cached;
        List<Entry> entries = new ArrayList<>();
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int exp = rs.getInt("exp");
                entries.add(new Entry(entries.size() + 1, rs.getString("username"), User.levelFromExpValue(exp), exp));
            }
        }
        cached = new Snapshot(List.copyOf(entries), now);
        return cached;
    }
}
