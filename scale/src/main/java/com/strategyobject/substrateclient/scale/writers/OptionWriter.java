package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class OptionWriter implements ScaleWriter<Optional<?>> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void write(@NonNull Optional<?> value, @NonNull OutputStream stream, @NonNull ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers.length == 1);
        Preconditions.checkNotNull(writers[0]);

        val nestedWriter = (ScaleWriter) writers[0];
        if (value.isPresent()) {
            stream.write(1);
            nestedWriter.write(value.get(), stream);
        } else {
            stream.write(0);
        }
    }
}
