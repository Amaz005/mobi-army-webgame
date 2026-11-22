package com.mobiarmy.portal;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb;
import com.mobiarmy.TestDb.MutableClock;
import com.mobiarmy.server.User;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

class RankingTest {
    @Test
    void topTenByExpWithoutSuperadminsCachedFor60s() throws Exception {
        TestDb.fresh();
        // Seed: a (id 1, exp 0, normal), a2 and nguoiduatin are superadmins.
        for (int i = 0; i < 11; i++) {
            TestDb.exec("INSERT INTO account (id, username, password, exp) VALUES (?, ?, 'x', ?)", 100 + i, "p" + i, i * 1000);
        }
        TestDb.exec("INSERT INTO account (id, username, password, exp) VALUES (200, 'tie', 'x', 10000)");

        MutableClock clock = new MutableClock();
        Ranking ranking = new Ranking(clock);
        List<Ranking.Entry> top = ranking.top().entries();

        assertEquals(10, top.size());
        assertEquals(new Ranking.Entry(1, "p10", User.levelFromExpValue(10000), 10000), top.get(0));
        assertEquals("tie", top.get(1).name(), "ties go to the lower id first, p10 is 110 < 200");
        assertEquals(2, top.get(1).rank());
        assertTrue(top.stream().noneMatch(e -> e.name().equals("a2") || e.name().equals("nguoiduatin")));
        for (int i = 1; i < top.size(); i++) assertTrue(top.get(i - 1).exp() >= top.get(i).exp());

        TestDb.exec("UPDATE account SET exp = 999999 WHERE username = 'a'");
        assertEquals("p10", ranking.top().entries().get(0).name(), "cached");
        clock.advance(Duration.ofSeconds(61));
        assertEquals("a", ranking.top().entries().get(0).name());
    }
}
