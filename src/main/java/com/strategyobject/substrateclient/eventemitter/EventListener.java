package com.strategyobject.substrateclient.eventemitter;

@FunctionalInterface
public interface EventListener {
    void onEvent(Object... args);

    default boolean isOnce() {
        return false;
    }
}
