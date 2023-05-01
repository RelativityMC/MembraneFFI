package com.ishland.membraneffi.util;

import java.lang.reflect.Array;

public class ReflectionUtils {

    public static Class<?> arrayType(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }

}
