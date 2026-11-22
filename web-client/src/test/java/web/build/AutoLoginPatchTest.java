package web.build;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/** Patches a copy of the original login screen class (Army165local is never modified). */
class AutoLoginPatchTest {
    private static final Path ORIGINAL = Paths.get("..", "Army165local", "coreLG", "dl.class");

    @Test
    void insertsTheTicketLoginAtTheStartOfTheInputMethodOnce(@TempDir Path dir) throws Exception {
        Path copy = dir.resolve("coreLG/dl.class");
        Files.createDirectories(copy.getParent());
        Files.copy(ORIGINAL, copy);

        AutoLoginPatch.main(new String[] { dir.toString() });
        byte[] once = Files.readAllBytes(copy);
        List<String> calls = callsInD(once);
        assertEquals("web/AutoLogin.pending", calls.get(0), "first thing d() does");
        assertTrue(calls.contains("web/AutoLogin.user") && calls.contains("web/AutoLogin.ticket"));
        assertTrue(calls.contains("coreLG/dl.g"), "presses Đăng nhập");

        AutoLoginPatch.main(new String[] { dir.toString() });
        assertArrayEquals(once, Files.readAllBytes(copy), "second run changes nothing");
        assertFalse(new String(Files.readAllBytes(ORIGINAL), "ISO-8859-1").contains("web/AutoLogin"), "original untouched");
    }

    private static List<String> callsInD(byte[] cls) {
        List<String> calls = new ArrayList<>();
        new ClassReader(cls).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                if (!"d".equals(name) || !"()V".equals(desc)) return null;
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitMethodInsn(int op, String owner, String n, String d, boolean itf) {
                        calls.add(owner + "." + n);
                    }
                };
            }
        }, 0);
        return calls;
    }
}
