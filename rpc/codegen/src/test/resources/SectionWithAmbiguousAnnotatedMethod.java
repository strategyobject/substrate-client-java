package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcSubscription;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "empty")
public interface SectionWithAmbiguousAnnotatedMethod {
    @RpcCall(method = "a")
    @RpcSubscription(type = "b", subscribeMethod = "subscribeB", unsubscribeMethod = "unsubscribeB")
    CompletableFuture<Boolean> doNothing();
}
