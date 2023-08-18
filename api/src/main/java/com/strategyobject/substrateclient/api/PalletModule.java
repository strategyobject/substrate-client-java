package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.strategyobject.substrateclient.pallet.GeneratedPalletFactory;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;

public class PalletModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            bind(PalletFactory.class)
                    .toConstructor(
                            GeneratedPalletFactory.class.getConstructor(
                                    ScaleReaderRegistry.class,
                                    ScaleWriterRegistry.class,
                                    RpcSectionFactory.class
                            ))
                    .asEagerSingleton();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
