package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface("empty")
public class ClassSection {
    @RpcCall("doNothing")
    CompletableFuture<Boolean> doNothing();
}
