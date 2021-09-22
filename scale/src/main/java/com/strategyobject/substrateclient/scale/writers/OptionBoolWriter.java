package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class OptionBoolWriter implements ScaleWriter<Optional<Boolean>> {
    @Override
    public void write(@NonNull Optional<Boolean> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        if (!value.isPresent()) {
            stream.write(0);
        } else if (value.get()) {
            stream.write(1);
        } else {
            stream.write(2);
        }
    }
}
