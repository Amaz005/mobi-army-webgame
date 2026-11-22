package com.mobiarmy.server;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One-time game tickets (specs/004-game-portal research D15). A player logged in on the portal gets a
 * ticket; the browser client sends it as the password of the normal login message, so the game logs in
 * without asking again. The portal never knows the real password, and the protocol is unchanged.
 * Tickets are letters and digits (what the login handler accepts), single-use, and expire quickly.
 */
public final class GameTickets {
    static final Duration TTL = Duration.ofMinutes(2);
    static final int LENGTH = 24;
    private static final char[] ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private record Ticket(int accountId, Instant expires) {}

    private static final Map<String, Ticket> TICKETS = new ConcurrentHashMap<>();
    static Clock clock = Clock.systemUTC();

    private GameTickets() {}

    /** A new ticket for the account. */
    public static String issue(int accountId) {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) sb.append(ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
        String ticket = sb.toString();
        Instant now = clock.instant();
        if (TICKETS.size() > 10_000) TICKETS.values().removeIf(t -> !now.isBefore(t.expires()));
        TICKETS.put(ticket, new Ticket(accountId, now.plus(TTL)));
        return ticket;
    }

    /** True if {@code ticket} was issued for this account and is still valid; it can't be used again. */
    public static boolean consume(int accountId, String ticket) {
        if (ticket == null || ticket.length() != LENGTH) return false;
        Ticket t = TICKETS.get(ticket);
        if (t == null || t.accountId() != accountId) return false;
        if (!TICKETS.remove(ticket, t)) return false;
        return clock.instant().isBefore(t.expires());
    }
}
