package com.ishland.jvmciffi.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Executable;

public class JVMCIAccess {

    private static final Class<?> clazz_JVMCI;
    private static final Class<?> clazz_JVMCIRuntime;
    private static final Class<?> clazz_JVMCIBackend;
    private static final Class<?> clazz_MetaAccessProvider;
    private static final Class<?> clazz_JavaKind;
    private static final Class<?> clazz_ResolvedJavaMethod;
    private static final Class<?> clazz_HotspotResolvedJavaMethod;
    private static final Class<?> clazz_HotSpotCompiledNmethod;
    private static final Class<?> clazz_Site;
    private static final Class<?> clazz_Assumptions$Assumption;
    private static final Class<?> clazz_HotSpotCompiledCode$Comment;
    private static final Class<?> clazz_DataPatch;
    private static final Class<?> clazz_StackSlot;
    private static final Class<?> clazz_JVMCICompiler;
    private static final Class<?> clazz_CodeCacheProvider;
    private static final Class<?> clazz_InstalledCode;
    private static final Class<?> clazz_CompiledCode;

    private static final MethodHandle method_JVMCI_getRuntime;
    private static final MethodHandle method_JVMCIRuntime_getHostJVMCIBackend;
    private static final MethodHandle method_JVMCIBackend_getMetaAccessProvider;
    private static final MethodHandle method_JavaKind_fromJavaClass;
    private static final MethodHandle method_MetaAccessProvider_getArrayBaseOffset;
    private static final MethodHandle method_MetaAccessProvider_lookupJavaMethod;
    private static final MethodHandle method_ResolvedJavaMethod_array_set;
    private static final MethodHandle method_HotspotResolvedJavaMethod_allocateCompileId;
    private static final MethodHandle method_HotspotResolvedJavaMethod_setNotInlinableOrCompilable;
    private static final MethodHandle method_JVMCIBackend_getCodeCache;
    private static final MethodHandle method_CodeCacheProvider_setDefaultCode;

    private static final MethodHandle constructor_HotSpotCompiledNmethod;
    private static final MethodHandle constructor_Site_array;
    private static final MethodHandle constructor_Assumptions$Assumption_array;
    private static final MethodHandle constructor_ResolvedJavaMethod_array;
    private static final MethodHandle constructor_HotSpotCompiledCode$Comment_array;
    private static final MethodHandle constructor_DataPatch_array;

    private static final MethodHandle method_JVMCICompiler_INVOCATION_ENTRY_BCI_get;

    static {
        try {
            clazz_JVMCI = Class.forName("jdk.vm.ci.runtime.JVMCI");
            clazz_JVMCIRuntime = Class.forName("jdk.vm.ci.runtime.JVMCIRuntime");
            clazz_JVMCIBackend = Class.forName("jdk.vm.ci.runtime.JVMCIBackend");
            clazz_MetaAccessProvider = Class.forName("jdk.vm.ci.meta.MetaAccessProvider");
            clazz_JavaKind = Class.forName("jdk.vm.ci.meta.JavaKind");
            clazz_ResolvedJavaMethod = Class.forName("jdk.vm.ci.meta.ResolvedJavaMethod");
            clazz_HotspotResolvedJavaMethod = Class.forName("jdk.vm.ci.hotspot.HotSpotResolvedJavaMethod");
            clazz_HotSpotCompiledNmethod = Class.forName("jdk.vm.ci.hotspot.HotSpotCompiledNmethod");
            clazz_Site = Class.forName("jdk.vm.ci.code.site.Site");
            clazz_Assumptions$Assumption = Class.forName("jdk.vm.ci.meta.Assumptions$Assumption");
            clazz_HotSpotCompiledCode$Comment = Class.forName("jdk.vm.ci.hotspot.HotSpotCompiledCode$Comment");
            clazz_DataPatch = Class.forName("jdk.vm.ci.code.site.DataPatch");
            clazz_StackSlot = Class.forName("jdk.vm.ci.code.StackSlot");
            clazz_JVMCICompiler = Class.forName("jdk.vm.ci.runtime.JVMCICompiler");
            clazz_CodeCacheProvider = Class.forName("jdk.vm.ci.code.CodeCacheProvider");
            clazz_InstalledCode = Class.forName("jdk.vm.ci.code.InstalledCode");
            clazz_CompiledCode = Class.forName("jdk.vm.ci.code.CompiledCode");

            method_JVMCI_getRuntime = MethodHandles.lookup().findStatic(clazz_JVMCI, "getRuntime", MethodType.methodType(clazz_JVMCIRuntime));
            method_JVMCIRuntime_getHostJVMCIBackend = MethodHandles.lookup().findVirtual(clazz_JVMCIRuntime, "getHostJVMCIBackend", MethodType.methodType(clazz_JVMCIBackend));
            method_JVMCIBackend_getMetaAccessProvider = MethodHandles.lookup().findVirtual(clazz_JVMCIBackend, "getMetaAccess", MethodType.methodType(clazz_MetaAccessProvider));
            method_JavaKind_fromJavaClass = MethodHandles.lookup().findStatic(clazz_JavaKind, "fromJavaClass", MethodType.methodType(clazz_JavaKind, Class.class));
            method_MetaAccessProvider_getArrayBaseOffset = MethodHandles.lookup().findVirtual(clazz_MetaAccessProvider, "getArrayBaseOffset", MethodType.methodType(int.class, clazz_JavaKind));
            method_MetaAccessProvider_lookupJavaMethod = MethodHandles.lookup().findVirtual(clazz_MetaAccessProvider, "lookupJavaMethod", MethodType.methodType(clazz_ResolvedJavaMethod, Executable.class));
            method_ResolvedJavaMethod_array_set = MethodHandles.arrayElementSetter(clazz_ResolvedJavaMethod.arrayType());
            method_HotspotResolvedJavaMethod_allocateCompileId = MethodHandles.lookup().findVirtual(clazz_HotspotResolvedJavaMethod, "allocateCompileId", MethodType.methodType(int.class, int.class));
            method_HotspotResolvedJavaMethod_setNotInlinableOrCompilable = MethodHandles.lookup().findVirtual(clazz_HotspotResolvedJavaMethod, "setNotInlinableOrCompilable", MethodType.methodType(void.class));
            method_JVMCIBackend_getCodeCache = MethodHandles.lookup().findVirtual(clazz_JVMCIBackend, "getCodeCache", MethodType.methodType(clazz_CodeCacheProvider));
            method_CodeCacheProvider_setDefaultCode = MethodHandles.lookup().findVirtual(clazz_CodeCacheProvider, "setDefaultCode", MethodType.methodType(clazz_InstalledCode, clazz_ResolvedJavaMethod, clazz_CompiledCode));

            constructor_HotSpotCompiledNmethod = MethodHandles.lookup().findConstructor(clazz_HotSpotCompiledNmethod, MethodType.methodType(void.class, String.class, byte[].class, int.class, clazz_Site.arrayType(), clazz_Assumptions$Assumption.arrayType(), clazz_ResolvedJavaMethod.arrayType(), clazz_HotSpotCompiledCode$Comment.arrayType(), byte[].class, int.class, clazz_DataPatch.arrayType(), boolean.class, int.class, clazz_StackSlot, clazz_HotspotResolvedJavaMethod, int.class, int.class, long.class, boolean.class));
            constructor_Site_array = MethodHandles.arrayConstructor(clazz_Site.arrayType());
            constructor_Assumptions$Assumption_array = MethodHandles.arrayConstructor(clazz_Assumptions$Assumption.arrayType());
            constructor_ResolvedJavaMethod_array = MethodHandles.arrayConstructor(clazz_ResolvedJavaMethod.arrayType());
            constructor_HotSpotCompiledCode$Comment_array = MethodHandles.arrayConstructor(clazz_HotSpotCompiledCode$Comment.arrayType());
            constructor_DataPatch_array = MethodHandles.arrayConstructor(clazz_DataPatch.arrayType());

            method_JVMCICompiler_INVOCATION_ENTRY_BCI_get = MethodHandles.lookup().findStaticGetter(clazz_JVMCICompiler, "INVOCATION_ENTRY_BCI", int.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object jvmci$getRuntime() {
        try {
            return method_JVMCI_getRuntime.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object jvmciRuntime$getHostJVMCIBackend() {
        try {
            return method_JVMCIRuntime_getHostJVMCIBackend.invoke(jvmci$getRuntime());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object jvmciBackend$getMetaAccessProvider() {
        try {
            return method_JVMCIBackend_getMetaAccessProvider.invoke(jvmciRuntime$getHostJVMCIBackend());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object javaKind$fromJavaClass(Class<?> clazz) {
        try {
            return method_JavaKind_fromJavaClass.invoke(clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int metaAccessProvider$getArrayBaseOffset(Object javaKind) {
        try {
            return (int) method_MetaAccessProvider_getArrayBaseOffset.invoke(jvmciBackend$getMetaAccessProvider(), javaKind);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object metaAccessProvider$lookupJavaMethod(Executable executable) {
        try {
            return method_MetaAccessProvider_lookupJavaMethod.invoke(jvmciBackend$getMetaAccessProvider(), executable);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void resolvedJavaMethod$array$set(Object array, int index, Object value) {
        try {
            method_ResolvedJavaMethod_array_set.invoke(array, index, value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hotspotResolvedJavaMethod$allocateCompileId(Object method, int id) {
        try {
            return (int) method_HotspotResolvedJavaMethod_allocateCompileId.invoke(method, id);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void hotspotResolvedJavaMethod$setNotInlinableOrCompilable(Object method) {
        try {
            method_HotspotResolvedJavaMethod_setNotInlinableOrCompilable.invoke(method);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object jvmciBackend$getCodeCache() {
        try {
            return method_JVMCIBackend_getCodeCache.invoke(jvmciRuntime$getHostJVMCIBackend());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object codeCacheProvider$setDefaultCode(Object method, Object code) {
        try {
            return method_CodeCacheProvider_setDefaultCode.invoke(jvmciBackend$getCodeCache(), method, code);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object hotSpotCompiledNmethod$constructor(String name, byte[] code, int length, Object sites, Object assumptions, Object methods, Object comments, byte[] dataSection, int dataSectionAlignment, Object dataSectionPatches, boolean isImmutablePIC, int totalFrameSize, Object deoptRescueSlot, Object method, int entryBCI, int id, long compileState, boolean hasUnsafeAccess) {
        try {
            return constructor_HotSpotCompiledNmethod.invoke(name, code, length, sites, assumptions, methods, comments, dataSection, dataSectionAlignment, dataSectionPatches, isImmutablePIC, totalFrameSize, deoptRescueSlot, method, entryBCI, id, compileState, hasUnsafeAccess);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object siteArray$constructor(int length) {
        try {
            return constructor_Site_array.invoke(length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object assumptions$AssumptionArray$constructor(int length) {
        try {
            return constructor_Assumptions$Assumption_array.invoke(length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object resolvedJavaMethodArray$constructor(int length) {
        try {
            return constructor_ResolvedJavaMethod_array.invoke(length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object hotSpotCompiledCode$CommentArray$constructor(int length) {
        try {
            return constructor_HotSpotCompiledCode$Comment_array.invoke(length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object dataPatchArray$constructor(int length) {
        try {
            return constructor_DataPatch_array.invoke(length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int jvmciCompiler$INVOCATION_ENTRY_BCI$get() {
        try {
            return (int) method_JVMCICompiler_INVOCATION_ENTRY_BCI_get.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
