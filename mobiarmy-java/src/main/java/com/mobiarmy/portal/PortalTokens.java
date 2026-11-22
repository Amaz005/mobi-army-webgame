package com.mobiarmy.portal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Portal sessions as signed JWTs (HS256, data-model.md › Portal session; research D6). Nothing is kept
 * per session on the server, so sessions survive a server restart as long as the signing secret
 * ({@code PORTAL_JWT_SECRET}) stays the same.
 * <ul>
 *   <li>Idle timeout: {@code exp} is "last use + idle"; tokens older than {@link #REFRESH_AFTER} are
 *       re-issued on use, keeping the original login time {@code auth}. Hard limit: 7 days after login.</li>
 *   <li>Password change ends every other session: {@code pv} is an HMAC of the stored password hash,
 *       checked on every use.</li>
 *   <li>Log-out: the token's {@code jti} is refused until it expires (in memory; after a restart a
 *       logged-out token is still dead because its cookie was cleared, and it expires within the idle time).</li>
 * </ul>
 */
public final class PortalTokens {
    static final Duration MAX_AGE = Duration.ofDays(7);
    static final Duration REFRESH_AFTER = Duration.ofMinutes(5);
    static final int MIN_SECRET_BYTES = 32;
    /** base64url({"alg":"HS256","typ":"JWT"}); anything else (alg "none", RS256, …) is rejected. */
    private static final String HEADER = b64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final SecureRandom RANDOM = new SecureRandom();

    /** The stored password (hash) of an account, or null if it no longer exists. */
    public interface PasswordLookup {
        String current(int accountId) throws Exception;
    }

    /** A verified token. */
    public static final class Session {
        public final int accountId;
        public final String username;
        final long auth;
        final String pv;
        final String jti;
        final long exp;
        /** Old enough to be re-issued with a fresh idle timeout. */
        final boolean refresh;

        Session(int accountId, String username, long auth, String pv, String jti, long exp, boolean refresh) {
            this.accountId = accountId;
            this.username = username;
            this.auth = auth;
            this.pv = pv;
            this.jti = jti;
            this.exp = exp;
            this.refresh = refresh;
        }
    }

    private final byte[] secret;
    private final Duration idle;
    private final Clock clock;
    private final PasswordLookup lookup;
    private final Map<String, Long> revoked = new ConcurrentHashMap<>();

    public PortalTokens(byte[] secret, Duration idle, Clock clock, PasswordLookup lookup) {
        if (secret.length < MIN_SECRET_BYTES) throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        this.secret = secret.clone();
        this.idle = idle;
        this.clock = clock;
        this.lookup = lookup;
    }

    /**
     * The signing key from {@code PORTAL_JWT_SECRET}. Missing or shorter than 32 bytes: a random key for
     * this run only, with a warning (every portal session then ends at the next restart).
     */
    public static byte[] secretFrom(String configured) {
        if (configured != null && configured.getBytes(StandardCharsets.UTF_8).length >= MIN_SECRET_BYTES) {
            return configured.getBytes(StandardCharsets.UTF_8);
        }
        System.out.println("WARNING: PORTAL_JWT_SECRET is not set (or shorter than 32 characters); portal logins"
                + " will end when the server restarts. Set it in .env (see .env.example).");
        byte[] random = new byte[MIN_SECRET_BYTES];
        RANDOM.nextBytes(random);
        return random;
    }

    /** A token for a fresh login. */
    public String issue(int accountId, String username, String storedPassword) {
        return sign(accountId, username, now(), fingerprint(storedPassword));
    }

    /** A new token for the same login (new idle timeout, same login time); new password if it changed. */
    public String reissue(Session s, String storedPasswordOrNull) {
        return sign(s.accountId, s.username, s.auth, storedPasswordOrNull == null ? s.pv : fingerprint(storedPasswordOrNull));
    }

    /** The session behind a token, or null if it is malformed, forged, expired, logged out or outdated. */
    public Session verify(String token) throws Exception {
        if (token == null) return null;
        String[] parts = token.split("\\.", -1);
        if (parts.length != 3 || !HEADER.equals(parts[0])) return null;
        byte[] expected = hmac((parts[0] + "." + parts[1]).getBytes(StandardCharsets.US_ASCII));
        byte[] given;
        JsonNode p;
        try {
            given = Base64.getUrlDecoder().decode(parts[2]);
            if (!MessageDigest.isEqual(expected, given)) return null;
            p = JSON.readTree(Base64.getUrlDecoder().decode(parts[1]));
        } catch (Exception e) {
            return null;
        }
        long now = now();
        long exp = p.path("exp").asLong(0);
        long auth = p.path("auth").asLong(0);
        long iat = p.path("iat").asLong(0);
        String jti = p.path("jti").asText("");
        int id;
        try {
            id = Integer.parseInt(p.path("sub").asText(""));
        } catch (NumberFormatException e) {
            return null;
        }
        if (now >= exp || now >= auth + MAX_AGE.getSeconds() || revoked.containsKey(jti)) return null;
        String stored = lookup.current(id);
        String pv = p.path("pv").asText("");
        if (stored == null || !MessageDigest.isEqual(pv.getBytes(StandardCharsets.US_ASCII),
                fingerprint(stored).getBytes(StandardCharsets.US_ASCII))) {
            return null;
        }
        return new Session(id, p.path("name").asText(""), auth, pv, jti, exp,
                now - iat >= REFRESH_AFTER.getSeconds());
    }

    /** Log-out: this token is refused from now on (until it would have expired anyway). */
    public void revoke(Session s) {
        if (s == null) return;
        long now = now();
        if (revoked.size() > 10_000) revoked.values().removeIf(exp -> exp <= now);
        revoked.put(s.jti, s.exp);
    }

    private String sign(int accountId, String username, long auth, String pv) {
        long now = now();
        byte[] jti = new byte[16];
        RANDOM.nextBytes(jti);
        ObjectNode p = JSON.createObjectNode();
        p.put("sub", String.valueOf(accountId));
        p.put("name", username);
        p.put("pv", pv);
        p.put("auth", auth);
        p.put("iat", now);
        p.put("exp", Math.min(now + idle.getSeconds(), auth + MAX_AGE.getSeconds()));
        p.put("jti", b64(jti));
        String body;
        try {
            body = b64(JSON.writeValueAsBytes(p));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        String unsigned = HEADER + "." + body;
        return unsigned + "." + b64(hmac(unsigned.getBytes(StandardCharsets.US_ASCII)));
    }

    /** Ties a token to the password it was issued under, without revealing anything about it. */
    private String fingerprint(String storedPassword) {
        byte[] mac = hmac(("pv:" + storedPassword).getBytes(StandardCharsets.UTF_8));
        return b64(mac).substring(0, 22);
    }

    private byte[] hmac(byte[] data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("HmacSHA256 unavailable", e);
        }
    }

    private long now() {
        return clock.instant().getEpochSecond();
    }

    private static String b64(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }
}
