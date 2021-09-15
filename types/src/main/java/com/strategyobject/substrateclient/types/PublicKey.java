package com.strategyobject.substrateclient.types;


import lombok.NonNull;

public class PublicKey extends FixedBytes {
    private static final int KEY_LENGTH = 32;

    protected PublicKey(byte[] data) {
        super(data, KEY_LENGTH);
    }

    public static PublicKey fromBytes(byte @NonNull [] data) {
        return new PublicKey(data);
    }
}
