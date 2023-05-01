package com.ishland.membraneffi.api;

import com.ishland.membraneffi.api.annotations.Link;
import com.ishland.membraneffi.api.annotations.VarargCall;
import com.ishland.membraneffi.util.JVMCIUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MembraneLinker {

    static {
        System.loadLibrary("java");
    }

    public static void linkClass(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(Link.class) != null) {
                linkMethod(method);
            }
        }

    }

    public static void linkMethod(Method method) {
        Link link = method.getAnnotation(Link.class);
        if (link == null) {
            throw new IllegalArgumentException("Method " + method + " is not annotated with @Link");
        }
        boolean isVarargCall = method.getAnnotation(VarargCall.class) != null;
        long address = 0;
        for (String symbol : link.value()) {
            address = findAddress(symbol);
            if (address != 0) break;
        }
        if (address == 0) {
            throw new IllegalStateException(String.format("Cannot find symbol for method %s, tried %s", method, Arrays.toString(link.value())));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CallingConventionAdapter adapter = CallingConventionAdapter.get();
        Parameter[] parameters = method.getParameters();
        CallingConventionAdapter.Argument[] arguments = new CallingConventionAdapter.Argument[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            arguments[i] = new CallingConventionAdapter.Argument(i, parameters[i].getType(), parameters[i].getAnnotations());
        }
        adapter.emit(out, arguments, method.getReturnType(), address, isVarargCall);

        final byte[] code = out.toByteArray();
        JVMCIUtils.installCode(method, code, code.length);
    }

    private static long findAddress(String symbol) {
        try {
            Method m = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);
            m.setAccessible(true);
            return (long) m.invoke(null, MembraneLinker.class.getClassLoader(), symbol);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(String.format("Failed to locate symbol %s", symbol), e);
        }
    }

}
