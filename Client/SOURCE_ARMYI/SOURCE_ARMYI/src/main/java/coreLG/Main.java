package coreLG;

/**
 * Wrapper launcher gọi MicroEmulator với MIDlet Mobiarmy.
 *
 * Cách 1 - Chạy trong NetBeans/IDE (tránh "No main classes found"):
 *   - Chuột phải file Main.java -> Run File (Shift+F6), hoặc
 *   - Chuột phải project -> Properties -> Run -> Main Class = coreLG.Main
 *
 * Cách 2 - Maven:
 *   mvn exec:java -Dexec.mainClass="coreLG.Main"
 *
 * Cách 3 - Dòng lệnh:
 *   java -cp <tất cả JARs> coreLG.Main [MIDlet-class-name]
 *
 * Lưu ý: MIDlet của game là {@link TerrainMidlet} trong cùng package.
 */
public final class Main {

    /** Tên MIDlet mặc định của game Mobiarmy. */
    public static final String DEFAULT_MIDLET = "coreLG.TerrainMidlet";

    private Main() {
    }

    public static void main(String[] args) {
        String midletClassName = args.length > 0 ? args[0] : DEFAULT_MIDLET;
        org.microemu.app.Main.main(new String[] { midletClassName });
    }
}