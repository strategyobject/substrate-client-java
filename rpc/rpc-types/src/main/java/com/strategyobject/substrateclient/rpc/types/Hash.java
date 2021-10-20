package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.types.FixedBytes;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;

public class Hash extends FixedBytes<Size.Of32> implements ScaleSelfWritable<Hash> {
    private Hash(byte[] data) {
        super(data, Size.of32);
    }

    public static Hash fromBytes(byte @NonNull [] data) {
        return new Hash(data);
    }
}
