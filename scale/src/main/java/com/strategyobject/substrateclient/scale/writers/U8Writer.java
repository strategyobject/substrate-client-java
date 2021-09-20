package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;

public class U8Writer implements ScaleWriter<Integer> {
    @Override
    public void write(Integer value, OutputStream stream) throws IOException {
        Preconditions.checkArgument(
                value >= 0 && value <= 0xff,
                "Only values in range 0..255 are supported");

        stream.write(value);
    }
}
