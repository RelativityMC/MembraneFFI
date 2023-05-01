package com.ishland.membraneffi.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Executable;

public class JVMCIAccess {

    public static final Class<?> clazz_JVMCI;
    public static final Class<?> clazz_JVMCIRuntime;
    public static final Class<?> clazz_JVMCIBackend;
    public static final Class<?> clazz_MetaAccessProvider;
    public static final Class<?> clazz_JavaKind;
    public static final Class<?> clazz_ResolvedJavaMethod;
    public static final Class<?> clazz_HotspotResolvedJavaMethod;
    public static final Class<?> clazz_HotSpotCompiledNmethod;
    public static final Class<?> clazz_Site;
    public static final Class<?> clazz_Assumptions$Assumption;
    public static final Class<?> clazz_HotSpotCompiledCode$Comment;
    public static final Class<?> clazz_DataPatch;
    public static final Class<?> clazz_StackSlot;
    public static final Class<?> clazz_JVMCICompiler;
    public static final Class<?> clazz_CodeCacheProvider;
    public static final Class<?> clazz_InstalledCode;
    public static final Class<?> clazz_CompiledCode;
    public static final Class<?> clazz_RegisterConfig;
    public static final Class<?> clazz_CallingConvention;
    public static final Class<?> clazz_CallingConvention$Type;
    public static final Class<?> clazz_JavaType;
    public static final Class<?> clazz_ValueKindFactory;
    public static final Class<?> clazz_PlatformKind;
    public static final Class<?> clazz_TargetDescription;
    public static final Class<?> clazz_Architecture;
    public static final Class<?> clazz_HotSpotCallingConventionType;
    public static final Class<?> clazz_ResolvedJavaType;
    public static final Class<?> clazz_AllocatableValue;
    public static final Class<?> clazz_RegisterValue;
    public static final Class<?> clazz_Register;

    private static final MethodHandle method_JVMCI_getRuntime;
    private static final MethodHandle method_JVMCIRuntime_getHostJVMCIBackend;
    private static final MethodHandle method_JVMCIBackend_getMetaAccessProvider;
    private static final MethodHandle method_JVMCIBackend_getTarget;
    private static final MethodHandle method_JavaKind_fromJavaClass;
    private static final MethodHandle method_MetaAccessProvider_getArrayBaseOffset;
    private static final MethodHandle method_MetaAccessProvider_lookupJavaMethod;
    private static final MethodHandle method_MetaAccessProvider_lookupJavaType;
    private static final MethodHandle method_ResolvedJavaMethod_array_set;
    private static final MethodHandle method_HotspotResolvedJavaMethod_allocateCompileId;
    private static final MethodHandle method_HotspotResolvedJavaMethod_setNotInlinableOrCompilable;
    private static final MethodHandle method_JVMCIBackend_getCodeCache;
    private static final MethodHandle method_CodeCacheProvider_setDefaultCode;
    private static final MethodHandle method_CodeCacheProvider_getRegisterConfig;
    private static final MethodHandle method_RegisterConfig_getCallingConvention;
    private static final MethodHandle method_Architecture_getPlatformKind;
    private static final MethodHandle method_CallingConvention_getArgument;
    private static final MethodHandle method_CallingConvention_getStackSize;
    private static final MethodHandle method_RegisterValue_getRegister;
    private static final MethodHandle method_StackSlot_getOffset;

    private static final MethodHandle constructor_HotSpotCompiledNmethod;
    private static final MethodHandle constructor_Site_array;
    private static final MethodHandle constructor_Assumptions$Assumption_array;
    private static final MethodHandle constructor_ResolvedJavaMethod_array;
    private static final MethodHandle constructor_HotSpotCompiledCode$Comment_array;
    private static final MethodHandle constructor_DataPatch_array;
    private static final MethodHandle constructor_JavaType_array;

    private static final MethodHandle getter_JVMCICompiler_INVOCATION_ENTRY_BCI_get;
    private static final MethodHandle getter_TargetDescription_arch_get;

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
            clazz_RegisterConfig = Class.forName("jdk.vm.ci.code.RegisterConfig");
            clazz_CallingConvention = Class.forName("jdk.vm.ci.code.CallingConvention");
            clazz_CallingConvention$Type = Class.forName("jdk.vm.ci.code.CallingConvention$Type");
            clazz_JavaType = Class.forName("jdk.vm.ci.meta.JavaType");
            clazz_ValueKindFactory = Class.forName("jdk.vm.ci.code.ValueKindFactory");
            clazz_PlatformKind = Class.forName("jdk.vm.ci.meta.PlatformKind");
            clazz_TargetDescription = Class.forName("jdk.vm.ci.code.TargetDescription");
            clazz_Architecture = Class.forName("jdk.vm.ci.code.Architecture");
            clazz_HotSpotCallingConventionType = Class.forName("jdk.vm.ci.hotspot.HotSpotCallingConventionType");
            clazz_ResolvedJavaType = Class.forName("jdk.vm.ci.meta.ResolvedJavaType");
            clazz_AllocatableValue = Class.forName("jdk.vm.ci.meta.AllocatableValue");
            clazz_RegisterValue = Class.forName("jdk.vm.ci.code.RegisterValue");
            clazz_Register = Class.forName("jdk.vm.ci.code.Register");

            method_JVMCI_getRuntime = MethodHandles.lookup().findStatic(clazz_JVMCI, "getRuntime", MethodType.methodType(clazz_JVMCIRuntime));
            method_JVMCIRuntime_getHostJVMCIBackend = MethodHandles.lookup().findVirtual(clazz_JVMCIRuntime, "getHostJVMCIBackend", MethodType.methodType(clazz_JVMCIBackend));
            method_JVMCIBackend_getMetaAccessProvider = MethodHandles.lookup().findVirtual(clazz_JVMCIBackend, "getMetaAccess", MethodType.methodType(clazz_MetaAccessProvider));
            method_JVMCIBackend_getTarget = MethodHandles.lookup().findVirtual(clazz_JVMCIBackend, "getTarget", MethodType.methodType(clazz_TargetDescription));
            method_JavaKind_fromJavaClass = MethodHandles.lookup().findStatic(clazz_JavaKind, "fromJavaClass", MethodType.methodType(clazz_JavaKind, Class.class));
            method_MetaAccessProvider_getArrayBaseOffset = MethodHandles.lookup().findVirtual(clazz_MetaAccessProvider, "getArrayBaseOffset", MethodType.methodType(int.class, clazz_JavaKind));
            method_MetaAccessProvider_lookupJavaMethod = MethodHandles.lookup().findVirtual(clazz_MetaAccessProvider, "lookupJavaMethod", MethodType.methodType(clazz_ResolvedJavaMethod, Executable.class));
            method_MetaAccessProvider_lookupJavaType = MethodHandles.lookup().findVirtual(clazz_MetaAccessProvider, "lookupJavaType", MethodType.methodType(clazz_ResolvedJavaType, Class.class));
            method_ResolvedJavaMethod_array_set = MethodHandles.arrayElementSetter(ReflectionUtils.arrayType(clazz_ResolvedJavaMethod));
            method_HotspotResolvedJavaMethod_allocateCompileId = MethodHandles.lookup().findVirtual(clazz_HotspotResolvedJavaMethod, "allocateCompileId", MethodType.methodType(int.class, int.class));
            method_HotspotResolvedJavaMethod_setNotInlinableOrCompilable = MethodHandles.lookup().findVirtual(clazz_HotspotResolvedJavaMethod, "setNotInlinableOrCompilable", MethodType.methodType(void.class));
            method_JVMCIBackend_getCodeCache = MethodHandles.lookup().findVirtual(clazz_JVMCIBackend, "getCodeCache", MethodType.methodType(clazz_CodeCacheProvider));
            method_CodeCacheProvider_setDefaultCode = MethodHandles.lookup().findVirtual(clazz_CodeCacheProvider, "setDefaultCode", MethodType.methodType(clazz_InstalledCode, clazz_ResolvedJavaMethod, clazz_CompiledCode));
            method_CodeCacheProvider_getRegisterConfig = MethodHandles.lookup().findVirtual(clazz_CodeCacheProvider, "getRegisterConfig", MethodType.methodType(clazz_RegisterConfig));
            method_RegisterConfig_getCallingConvention = MethodHandles.lookup().findVirtual(clazz_RegisterConfig, "getCallingConvention", MethodType.methodType(clazz_CallingConvention, clazz_CallingConvention$Type, clazz_JavaType, ReflectionUtils.arrayType(clazz_JavaType), clazz_ValueKindFactory));
            method_Architecture_getPlatformKind = MethodHandles.lookup().findVirtual(clazz_Architecture, "getPlatformKind", MethodType.methodType(clazz_PlatformKind, clazz_JavaKind));
            method_CallingConvention_getArgument = MethodHandles.lookup().findVirtual(clazz_CallingConvention, "getArgument", MethodType.methodType(clazz_AllocatableValue, int.class));
            method_CallingConvention_getStackSize = MethodHandles.lookup().findVirtual(clazz_CallingConvention, "getStackSize", MethodType.methodType(int.class));
            method_RegisterValue_getRegister = MethodHandles.lookup().findVirtual(clazz_RegisterValue, "getRegister", MethodType.methodType(clazz_Register));
            method_StackSlot_getOffset = MethodHandles.lookup().findVirtual(clazz_StackSlot, "getOffset", MethodType.methodType(int.class, int.class));

            constructor_HotSpotCompiledNmethod = MethodHandles.lookup().findConstructor(clazz_HotSpotCompiledNmethod, MethodType.methodType(void.class, String.class, byte[].class, int.class, ReflectionUtils.arrayType(clazz_Site), ReflectionUtils.arrayType(clazz_Assumptions$Assumption), ReflectionUtils.arrayType(clazz_ResolvedJavaMethod), ReflectionUtils.arrayType(clazz_HotSpotCompiledCode$Comment), byte[].class, int.class, ReflectionUtils.arrayType(clazz_DataPatch), boolean.class, int.class, clazz_StackSlot, clazz_HotspotResolvedJavaMethod, int.class, int.class, long.class, boolean.class));
            constructor_Site_array = MethodHandles.arrayConstructor(ReflectionUtils.arrayType(clazz_Site));
            constructor_Assumptions$Assumption_array = MethodHandles.arrayConstructor(ReflectionUtils.arrayType(clazz_Assumptions$Assumption));
            constructor_ResolvedJavaMethod_array = MethodHandles.arrayConstructor(ReflectionUtils.arrayType(clazz_ResolvedJavaMethod));
            constructor_HotSpotCompiledCode$Comment_array = MethodHandles.arrayConstructor(ReflectionUtils.arrayType(clazz_HotSpotCompiledCode$Comment));
            constructor_DataPatch_array = MethodHandles.arrayConstructor(ReflectionUtils.arrayType(clazz_DataPatch));
            constructor_JavaType_array = MethodHandles.arrayConstructor(ReflectionUtils.arrayType(clazz_JavaType));

            getter_JVMCICompiler_INVOCATION_ENTRY_BCI_get = MethodHandles.lookup().findStaticGetter(clazz_JVMCICompiler, "INVOCATION_ENTRY_BCI", int.class);
            getter_TargetDescription_arch_get = MethodHandles.lookup().findGetter(clazz_TargetDescription, "arch", clazz_Architecture);
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

    public static Object jvmciBackend$getTarget() {
        try {
            return method_JVMCIBackend_getTarget.invoke(jvmciRuntime$getHostJVMCIBackend());
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

    public static Object metaAccessProvider$lookupJavaType(Class<?> clazz) {
        try {
            return method_MetaAccessProvider_lookupJavaType.invoke(jvmciBackend$getMetaAccessProvider(), clazz);
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

    public static Object codeCacheProvider$getRegisterConfig() {
        try {
            return method_CodeCacheProvider_getRegisterConfig.invoke(jvmciBackend$getCodeCache());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object registerConfig$getCallingConvention(Object registerConfig, Object type, Object returnType, Object parameterTypes, Object valueKindFactory) {
        try {
            return method_RegisterConfig_getCallingConvention.invoke(registerConfig, type, returnType, parameterTypes, valueKindFactory);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object architecture$getPlatformKind(Object architecture, Object javaKind) {
        try {
            return method_Architecture_getPlatformKind.invoke(architecture, javaKind);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callingConvention$getArgument(Object callingConvention, int index) {
        try {
            return method_CallingConvention_getArgument.invoke(callingConvention, index);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int callingConvention$getStackSize(Object callingConvention) {
        try {
            return (int) method_CallingConvention_getStackSize.invoke(callingConvention);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object registerValue$getRegister(Object registerValue) {
        try {
            return method_RegisterValue_getRegister.invoke(registerValue);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int stackSlot$getOffset(Object stackSlot, int totalFrameSize) {
        try {
            return (int) method_StackSlot_getOffset.invoke(stackSlot, totalFrameSize);
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

    public static <T> T[] javaTypeArray$constructor(int length) {
        try {
            return (T[]) constructor_JavaType_array.invoke(length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int jvmciCompiler$INVOCATION_ENTRY_BCI$get() {
        try {
            return (int) getter_JVMCICompiler_INVOCATION_ENTRY_BCI_get.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static Object targetDescription$arch$get(Object target) {
        try {
            return getter_TargetDescription_arch_get.invoke(target);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
