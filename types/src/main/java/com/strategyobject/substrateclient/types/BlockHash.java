package com.strategyobject.substrateclient.types;

import lombok.NonNull;

public class BlockHash extends FixedBytes {
    private static final int HASH_SIZE_IN_BYTES = 32;

    protected BlockHash(byte[] data) {
        super(data, HASH_SIZE_IN_BYTES);
    }

    public static BlockHash fromBytes(byte @NonNull [] data) {
        return new BlockHash(data);
    }
}
