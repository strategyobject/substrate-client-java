package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ListWriter<T> implements ScaleWriter<List<T>> {
    @Override
    @SuppressWarnings("unchecked")
    public void write(@NonNull List<T> value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers != null && writers.length == 1);
        Preconditions.checkNotNull(writers[0]);

        val nestedWriter = (ScaleWriter<T>) writers[0];

        CompactIntegerWriter.writeInternal(value.size(), stream);
        for (T item : value) {
            nestedWriter.write(item, stream);
        }
    }
}
