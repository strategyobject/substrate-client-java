package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.Bytes;
import lombok.NonNull;

public interface Hash extends Bytes {
    byte @NonNull [] getBytes();
}
