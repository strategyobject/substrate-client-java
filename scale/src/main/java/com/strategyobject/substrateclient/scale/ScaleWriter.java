package com.strategyobject.substrateclient.scale;

import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public interface ScaleWriter<T> {
    void write(@NonNull T value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException;
}