package com.strategyobject.substrateclient.scale;

import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;

public interface ScaleEncoded<T extends ScaleEncoded<T>> {
    @SuppressWarnings("unchecked")
    default void write(@NonNull OutputStream stream) throws ScaleWriterNotFoundException, IOException {
        ScaleWriterRegistry
                .getInstance()
                .resolve((Class<T>) this.getClass())
                .write((T) this, stream);
    }
}
