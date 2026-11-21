package javax.microedition.lcdui;

/** MIDP Displayable: only what Canvas needs. */
public abstract class Displayable {
    boolean shown;

    Displayable() {}

    public int getWidth() {
        return Display.screenWidth;
    }

    public int getHeight() {
        return Display.screenHeight;
    }

    public boolean isShown() {
        return shown;
    }

    public void setTitle(String title) {}

    public String getTitle() {
        return null;
    }

    void showNotifyInternal() {}

    void hideNotifyInternal() {}
}
