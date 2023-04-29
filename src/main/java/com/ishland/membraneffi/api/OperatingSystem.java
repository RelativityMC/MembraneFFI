package com.ishland.membraneffi.api;

import java.util.Locale;

public enum OperatingSystem {

    LINUX, OSX, WINDOWS, UNSUPPORTED;

    public static OperatingSystem get() {
        String os = System.getProperty("os.name");
        String normalizedOs = os.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        if (normalizedOs.startsWith("linux")) return LINUX;
        if (normalizedOs.startsWith("macosx") || normalizedOs.startsWith("osx") || normalizedOs.startsWith("darwin")) return OSX;
        if (normalizedOs.startsWith("windows")) return WINDOWS;
        return UNSUPPORTED;
    }

}
