package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BuyNvTest {
    @Test
    void parseKeepsOwnedAndForcesStarters() {
        boolean[] owned = User.parseNvBuy("[0, 0, 0, 1, 0, 0, 0, 0, 1]", 9);
        assertArrayEquals(new boolean[] {true, true, true, true, false, false, false, false, true}, owned);
    }

    @Test
    void parseHandlesNullAndGarbage() {
        boolean[] starters = {true, true, true, false, false, false, false, false, false};
        assertArrayEquals(starters, User.parseNvBuy(null, 9));
        assertArrayEquals(starters, User.parseNvBuy("xyz", 9));
        assertArrayEquals(starters, User.parseNvBuy("[1, 1, 1]", 9));
    }

    @Test
    void formatRoundTrips() {
        String s = "[1, 1, 1, 0, 1, 0, 0, 0, 0]";
        assertEquals(s, User.formatNvBuy(User.parseNvBuy(s, 9)));
    }

    @Test
    void checkBuyNv() {
        boolean[] owned = User.parseNvBuy("[1, 1, 1, 1, 0, 0, 0, 0, 0]", 9);
        assertNull(User.checkBuyNv(owned, 4, 30000, 30000));
        assertNotNull(User.checkBuyNv(owned, 4, 29999, 30000), "not enough xu");
        assertNotNull(User.checkBuyNv(owned, 3, 999999, 40000), "already owned");
        assertNotNull(User.checkBuyNv(owned, 2, 999999, 0), "starter");
        assertNotNull(User.checkBuyNv(owned, 9, 999999, 0), "out of range");
        assertNotNull(User.checkBuyNv(owned, -125, 999999, 0), "negative");
        assertNotNull(User.checkBuyNv(owned, 5, 999999, -1), "unknown price");
        assertNotNull(User.checkBuyNv(null, 4, 999999, 0));
    }
}
