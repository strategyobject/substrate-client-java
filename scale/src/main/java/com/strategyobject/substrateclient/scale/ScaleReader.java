package com.strategyobject.substrateclient.scale;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface ScaleReader<T> {
    T read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException;

    default ScaleReader<T> inject(ScaleReader<?>... dependencies) {
        return (stream, readers) -> ScaleReader.this.read(stream, dependencies);
    }
}
