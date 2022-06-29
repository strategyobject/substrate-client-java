package com.strategyobject.substrateclient.common.types;

import lombok.NonNull;

@FunctionalInterface
public interface Bytes {
    byte @NonNull [] getBytes();
}
