package com.ishland.membraneffi.api;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

public interface CallingConventionAdapter {

    void emit(ByteArrayOutputStream out, Argument[] arguments, Class<?> returnType, long address);

    public static CallingConventionAdapter get() {
        switch (OperatingSystem.get()) {
            case LINUX:
            case OSX:
                switch (Architecture.get()) {
                    case X86_64:
                        return new com.ishland.membraneffi.impl.LinuxX86_64CallingConvention();
                    default:
                        throw new UnsupportedOperationException("Unsupported architecture: " + Architecture.get());
                }
            default: // includes windows
                switch (Architecture.get()) {
                    case X86_64:
                        return new com.ishland.membraneffi.impl.LinuxX86_64CallingConvention();
                    default:
                        throw new UnsupportedOperationException("Unsupported architecture: " + Architecture.get());
                }
        }
    }

    public static final class Argument {
        private final int index;
        private final Class<?> type;
        private final Annotation[] annotations;

        public Argument(int index, Class<?> type, Annotation[] annotations) {
            this.index = index;
            this.type = type;
            this.annotations = annotations;
        }

        public int index() {
            return index;
        }

        public Class<?> type() {
            return type;
        }

        public Annotation[] annotations() {
            return annotations;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            Argument that = (Argument) obj;
            return this.index == that.index &&
                   Objects.equals(this.type, that.type) &&
                   Arrays.equals(this.annotations, that.annotations);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, type, annotations);
        }

        @Override
        public String toString() {
            return "Argument[" +
                   "index=" + index + ", " +
                   "type=" + type + ", " +
                   "annotations=" + annotations + ']';
        }

    }

}
