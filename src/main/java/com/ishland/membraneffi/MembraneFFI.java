package com.ishland.membraneffi;

import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MembraneFFI {
    
    private static boolean initialized = false;

    public static void initialize(Instrumentation inst) {
        if (initialized) return;
        synchronized (MembraneFFI.class) {
            if (initialized) return;
            initialize0(inst);
            initialized = true;
        }
    }

    private static void initialize0(Instrumentation inst) {
        exportJvmciPackages(inst);
        openJavaLangPackages(inst);
    }

    private static void openJavaLangPackages(Instrumentation inst) {
        Optional<Module> javaLangModule = ModuleLayer.boot().findModule("java.base");
        if (javaLangModule.isEmpty()) {
            throw new IllegalStateException("java.base module not found ?????");
        }

        Set<Module> unnamed = Set.of(
                MembraneFFI.class.getModule()
        );

        Map<String, Set<Module>> extraOpens = Map.of(
                "java.lang", unnamed
        );

        inst.redefineModule(javaLangModule.get(), Collections.emptySet(), Collections.emptyMap(),
                extraOpens, Collections.emptySet(), Collections.emptyMap());
    }

    private static void exportJvmciPackages(Instrumentation inst) {
        Optional<Module> jvmciModule = ModuleLayer.boot().findModule("jdk.internal.vm.ci");
        if (jvmciModule.isEmpty()) {
            throw new IllegalStateException("JVMCI module not found. Use -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI");
        }

        Set<Module> unnamed = Set.of(
                MembraneFFI.class.getModule()
        );

        Map<String, Set<Module>> extraExports = Map.of(
                "jdk.vm.ci.code", unnamed,
                "jdk.vm.ci.code.site", unnamed,
                "jdk.vm.ci.hotspot", unnamed,
                "jdk.vm.ci.meta", unnamed,
                "jdk.vm.ci.runtime", unnamed
        );

        inst.redefineModule(jvmciModule.get(), Collections.emptySet(), extraExports,
                Collections.emptyMap(), Collections.emptySet(), Collections.emptyMap());
    }

}
