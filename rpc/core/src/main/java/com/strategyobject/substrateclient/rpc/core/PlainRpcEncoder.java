package com.strategyobject.substrateclient.rpc.core;

public class PlainRpcEncoder<T> implements RpcEncoder<T> {
    @Override
    public Object encode(T obj) {
        return obj;
    }
}
