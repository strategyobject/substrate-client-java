package com.strategyobject.substrateclient.common.types;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class FixedBytes<S extends Size> implements Fixed<S> {
    @Getter
    private final byte[] data;

    protected FixedBytes(byte[] data, S size) {
        Preconditions.checkNotNull(data);
        assertSize(size, data.length);

        this.data = data;
    }

    public int getSize() {
        return data.length;
    }
}
