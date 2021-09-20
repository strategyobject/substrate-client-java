package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class I8Reader implements ScaleReader<Byte> {
    @Override
    public Byte read(@NonNull InputStream stream) throws IOException {
        val bytes = StreamUtils.readBytes(1, stream);
        return bytes[0];
    }
}
