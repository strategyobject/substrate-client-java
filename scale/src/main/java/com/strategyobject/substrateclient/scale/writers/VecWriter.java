package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class VecWriter implements ScaleWriter<List<?>> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void write(@NonNull List<?> value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers.length == 1);
        Preconditions.checkNotNull(writers[0]);

        val nestedWriter = (ScaleWriter) writers[0];

        CompactIntegerWriter.writeInternal(value.size(), stream);
        for (val item : value) {
            nestedWriter.write(item, stream);
        }
    }
}
