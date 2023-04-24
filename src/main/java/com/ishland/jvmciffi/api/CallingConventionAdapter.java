package com.ishland.jvmciffi.api;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;

public interface CallingConventionAdapter {

    void emitCallPrelude(ByteArrayOutputStream out, Argument[] arguments);

    void emitCall(ByteArrayOutputStream buf, long address);

    public static CallingConventionAdapter get() {
        switch (OperatingSystem.get()) {
            case LINUX, OSX -> {
                switch (Architecture.get()) {
                    case X86_64 -> {
                        return new com.ishland.jvmciffi.impl.LinuxX86_64CallingConvention();
                    }
                    default -> throw new UnsupportedOperationException("Unsupported architecture: " + Architecture.get());
                }
            }
            default -> throw new UnsupportedOperationException("Unsupported operating system: " + OperatingSystem.get());
        }
    }

    public record Argument(int index, Class<?> type, Annotation[] annotations) {
    }

}
