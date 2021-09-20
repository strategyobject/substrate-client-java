package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;

public class U16Writer implements ScaleWriter<Integer> {
    @Override
    public void write(Integer value, OutputStream stream) throws IOException {
        Preconditions.checkArgument(
                value >= 0 && value <= 0xff_ff,
                "Only values in range 0..65535 are supported");

        stream.write(value & 0xff);
        stream.write((value >> 8) & 0xff);
    }
}
