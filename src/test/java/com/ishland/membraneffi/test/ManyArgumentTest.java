package com.ishland.membraneffi.test;

import com.ishland.membraneffi.api.MembraneLinker;
import com.ishland.membraneffi.api.annotations.LinkTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManyArgumentTest {

    @LinkTo("sprintf")
    private static void sprintf_test(byte[] res, byte[] format,
                                            int arg1, double arg2, byte[] arg3,
                                            int arg4, double arg5, byte[] arg6,
                                            int arg7, double arg8, byte[] arg9,
                                            int arg10, double arg11, byte[] arg12,
                                            int arg13, double arg14, byte[] arg15,
                                            int arg16, double arg17, byte[] arg18,
                                            int arg19, double arg20, byte[] arg21,
                                            int arg22, double arg23, byte[] arg24,
                                            int arg25, double arg26, byte[] arg27,
                                            int arg28, double arg29, byte[] arg30,
                                            int arg31, double arg32, byte[] arg33,
                                            int arg34, double arg35, byte[] arg36,
                                            int arg37, double arg38, byte[] arg39,
                                            int arg40, double arg41, byte[] arg42,
                                            int arg43, double arg44, byte[] arg45,
                                            int arg46, double arg47, byte[] arg48,
                                            int arg49, double arg50, byte[] arg51,
                                            int arg52, double arg53, byte[] arg54,
                                            int arg55, double arg56, byte[] arg57,
                                            int arg58, double arg59, byte[] arg60,
                                            int arg61, double arg62, byte[] arg63,
                                            int arg64, double arg65, byte[] arg66,
                                            int arg67, double arg68, byte[] arg69,
                                            int arg70, double arg71, byte[] arg72,
                                            int arg73, double arg74, byte[] arg75,
                                            int arg76, double arg77, byte[] arg78,
                                            int arg79, double arg80, byte[] arg81,
                                            int arg82, double arg83, byte[] arg84,
                                            int arg85, double arg86, byte[] arg87,
                                            int arg88, double arg89, byte[] arg90,
                                            int arg91, double arg92, byte[] arg93,
                                            int arg94, double arg95, byte[] arg96,
                                            int arg97, double arg98, byte[] arg99,
                                            int arg100, double arg101, byte[] arg102,
                                            int arg103, double arg104, byte[] arg105,
                                            int arg106, double arg107, byte[] arg108,
                                            int arg109, double arg110, byte[] arg111,
                                            int arg112, double arg113, byte[] arg114,
                                            int arg115, double arg116, byte[] arg117,
                                            int arg118, double arg119, byte[] arg120) {
        throw new RuntimeException("Not installed");
    }

    private static byte[] toCString(String s) {
        byte[] bytes = s.getBytes();
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        return result;
    }

    private static String fromCString(byte[] bytes) {
        int i = 0;
        while (bytes[i] != 0) i++;
        return new String(bytes, 0, i);
    }

    @Test
    public void test_sprintf() {
        final String s = "%d %f %s ".repeat(40);
        final byte[] formatBytes = toCString(s);
        final int arg1 = 12345678;
        final double arg2 = 07654321.12345678;
        final String arg3 = "test";
        final byte[] arg3Bytes = toCString(arg3);

        final byte[] res = new byte[65536];
        sprintf_test(res, formatBytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes,
                arg1, arg2, arg3Bytes
        );
        final String expected = String.format(s,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3,
                arg1, arg2, arg3
                );

        Assertions.assertEquals(expected, fromCString(res));
    }

    static {
        TestUtil.init();
        MembraneLinker.linkClass(ManyArgumentTest.class);
    }

}
