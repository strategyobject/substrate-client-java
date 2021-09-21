package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringReader implements ScaleReader<String> {
    @Override
    public String read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val len = CompactIntegerReader.readInternal(stream);
        val bytes = Streamer.readBytes(len, stream);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
