package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class BoolWriter implements ScaleWriter<Boolean> {
    @Override
    public void write(@NonNull Boolean value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        stream.write(value ? 1 : 0);
    }
}
