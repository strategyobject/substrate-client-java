package com.strategyobject.substrateclient.types;

import lombok.NonNull;

public class SignatureData extends FixedBytes<Size.Of64> {
    protected SignatureData(byte[] data) {
        super(data, Size.of64);
    }

    public static SignatureData fromBytes(byte @NonNull [] data) {
        return new SignatureData(data);
    }
}
