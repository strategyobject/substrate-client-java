package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@RpcInterface("empty")
public interface SectionWithoutCallback {
    @RpcSubscription(type = "nothing", subscribeMethod = "doNothing", unsubscribeMethod = "undoNothing")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> doNothing(boolean a);
}
