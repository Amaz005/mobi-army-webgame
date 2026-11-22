package com.mobiarmy.portal;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory brute-force limits (data-model.md › Login throttle). Keys are login names for portal
 * logins, {@code "#<accountId>"} for wrong current passwords on password change, and visitor IPs
 * for registrations. nginx adds a per-IP request rate on top (research D6).
 */
public final class LoginThrottle {
    static final int MAX_FAILURES = 5;
    static final Duration WINDOW = Duration.ofMinutes(15);
    static final Duration LOCK = Duration.ofMinutes(15);
    static final int MAX_REGISTRATIONS = 5;
    static final Duration REGISTRATION_WINDOW = Duration.ofHours(1);

    private final Map<String, Deque<Instant>> failures = new ConcurrentHashMap<>();
    private final Map<String, Instant> lockedUntil = new ConcurrentHashMap<>();
    private final Map<String, Deque<Instant>> registrations = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginThrottle(Clock clock) {
        this.clock = clock;
    }

    public boolean isLocked(String key) {
        Instant until = lockedUntil.get(key);
        if (until == null) return false;
        if (clock.instant().isBefore(until)) return true;
        lockedUntil.remove(key);
        return false;
    }

    /** Records a failed attempt; returns true if the key is now locked. */
    public boolean recordFailure(String key) {
        Instant now = clock.instant();
        Deque<Instant> q = failures.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (q) {
            q.addLast(now);
            while (!q.isEmpty() && q.peekFirst().isBefore(now.minus(WINDOW))) q.pollFirst();
            if (q.size() >= MAX_FAILURES) {
                q.clear();
                lockedUntil.put(key, now.plus(LOCK));
                return true;
            }
        }
        if (failures.size() > 10_000) prune(now);
        return false;
    }

    public void recordSuccess(String key) {
        failures.remove(key);
    }

    /** True if this visitor may register another account now. */
    public boolean allowRegistration(String ip) {
        Deque<Instant> q = registrations.get(ip);
        if (q == null) return true;
        Instant now = clock.instant();
        synchronized (q) {
            while (!q.isEmpty() && q.peekFirst().isBefore(now.minus(REGISTRATION_WINDOW))) q.pollFirst();
            return q.size() < MAX_REGISTRATIONS;
        }
    }

    public void recordRegistration(String ip) {
        Deque<Instant> q = registrations.computeIfAbsent(ip, k -> new ArrayDeque<>());
        synchronized (q) {
            q.addLast(clock.instant());
        }
        if (registrations.size() > 10_000) prune(clock.instant());
    }

    private void prune(Instant now) {
        failures.values().removeIf(q -> {
            synchronized (q) {
                return q.isEmpty() || q.peekLast().isBefore(now.minus(WINDOW));
            }
        });
        registrations.values().removeIf(q -> {
            synchronized (q) {
                return q.isEmpty() || q.peekLast().isBefore(now.minus(REGISTRATION_WINDOW));
            }
        });
        lockedUntil.values().removeIf(until -> !now.isBefore(until));
    }
}
