package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;

public class ApiModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            bind(Api.class)
                    .toConstructor(
                            Api.class.getConstructor(
                                    RpcSectionFactory.class,
                                    PalletFactory.class,
                                    MetadataProvider.class))
                    .asEagerSingleton();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
