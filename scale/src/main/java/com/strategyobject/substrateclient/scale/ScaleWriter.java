package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.inject.Dependant;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public interface ScaleWriter<T> extends Dependant<ScaleWriter<T>, ScaleWriter<?>> {
    void write(T value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException;

    @Override
    default ScaleWriter<T> inject(ScaleWriter<?>... dependencies) {
        return (value, stream, writers) -> ScaleWriter.this.write(value, stream, dependencies);
    }
}
