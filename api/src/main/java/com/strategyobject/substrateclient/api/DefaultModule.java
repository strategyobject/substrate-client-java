package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.strategyobject.substrateclient.common.types.AutoRegistry;
import com.strategyobject.substrateclient.pallet.GeneratedPalletFactory;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.rpc.GeneratedRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class DefaultModule extends AbstractModule {
    private final @NonNull ProviderInterface providerInterface;

    private Consumer<ScaleReaderRegistry> configureScaleReaderRegistry = this::autoRegister;
    private Consumer<ScaleWriterRegistry> configureScaleWriterRegistry = this::autoRegister;
    private Consumer<RpcDecoderRegistry> configureRpcDecoderRegistry = this::autoRegister;
    private Consumer<RpcEncoderRegistry> configureRpcEncoderRegistry = this::autoRegister;

    private void autoRegister(AutoRegistry registry) {
        registry.registerAnnotatedFrom("com.strategyobject.substrateclient");
    }

    public DefaultModule configureScaleReaderRegistry(Consumer<ScaleReaderRegistry> configure) {
        configureScaleReaderRegistry = configureScaleReaderRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureScaleWriterRegistry(Consumer<ScaleWriterRegistry> configure) {
        configureScaleWriterRegistry = configureScaleWriterRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureRpcDecoderRegistry(Consumer<RpcDecoderRegistry> configure) {
        configureRpcDecoderRegistry = configureRpcDecoderRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureRpcEncoderRegistry(Consumer<RpcEncoderRegistry> configure) {
        configureRpcEncoderRegistry = configureRpcEncoderRegistry.andThen(configure);
        return this;
    }

    @Override
    protected void configure() {
        try {
            bind(ProviderInterface.class).toInstance(providerInterface);
            bind(RpcSectionFactory.class)
                    .toConstructor(
                            GeneratedRpcSectionFactory.class.getConstructor(
                                    ProviderInterface.class,
                                    RpcEncoderRegistry.class,
                                    ScaleWriterRegistry.class,
                                    RpcDecoderRegistry.class,
                                    ScaleReaderRegistry.class))
                    .asEagerSingleton();
            bind(PalletFactory.class)
                    .toConstructor(
                            GeneratedPalletFactory.class.getConstructor(
                                    ScaleReaderRegistry.class,
                                    ScaleWriterRegistry.class,
                                    State.class
                            ))
                    .asEagerSingleton();
            bind(Api.class)
                    .toConstructor(
                            Api.class.getConstructor(
                                    RpcSectionFactory.class,
                                    PalletFactory.class))
                    .asEagerSingleton();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public ScaleReaderRegistry provideScaleReaderRegistry() {
        val registry = new ScaleReaderRegistry();
        configureScaleReaderRegistry.accept(registry);
        return registry;
    }

    @Provides
    @Singleton
    public ScaleWriterRegistry provideScaleWriterRegistry() {
        val registry = new ScaleWriterRegistry();
        configureScaleWriterRegistry.accept(registry);
        return registry;
    }

    @Provides
    @Singleton
    public RpcDecoderRegistry provideRpcDecoderRegistry(ScaleReaderRegistry scaleReaderRegistry) {
        val registry = new RpcDecoderRegistry(scaleReaderRegistry);
        configureRpcDecoderRegistry.accept(registry);
        return registry;
    }

    @Provides
    @Singleton
    public RpcEncoderRegistry provideRpcEncoderRegistry(ScaleWriterRegistry scaleWriterRegistry) {
        val registry = new RpcEncoderRegistry(scaleWriterRegistry);
        configureRpcEncoderRegistry.accept(registry);
        return registry;
    }

    @Provides
    @Singleton
    public State provideState(RpcSectionFactory rpcSectionFactory) {
        return rpcSectionFactory.create(State.class);
    }
}
