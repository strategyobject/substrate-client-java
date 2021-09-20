package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringReader implements ScaleReader<String> {
    @Override
    public String read(@NonNull InputStream stream) throws IOException {
        val len = CompactIntegerReader.readInternal(stream);
        val bytes = StreamUtils.readBytes(len, stream);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
