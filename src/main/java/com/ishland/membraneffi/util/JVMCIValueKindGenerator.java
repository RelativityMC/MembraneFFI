package com.ishland.membraneffi.util;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Proxy;

public class JVMCIValueKindGenerator {

    public static final Class<?> generatedClazz;
    public static final Object generatedFactory;

    static {
        try {
            generatedClazz = MethodHandles.lookup().defineClass(generate());
            generatedFactory = generateFactory();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
    }

    private static byte[] generate() {
        try {
            final Class<?> clazz_ValueKind = Class.forName("jdk.vm.ci.meta.ValueKind");
            final Class<?> clazz_PlatformKind = Class.forName("jdk.vm.ci.meta.PlatformKind");
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            final String name = "com/ishland/membraneffi/util/GeneratedValueKindImpl";
            writer.visit(Opcodes.V17, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, name, null, Type.getInternalName(clazz_ValueKind), new String[0]);

            // <clinit> that does nothing
            {
                final MethodVisitor visitor = writer.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
                visitor.visitCode();
                visitor.visitInsn(Opcodes.RETURN);
                visitor.visitMaxs(0, 0);
                visitor.visitEnd();
            }

            // <init>
            {
                final MethodVisitor visitor = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(clazz_PlatformKind)), null, null);
                visitor.visitCode();
                visitor.visitVarInsn(Opcodes.ALOAD, 0);
                visitor.visitVarInsn(Opcodes.ALOAD, 1);
                visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(clazz_ValueKind), "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(clazz_PlatformKind)), false);
                visitor.visitInsn(Opcodes.RETURN);
                visitor.visitMaxs(0, 0);
                visitor.visitEnd();
            }

            // changeType
            {
                final MethodVisitor visitor = writer.visitMethod(Opcodes.ACC_PUBLIC, "changeType", Type.getMethodDescriptor(Type.getType(clazz_ValueKind), Type.getType(clazz_PlatformKind)), null, null);
                visitor.visitCode();
                //    NEW name
                //    DUP
                //    ALOAD 1
                //    INVOKESPECIAL name.<init> (Ljdk/vm/ci/meta/PlatformKind;)V
                //    ARETURN
                visitor.visitTypeInsn(Opcodes.NEW, name);
                visitor.visitInsn(Opcodes.DUP);
                visitor.visitVarInsn(Opcodes.ALOAD, 1);
                visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(clazz_PlatformKind)), false);
                visitor.visitInsn(Opcodes.ARETURN);
                visitor.visitMaxs(0, 0);
                visitor.visitEnd();
            }

            return writer.toByteArray();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static Object generateFactory() {
        try {
            final Class<?> clazz_PlatformKind = Class.forName("jdk.vm.ci.meta.PlatformKind");
            final Class<?> clazz_ValueKindFactory = Class.forName("jdk.vm.ci.code.ValueKindFactory");
            return Proxy.newProxyInstance(JVMCIValueKindGenerator.class.getClassLoader(), new Class<?>[]{clazz_ValueKindFactory}, (proxy, method, args) -> {
                if (method.getName().equals("getValueKind")) {
                    final Object javaKind = args[0];
                    final Object targetDescription = JVMCIAccess.jvmciBackend$getTarget();
                    final Object architecture = JVMCIAccess.targetDescription$arch$get(targetDescription);
                    return generatedClazz.getConstructor(clazz_PlatformKind).newInstance(
                            JVMCIAccess.architecture$getPlatformKind(architecture, javaKind)
                    );
                }
                throw new UnsupportedOperationException();
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
