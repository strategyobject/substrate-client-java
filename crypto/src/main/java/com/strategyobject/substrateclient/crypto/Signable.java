package com.strategyobject.substrateclient.crypto;

import lombok.NonNull;

public interface Signable {
    byte @NonNull [] getBytes();
}
