package com.ishland.membraneffi.api;

import java.util.Locale;

public enum Architecture {

    X86_64, UNSUPPORTED;

    public static Architecture get() {
        String arch = System.getProperty("os.arch");
        String normalizedArch = arch.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        if (normalizedArch.matches("^(x8664|amd64|ia32e|em64t|x64)$")) return X86_64;
        return UNSUPPORTED;
    }

}
