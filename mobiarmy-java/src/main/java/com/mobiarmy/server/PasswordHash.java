package com.mobiarmy.server;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Salted, slow password hashes (constitution IV; specs/004-game-portal research D2).
 * Stored as {@code pbkdf2_sha256$<iterations>$<salt b64>$<hash b64>}. Anything else is a legacy
 * plaintext password, accepted only until it is re-hashed.
 */
public final class PasswordHash {
    static final String PREFIX = "pbkdf2_sha256$";
    static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHash() {}

    public static String hash(String password) {
        return hash(password, ITERATIONS);
    }

    static String hash(String password, int iterations) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        Base64.Encoder b64 = Base64.getEncoder();
        return PREFIX + iterations + "$" + b64.encodeToString(salt) + "$"
                + b64.encodeToString(derive(password, salt, iterations));
    }

    /** True if {@code stored} is not in hashed form (a plaintext password from before 004). */
    public static boolean isLegacy(String stored) {
        return stored == null || !stored.startsWith(PREFIX);
    }

    /** Checks a typed password against a stored hash. Legacy values never match here. */
    public static boolean verify(String password, String stored) {
        if (password == null || isLegacy(stored)) return false;
        String[] parts = stored.substring(PREFIX.length()).split("\\$");
        if (parts.length != 3) return false;
        try {
            int iterations = Integer.parseInt(parts[0]);
            if (iterations < 1) return false;
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            return MessageDigest.isEqual(expected, derive(password, salt, iterations));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("PBKDF2WithHmacSHA256 unavailable", e);
        } finally {
            spec.clearPassword();
        }
    }
}
