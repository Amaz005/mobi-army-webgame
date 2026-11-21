package web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Every row of specs/002-web-client/contracts/key-mapping.md. */
class KeyMapTest {

    private static int k(String key, String code) {
        return KeyMap.fromBrowser(key, code, false);
    }

    @Test
    void arrowsAndFire() {
        assertEquals(-1, k("ArrowUp", "ArrowUp"));
        assertEquals(-2, k("ArrowDown", "ArrowDown"));
        assertEquals(-3, k("ArrowLeft", "ArrowLeft"));
        assertEquals(-4, k("ArrowRight", "ArrowRight"));
        assertEquals(-5, k("Enter", "Enter"));
        assertEquals(-5, k("Enter", "NumpadEnter"));
    }

    @Test
    void softKeys() {
        assertEquals(-6, k("F1", "F1"));
        assertEquals(-7, k("F2", "F2"));
        assertEquals(-7, k("Escape", "Escape"), "Esc = right soft key (usually Back/Close)");
    }

    @Test
    void printableCharactersAreTypedAsTheirCharacterCode() {
        assertEquals('a', k("a", "KeyA"));
        assertEquals('Q', k("Q", "KeyQ"), "Q and W are letters now, not soft keys");
        assertEquals('w', k("w", "KeyW"));
        assertEquals(' ', k(" ", "Space"), "Space types a space (Enter fires)");
        assertEquals('@', k("@", "Digit2"));
        assertEquals('/', k("/", "NumpadDivide"));
        assertEquals('ấ', k("ấ", "KeyA"), "IME / Vietnamese input arrives as one character");
    }

    @Test
    void digitsStarAndPoundKeepTheirPhoneCodes() {
        for (int d = 0; d <= 9; d++) {
            assertEquals(48 + d, k(String.valueOf(d), "Digit" + d), "top row " + d);
            assertEquals(48 + d, k(String.valueOf(d), "Numpad" + d), "numpad " + d);
        }
        assertEquals(42, k("*", "Digit8"));
        assertEquals(42, k("*", "NumpadMultiply"));
        assertEquals(35, k("#", "Digit3"));
    }

    @Test
    void backspaceDeletes() {
        assertEquals(KeyMap.BACKSPACE, k("Backspace", "Backspace"));
        assertEquals(8, KeyMap.BACKSPACE, "the client's text field treats 8 as delete");
    }

    @Test
    void browserShortcutsAndNonCharacterKeysPassThrough() {
        assertEquals(KeyMap.PASS_THROUGH, KeyMap.fromBrowser("r", "KeyR", true), "Ctrl/Cmd+R reloads");
        assertEquals(KeyMap.PASS_THROUGH, KeyMap.fromBrowser("c", "KeyC", true));
        assertEquals(KeyMap.PASS_THROUGH, k("Tab", "Tab"));
        assertEquals(KeyMap.PASS_THROUGH, k("F5", "F5"));
        assertEquals(KeyMap.PASS_THROUGH, k("Shift", "ShiftLeft"));
        assertEquals(KeyMap.PASS_THROUGH, k("Dead", "BracketLeft"), "dead keys wait for the composed character");
    }

    @Test
    void qwertyModeIsPrimedOnlyBeforeTheFirstLetterForNonLetters() {
        // The client's text field does multi-tap for digits until it has seen a letter key (65..122).
        assertTrue(KeyMap.needsQwertyPrime('3', false), "a digit typed first would otherwise multi-tap to 'd'");
        assertTrue(KeyMap.needsQwertyPrime('@', false));
        assertFalse(KeyMap.needsQwertyPrime('a', false), "letters switch the client to QWERTY mode themselves");
        assertFalse(KeyMap.needsQwertyPrime('3', true), "only once per page");
        assertFalse(KeyMap.needsQwertyPrime(-5, false), "controls never prime");
        assertFalse(KeyMap.needsQwertyPrime(KeyMap.BACKSPACE, false));
        assertTrue(KeyMap.isLetter('a') && KeyMap.isLetter('Z') && !KeyMap.isLetter('3'));
    }

    @Test
    void gameActionsFollowMidp() {
        assertEquals(KeyMap.UP, KeyMap.gameAction(-1));
        assertEquals(KeyMap.DOWN, KeyMap.gameAction(-2));
        assertEquals(KeyMap.LEFT, KeyMap.gameAction(-3));
        assertEquals(KeyMap.RIGHT, KeyMap.gameAction(-4));
        assertEquals(KeyMap.FIRE, KeyMap.gameAction(-5));
        assertEquals(KeyMap.UP, KeyMap.gameAction(50), "2 = up");
        assertEquals(KeyMap.LEFT, KeyMap.gameAction(52), "4 = left");
        assertEquals(KeyMap.FIRE, KeyMap.gameAction(53), "5 = fire");
        assertEquals(KeyMap.RIGHT, KeyMap.gameAction(54), "6 = right");
        assertEquals(KeyMap.DOWN, KeyMap.gameAction(56), "8 = down");
        assertEquals(0, KeyMap.gameAction(-6), "soft keys have no game action");
        assertEquals(0, KeyMap.gameAction(48));
    }
}
