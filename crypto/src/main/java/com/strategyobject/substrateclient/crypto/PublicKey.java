package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import lombok.NonNull;

public class PublicKey extends FixedBytes<Size.Of32> {
    protected PublicKey(byte[] data) {
        super(data, Size.of32);
    }

    public static PublicKey fromBytes(byte @NonNull [] data) {
        return new PublicKey(data);
    }
}
