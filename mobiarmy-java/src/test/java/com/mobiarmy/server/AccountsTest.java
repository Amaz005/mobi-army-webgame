package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb;
import org.junit.jupiter.api.Test;

/** Shared credential checks and the plaintext migration (research D3). */
class AccountsTest {
    @Test
    void checksHashedAndLegacyPasswords() {
        String hashed = PasswordHash.hash("abc123", 1000);
        assertTrue(Accounts.checkPassword(hashed, "abc123"));
        assertFalse(Accounts.checkPassword(hashed, "abc124"));
        assertTrue(Accounts.checkPassword("3", "3"));
        assertFalse(Accounts.checkPassword("3", "33"));
        assertFalse(Accounts.checkPassword(null, "3"));
        assertFalse(Accounts.checkPassword("3", null));
    }

    @Test
    void seedAccountsAreHashedAndStillWork() throws Exception {
        TestDb.fresh();
        assertEquals(0, Accounts.countLegacy());
        assertTrue(Accounts.checkPassword(Accounts.find("a").password(), "1"));
        assertTrue(Accounts.checkPassword(Accounts.find("a2").password(), "1"));
        assertTrue(Accounts.checkPassword(Accounts.find("nguoiduatin").password(), "3"));
    }

    @Test
    void findFallsBackToCaseInsensitive() throws Exception {
        TestDb.fresh();
        TestDb.exec("INSERT INTO account (id, username, password) VALUES (50, 'Abc', 'x')");
        assertEquals(50, Accounts.find("abc").id());
        assertEquals("Abc", Accounts.find("abc").username());
        assertNull(Accounts.find("nosuchuser"));
    }

    @Test
    void legacyLoginIsRehashedAndHashPasswordsMigratesTheRest() throws Exception {
        TestDb.fresh();
        TestDb.exec("INSERT INTO account (id, username, password) VALUES (50, 'old1', 'pw1')");
        TestDb.exec("INSERT INTO account (id, username, password) VALUES (51, 'old2', 'pw2')");
        assertEquals(2, Accounts.countLegacy());

        Accounts.rehashIfLegacy(50, "pw1", "pw1");
        assertFalse(PasswordHash.isLegacy(Accounts.findById(50).password()));
        assertEquals(1, Accounts.countLegacy());

        assertEquals(1, HashPasswords.run());
        assertEquals(0, Accounts.countLegacy());
        assertTrue(Accounts.checkPassword(Accounts.find("old2").password(), "pw2"));
        assertEquals(0, HashPasswords.run());
    }
}
