package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class U32Reader implements ScaleReader<Long> {
    @Override
    public Long read(@NonNull InputStream stream) throws IOException {
        val bytes = StreamUtils.readBytes(4, stream);
        return Byte.toUnsignedLong(bytes[0])
                + (Byte.toUnsignedLong(bytes[1]) << 8)
                + (Byte.toUnsignedLong(bytes[2]) << 16)
                + (Byte.toUnsignedLong(bytes[3]) << 24);
    }
}
