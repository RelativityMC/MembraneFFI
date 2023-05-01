package com.ishland.membraneffi.api.annotations;

import com.ishland.membraneffi.api.Architecture;
import com.ishland.membraneffi.api.OperatingSystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Install machine code directly to the method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InstallMachineCode {

    /**
     * Entries of the machine code
     */
    Entry[] value();

    public @interface Entry {

        /**
         * Targets of the machine code
         */
        OsArchPair[] targets();

        /**
         * Machine code for the method implementation
         */
        String code();
    }

}
