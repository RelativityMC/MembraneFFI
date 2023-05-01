package com.ishland.membraneffi.api.annotations;

import com.ishland.membraneffi.api.Architecture;
import com.ishland.membraneffi.api.OperatingSystem;

/**
 * A pair of {@link OperatingSystem} and {@link Architecture}
 */
public @interface OsArchPair {

    /**
     * The operating system
     */
    OperatingSystem os();

    /**
     * The architecture
     */
    Architecture arch();

}
