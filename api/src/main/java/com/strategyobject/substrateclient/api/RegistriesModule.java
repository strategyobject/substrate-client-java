package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.strategyobject.substrateclient.api.pallet.balances.AccountData;
import com.strategyobject.substrateclient.api.pallet.balances.AccountDataReader;
import com.strategyobject.substrateclient.api.pallet.system.System;
import com.strategyobject.substrateclient.common.types.Lambda;
import com.strategyobject.substrateclient.pallet.events.EventDescriptor;
import com.strategyobject.substrateclient.pallet.events.EventDescriptorReader;
import com.strategyobject.substrateclient.pallet.events.EventRegistry;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContext;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContextFactory;
import com.strategyobject.substrateclient.rpc.context.RpcEncoderContext;
import com.strategyobject.substrateclient.rpc.context.RpcEncoderContextFactory;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.val;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class RegistriesModule<T extends Module> extends AbstractModule {
    private static final String PREFIX = "com.strategyobject.substrateclient";

    protected Consumer<ScaleReaderRegistry> configureScaleReaderRegistry = Lambda::noop;
    protected Consumer<ScaleWriterRegistry> configureScaleWriterRegistry = Lambda::noop;
    protected BiConsumer<RpcDecoderRegistry, RpcDecoderContextFactory> configureRpcDecoderRegistry = Lambda::noop;
    protected BiConsumer<RpcEncoderRegistry, RpcEncoderContextFactory> configureRpcEncoderRegistry = Lambda::noop;
    protected Consumer<EventRegistry> configureEventRegistry = Lambda::noop;

    public T configureScaleReaderRegistry(Consumer<ScaleReaderRegistry> configure) {
        configureScaleReaderRegistry = configureScaleReaderRegistry.andThen(configure);
        return (T) this;
    }

    public T configureScaleWriterRegistry(Consumer<ScaleWriterRegistry> configure) {
        configureScaleWriterRegistry = configureScaleWriterRegistry.andThen(configure);
        return (T) this;
    }

    public T configureRpcDecoderRegistry(BiConsumer<RpcDecoderRegistry, RpcDecoderContextFactory> configure) {
        configureRpcDecoderRegistry = configureRpcDecoderRegistry.andThen(configure);
        return (T) this;
    }

    public T configureRpcEncoderRegistry(BiConsumer<RpcEncoderRegistry, RpcEncoderContextFactory> configure) {
        configureRpcEncoderRegistry = configureRpcEncoderRegistry.andThen(configure);
        return (T) this;
    }

    public T configureEventRegistry(Consumer<EventRegistry> configure) {
        configureEventRegistry = configureEventRegistry.andThen(configure);
        return (T) this;
    }

    @Provides
    @Singleton
    public ScaleReaderRegistry provideScaleReaderRegistry(MetadataProvider metadataProvider,
                                                          EventRegistry eventRegistry) {
        val registry = new ScaleReaderRegistry();
        registry.registerAnnotatedFrom(PREFIX);
        registry.register(new AccountDataReader(registry), System.AccountData.class, AccountData.class);
        registry.register(new EventDescriptorReader(registry, metadataProvider, eventRegistry), EventDescriptor.class);

        configureScaleReaderRegistry.accept(registry);
        return registry;
    }

    @Provides
    @Singleton
    public ScaleWriterRegistry provideScaleWriterRegistry() {
        val registry = new ScaleWriterRegistry();
        registry.registerAnnotatedFrom(PREFIX);

        configureScaleWriterRegistry.accept(registry);
        return registry;
    }

    @Provides
    @Singleton
    public RpcDecoderRegistry provideRpcDecoderRegistry(MetadataProvider metadataProvider,
                                                        ScaleReaderRegistry scaleReaderRegistry) {
        val registry = new RpcDecoderRegistry();
        val context = new RpcDecoderContext(
                metadataProvider,
                registry,
                scaleReaderRegistry);
        registry.registerAnnotatedFrom(() -> context, PREFIX);

        configureRpcDecoderRegistry.accept(registry, () -> context);
        return registry;
    }

    @Provides
    @Singleton
    public RpcEncoderRegistry provideRpcEncoderRegistry(MetadataProvider metadataProvider,
                                                        ScaleWriterRegistry scaleWriterRegistry) {
        val registry = new RpcEncoderRegistry();
        val context = new RpcEncoderContext(
                metadataProvider,
                registry,
                scaleWriterRegistry);
        registry.registerAnnotatedFrom(() -> context, PREFIX);

        configureRpcEncoderRegistry.accept(registry, () -> context);
        return registry;
    }

    @Provides
    @Singleton
    public EventRegistry provideEventRegistry() {
        val registry = new EventRegistry();
        registry.registerAnnotatedFrom(PREFIX);

        configureEventRegistry.accept(registry);
        return registry;
    }
}

