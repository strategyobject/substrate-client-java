package com.strategyobject.substrateclient.storage;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represent the argument of multi query to storage.
 * It contains the list of keys which are required for the specific storage.
 */
public class Arg {
    /**
     * List of keys.
     */
    @Getter
    private final Object[] list;

    private Arg(Object[] list) {
        this.list = list;
    }

    /**
     * Creates a new Arg.
     * @param keys any number of keys.
     * @return a new Arg with given keys.
     */
    public static Arg of(@NonNull Object... keys) {
        return new Arg(keys);
    }
}
