package com.strategyobject.substrateclient.pallet.annotations;

/**
 * Represents a kind of key's hash algorithm
 */
public enum StorageHasher {
    /**
     * Blake2 128 Concat hash algorithm.
     */
    Blake2B128Concat,
    /**
     * TwoX 64 Concat hash algorithm.
     */
    TwoX64Concat,
    /**
     * Identity hash algorithm.
     */
    Identity
}
