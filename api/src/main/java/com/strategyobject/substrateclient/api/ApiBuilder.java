package com.strategyobject.substrateclient.api;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class ApiBuilder<M extends Module> {
    private final @NonNull M module;

    public ApiBuilder<M> configure(@NonNull Consumer<M> configuration) {
        configuration.accept(module);
        return this;
    }

    public <N extends Module> ApiBuilder<N> reconfigure(@NonNull Function<M, N> configuration) {
        return new ApiBuilder<>(configuration.apply(this.module));
    }

    public CompletableFuture<Api> build() {
        val injector = Guice.createInjector(new RequireModule(), module);
        val provider = injector.getInstance(ProviderInterface.class);
        val result = provider.isConnected() ?
                CompletableFuture.<Void>completedFuture(null) :
                provider.connect();

        return result.thenApply(ignored -> injector.getInstance(Api.class));
    }
}
