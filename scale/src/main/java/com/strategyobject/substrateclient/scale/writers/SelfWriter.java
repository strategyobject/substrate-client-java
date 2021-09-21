package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registry.ScaleWriterNotFoundException;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public class SelfWriter implements ScaleWriter<ScaleSelfWritable<?>> {
    @Override
    public void write(@NonNull ScaleSelfWritable<?> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        try {
            value.write(stream);
        } catch (ScaleWriterNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
