package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.pallet.events.EventRegistry;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;

public class RequireModule extends AbstractModule {
    @Override
    protected void configure() {
        requireBinding(ProviderInterface.class);
        requireBinding(ScaleReaderRegistry.class);
        requireBinding(ScaleWriterRegistry.class);
        requireBinding(RpcDecoderRegistry.class);
        requireBinding(RpcEncoderRegistry.class);
        requireBinding(EventRegistry.class);
        requireBinding(MetadataProvider.class);
        requireBinding(RpcSectionFactory.class);
        requireBinding(PalletFactory.class);
        requireBinding(Api.class);
    }
}
