package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/**
 * A custom error in a module.
 */
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
    @ScaleGeneric(
        template = "Vec<U8>",
        types = {
            @Scale(ScaleType.Vec.class),
            @Scale(ScaleType.U8.class)
        }
    )
    private List<Integer> error;
}
