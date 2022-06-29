package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.OutputStream;

public class VoidWriter implements ScaleWriter<Void> {
    @Override
    public void write(Void value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
        Preconditions.checkArgument(writers == null || writers.length == 0);
    }
}
