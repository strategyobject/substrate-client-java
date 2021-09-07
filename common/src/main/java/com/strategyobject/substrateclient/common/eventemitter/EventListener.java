package com.strategyobject.substrateclient.common.eventemitter;

@FunctionalInterface
public interface EventListener {
    void onEvent(Object... args);

    default boolean isOnce() {
        return false;
    }
}
