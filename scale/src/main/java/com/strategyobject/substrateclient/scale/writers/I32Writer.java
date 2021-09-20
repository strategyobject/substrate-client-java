package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class I32Writer implements ScaleWriter<Integer> {
    @Override
    public void write(Integer value, OutputStream stream) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(value);
        buf.flip();
        stream.write(buf.array());
    }
}
