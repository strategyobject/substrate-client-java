package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class I64Writer implements ScaleWriter<Long> {
    @Override
    public void write(Long value, OutputStream stream) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(value);
        buf.flip();
        stream.write(buf.array());
    }
}
