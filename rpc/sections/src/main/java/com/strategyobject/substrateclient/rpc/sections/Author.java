package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;
import com.strategyobject.substrateclient.types.PublicKey;

import java.util.concurrent.CompletableFuture;

@RpcInterface(section = "author")
public interface Author {
    @RpcCall(method = "hasKey")
    CompletableFuture<Boolean> hasKey(PublicKey publicKey, String keyType);
}
