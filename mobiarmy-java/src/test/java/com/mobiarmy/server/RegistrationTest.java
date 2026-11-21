package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RegistrationTest {
    @Test
    void normalizesLikeTheClient() {
        assertEquals("abc12", Registration.normalize("  AbC12 "));
        assertEquals("", Registration.normalize(null));
    }

    @Test
    void acceptsLettersAndDigitsUpTo20() {
        assertTrue(Registration.isValidUsername("a"));
        assertTrue(Registration.isValidUsername("player2024"));
        assertTrue(Registration.isValidUsername("a".repeat(20)));
    }

    @Test
    void rejectsEmptyTooLongAndSymbols() {
        assertFalse(Registration.isValidUsername(""));
        assertFalse(Registration.isValidUsername("a".repeat(21)));
        assertFalse(Registration.isValidUsername("a b"));
        assertFalse(Registration.isValidUsername("a'--"));
        assertFalse(Registration.isValidUsername("tên"));
    }

    @Test
    void passwordIsLettersAndDigitsUpTo50() {
        assertTrue(Registration.isValidPassword("3"));
        assertTrue(Registration.isValidPassword("MatKhau123"));
        assertTrue(Registration.isValidPassword("a".repeat(50)));
        assertFalse(Registration.isValidPassword(null));
        assertFalse(Registration.isValidPassword(""));
        assertFalse(Registration.isValidPassword("a".repeat(51)));
        assertFalse(Registration.isValidPassword("mat khau"));
        assertFalse(Registration.isValidPassword("abc!"));
    }
}
