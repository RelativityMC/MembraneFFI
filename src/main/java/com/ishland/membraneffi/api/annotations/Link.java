package com.ishland.membraneffi.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Link to a native method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Link {

    /**
     * Alternative name of the native function.
     * If not specified, Java method name is assumed.
     */
    String[] value() default {};

}
