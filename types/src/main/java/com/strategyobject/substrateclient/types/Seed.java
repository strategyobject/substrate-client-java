package com.strategyobject.substrateclient.types;


import lombok.NonNull;

public class Seed extends FixedBytes {
    private static final int SIZE = 32;

    protected Seed(byte[] data) {
        super(data, SIZE);
    }

    public static Seed fromBytes(byte @NonNull [] data) {
        return new Seed(data);
    }
}
