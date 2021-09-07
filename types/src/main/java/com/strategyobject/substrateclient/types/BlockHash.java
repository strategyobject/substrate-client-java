package com.strategyobject.substrateclient.types;

public class BlockHash extends FixedBytes {
    private static final int HASH_SIZE_IN_BYTES = 32;

    public BlockHash(byte[] data) {
        super(data, HASH_SIZE_IN_BYTES);
    }
}
