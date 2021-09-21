package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "empty")
public interface SectionWithAmbiguousAnnotatedMethod {
    @RpcCall(method = "a")
    @RpcSubscription(type = "b", subscribeMethod = "subscribeB", unsubscribeMethod = "unsubscribeB")
    CompletableFuture<Boolean> doNothing();
}
