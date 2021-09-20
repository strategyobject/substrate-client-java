package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;

public class BoolWriter implements ScaleWriter<Boolean> {
    @Override
    public void write(Boolean value, OutputStream stream) throws IOException {
        stream.write(value ? 1 : 0);
    }
}
