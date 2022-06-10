package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import lombok.NonNull;

public class SignatureData extends FixedBytes<Size.Of64> {
    protected SignatureData(byte[] data) {
        super(data, Size.of64);
    }

    public static SignatureData fromBytes(byte @NonNull [] data) {
        return new SignatureData(data);
    }
}
