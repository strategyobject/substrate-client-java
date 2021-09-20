package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@RequiredArgsConstructor
public class OptionWriter<T> implements ScaleWriter<Optional<T>> {
    private final ScaleWriter<T> nestedWriter;

    @Override
    public void write(Optional<T> value, OutputStream stream) throws IOException {
        if (value.isPresent()) {
            stream.write(1);
            nestedWriter.write(value.get(), stream);
        } else {
            stream.write(0);
        }
    }
}
