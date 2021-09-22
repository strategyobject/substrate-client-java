package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class U32Writer implements ScaleWriter<Long> {
    @Override
    public void write(@NonNull Long value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);
        Preconditions.checkArgument(
                value >= 0 && value <= 0xff_ff_ff_ffL,
                "Only values in range 0..4294967295 are supported");

        stream.write((int) (value & 0xff));
        stream.write((int) ((value >> 8) & 0xff));
        stream.write((int) ((value >> 16) & 0xff));
        stream.write((int) ((value >> 24) & 0xff));
    }
}
