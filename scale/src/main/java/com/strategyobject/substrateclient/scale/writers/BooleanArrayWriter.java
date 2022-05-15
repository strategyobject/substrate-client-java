package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.var;

import java.io.IOException;
import java.io.OutputStream;

public class BooleanArrayWriter implements ScaleWriter<boolean[]> {
    @Override
    public void write(boolean @NonNull [] value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        CompactIntegerWriter.writeInternal(value.length, stream);
        for (var e : value) {
            stream.write(e ? 1 : 0);
        }
    }
}
