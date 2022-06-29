package com.strategyobject.substrateclient.common.types;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public abstract class FixedBytes<S extends Size> implements Fixed<S>, Bytes {
    private final byte @NonNull [] bytes;

    @Override
    public byte @NonNull [] getBytes() {
        return bytes;
    }

    protected FixedBytes(byte @NonNull [] bytes, S size) {
        Preconditions.checkNotNull(bytes);
        assertSize(size, bytes.length);

        this.bytes = bytes;
    }

    public int getSize() {
        return bytes.length;
    }
}
