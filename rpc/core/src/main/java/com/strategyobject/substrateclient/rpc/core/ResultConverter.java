package com.strategyobject.substrateclient.rpc.core;

public interface ResultConverter {
    <RpcResult, TOut> TOut convert(RpcResult result);
}
