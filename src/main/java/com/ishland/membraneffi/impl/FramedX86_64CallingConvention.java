package com.ishland.membraneffi.impl;

import com.github.icedland.iced.x86.asm.AsmMemoryOperand;
import com.github.icedland.iced.x86.asm.AsmRegister64;
import com.github.icedland.iced.x86.asm.AsmRegisterXMM;
import com.github.icedland.iced.x86.asm.AsmRegisters;
import com.github.icedland.iced.x86.asm.CodeAssembler;
import com.github.icedland.iced.x86.asm.CodeAssemblerResult;
import com.ishland.membraneffi.api.CallingConventionAdapter;
import com.ishland.membraneffi.util.JVMCIAccess;
import com.ishland.membraneffi.util.JVMCIUtils;
import com.ishland.membraneffi.util.JVMCIValueKindGenerator;
import com.ishland.membraneffi.util.MathHelper;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class FramedX86_64CallingConvention implements CallingConventionAdapter {

    @Override
    public void emit(ByteArrayOutputStream out, Argument[] arguments, Class<?> returnType, long address) {
        CodeAssembler as = new CodeAssembler(64);

        final Object registerConfig = JVMCIAccess.codeCacheProvider$getRegisterConfig();
        final Object metaAccess = JVMCIAccess.jvmciBackend$getMetaAccessProvider();
        @SuppressWarnings("unchecked") final Object javaCallingConvention = JVMCIAccess.registerConfig$getCallingConvention(registerConfig,
                Enum.valueOf((Class<? extends Enum>) JVMCIAccess.clazz_HotSpotCallingConventionType, "JavaCall"),
                JVMCIAccess.metaAccessProvider$lookupJavaType(returnType),
                Arrays.stream(arguments).map(argument -> JVMCIAccess.metaAccessProvider$lookupJavaType(argument.type())).toArray(JVMCIAccess::javaTypeArray$constructor),
                JVMCIValueKindGenerator.generatedFactory
        );
        @SuppressWarnings("unchecked") final Object nativeCallingConvention = JVMCIAccess.registerConfig$getCallingConvention(registerConfig,
                Enum.valueOf((Class<? extends Enum>) JVMCIAccess.clazz_HotSpotCallingConventionType, "NativeCall"),
                JVMCIAccess.metaAccessProvider$lookupJavaType(returnType),
                Arrays.stream(arguments).map(argument -> JVMCIAccess.metaAccessProvider$lookupJavaType(argument.type())).toArray(JVMCIAccess::javaTypeArray$constructor),
                JVMCIValueKindGenerator.generatedFactory
        );

        as.push(AsmRegisters.rbp); // for alignment
        as.push(AsmRegisters.rbp);
        as.mov(AsmRegisters.rbp, AsmRegisters.rsp);

        int currentFrameSize = 0;

        // spill incoming registers arguments to stack
        LinkedHashMap<String, Integer> spilledRegisters = new LinkedHashMap<>(); // offset to rbp

        for (int i = 0; i < arguments.length; i ++) {
            Object javaArgument = JVMCIAccess.callingConvention$getArgument(javaCallingConvention, i);
            if (JVMCIAccess.clazz_RegisterValue.isInstance(javaArgument)) {
                final Object registerValue = javaArgument;
                spilledRegisters.put(JVMCIAccess.registerValue$getRegister(registerValue).toString(), - spilledRegisters.size() * 8);
            }
        }

        as.sub(AsmRegisters.rsp, spilledRegisters.size() * 8);
        currentFrameSize += spilledRegisters.size() * 8;

        for (java.util.Map.Entry<String, Integer> entry : spilledRegisters.entrySet()) {
            final Object icedRegister = toIcedRegister(entry.getKey());
            if (icedRegister instanceof AsmRegister64) {
                AsmRegister64 reg64 = (AsmRegister64) icedRegister;
                as.mov(AsmRegisters.qword_ptr(AsmRegisters.rbp, entry.getValue()), reg64);
            } else if (icedRegister instanceof AsmRegisterXMM) {
                AsmRegisterXMM regxmm = (AsmRegisterXMM) icedRegister;
                as.movq(AsmRegisters.qword_ptr(AsmRegisters.rbp, entry.getValue()), regxmm);
            }
        }

        if (spilledRegisters.size() % 2 == 1) { // call pushes stack by 8 bytes
            as.sub(AsmRegisters.rsp, 8);
            currentFrameSize += 8;
        }

        int actualConventionStackSize = MathHelper.roundUpToMultiple(JVMCIAccess.callingConvention$getStackSize(nativeCallingConvention) + 8, 16) - 8;
        if (actualConventionStackSize > 0) {
            as.sub(AsmRegisters.rsp, actualConventionStackSize);
            currentFrameSize += actualConventionStackSize;
        }

        for (int i = 0; i < arguments.length; i++) {
            Object javaArgument = JVMCIAccess.callingConvention$getArgument(javaCallingConvention, i);
            Object nativeArgument = JVMCIAccess.callingConvention$getArgument(nativeCallingConvention, i);
            Argument argument = arguments[i];

            AsmMemoryOperand source;
            if (JVMCIAccess.clazz_RegisterValue.isInstance(javaArgument)) {
                Object registerValue = javaArgument;
                source = AsmRegisters.qword_ptr(AsmRegisters.rbp, spilledRegisters.get(JVMCIAccess.registerValue$getRegister(registerValue).toString()));
            } else if (JVMCIAccess.clazz_StackSlot.isInstance(javaArgument)) {
                Object slot = javaArgument;
                source = AsmRegisters.qword_ptr(AsmRegisters.rbp, JVMCIAccess.stackSlot$getOffset(slot, 0) + 0x18);
            } else {
                throw new UnsupportedOperationException("Unsupported argument type: " + javaArgument);
            }
            if (JVMCIAccess.clazz_RegisterValue.isInstance(nativeArgument)) {
                Object register = nativeArgument;
                final Object icedRegister = toIcedRegister(JVMCIAccess.registerValue$getRegister(register).toString());
                if (icedRegister instanceof AsmRegister64) {
                    AsmRegister64 reg64 = (AsmRegister64) icedRegister;
                    as.mov(reg64, source);
                    if (!argument.type().isPrimitive()) {
                        as.add(reg64, JVMCIUtils.arrayBaseOffset(argument.type()));
                    }
                } else if (icedRegister instanceof AsmRegisterXMM) {
                    AsmRegisterXMM regxmm = (AsmRegisterXMM) icedRegister;
                    as.movq(regxmm, source);
                    if (!argument.type().isPrimitive()) {
                        throw new AssertionError();
                    }
                }
            } else if (JVMCIAccess.clazz_StackSlot.isInstance(nativeArgument)) {
                Object slot = nativeArgument;
                as.mov(AsmRegisters.rax, source);
                if (!argument.type().isPrimitive()) {
                    as.add(AsmRegisters.rax, JVMCIUtils.arrayBaseOffset(argument.type()));
                }
                as.mov(AsmRegisters.qword_ptr(AsmRegisters.rsp, JVMCIAccess.stackSlot$getOffset(slot, 0)), AsmRegisters.rax);
            } else {
                throw new UnsupportedOperationException("Unsupported argument type: " + nativeArgument);
            }
        }

        // invoke call
        as.mov(AsmRegisters.rax, address);
        as.call(AsmRegisters.rax);

        as.add(AsmRegisters.rsp, currentFrameSize);
        as.pop(AsmRegisters.rbp);
        as.pop(AsmRegisters.rbp); // for alignment

        as.ret();

        final Object result = as.assemble(out::write, 0);
        if (result instanceof String) {
            String error = (String) result;
            throw new RuntimeException(error);
        } else if (result instanceof CodeAssemblerResult) {
            CodeAssemblerResult assemblerResult = (CodeAssemblerResult) result;
            return;
        }

        throw new AssertionError(String.format("Unexpected result type: %s", result.getClass().getName()));
    }

    private static Object toIcedRegister(String name) {
        try {
            return AsmRegisters.class.getField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}
