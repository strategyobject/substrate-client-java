package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class DispatchingWriter<T> implements ScaleWriter<T> {
    private final @NonNull ScaleWriterRegistry registry;

    @SuppressWarnings("unchecked")
    @Override
    public void write(@NonNull T value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        val writer = (ScaleWriter<T>) registry.resolve(value.getClass());
        writer.write(value, stream, writers);
    }
}
