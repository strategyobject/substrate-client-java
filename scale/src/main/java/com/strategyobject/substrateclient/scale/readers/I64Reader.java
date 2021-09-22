package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class I64Reader implements ScaleReader<Long> {
    @Override
    public Long read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val bytes = StreamUtils.readBytes(8, stream);
        ByteBuffer buf = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buf.put(bytes);
        buf.flip();
        return buf.getLong();
    }
}
