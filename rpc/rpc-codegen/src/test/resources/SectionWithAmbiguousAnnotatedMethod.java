package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;

import java.util.concurrent.CompletableFuture;

@RpcInterface("empty")
public interface SectionWithAmbiguousAnnotatedMethod {
    @RpcCall("a")
    @RpcSubscription(type = "b", subscribeMethod = "subscribeB", unsubscribeMethod = "unsubscribeB")
    CompletableFuture<Boolean> doNothing();
}
