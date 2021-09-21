package com.strategyobject.substrateclient.types;

import lombok.NonNull;

public interface Signable {
    byte @NonNull [] getBytes();
}
