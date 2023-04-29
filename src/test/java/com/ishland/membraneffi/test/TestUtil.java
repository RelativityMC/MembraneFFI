package com.ishland.membraneffi.test;

import com.ishland.membraneffi.JvmciFFI;
import net.bytebuddy.agent.ByteBuddyAgent;

public class TestUtil {

    public static void init() {
    }

    static {
        JvmciFFI.initialize(ByteBuddyAgent.install());
    }

}
