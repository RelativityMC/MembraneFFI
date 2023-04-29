package com.ishland.membraneffi.impl;

import com.github.icedland.iced.x86.asm.AsmRegisters;
import com.github.icedland.iced.x86.asm.CodeAssembler;
import com.github.icedland.iced.x86.asm.CodeAssemblerResult;
import com.ishland.membraneffi.api.CallingConventionAdapter;
import com.ishland.membraneffi.util.JVMCIUtils;

import java.io.ByteArrayOutputStream;

public class LinuxX86_64CallingConvention implements CallingConventionAdapter {

    // x64 calling convention (Linux, macOS):
    //     Java: rsi, rdx, rcx,  r8,  r9, rdi, stack
    //   Native: rdi, rsi, rdx, rcx,  r8,  r9, stack

    @Override
    public void emit(ByteArrayOutputStream out, Argument[] arguments, Class<?> returnType, long address) {
        CodeAssembler as = new CodeAssembler(64);

        if (arguments.length >= 6) {
            // 6th Java argument clashes with the 1st native arg
            as.mov(AsmRegisters.rax, AsmRegisters.rdi);
        }

        int generalPurposeArgIndex = 0;
        int xmmArgIndex = 0;
        int stackArgIndex = 0;
        for (Argument arg : arguments) {
            if (arg.type().isPrimitive()) {
                if (arg.type() == float.class || arg.type() == double.class) { // floating points are passed in xmm registers
                    if (xmmArgIndex++ >= 8) { // xmm8+ are passed on stack
                        stackArgIndex ++;
                    }
                } else {
                    if (generalPurposeArgIndex < 6) { // adapt calling convention
//                        emit(buf, (type == long.class ? MOVE_LONG_ARG_REG : MOVE_INT_ARG_REG)[generalPurposeArgIndex++]);
                        switch (generalPurposeArgIndex++) {
                            case 0 -> as.mov(AsmRegisters.rdi, AsmRegisters.rsi);
                            case 1 -> as.mov(AsmRegisters.rsi, AsmRegisters.rdx);
                            case 2 -> as.mov(AsmRegisters.rdx, AsmRegisters.rcx);
                            case 3 -> as.mov(AsmRegisters.rcx, AsmRegisters.r8);
                            case 4 -> as.mov(AsmRegisters.r8, AsmRegisters.r9);
                            case 5 -> as.mov(AsmRegisters.r9, AsmRegisters.rax);
                            default -> throw new AssertionError();
                        }
                    } else { // the rest is passed on stack
                        stackArgIndex++;
                    }
                }
            } else {
                final int baseOffset = JVMCIUtils.baseOffset(arg);
                if (generalPurposeArgIndex < 6) { // adapt calling convention + offset
                    switch (generalPurposeArgIndex++) {
                        case 0 -> as.lea(AsmRegisters.rdi, AsmRegisters.rsi.add(baseOffset));
                        case 1 -> as.lea(AsmRegisters.rsi, AsmRegisters.rdx.add(baseOffset));
                        case 2 -> as.lea(AsmRegisters.rdx, AsmRegisters.rcx.add(baseOffset));
                        case 3 -> as.lea(AsmRegisters.rcx, AsmRegisters.r8.add(baseOffset));
                        case 4 -> as.lea(AsmRegisters.r8, AsmRegisters.r9.add(baseOffset));
                        case 5 -> as.lea(AsmRegisters.r9, AsmRegisters.rax.add(baseOffset));
                        default -> throw new AssertionError();
                    }
                } else { // the rest is passed on stack
                    int adjustedStackOffset = (stackArgIndex + 1) * 8;
                    // add qword ptr [rsp-adjustedStackOffset], baseOffset
                    as.add(AsmRegisters.qword_ptr(AsmRegisters.rsp, adjustedStackOffset), baseOffset);
                    stackArgIndex++;
                }
            }
        }

        as.mov(AsmRegisters.rax, address);
        as.jmp(AsmRegisters.rax);

        final Object result = as.assemble(out::write, 0);
        if (result instanceof String error) {
            throw new RuntimeException(error);
        } else if (result instanceof CodeAssemblerResult assemblerResult) {
            return;
        }

        throw new AssertionError(String.format("Unexpected result type: %s", result.getClass().getName()));
    }

}
