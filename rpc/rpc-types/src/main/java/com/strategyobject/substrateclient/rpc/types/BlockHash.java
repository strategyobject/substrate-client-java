package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.RpcEncoded;
import com.strategyobject.substrateclient.scale.ScaleEncoded;
import com.strategyobject.substrateclient.types.FixedBytes;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;

public class BlockHash
        extends FixedBytes<Size.Of32>
        implements ScaleEncoded<BlockHash>, RpcEncoded<BlockHash> {
    protected BlockHash(byte[] data) {
        super(data, Size.of32);
    }

    public static BlockHash fromBytes(byte @NonNull [] data) {
        return new BlockHash(data);
    }
}
