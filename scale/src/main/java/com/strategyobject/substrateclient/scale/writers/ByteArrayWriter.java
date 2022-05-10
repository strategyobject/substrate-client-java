package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayWriter implements ScaleWriter<byte[]> {
    @Override
    public void write(byte @NonNull [] value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        CompactIntegerWriter.writeInternal(value.length, stream);
        Streamer.writeBytes(value, stream);
    }
}
