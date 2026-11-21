package com.mobiarmy.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SelectNvTest {
    private final boolean[] owned = {true, true, true, false, false, false, false, false, false};

    @Test
    void ownedCharactersCanBeSelected() {
        assertTrue(User.canSelectNv(owned, 0));
        assertTrue(User.canSelectNv(owned, 2));
    }

    @Test
    void unownedOrOutOfRangeCannot() {
        assertFalse(User.canSelectNv(owned, 3));
        assertFalse(User.canSelectNv(owned, 8));
        assertFalse(User.canSelectNv(owned, 9));
        assertFalse(User.canSelectNv(owned, -1));
        assertFalse(User.canSelectNv(null, 0));
    }
}
