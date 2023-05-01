package com.ishland.membraneffi.util;

public class CallingConventionOverride {

    private static Class<?> callingConventionOverride = null;

    public static void setCallingConventionOverride(Class<?> clazz) {
        callingConventionOverride = clazz;
    }

    public static Class<?> getCallingConventionOverride() {
        return callingConventionOverride;
    }

}
