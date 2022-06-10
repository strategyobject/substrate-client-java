package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface("")
public interface NamelessSection {
    @RpcCall("doNothing")
    CompletableFuture<Boolean> doNothing();
}
