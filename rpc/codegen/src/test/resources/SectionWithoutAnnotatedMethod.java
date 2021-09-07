package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "empty")
public interface SectionWithoutAnnotatedMethod {
    CompletableFuture<Boolean> doNothing();
}
