package com.ishland.membraneffi.util;

import com.ishland.membraneffi.api.CallingConventionAdapter;

import java.lang.reflect.Method;

public class JVMCIUtils {

    public static int baseOffset(CallingConventionAdapter.Argument argument) {
        if (argument.type().isArray() && argument.type().getComponentType().isPrimitive()) {
            return arrayBaseOffset(argument.type());
        }

        throw new UnsupportedOperationException("Unsupported type: " + argument.type());
    }

    public static int arrayBaseOffset(Class<?> arrayType) {
        final Object componentType = JVMCIAccess.javaKind$fromJavaClass(arrayType.getComponentType());
        return JVMCIAccess.metaAccessProvider$getArrayBaseOffset(componentType);
    }

    public static void installCode(Method m, byte[] code, int length) {
        final Object resolvedJavaMethod = JVMCIAccess.metaAccessProvider$lookupJavaMethod(m);

        final Object resolvedJavaMethodArray = JVMCIAccess.resolvedJavaMethodArray$constructor(1);
        JVMCIAccess.resolvedJavaMethod$array$set(resolvedJavaMethodArray, 0, resolvedJavaMethod);
        Object hotspotCompiledNmethod = JVMCIAccess.hotSpotCompiledNmethod$constructor(
                m.getName(),
                code,
                length,
                JVMCIAccess.siteArray$constructor(0),
                JVMCIAccess.assumptions$AssumptionArray$constructor(0),
                resolvedJavaMethodArray,
                JVMCIAccess.hotSpotCompiledCode$CommentArray$constructor(0),
                new byte[0],
                1,
                JVMCIAccess.dataPatchArray$constructor(0),
                true,
                0,
                null,
                resolvedJavaMethod,
                JVMCIAccess.jvmciCompiler$INVOCATION_ENTRY_BCI$get(),
                JVMCIAccess.hotspotResolvedJavaMethod$allocateCompileId(resolvedJavaMethod, JVMCIAccess.jvmciCompiler$INVOCATION_ENTRY_BCI$get()),
                0,
                false
        );

        JVMCIAccess.codeCacheProvider$setDefaultCode(resolvedJavaMethod, hotspotCompiledNmethod);

        JVMCIAccess.hotspotResolvedJavaMethod$setNotInlinableOrCompilable(resolvedJavaMethod);
    }

}
