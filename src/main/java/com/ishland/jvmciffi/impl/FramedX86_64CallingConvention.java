package com.ishland.jvmciffi.impl;

import com.github.icedland.iced.x86.asm.AsmMemoryOperand;
import com.github.icedland.iced.x86.asm.AsmRegister64;
import com.github.icedland.iced.x86.asm.AsmRegisterSegment;
import com.github.icedland.iced.x86.asm.AsmRegisterXMM;
import com.github.icedland.iced.x86.asm.AsmRegisters;
import com.github.icedland.iced.x86.asm.AsmRegistersSegment;
import com.github.icedland.iced.x86.asm.CodeAssembler;
import com.github.icedland.iced.x86.asm.CodeAssemblerResult;
import com.ishland.jvmciffi.api.CallingConventionAdapter;
import com.ishland.jvmciffi.util.JVMCIAccess;
import com.ishland.jvmciffi.util.JVMCIUtils;
import com.ishland.jvmciffi.util.MathHelper;
import jdk.vm.ci.code.CallingConvention;
import jdk.vm.ci.code.Register;
import jdk.vm.ci.code.RegisterConfig;
import jdk.vm.ci.code.RegisterValue;
import jdk.vm.ci.code.StackSlot;
import jdk.vm.ci.code.ValueKindFactory;
import jdk.vm.ci.hotspot.HotSpotCallingConventionType;
import jdk.vm.ci.meta.AllocatableValue;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.JavaType;
import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.meta.PlatformKind;
import jdk.vm.ci.meta.ValueKind;
import jdk.vm.ci.runtime.JVMCI;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FramedX86_64CallingConvention implements CallingConventionAdapter {

    @Override
    public void emit(ByteArrayOutputStream out, Argument[] arguments, Class<?> returnType, long address) {
        CodeAssembler as = new CodeAssembler(64);

        final RegisterConfig registerConfig = JVMCI.getRuntime().getHostJVMCIBackend().getCodeCache().getRegisterConfig();
        final MetaAccessProvider metaAccess = JVMCI.getRuntime().getHostJVMCIBackend().getMetaAccess();
        final CallingConvention javaCallingConvention = registerConfig.getCallingConvention(HotSpotCallingConventionType.JavaCall,
                metaAccess.lookupJavaType(returnType),
                Arrays.stream(arguments).map(argument -> metaAccess.lookupJavaType(argument.type())).toArray(JavaType[]::new),
                ValueKindImpl.factory
        );
        final CallingConvention nativeCallingConvention = registerConfig.getCallingConvention(HotSpotCallingConventionType.NativeCall,
                metaAccess.lookupJavaType(returnType),
                Arrays.stream(arguments).map(argument -> metaAccess.lookupJavaType(argument.type())).toArray(JavaType[]::new),
                ValueKindImpl.factory
        );

        as.push(AsmRegisters.rbp); // for alignment
        as.push(AsmRegisters.rbp);
        as.mov(AsmRegisters.rbp, AsmRegisters.rsp);

        int currentFrameSize = 0;

        // spill incoming registers arguments to stack
        LinkedHashMap<String, Integer> spilledRegisters = new LinkedHashMap<>(); // offset to rbp

        for (int i = 0; i < arguments.length; i ++) {
            AllocatableValue javaArgument = javaCallingConvention.getArgument(i);
            if (javaArgument instanceof RegisterValue registerValue) {
                spilledRegisters.put(registerValue.getRegister().toString(), - spilledRegisters.size() * 8);
            }
        }

        as.sub(AsmRegisters.rsp, spilledRegisters.size() * 8);
        currentFrameSize += spilledRegisters.size() * 8;

        for (var entry : spilledRegisters.entrySet()) {
            final Object icedRegister = toIcedRegister(entry.getKey());
            if (icedRegister instanceof AsmRegister64 reg64) {
                as.mov(AsmRegisters.qword_ptr(AsmRegisters.rbp, entry.getValue()), reg64);
            } else if (icedRegister instanceof AsmRegisterXMM regxmm) {
                as.movq(AsmRegisters.qword_ptr(AsmRegisters.rbp, entry.getValue()), regxmm);
            }
        }

        if (spilledRegisters.size() % 2 == 1) { // call pushes stack by 8 bytes
            as.sub(AsmRegisters.rsp, 8);
            currentFrameSize += 8;
        }

        int actualConventionStackSize = MathHelper.roundUpToMultiple(nativeCallingConvention.getStackSize() + 8, 16) - 8;
        if (actualConventionStackSize > 0) {
            as.sub(AsmRegisters.rsp, actualConventionStackSize);
            currentFrameSize += actualConventionStackSize;
        }

        for (int i = 0; i < arguments.length; i++) {
            AllocatableValue javaArgument = javaCallingConvention.getArgument(i);
            AllocatableValue nativeArgument = nativeCallingConvention.getArgument(i);
            Argument argument = arguments[i];

            AsmMemoryOperand source;
            if (javaArgument instanceof RegisterValue registerValue) {
                source = AsmRegisters.qword_ptr(AsmRegisters.rbp, spilledRegisters.get(registerValue.getRegister().toString()));
            } else if (javaArgument instanceof StackSlot slot) {
                source = AsmRegisters.qword_ptr(AsmRegisters.rbp, slot.getOffset(0) + 0x18);
            } else {
                throw new UnsupportedOperationException("Unsupported argument type: " + javaArgument);
            }
            if (nativeArgument instanceof RegisterValue register) {
                final Object icedRegister = toIcedRegister(register.getRegister());
                if (icedRegister instanceof AsmRegister64 reg64) {
                    as.mov(reg64, source);
                    if (!argument.type().isPrimitive()) {
                        as.add(reg64, JVMCIUtils.arrayBaseOffset(argument.type()));
                    }
                } else if (icedRegister instanceof AsmRegisterXMM regxmm) {
                    as.movq(regxmm, source);
                    if (!argument.type().isPrimitive()) {
                        throw new AssertionError();
                    }
                }
            } else if (nativeArgument instanceof StackSlot slot) {
                as.mov(AsmRegisters.rax, source);
                if (!argument.type().isPrimitive()) {
                    as.add(AsmRegisters.rax, JVMCIUtils.arrayBaseOffset(argument.type()));
                }
                as.mov(AsmRegisters.qword_ptr(AsmRegisters.rsp, slot.getOffset(0)), AsmRegisters.rax);
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
        if (result instanceof String error) {
            throw new RuntimeException(error);
        } else if (result instanceof CodeAssemblerResult assemblerResult) {
            return;
        }

        throw new AssertionError(String.format("Unexpected result type: %s", result.getClass().getName()));
    }

    private static Object toIcedRegister(Register register) {
        try {
            return AsmRegisters.class.getField(register.toString()).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object toIcedRegister(String name) {
        try {
            return AsmRegisters.class.getField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ValueKindImpl extends ValueKind<ValueKindImpl> {

        private static final ValueKindFactory<ValueKindImpl> factory = new ValueKindFactory<ValueKindImpl>() {
            @Override
            public ValueKindImpl getValueKind(JavaKind javaKind) {
                return new ValueKindImpl(JVMCI.getRuntime().getHostJVMCIBackend().getTarget().arch.getPlatformKind(javaKind));
            }
        };

        public ValueKindImpl(PlatformKind platformKind) {
            super(platformKind);
        }

        @Override
        public ValueKindImpl changeType(PlatformKind newPlatformKind) {
            return new ValueKindImpl(newPlatformKind);
        }
    }

}
