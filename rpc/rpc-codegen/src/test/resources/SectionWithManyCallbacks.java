package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("empty")
public interface SectionWithManyCallbacks {
    @RpcSubscription(type = "nothing", subscribeMethod = "doNothing", unsubscribeMethod = "undoNothing")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> doNothing(
            boolean a,
            BiConsumer<Exception, Integer> b,
            BiConsumer<Exception, String> c);
}
