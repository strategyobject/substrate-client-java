package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.common.types.Size;
import lombok.val;
import net.openhft.hashing.LongHashFunction;
import org.bouncycastle.crypto.digests.Blake2bDigest;

public class Hasher {
    public static byte[] blake2(Size size, byte[] value) {
        val digest = new Blake2bDigest(size.getValue());
        digest.update(value, 0, value.length);

        val result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return result;
    }

    public static long xx(long seed, byte[] value) {
        return LongHashFunction.xx(seed).hashBytes(value);
    }

    public static byte[] xx64(long seed, byte[] value) {
        val hash = xx(seed, value);

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

    private Hasher() {
    }
}
