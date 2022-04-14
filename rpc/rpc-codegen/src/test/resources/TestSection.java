package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcSubscription;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("empty")
public interface TestSection {
    @RpcCall("doNothing")
    CompletableFuture<Boolean> doNothing(boolean a);

    @RpcSubscription(type = "nothing", subscribeMethod = "subscribeNothing", unsubscribeMethod = "unsubscribeNothing")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNothing(boolean a, BiConsumer<Exception, String> b);
}
