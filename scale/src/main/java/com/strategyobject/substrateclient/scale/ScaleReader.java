package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.inject.Dependant;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface ScaleReader<T> extends Dependant<ScaleReader<T>, ScaleReader<?>> {
    T read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException;

    @Override
    default ScaleReader<T> inject(ScaleReader<?>... dependencies) {
        return (stream, readers) -> ScaleReader.this.read(stream, dependencies);
    }
}
