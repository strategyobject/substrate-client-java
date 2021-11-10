package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;
import com.strategyobject.substrateclient.rpc.types.Metadata;
import com.strategyobject.substrateclient.rpc.types.RuntimeVersion;
import com.strategyobject.substrateclient.scale.annotations.Scale;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "state")
public interface State {
    @RpcCall(method = "getRuntimeVersion")
    CompletableFuture<RuntimeVersion> getRuntimeVersion();

    @RpcCall(method = "getMetadata")
    @Scale
    CompletableFuture<Metadata> getMetadata();
}
