package com.strategyobject.substrateclient.crypto;

import com.strategyobject.substrateclient.common.types.FixedBytes;
import com.strategyobject.substrateclient.common.types.Size;
import lombok.NonNull;

public class Seed extends FixedBytes<Size.Of32> {
    protected Seed(byte[] data) {
        super(data, Size.of32);
    }

    public static Seed fromBytes(byte @NonNull [] data) {
        return new Seed(data);
    }
}
