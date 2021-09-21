package com.strategyobject.substrateclient.types;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public abstract class FixedBytes<S extends Size> {
    @Getter
    private final byte[] data;

    protected FixedBytes(byte[] data, S size) {
        Preconditions.checkNotNull(data);
        Preconditions.checkArgument(
                data.length == size.getValue(),
                "The data size must be %s; received %s.", size.getValue(), data.length);

        this.data = data;
    }

    public int getSize() {
        return data.length;
    }
}
