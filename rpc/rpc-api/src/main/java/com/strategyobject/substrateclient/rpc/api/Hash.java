package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.common.types.Bytes;

public interface Hash extends Bytes {
    byte[] getData();
}
