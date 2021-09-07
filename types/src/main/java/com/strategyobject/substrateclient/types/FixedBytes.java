package com.strategyobject.substrateclient.types;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
abstract class FixedBytes {
    @Getter
    private final byte[] data;

    protected FixedBytes(byte[] data, int size) {
        Preconditions.checkNotNull(data);
        Preconditions.checkArgument(
                data.length == size,
                "The data size must be %s; received %s.", size, data.length);

        this.data = data;
    }

    public int getSize() {
        return data.length;
    }
}
