package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import com.mobiarmy.TestDb;
import com.mobiarmy.TestDb.MutableClock;
import java.time.Clock;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class GameTicketsTest {
    @AfterEach
    void realClock() {
        GameTickets.clock = Clock.systemUTC();
    }

    @Test
    void ticketIsLettersAndDigitsSingleUseAndBoundToTheAccount() {
        String t = GameTickets.issue(7);
        assertTrue(t.matches("^[A-Za-z0-9]{24}$"), t);
        assertFalse(GameTickets.consume(8, t), "other account");
        assertTrue(GameTickets.consume(7, t));
        assertFalse(GameTickets.consume(7, t), "single use");
        assertFalse(GameTickets.consume(7, null));
        assertFalse(GameTickets.consume(7, "short"));
        assertNotEquals(GameTickets.issue(7), GameTickets.issue(7));
    }

    @Test
    void ticketExpiresAfterTwoMinutes() {
        MutableClock clock = new MutableClock();
        GameTickets.clock = clock;
        String fresh = GameTickets.issue(7);
        String old = GameTickets.issue(7);
        clock.advance(Duration.ofSeconds(119));
        assertTrue(GameTickets.consume(7, fresh));
        clock.advance(Duration.ofSeconds(2));
        assertFalse(GameTickets.consume(7, old));
    }

    @Test
    void gameLoginAcceptsTheTicketWithoutTouchingThePassword() throws Exception {
        TestDb.fresh();
        Accounts.Row a = Accounts.find("a");
        String t = GameTickets.issue(a.id());
        assertTrue(Accounts.checkLogin(a.id(), a.password(), t));
        assertEquals(a.password(), Accounts.findById(a.id()).password(), "ticket never becomes the password");
        assertFalse(Accounts.checkLogin(a.id(), a.password(), t), "used up");
        assertTrue(Accounts.checkLogin(a.id(), a.password(), "1"), "the real password still works");
        assertFalse(Accounts.checkLogin(a.id(), a.password(), "2"));
    }

    @Test
    void legacyAccountLoggingInWithATicketKeepsItsPassword() throws Exception {
        TestDb.fresh();
        TestDb.exec("INSERT INTO account (id, username, password) VALUES (70, 'oldie', 'pw70')");
        String t = GameTickets.issue(70);
        assertTrue(Accounts.checkLogin(70, "pw70", t));
        assertEquals("pw70", Accounts.findById(70).password(), "no re-hash from a ticket");
    }
}
