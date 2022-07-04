package com.strategyobject.substrateclient.rpc.api.impl;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.rpc.api.BlockHash;
import com.strategyobject.substrateclient.rpc.api.Hash;
import lombok.NonNull;

public class Hash256 extends FixedBytes<Size.Of32> implements Hash, BlockHash {
    private Hash256(byte[] data) {
        super(data, Size.of32);
    }

    public static Hash256 fromBytes(byte @NonNull [] data) {
        return new Hash256(data);
    }
}
