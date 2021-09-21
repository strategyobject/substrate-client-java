package com.strategyobject.substrateclient.types;


import lombok.NonNull;

public class PublicKey extends FixedBytes<Size.Of32> {
    protected PublicKey(byte[] data) {
        super(data, Size.of32);
    }

    public static PublicKey fromBytes(byte @NonNull [] data) {
        return new PublicKey(data);
    }
}
