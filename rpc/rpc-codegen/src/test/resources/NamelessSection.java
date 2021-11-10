package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "")
public interface NamelessSection {
    @RpcCall(method = "doNothing")
    CompletableFuture<Boolean> doNothing();
}
