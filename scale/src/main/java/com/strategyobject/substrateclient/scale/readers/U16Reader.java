package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class U16Reader implements ScaleReader<Integer> {
    @Override
    public Integer read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val bytes = StreamUtils.readBytes(2, stream);
        return Byte.toUnsignedInt(bytes[0]) + (Byte.toUnsignedInt(bytes[1]) << 8);
    }
}
