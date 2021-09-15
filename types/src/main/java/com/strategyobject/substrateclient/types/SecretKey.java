package com.strategyobject.substrateclient.types;

import lombok.NonNull;

public class SecretKey extends FixedBytes {
    private static final int KEY_LENGTH = 64;

    protected SecretKey(byte[] data) {
        super(data, KEY_LENGTH);
    }

    public static SecretKey fromBytes(byte @NonNull [] data) {
        return new SecretKey(data);
    }
}
