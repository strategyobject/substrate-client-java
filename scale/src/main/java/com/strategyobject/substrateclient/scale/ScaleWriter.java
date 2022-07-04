package com.strategyobject.substrateclient.scale;

import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public interface ScaleWriter<T> {
    void write(T value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException;

    default ScaleWriter<T> inject(ScaleWriter<?>... dependencies) {
        return (value, stream, writers) -> ScaleWriter.this.write(value, stream, dependencies);
    }
}
