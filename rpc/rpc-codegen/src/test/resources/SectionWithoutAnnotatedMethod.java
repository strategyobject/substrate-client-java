package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface("empty")
public interface SectionWithoutAnnotatedMethod {
    CompletableFuture<Boolean> doNothing();
}
