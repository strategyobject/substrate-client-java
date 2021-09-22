package com.strategyobject.substrateclient.scale;

public class ScaleWriterNotFoundException extends Exception {
    public <T> ScaleWriterNotFoundException(Class<T> clazz) {
        super(String.format("ScaleCodec for %s wasn't registered.", clazz));
    }
}
