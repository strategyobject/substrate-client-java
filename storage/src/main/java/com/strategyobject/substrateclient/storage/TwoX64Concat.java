package com.strategyobject.substrateclient.storage;

import lombok.NonNull;
import lombok.val;
import net.openhft.hashing.LongHashFunction;

import java.nio.ByteBuffer;

/**
 * TwoX 64 Concat hash algorithm.
 * This algorithm is non-cryptographic and transparent.
 * It's used to generate storage map's key.
 */
public class TwoX64Concat implements KeyHashingAlgorithm {
    private static final int XX_HASH_SIZE = 8;
    private static volatile TwoX64Concat instance;

    private static byte[] xxHash64(byte[] value) {
        val hash = LongHashFunction.xx(0).hashBytes(value);

        return new byte[]{
                (byte) (hash),
                (byte) (hash >> 8),
                (byte) (hash >> 16),
                (byte) (hash >> 24),
                (byte) (hash >> 32),
                (byte) (hash >> 40),
                (byte) (hash >> 48),
                (byte) (hash >> 56)
        };
    }

    private static byte[] xxHash64Concat(byte[] value) {
        val buf = ByteBuffer.allocate(XX_HASH_SIZE + value.length);

        buf.put(xxHash64(value));
        buf.put(value);

        return buf.array();
    }

    /**
     * @return the instance of the algorithm.
     */
    public static TwoX64Concat getInstance() {
        if (instance == null) {
            synchronized (TwoX64Concat.class) {
                if (instance == null) {
                    instance = new TwoX64Concat();
                }
            }
        }
        return instance;
    }

    /**
     * @param encodedKey binary data that represents the encoded key.
     * @return hash for given key.
     */
    @Override
    public byte[] getHash(byte @NonNull [] encodedKey) {
        return xxHash64Concat(encodedKey);
    }

    /**
     * @return size of the algorithm.
     */
    @Override
    public int hashSize() {
        return XX_HASH_SIZE;
    }

}
