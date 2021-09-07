package com.strategyobject.substrateclient.rpc.codegen.substitutes;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "empty")
public interface TestSection {
    @RpcCall(method = "doNothing")
    CompletableFuture<Boolean> doNothing(String a);
}
