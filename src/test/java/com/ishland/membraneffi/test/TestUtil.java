package com.ishland.membraneffi.test;

import com.ishland.membraneffi.MembraneFFI;
import com.ishland.membraneffi.api.OperatingSystem;
import net.bytebuddy.agent.ByteBuddyAgent;

public class TestUtil {

    public static void init() {
    }

    static {
        MembraneFFI.initialize(ByteBuddyAgent.install());
        loadWindowsLibrary();
    }

    private static void loadWindowsLibrary() {
        if (OperatingSystem.get() == OperatingSystem.WINDOWS) {
            try {
                System.loadLibrary("msvcrt");
            } catch (Throwable t) {
                System.out.println("Failed to load msvcrt.dll: %s".formatted(t.toString()));
            }
            try {
                System.loadLibrary("ucrtbase");
            } catch (Throwable t) {
                System.out.println("Failed to load ucrtbase.dll: %s".formatted(t.toString()));
            }
        }
    }

}
