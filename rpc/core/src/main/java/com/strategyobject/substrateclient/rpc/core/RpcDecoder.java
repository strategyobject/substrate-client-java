package com.strategyobject.substrateclient.rpc.core;

public interface RpcDecoder<T> {
    T decode(Object rpcObj);
}
