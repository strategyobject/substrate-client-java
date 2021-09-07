package com.strategyobject.substrateclient.scale;

import java.io.OutputStream;

public interface ScaleWriter<T> {
    void write(T value, OutputStream stream);
}
