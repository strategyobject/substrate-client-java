package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class U8Writer implements ScaleWriter<Integer> {
    @Override
    public void write(@NonNull Integer value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);
        Preconditions.checkArgument(
                value >= 0 && value <= 0xff,
                "Only values in range 0..255 are supported");

        stream.write(value);
    }
}
