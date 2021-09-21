package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class U32Reader implements ScaleReader<Long> {
    @Override
    public Long read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val bytes = Streamer.readBytes(4, stream);
        return Byte.toUnsignedLong(bytes[0])
                + (Byte.toUnsignedLong(bytes[1]) << 8)
                + (Byte.toUnsignedLong(bytes[2]) << 16)
                + (Byte.toUnsignedLong(bytes[3]) << 24);
    }
}
