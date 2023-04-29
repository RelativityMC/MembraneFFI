package com.ishland.jvmciffi.api;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;

public interface CallingConventionAdapter {

    void emit(ByteArrayOutputStream out, Argument[] arguments, Class<?> returnType, long address);

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
            case WINDOWS -> {
                switch (Architecture.get()) {
                    case X86_64 -> {
                        return new com.ishland.jvmciffi.impl.FramedX86_64CallingConvention();
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
