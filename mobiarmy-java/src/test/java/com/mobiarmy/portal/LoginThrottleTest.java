package com.mobiarmy.portal;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb.MutableClock;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class LoginThrottleTest {
    private final MutableClock clock = new MutableClock();
    private final LoginThrottle throttle = new LoginThrottle(clock);

    @Test
    void fiveFailuresLockTheNameForFifteenMinutes() {
        for (int i = 0; i < 4; i++) assertFalse(throttle.recordFailure("abc"));
        assertFalse(throttle.isLocked("abc"));
        assertTrue(throttle.recordFailure("abc"));
        assertTrue(throttle.isLocked("abc"));
        assertFalse(throttle.isLocked("other"));
        clock.advance(Duration.ofMinutes(14));
        assertTrue(throttle.isLocked("abc"));
        clock.advance(Duration.ofMinutes(2));
        assertFalse(throttle.isLocked("abc"));
    }

    @Test
    void failuresOutsideTheWindowDontCount() {
        for (int i = 0; i < 4; i++) throttle.recordFailure("abc");
        clock.advance(Duration.ofMinutes(16));
        assertFalse(throttle.recordFailure("abc"));
        assertFalse(throttle.isLocked("abc"));
    }

    @Test
    void successResetsTheCount() {
        for (int i = 0; i < 4; i++) throttle.recordFailure("abc");
        throttle.recordSuccess("abc");
        assertFalse(throttle.recordFailure("abc"));
    }

    @Test
    void fiveRegistrationsPerIpPerHour() {
        for (int i = 0; i < 5; i++) {
            assertTrue(throttle.allowRegistration("1.2.3.4"));
            throttle.recordRegistration("1.2.3.4");
        }
        assertFalse(throttle.allowRegistration("1.2.3.4"));
        assertTrue(throttle.allowRegistration("5.6.7.8"));
        clock.advance(Duration.ofMinutes(61));
        assertTrue(throttle.allowRegistration("1.2.3.4"));
    }
}
