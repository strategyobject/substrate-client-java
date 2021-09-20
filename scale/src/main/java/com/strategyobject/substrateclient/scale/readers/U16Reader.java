package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class U16Reader implements ScaleReader<Integer> {
    @Override
    public Integer read(@NonNull InputStream stream) throws IOException {
        val bytes = StreamUtils.readBytes(2, stream);
        return Byte.toUnsignedInt(bytes[0]) + (Byte.toUnsignedInt(bytes[1]) << 8);
    }
}
