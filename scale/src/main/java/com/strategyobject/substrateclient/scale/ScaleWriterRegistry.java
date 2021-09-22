package com.strategyobject.substrateclient.scale;

import lombok.NonNull;

public class ScaleWriterRegistry {
    public <T> ScaleWriter<T> resolve(@NonNull Class<T> clazz) throws ScaleWriterNotFoundException {
        throw new ScaleWriterNotFoundException(clazz); // TODO It will be done later.
    };

    public static ScaleWriterRegistry getInstance() {
        throw new UnsupportedOperationException(); // TODO It will be done later.
    }
}
