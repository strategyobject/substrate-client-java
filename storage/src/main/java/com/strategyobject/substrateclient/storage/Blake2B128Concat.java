package com.strategyobject.substrateclient.storage;

import lombok.NonNull;
import lombok.val;
import org.bouncycastle.crypto.digests.Blake2bDigest;

import java.nio.ByteBuffer;

/**
 * Blake2 128 Concat hash algorithm.
 * This algorithm is cryptographic and transparent.
 * It's used to generate storage map's key.
 */
public class Blake2B128Concat implements KeyHashingAlgorithm {
    private static final int BLAKE_128_HASH_SIZE = 16;
    private static volatile Blake2B128Concat instance;

    private static byte[] blake2_128(byte[] value) {
        val digest = new Blake2bDigest(128);
        digest.update(value, 0, value.length);

        val result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }

    /**
     * @return the instance of the algorithm.
     */
    public static Blake2B128Concat getInstance() {
        if (instance == null) {
            synchronized (Blake2B128Concat.class) {
                if (instance == null) {
                    instance = new Blake2B128Concat();
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
        return ByteBuffer.allocate(BLAKE_128_HASH_SIZE + encodedKey.length)
                .put(blake2_128(encodedKey))
                .put(encodedKey)
                .array();
    }

    /**
     * @return size of the algorithm.
     */
    @Override
    public int hashSize() {
        return BLAKE_128_HASH_SIZE;
    }
}
