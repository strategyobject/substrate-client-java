package com.strategyobject.substrateclient.pallet.storage;

import lombok.NonNull;

/**
 * Identity hash algorithm.
 * This algorithm doesn't produce a hash and returns a key as is.
 * Supposed the algorithm will be used only for keys of fixed length.
 * This algorithm is non-cryptographic and transparent.
 * It's used to generate storage map's key.
 */
public class Identity implements KeyHashingAlgorithm {
    private static volatile Identity instance;

    /**
     * @return the instance of the algorithm.
     */
    public static Identity getInstance() {
        if (instance == null) {
            synchronized (Identity.class) {
                if (instance == null) {
                    instance = new Identity();
                }
            }
        }
        return instance;
    }

    /**
     * @param encodedKey binary data that represents the encoded key.
     * @return the given key as is. The length of the key must have fixed size.
     */
    @Override
    public byte[] getHash(byte @NonNull [] encodedKey) {
        return encodedKey;
    }

    /**
     * @return size of the algorithm. In that case it's zero.
     */
    @Override
    public int hashSize() {
        return 0;
    }
}
