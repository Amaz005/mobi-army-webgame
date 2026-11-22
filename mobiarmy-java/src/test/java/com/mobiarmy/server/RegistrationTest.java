package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb;
import com.mobiarmy.data.SqlConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    // --- portal rules (data-model.md › Account.username, Account.password; research D5) ---

    @Test
    void portalUsernameIs3To20LowercaseLettersAndDigits() {
        assertTrue(Registration.isValidPortalUsername("testuser1"));
        assertTrue(Registration.isValidPortalUsername("abc"));
        assertTrue(Registration.isValidPortalUsername("a".repeat(20)));
        assertFalse(Registration.isValidPortalUsername("ab"));
        assertFalse(Registration.isValidPortalUsername("a".repeat(21)));
        assertFalse(Registration.isValidPortalUsername("Test_1"));
        assertFalse(Registration.isValidPortalUsername("TestUser"));
        assertFalse(Registration.isValidPortalUsername(null));
        assertEquals("testuser1", Registration.normalize(" TestUser1 "));
    }

    @Test
    void portalPasswordIs6To50LettersAndDigits() {
        assertTrue(Registration.isValidPortalPassword("abc123"));
        assertTrue(Registration.isValidPortalPassword("A".repeat(50)));
        assertFalse(Registration.isValidPortalPassword("abc12"));
        assertFalse(Registration.isValidPortalPassword("abc 123"));
        assertFalse(Registration.isValidPortalPassword("abc12!"));
        assertFalse(Registration.isValidPortalPassword("a".repeat(51)));
        assertFalse(Registration.isValidPortalPassword(null));
        // in-game rules are unchanged
        assertTrue(Registration.isValidPassword("3"));
        assertTrue(Registration.isValidUsername("a"));
    }

    @Test
    void createStoresAHashWithSchemaDefaultsAndRefusesTakenNames() throws Exception {
        TestDb.fresh();
        assertTrue(Registration.create("newplayer", "abc123"));
        try (Connection conn = SqlConnectionManager.createConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT password, xu, nvBuy, nhanvat, exp, superadmin FROM account WHERE username = 'newplayer'");
             ResultSet rs = stmt.executeQuery()) {
            assertTrue(rs.next());
            String stored = rs.getString("password");
            assertFalse(PasswordHash.isLegacy(stored));
            assertTrue(PasswordHash.verify("abc123", stored));
            assertEquals(1000, rs.getInt("xu"));
            assertEquals("[1, 1, 1, 0, 0, 0, 0, 0, 0]", rs.getString("nvBuy"));
            assertEquals(0, rs.getInt("nhanvat"));
            assertEquals(0, rs.getInt("exp"));
            assertEquals(0, rs.getInt("superadmin"));
        }
        assertFalse(Registration.create("newplayer", "other1"));
        assertFalse(Registration.create("NEWPLAYER", "other1"), "names are unique regardless of case");
        assertTrue(Registration.exists("NewPlayer"));
    }

    @Test
    void uniqueIndexViolationCountsAsTaken() throws Exception {
        TestDb.fresh();
        // Simulates the loser of a race: NOT EXISTS passed, but the index rejects the insert.
        java.sql.SQLException e = assertThrows(java.sql.SQLException.class,
                () -> TestDb.exec("INSERT INTO account (id, username, password) VALUES (99, 'a', 'x')"));
        assertTrue(Registration.isUniqueViolation(e));
    }
}
