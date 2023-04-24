package com.ishland.jvmciffi.test;

import com.ishland.jvmciffi.JvmciFFI;
import net.bytebuddy.agent.ByteBuddyAgent;

public class TestUtil {

    public static void init() {
    }

    static {
        JvmciFFI.initialize(ByteBuddyAgent.install());
    }

}
