package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.annotation.RpcSubscription;
import com.strategyobject.substrateclient.rpc.codegen.substitutes.TestDispatch;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@RpcInterface("empty")
public interface TestSection {
    @RpcCall("doNothing")
    CompletableFuture<Boolean> doNothing(
            @ScaleGeneric(template = "TestDispatch", types = @Scale(TestDispatch.class))
            TestDispatch<?, ?, ?> a);

    @RpcSubscription(type = "nothing", subscribeMethod = "subscribeNothing", unsubscribeMethod = "unsubscribeNothing")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> subscribeNothing(boolean a, BiConsumer<Exception, String> b);
}
