package com.strategyobject.substrateclient.pallet.annotation;

/**
 * Represents a kind of key's hash algorithm
 */
public enum StorageHasher {
    /**
     * Blake2 128 Concat hash algorithm.
     */
    BLAKE2_128_CONCAT,
    /**
     * TwoX 64 Concat hash algorithm.
     */
    TWOX_64_CONCAT,
    /**
     * Identity hash algorithm.
     */
    IDENTITY
}
