package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "")
public interface NamelessSection {
    @RpcCall(method = "doNothing")
    CompletableFuture<Boolean> doNothing();
}
