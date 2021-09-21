package com.strategyobject.substrateclient.rpc.core;

public interface RpcEncoder<T> {
    Object encode(T obj);
}
