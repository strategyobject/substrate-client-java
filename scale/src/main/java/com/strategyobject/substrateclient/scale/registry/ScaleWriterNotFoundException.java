package com.strategyobject.substrateclient.scale.registry;

public class ScaleWriterNotFoundException extends Exception {
    public <T> ScaleWriterNotFoundException(Class<T> clazz) {
        super(String.format("ScaleWriter for %s wasn't found in the registry.", clazz));
    }
}
