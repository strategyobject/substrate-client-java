package com.strategyobject.substrateclient.scale.registry;

public class ScaleReaderNotFoundException extends Exception {
    public <T> ScaleReaderNotFoundException(Class<T> clazz) {
        super(String.format("ScaleReader for %s wasn't found in the registry.", clazz));
    }
}
