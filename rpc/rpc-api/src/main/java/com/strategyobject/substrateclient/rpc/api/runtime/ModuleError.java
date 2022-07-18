package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

/**
 * A custom error in a module.
 */
@ScaleReader
@Getter
@Setter
public class ModuleError {
    /**
     * Module index, matching the metadata module index.
     */
    @Scale(ScaleType.U8.class)
    private Integer index;

    /**
     * Module specific error value.
     */
    @Scale(ScaleType.U8.class)
    private Integer error;
}
