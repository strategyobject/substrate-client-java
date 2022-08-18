package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransportModule extends AbstractModule {
    private final @NonNull ProviderInterface providerInterface;

    @Override
    protected void configure() {
        bind(ProviderInterface.class).toInstance(providerInterface);
    }
}
