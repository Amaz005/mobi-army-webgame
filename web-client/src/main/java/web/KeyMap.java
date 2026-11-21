package web;

/** Browser keys → MIDP key codes (specs/002-web-client/contracts/key-mapping.md). Pure logic, no browser APIs. */
public final class KeyMap {
    /** Returned for keys the game doesn't use; the browser keeps its default behaviour for them. */
    public static final int PASS_THROUGH = 0;

    /** The client's text field deletes on key code 8. */
    public static final int BACKSPACE = 8;

    // MIDP Canvas game actions.
    public static final int UP = 1, LEFT = 2, RIGHT = 5, DOWN = 6, FIRE = 8;

    private KeyMap() {}

    /**
     * @param shortcut true when Ctrl or Cmd is held: those combinations stay with the browser.
     * @return the MIDP key code, a character code for typed text, or {@link #PASS_THROUGH}.
     */
    public static int fromBrowser(String key, String code, boolean shortcut) {
        if (shortcut) return PASS_THROUGH;
        if (code != null) {
            switch (code) {
                case "ArrowUp": return -1;
                case "ArrowDown": return -2;
                case "ArrowLeft": return -3;
                case "ArrowRight": return -4;
                case "Enter": case "NumpadEnter": return -5;
                case "F1": return -6;
                case "F2": case "Escape": return -7;
                case "Backspace": return BACKSPACE;
                default: break;
            }
        }
        // Any single printable character is typed as itself. Digits, '*' and '#' have the same codes
        // as the phone keypad (48–57, 42, 35), so they still work as game keys outside text fields.
        if (key != null && key.length() == 1 && key.charAt(0) >= 32) {
            return key.charAt(0);
        }
        return PASS_THROUGH;
    }

    public static boolean isLetter(int c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * The client's text field multi-taps digits until it has seen one letter key, then types everything
     * literally (a one-way, page-wide switch). Before the first typed non-letter, the web layer sends a
     * letter + Backspace so a password like "3" isn't turned into "d".
     */
    public static boolean needsQwertyPrime(int keyCode, boolean primed) {
        return !primed && keyCode >= 32 && !isLetter(keyCode);
    }

    /** MIDP {@code Canvas.getGameAction}: arrows/fire plus the usual 2/4/5/6/8 keypad layout. */
    public static int gameAction(int keyCode) {
        switch (keyCode) {
            case -1: case '2': return UP;
            case -2: case '8': return DOWN;
            case -3: case '4': return LEFT;
            case -4: case '6': return RIGHT;
            case -5: case '5': return FIRE;
            default: return 0;
        }
    }

    /** MIDP {@code Canvas.getKeyCode}: the primary key for a game action. */
    public static int keyCode(int gameAction) {
        switch (gameAction) {
            case UP: return -1;
            case DOWN: return -2;
            case LEFT: return -3;
            case RIGHT: return -4;
            case FIRE: return -5;
            default: return 0;
        }
    }
}
