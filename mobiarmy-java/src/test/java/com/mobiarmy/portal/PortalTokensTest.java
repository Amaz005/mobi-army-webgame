package com.mobiarmy.portal;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb.MutableClock;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PortalTokensTest {
    private static final byte[] SECRET = "test-secret-test-secret-test-secret!".getBytes(StandardCharsets.UTF_8);
    private final MutableClock clock = new MutableClock();
    private final Map<Integer, String> passwords = new HashMap<>(Map.of(7, "pbkdf2_sha256$1$a$b"));
    private final PortalTokens tokens = tokens(SECRET);

    private PortalTokens tokens(byte[] secret) {
        return new PortalTokens(secret, Duration.ofMinutes(30), clock, passwords::get);
    }

    @Test
    void roundTripAndSurvivesARestart() throws Exception {
        String t = tokens.issue(7, "abc", passwords.get(7));
        assertEquals(3, t.split("\\.").length);
        PortalTokens.Session s = tokens.verify(t);
        assertEquals(7, s.accountId);
        assertEquals("abc", s.username);
        // A restarted server (new instance, same secret) still accepts it.
        assertEquals(7, tokens(SECRET).verify(t).accountId);
        // A different secret does not.
        assertNull(tokens("another-secret-another-secret-12345".getBytes(StandardCharsets.UTF_8)).verify(t));
    }

    @Test
    void tamperedOrForeignTokensAreRejected() throws Exception {
        String t = tokens.issue(7, "abc", passwords.get(7));
        String[] p = t.split("\\.");
        String otherBody = Base64.getUrlEncoder().withoutPadding().encodeToString(
                new String(Base64.getUrlDecoder().decode(p[1]), StandardCharsets.UTF_8).replace("\"sub\":\"7\"", "\"sub\":\"8\"")
                        .getBytes(StandardCharsets.UTF_8));
        passwords.put(8, passwords.get(7));
        assertNull(tokens.verify(p[0] + "." + otherBody + "." + p[2]), "changed claims");
        String none = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes());
        assertNull(tokens.verify(none + "." + p[1] + "."), "alg none");
        assertNull(tokens.verify(none + "." + p[1] + "." + p[2]), "other header");
        assertNull(tokens.verify("garbage"));
        assertNull(tokens.verify(null));
        assertNull(tokens.verify(p[0] + "." + p[1] + ".%%%"));
    }

    @Test
    void idleTimeoutSlidesAndSevenDaysIsTheLimit() throws Exception {
        String t = tokens.issue(7, "abc", passwords.get(7));
        clock.advance(Duration.ofMinutes(4));
        assertFalse(tokens.verify(t).refresh);
        clock.advance(Duration.ofMinutes(2));
        PortalTokens.Session s = tokens.verify(t);
        assertTrue(s.refresh, "older than 5 minutes");
        String renewed = tokens.reissue(s, null);
        clock.advance(Duration.ofMinutes(29));
        assertNull(tokens.verify(t), "the old token timed out (35 min idle)");
        assertNotNull(tokens.verify(renewed), "the renewed one is still valid");

        // Kept alive, it still ends 7 days after the login.
        String keep = renewed;
        for (int i = 0; i < 7 * 24 * 3; i++) {
            clock.advance(Duration.ofMinutes(20));
            PortalTokens.Session k = tokens.verify(keep);
            if (k == null) break;
            keep = tokens.reissue(k, null);
        }
        assertNull(tokens.verify(keep));
    }

    @Test
    void passwordChangeEndsOtherSessionsAndLogoutRevokes() throws Exception {
        String a = tokens.issue(7, "abc", passwords.get(7));
        String b = tokens.issue(7, "abc", passwords.get(7));
        passwords.put(7, "pbkdf2_sha256$1$c$d");
        assertNull(tokens.verify(a));
        assertNull(tokens.verify(b));
        String fresh = tokens.reissue(new PortalTokens.Session(7, "abc", clock.instant().getEpochSecond(), "", "x", 0, false),
                passwords.get(7));
        PortalTokens.Session s = tokens.verify(fresh);
        assertNotNull(s);
        tokens.revoke(s);
        assertNull(tokens.verify(fresh), "logged out");
        passwords.remove(7);
        assertNull(tokens.verify(tokens.issue(7, "abc", "x")), "deleted account");
    }

    @Test
    void shortOrMissingSecretFallsBackToARandomOne() {
        assertEquals(32, PortalTokens.secretFrom(null).length);
        assertEquals(32, PortalTokens.secretFrom("short").length);
        assertArrayEquals("x".repeat(40).getBytes(StandardCharsets.UTF_8), PortalTokens.secretFrom("x".repeat(40)));
        assertThrows(IllegalArgumentException.class, () -> tokens(new byte[8]));
    }
}
