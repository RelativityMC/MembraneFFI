package com.ishland.jvmciffi;

import com.ishland.jvmciffi.api.CallingConventionAdapter;
import com.ishland.jvmciffi.api.annotations.LinkTo;
import com.ishland.jvmciffi.util.JVMCIUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Linker {

    static {
        System.loadLibrary("java");
    }

    public static void linkClass(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(LinkTo.class) != null) {
                linkMethod(method);
            }
        }

    }

    private static void linkMethod(Method method) {
        LinkTo linkTo = method.getAnnotation(LinkTo.class);
        if (linkTo == null) {
            throw new IllegalArgumentException("Method " + method + " is not annotated with @LinkTo");
        }
        String symbol = linkTo.value();
        long address = findAddress(symbol);
        if (address == 0) {
            throw new IllegalStateException("Symbol " + symbol + " not found");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CallingConventionAdapter adapter = CallingConventionAdapter.get();
        Parameter[] parameters = method.getParameters();
        CallingConventionAdapter.Argument[] arguments = new CallingConventionAdapter.Argument[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            arguments[i] = new CallingConventionAdapter.Argument(i, parameters[i].getType(), parameters[i].getAnnotations());
        }
        adapter.emitCallPrelude(out, arguments);
        adapter.emitCall(out, address);

        final byte[] code = out.toByteArray();
        JVMCIUtils.installCode(method, code, code.length);
    }

    private static long findAddress(String symbol) {
        try {
            Method m = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);
            m.setAccessible(true);
            return (long) m.invoke(null, Linker.class.getClassLoader(), symbol);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to locate symbol %s".formatted(symbol), e);
        }
    }

}
