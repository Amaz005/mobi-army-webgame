package com.mobiarmy.portal;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb.MutableClock;
import java.time.Duration;
import java.util.Base64;
import org.junit.jupiter.api.Test;

class PortalSessionsTest {
    private final MutableClock clock = new MutableClock();
    private final PortalSessions sessions = new PortalSessions(Duration.ofMinutes(30), clock);

    @Test
    void createLookupLogout() {
        String t = sessions.create(7, "abc");
        assertEquals(32, Base64.getUrlDecoder().decode(t).length);
        assertEquals(7, sessions.get(t).accountId);
        assertEquals("abc", sessions.get(t).username);
        sessions.remove(t);
        assertNull(sessions.get(t));
        assertNull(sessions.get(null));
        assertNull(sessions.get("nope"));
    }

    @Test
    void idleTimeoutIsRefreshedByUse() {
        String t = sessions.create(1, "abc");
        clock.advance(Duration.ofMinutes(29));
        assertNotNull(sessions.get(t));
        clock.advance(Duration.ofMinutes(29));
        assertNotNull(sessions.get(t), "use refreshes the idle timer");
        clock.advance(Duration.ofMinutes(31));
        assertNull(sessions.get(t));
    }

    @Test
    void sevenDayLimitEvenWhenActive() {
        String t = sessions.create(1, "abc");
        Duration elapsed = Duration.ZERO;
        while (elapsed.compareTo(Duration.ofDays(7)) < 0) {
            clock.advance(Duration.ofMinutes(20));
            elapsed = elapsed.plusMinutes(20);
            assertNotNull(sessions.get(t), "still valid at " + elapsed);
        }
        clock.advance(Duration.ofMinutes(20));
        assertNull(sessions.get(t));
    }

    @Test
    void removeOthersKeepsTheCurrentSession() {
        String a = sessions.create(1, "abc");
        String b = sessions.create(1, "abc");
        String other = sessions.create(2, "xyz");
        sessions.removeOthers(1, a);
        assertNotNull(sessions.get(a));
        assertNull(sessions.get(b));
        assertNotNull(sessions.get(other));
    }
}
