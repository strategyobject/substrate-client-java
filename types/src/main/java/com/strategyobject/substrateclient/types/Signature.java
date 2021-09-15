package com.strategyobject.substrateclient.types;

import lombok.NonNull;

public class Signature extends FixedBytes {
    private static final int SIZE = 64;

    protected Signature(byte[] data) {
        super(data, SIZE);
    }

    public static Signature fromBytes(byte @NonNull [] data) {
        return new Signature(data);
    }
}
