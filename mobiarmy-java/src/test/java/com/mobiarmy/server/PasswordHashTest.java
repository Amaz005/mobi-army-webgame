package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PasswordHashTest {
    // Low iteration count keeps the test fast; the format and checks are the same.
    private static final int FAST = 1000;

    @Test
    void verifiesTheRightPasswordOnly() {
        String h = PasswordHash.hash("abc123", FAST);
        assertTrue(PasswordHash.verify("abc123", h));
        assertFalse(PasswordHash.verify("abc124", h));
        assertFalse(PasswordHash.verify("", h));
    }

    @Test
    void saltMakesEveryHashDifferent() {
        assertNotEquals(PasswordHash.hash("abc123", FAST), PasswordHash.hash("abc123", FAST));
    }

    @Test
    void defaultFormatMatchesDataModel() {
        String h = PasswordHash.hash("1");
        assertTrue(h.matches("^pbkdf2_sha256\\$600000\\$[A-Za-z0-9+/=]+\\$[A-Za-z0-9+/=]+$"), h);
        assertTrue(h.length() <= 255);
        assertTrue(PasswordHash.verify("1", h));
    }

    @Test
    void iterationsAreReadFromTheStoredValue() {
        String h = PasswordHash.hash("abc123", 1234);
        assertTrue(h.startsWith("pbkdf2_sha256$1234$"));
        assertTrue(PasswordHash.verify("abc123", h));
    }

    @Test
    void legacyDetection() {
        assertTrue(PasswordHash.isLegacy("3"));
        assertTrue(PasswordHash.isLegacy("MatKhau123"));
        assertFalse(PasswordHash.isLegacy(PasswordHash.hash("3", FAST)));
    }

    @Test
    void tamperedOrMalformedHashesNeverVerify() {
        String h = PasswordHash.hash("abc123", FAST);
        int i = h.length() - 10;
        String tampered = h.substring(0, i) + (h.charAt(i) == 'A' ? 'B' : 'A') + h.substring(i + 1);
        assertFalse(PasswordHash.verify("abc123", tampered));
        assertFalse(PasswordHash.verify("abc123", "pbkdf2_sha256$x$y$z"));
        assertFalse(PasswordHash.verify("abc123", "pbkdf2_sha256$1000"));
        assertFalse(PasswordHash.verify("abc123", null));
    }
}
