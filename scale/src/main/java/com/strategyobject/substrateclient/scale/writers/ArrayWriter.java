package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

public class ArrayWriter implements ScaleWriter<Object[]> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void write(Object @NonNull [] value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers != null && writers.length == 1);
        Preconditions.checkNotNull(writers[0]);

        val nestedWriter = (ScaleWriter) writers[0];

        CompactIntegerWriter.writeInternal(value.length, stream);
        for (val item : value) {
            nestedWriter.write(item, stream);
        }
    }
}
