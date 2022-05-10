package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;
import lombok.var;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LongArrayWriter implements ScaleWriter<long[]> {
    @Override
    public void write(long @NonNull [] value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        CompactIntegerWriter.writeInternal(value.length, stream);
        val buf = ByteBuffer.allocate(value.length * 8).order(ByteOrder.LITTLE_ENDIAN);
        for (var e : value) {
            buf.putLong(e);
        }

        buf.flip();
        Streamer.writeBytes(buf.array(), stream);
    }
}
