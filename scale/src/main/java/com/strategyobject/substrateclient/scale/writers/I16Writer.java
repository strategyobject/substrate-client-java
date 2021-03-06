package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class I16Writer implements ScaleWriter<Short> {
    @Override
    public void write(@NonNull Short value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        stream.write(value & 0xff);
        stream.write((value >> 8) & 0xff);
    }
}
