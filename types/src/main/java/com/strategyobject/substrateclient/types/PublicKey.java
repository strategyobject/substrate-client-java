package com.strategyobject.substrateclient.types;


public class PublicKey extends FixedBytes {
    private static final int KEY_LENGTH = 32;

    public PublicKey(byte[] data) {
        super(data, KEY_LENGTH);
    }
}
