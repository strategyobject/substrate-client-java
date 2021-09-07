package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcSubscription;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface(section = "empty")
public interface TestSection {
    @RpcCall(method = "doNothing")
    CompletableFuture<Boolean> doNothing(boolean a);

    @RpcSubscription(type = "nothing", subscribeMethod = "subscribeNothing", unsubscribeMethod = "unsubscribeNothing")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNothing(boolean a, BiConsumer<Exception, String> b);
}
