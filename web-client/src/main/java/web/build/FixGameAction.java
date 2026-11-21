package web.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Build-time only. The original client's {@code coreLG.el.getGameAction(int)} calls itself (in both the
 * original MIDlet bytecode and the decompiled source), so every key press would overflow the stack.
 * It is meant to call {@code Canvas.getGameAction}. This rewrites that one call in the compiled class,
 * leaving Client/ untouched. Fails the build if the expected call isn't found exactly once.
 */
public final class FixGameAction {
    private static final String OWNER = "coreLG/el";
    private static final String CANVAS = "javax/microedition/lcdui/Canvas";

    public static void main(String[] args) throws IOException {
        Path file = Paths.get(args[0], OWNER + ".class");
        byte[] in = Files.readAllBytes(file);
        int[] fixed = {0};
        ClassWriter cw = new ClassWriter(0);
        new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                MethodVisitor mv = super.visitMethod(access, name, desc, sig, ex);
                if (!"getGameAction".equals(name) || !"(I)I".equals(desc)) return mv;
                return new MethodVisitor(Opcodes.ASM9, mv) {
                    @Override
                    public void visitMethodInsn(int op, String owner, String n, String d, boolean itf) {
                        if (op == Opcodes.INVOKEVIRTUAL && OWNER.equals(owner) && "getGameAction".equals(n) && "(I)I".equals(d)) {
                            super.visitMethodInsn(Opcodes.INVOKESPECIAL, CANVAS, n, d, false);
                            fixed[0]++;
                        } else {
                            super.visitMethodInsn(op, owner, n, d, itf);
                        }
                    }
                };
            }
        }, 0);
        if (fixed[0] == 0 && isAlreadyFixed(in)) {
            System.out.println("[FixGameAction] already fixed");
            return;
        }
        if (fixed[0] != 1) {
            throw new IllegalStateException("expected exactly one self-call in " + OWNER + ".getGameAction, found " + fixed[0]);
        }
        Files.write(file, cw.toByteArray());
        System.out.println("[FixGameAction] " + OWNER + ".getGameAction now calls Canvas.getGameAction");
    }

    private static boolean isAlreadyFixed(byte[] in) {
        boolean[] found = {false};
        new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                if (!"getGameAction".equals(name) || !"(I)I".equals(desc)) return null;
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitMethodInsn(int op, String owner, String n, String d, boolean itf) {
                        if (op == Opcodes.INVOKESPECIAL && CANVAS.equals(owner)) found[0] = true;
                    }
                };
            }
        }, 0);
        return found[0];
    }
}
