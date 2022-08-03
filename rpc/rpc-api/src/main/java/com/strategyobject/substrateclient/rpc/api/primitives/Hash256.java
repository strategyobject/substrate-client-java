package com.strategyobject.substrateclient.rpc.api.primitives;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import lombok.NonNull;

public class Hash256 extends FixedBytes<Size.Of32> implements Hash, BlockHash, CallHash {
    private Hash256(byte[] data) {
        super(data, Size.of32);
    }

    public static Hash256 fromBytes(byte @NonNull [] data) {
        return new Hash256(data);
    }
}
