package com.mobiarmy.portal;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Logged-in portal browsers, kept in memory only (data-model.md › Portal session). A server
 * restart logs everyone out of the portal; game sessions are separate and unaffected.
 */
public final class PortalSessions {
    /** data-model.md › Portal session.token, .accountId, .createdAt, .lastUsedAt */
    public static final class Session {
        final String token;
        public final int accountId;
        public final String username;
        final Instant createdAt;
        volatile Instant lastUsedAt;

        Session(String token, int accountId, String username, Instant now) {
            this.token = token;
            this.accountId = accountId;
            this.username = username;
            this.createdAt = now;
            this.lastUsedAt = now;
        }
    }

    static final Duration MAX_AGE = Duration.ofDays(7);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final Duration idle;
    private final Clock clock;

    public PortalSessions(Duration idle, Clock clock) {
        this.idle = idle;
        this.clock = clock;
    }

    /** Starts a session and returns its token (32 random bytes, base64url). */
    public String create(int accountId, String username) {
        byte[] raw = new byte[32];
        RANDOM.nextBytes(raw);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        sessions.put(token, new Session(token, accountId, username, clock.instant()));
        if (sessions.size() > 10_000) prune();
        return token;
    }

    /** The live session for {@code token}, refreshing its idle timer; null if absent or expired. */
    public Session get(String token) {
        if (token == null) return null;
        Session s = sessions.get(token);
        if (s == null) return null;
        Instant now = clock.instant();
        if (expired(s, now)) {
            sessions.remove(token);
            return null;
        }
        s.lastUsedAt = now;
        return s;
    }

    public void remove(String token) {
        if (token != null) sessions.remove(token);
    }

    /** Ends every other session of the account (after a password change). */
    public void removeOthers(int accountId, String keepToken) {
        sessions.values().removeIf(s -> s.accountId == accountId && !s.token.equals(keepToken));
    }

    int size() {
        return sessions.size();
    }

    private boolean expired(Session s, Instant now) {
        return now.isAfter(s.lastUsedAt.plus(idle)) || now.isAfter(s.createdAt.plus(MAX_AGE));
    }

    private void prune() {
        Instant now = clock.instant();
        sessions.values().removeIf(s -> expired(s, now));
    }
}
