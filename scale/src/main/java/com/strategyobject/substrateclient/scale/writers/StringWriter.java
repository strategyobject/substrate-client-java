package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StringWriter implements ScaleWriter<String> {
    @Override
    public void write(String value, OutputStream stream) throws IOException {
        CompactIntegerWriter.writeInternal(value.length(), stream);
        StreamUtils.writeBytes(value.getBytes(StandardCharsets.UTF_8), stream);
    }
}
