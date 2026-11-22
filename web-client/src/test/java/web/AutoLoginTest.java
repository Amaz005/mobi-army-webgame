package web;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class AutoLoginTest {
    private static final String TICKET = "Ab3dEf6hIj9kLm2nOp5qRs8t";

    @Test
    void onlyTheTicketInThePasswordStoreIsRecognised() {
        byte[] t = TICKET.getBytes(StandardCharsets.US_ASCII);
        assertTrue(AutoLogin.isTicket("caropass", t, TICKET));
        assertFalse(AutoLogin.isTicket("caroun", t, TICKET), "other store");
        assertFalse(AutoLogin.isTicket("caropass", "realpass1".getBytes(StandardCharsets.US_ASCII), TICKET));
        assertFalse(AutoLogin.isTicket("caropass", t, null), "no ticket this session");
        assertFalse(AutoLogin.isTicket("caropass", null, TICKET));
    }
}
