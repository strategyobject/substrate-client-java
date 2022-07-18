package com.strategyobject.substrateclient.common.types;

public interface Into {
    @SuppressWarnings("unchecked")
    default <T> T into(Class<T> clazz) {
        return (T) this;
    }
}
