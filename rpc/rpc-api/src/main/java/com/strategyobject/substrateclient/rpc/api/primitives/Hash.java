package com.strategyobject.substrateclient.rpc.api.primitives;

import com.strategyobject.substrateclient.common.types.Bytes;
import lombok.NonNull;

/**
 * A hash of some data used by the chain.
 */
public interface Hash extends Bytes {
    byte @NonNull [] getBytes();
}
