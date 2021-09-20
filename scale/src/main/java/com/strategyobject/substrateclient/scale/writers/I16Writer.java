package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;

public class I16Writer implements ScaleWriter<Short> {
    @Override
    public void write(Short value, OutputStream stream) throws IOException {
        stream.write(value & 0xff);
        stream.write((value >> 8) & 0xff);
    }
}
