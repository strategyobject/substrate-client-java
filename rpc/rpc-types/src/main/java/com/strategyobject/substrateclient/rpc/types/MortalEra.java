package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotations.Scale;
import com.strategyobject.substrateclient.scale.annotations.ScaleWriter;
import lombok.Getter;

@Getter
@ScaleWriter
public class MortalEra implements Era {
    @Scale(ScaleType.CompactInteger.class)
    private final int encoded;

    MortalEra(int encoded) {
        this.encoded = encoded;
    }
}
