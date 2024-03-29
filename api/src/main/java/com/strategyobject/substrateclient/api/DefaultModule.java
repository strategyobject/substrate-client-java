package com.strategyobject.substrateclient.api;

import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultModule extends RegistriesModule<DefaultModule> {

    private final @NonNull ProviderInterface providerInterface;

    @Override
    protected void configure() {
        install(new TransportModule(providerInterface));
        install(new RpcModule());
        install(new PalletModule());
        install(new ApiModule());
    }
}
