package com.ishland.membraneffi.api;

import com.ishland.membraneffi.api.annotations.HasJavaFallback;
import com.ishland.membraneffi.api.annotations.InstallMachineCode;
import com.ishland.membraneffi.api.annotations.Link;
import com.ishland.membraneffi.api.annotations.OsArchPair;
import com.ishland.membraneffi.api.annotations.VarargCall;
import com.ishland.membraneffi.util.JVMCIUtils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MembraneLinker {

    static {
        System.loadLibrary("java");
    }

    public static void linkClass(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(Link.class) != null || method.getAnnotation(InstallMachineCode.class) != null) {
                linkMethod(method);
            }
        }

    }

    public static void linkMethod(Method method) {
        boolean ignoreLinkFailures = method.getAnnotation(HasJavaFallback.class) != null;
        if (ignoreLinkFailures && Modifier.isNative(method.getModifiers())) {
            throw new IllegalArgumentException("Method " + method + " is native and has @HasJavaFallback annotation");
        }

        InstallMachineCode installMachineCode = method.getAnnotation(InstallMachineCode.class);
        Link link = method.getAnnotation(Link.class);

        if (installMachineCode != null && link != null) {
            throw new IllegalArgumentException("Method " + method + " is annotated with both @InstallMachineCode and @Link");
        }

        if (installMachineCode != null) {
            final Architecture architecture = Architecture.get();
            final OperatingSystem operatingSystem = OperatingSystem.get();
            for (InstallMachineCode.Entry entry : installMachineCode.value()) {
                for (OsArchPair pair : entry.targets()) {
                    if (pair.arch() == architecture && pair.os() == operatingSystem) {
                        installMachineCode0(method, parseHexString(entry.code()));
                        return;
                    }
                }
            }
            if (!ignoreLinkFailures) {
                throw new UnsatisfiedLinkError(String.format("Cannot find machine code for method %s", method));
            }
            return;
        }

        if (link == null) {
            if (!ignoreLinkFailures) {
                throw new IllegalArgumentException("Method " + method + " is not annotated with @Link");
            }
            return;
        }

        boolean isVarargCall = method.getAnnotation(VarargCall.class) != null;
        long address = 0;
        for (String symbol : link.value()) {
            address = findAddress(symbol);
            if (address != 0) break;
        }
        if (address == 0) {
            if (!ignoreLinkFailures) {
                throw new UnsatisfiedLinkError(String.format("Cannot find symbol for method %s, tried %s", method, Arrays.toString(link.value())));
            }
            return;
        }

        linkMethod0(method, address, isVarargCall);
    }

    public static void installMachineCode0(Method method, byte[] code) {
        JVMCIUtils.installCode(method, code, code.length);
    }

    public static void linkMethod0(Method method, long address, boolean isVarargCall) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CallingConventionAdapter adapter = CallingConventionAdapter.get();
        Parameter[] parameters = method.getParameters();
        CallingConventionAdapter.Argument[] arguments = new CallingConventionAdapter.Argument[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            arguments[i] = new CallingConventionAdapter.Argument(i, parameters[i].getType(), parameters[i].getAnnotations());
        }
        adapter.emit(out, arguments, method.getReturnType(), address, isVarargCall);

        installMachineCode0(method, out.toByteArray());
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

    // parse strings used for @InstallMachineCode, spaces are allowed
    private static byte[] parseHexString(String hexString) {
        hexString = hexString.replaceAll("\\s+", "");
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
        }
        return result;
    }

}
