package com.strategyobject.substrateclient.rpc.codegen.substitutes;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "empty")
public interface TestSection {
    @RpcCall(method = "doNothing")
    CompletableFuture<Boolean> doNothing(String a);
}
