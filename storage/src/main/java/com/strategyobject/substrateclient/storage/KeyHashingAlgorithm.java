package com.strategyobject.substrateclient.storage;

import lombok.NonNull;

/**
 * An algorithm that's used to generate storage map's key.
 * As a general rule it should be a transparent algorithm since
 * non-transparent hashers are deprecated in Substrate.
 */
public interface KeyHashingAlgorithm {
    /**
     * @param encodedKey binary data that represents the encoded key.
     * @return the hash of the key produced by the algorithm.
     */
    byte[] getHash(byte @NonNull [] encodedKey);

    /**
     * @return purely the size of the certain hashing algorithm.
     * Supposed that algorithm is transparent and the method returns
     * exceptionally the hash size without considering any concatenated parts.
     */
    int hashSize();
}
