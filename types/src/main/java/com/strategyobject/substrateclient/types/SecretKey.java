package com.strategyobject.substrateclient.types;

import lombok.NonNull;

public class SecretKey extends FixedBytes<Size.Of64> {
    protected SecretKey(byte[] data) {
        super(data, Size.of64);
    }

    public static SecretKey fromBytes(byte @NonNull [] data) {
        return new SecretKey(data);
    }
}
