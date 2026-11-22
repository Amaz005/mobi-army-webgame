package web.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Build-time only (specs/004-game-portal research D15). Adds portal auto-login to the original client's
 * login screen {@code coreLG.dl}, leaving Client/ and Army165local/ untouched. At the start of its
 * per-frame input method {@code d()} it inserts:
 * <pre>
 * if (web.AutoLogin.pending()) {
 *     if (this.h) { el.H[12] = true; }                   // h: "Bật âm thanh?" asked: answer "Bật" (left soft key),
 *                                                        //    keeping the saved volume; the code below handles it
 *     else if (this.t) { this.w = true; }                // t: intro running; w: fast-forward (as "Qua nhanh")
 *     else {                                             // the login form
 *         web.AutoLogin.consume();
 *         this.a.a(web.AutoLogin.user());                // name field
 *         this.b.a(web.AutoLogin.ticket());              // password field
 *         this.g();                                      // the "Đăng nhập" action
 *         return;
 *     }
 * }
 * </pre>
 * Fails the build if the class doesn't have the expected fields and methods.
 */
public final class AutoLoginPatch {
    private static final String DL = "coreLG/dl";
    private static final String FIELD_TYPE = "Lbc;";
    private static final String AUTO = "web/AutoLogin";

    public static void main(String[] args) throws IOException {
        Path file = Paths.get(args[0], DL + ".class");
        byte[] in = Files.readAllBytes(file);
        if (alreadyPatched(in)) {
            System.out.println("[AutoLoginPatch] already patched");
            return;
        }
        checkShape(in);
        int[] patched = {0};
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                MethodVisitor mv = super.visitMethod(access, name, desc, sig, ex);
                if (!"d".equals(name) || !"()V".equals(desc)) return mv;
                return new MethodVisitor(Opcodes.ASM9, mv) {
                    @Override
                    public void visitCode() {
                        super.visitCode();
                        insert(mv);
                        patched[0]++;
                    }
                };
            }
        }, 0);
        if (patched[0] != 1) throw new IllegalStateException("expected one " + DL + ".d()V, found " + patched[0]);
        Files.write(file, cw.toByteArray());
        System.out.println("[AutoLoginPatch] " + DL + ".d() now logs in with a portal ticket when one is pending");
    }

    private static void insert(MethodVisitor mv) {
        Label end = new Label();
        Label form = new Label();
        Label notAsking = new Label();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, AUTO, "pending", "()Z", false);
        mv.visitJumpInsn(Opcodes.IFEQ, end);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, DL, "h", "Z");
        mv.visitJumpInsn(Opcodes.IFEQ, notAsking);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "coreLG/el", "H", "[Z");
        mv.visitIntInsn(Opcodes.BIPUSH, 12);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitInsn(Opcodes.BASTORE);
        mv.visitJumpInsn(Opcodes.GOTO, end);
        mv.visitLabel(notAsking);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, DL, "t", "Z");
        mv.visitJumpInsn(Opcodes.IFEQ, form);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitFieldInsn(Opcodes.PUTFIELD, DL, "w", "Z");
        mv.visitJumpInsn(Opcodes.GOTO, end);
        mv.visitLabel(form);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, AUTO, "consume", "()V", false);
        setText(mv, "a", "user");
        setText(mv, "b", "ticket");
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, DL, "g", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitLabel(end);
    }

    private static void setText(MethodVisitor mv, String field, String getter) {
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, DL, field, FIELD_TYPE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, AUTO, getter, "()Ljava/lang/String;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "bc", "a", "(Ljava/lang/String;)V", false);
    }

    /** The fields and methods the inserted code relies on, with the expected types. */
    private static void checkShape(byte[] in) {
        java.util.Set<String> found = new java.util.HashSet<>();
        new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public org.objectweb.asm.FieldVisitor visitField(int access, String name, String desc, String sig, Object value) {
                found.add("F " + name + " " + desc);
                return null;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                found.add("M " + name + desc);
                return null;
            }
        }, 0);
        String[] need = { "F h Z", "F t Z", "F w Z", "F a " + FIELD_TYPE, "F b " + FIELD_TYPE, "M g()V", "M d()V" };
        for (String n : need) {
            if (!found.contains(n)) throw new IllegalStateException(DL + " has no " + n + "; the client changed, update AutoLoginPatch");
        }
    }

    private static boolean alreadyPatched(byte[] in) {
        return new String(in, java.nio.charset.StandardCharsets.ISO_8859_1).contains(AUTO);
    }
}
