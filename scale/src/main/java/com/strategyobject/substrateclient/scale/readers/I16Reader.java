package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class I16Reader implements ScaleReader<Short> {
    @Override
    public Short read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val bytes = Streamer.readBytes(2, stream);
        val buf = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        buf.put(bytes);
        buf.flip();
        return buf.getShort();
    }
}
