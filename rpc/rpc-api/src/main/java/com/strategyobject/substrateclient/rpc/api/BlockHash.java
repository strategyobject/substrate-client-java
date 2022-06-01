package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import lombok.NonNull;

public class BlockHash
        extends FixedBytes<Size.Of32>
        implements ScaleSelfWritable<BlockHash> {
    protected BlockHash(byte[] data) {
        super(data, Size.of32);
    }

    public static BlockHash fromBytes(byte @NonNull [] data) {
        return new BlockHash(data);
    }
}
