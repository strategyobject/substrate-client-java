package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;

public class I8Writer implements ScaleWriter<Byte> {
    @Override
    public void write(Byte value, OutputStream stream) throws IOException {
        stream.write(value);
    }
}
