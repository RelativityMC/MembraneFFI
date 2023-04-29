package com.ishland.jvmciffi.util;

public class MathHelper {

    public static int roundUpToMultiple(int value, int divisor) {
        return ceilDiv(value, divisor) * divisor;
    }

    public static int ceilDiv(int a, int b) {
        return -Math.floorDiv(-a, b);
    }

}
