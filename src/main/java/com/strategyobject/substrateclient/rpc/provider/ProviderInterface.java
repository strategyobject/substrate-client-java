package com.strategyobject.substrateclient.rpc.provider;

import com.strategyobject.substrateclient.eventemitter.EventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public interface ProviderInterface {
    boolean hasSubscriptions();

    boolean isConnected();

    CompletableFuture<Void> connect();

    void disconnect();

    Runnable on(ProviderInterfaceEmitted type, EventListener sub);

    <T> CompletableFuture<T> send(String method,
                                  List<Object> params);

    <T> CompletableFuture<T> send(String method);

    <T> CompletableFuture<String> subscribe(String type,
                                            String method,
                                            List<Object> params,
                                            BiConsumer<Exception, T> callback);

    CompletableFuture<Boolean> unsubscribe(String type, String method, String id);
}
