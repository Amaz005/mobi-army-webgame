package web.build;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.teavm.classlib.ResourceSupplier;
import org.teavm.classlib.ResourceSupplierContext;

/**
 * Build-time only (runs inside the TeaVM compiler, never in the browser): embeds the game's resource
 * files so the client's {@code getResourceAsStream("/…")} calls work (research.md R4 P2).
 * The files are copied onto the classpath from ../Army165local by the POM; this lists their names.
 */
public final class GameResources implements ResourceSupplier {
    @Override
    public String[] supplyResources(ResourceSupplierContext context) {
        String dir = context.getProperties().getProperty("mobiarmy.resources");
        if (dir == null) throw new IllegalStateException("TeaVM property mobiarmy.resources is not set");
        Path root = Paths.get(dir);
        try (Stream<Path> files = Files.walk(root)) {
            return files.filter(Files::isRegularFile)
                    .map(p -> root.relativize(p).toString().replace('\\', '/'))
                    .filter(name -> !name.endsWith(".class") && !name.startsWith("META-INF/")
                            && !name.contains(".svn/") && !name.endsWith("Thumbs.db") && !name.startsWith("verify/") && !name.startsWith("network/"))
                    .sorted()
                    .toArray(String[]::new);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
