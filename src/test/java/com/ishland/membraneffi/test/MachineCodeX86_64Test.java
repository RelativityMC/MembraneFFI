package com.ishland.membraneffi.test;

import com.ishland.membraneffi.api.Architecture;
import com.ishland.membraneffi.api.MembraneLinker;
import com.ishland.membraneffi.api.OperatingSystem;
import com.ishland.membraneffi.api.annotations.HasJavaFallback;
import com.ishland.membraneffi.api.annotations.InstallMachineCode;
import com.ishland.membraneffi.api.annotations.OsArchPair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

public class MachineCodeX86_64Test {

    @HasJavaFallback
    @InstallMachineCode({
            @InstallMachineCode.Entry(
                    targets = {
                            @OsArchPair(os = OperatingSystem.LINUX, arch = Architecture.X86_64),
                            @OsArchPair(os = OperatingSystem.OSX, arch = Architecture.X86_64),
                            @OsArchPair(os = OperatingSystem.WINDOWS, arch = Architecture.X86_64),
                    },
                    // movabs rax,0x1234567890abcdef
                    // ret
                    code = "48b8efcdab9078563412 c3"
            )
    })
    private static long testImpl() {
        return 0x1234567890abcdefL;
    }

    @Test
    public void test() {
        MembraneLinker.linkClass(MachineCodeX86_64Test.class);
        Assertions.assertEquals(0x1234567890abcdefL, testImpl());
    }

    static {
        TestUtil.init();
    }

}
