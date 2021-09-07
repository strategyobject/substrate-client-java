package com.strategyobject.substrateclient.scale;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public interface ScaleReader<T> {
    T read(@NonNull InputStream stream) throws IOException;
}
