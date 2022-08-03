package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.strategyobject.substrateclient.api.pallet.balances.AccountData;
import com.strategyobject.substrateclient.api.pallet.balances.AccountDataReader;
import com.strategyobject.substrateclient.api.pallet.system.System;
import com.strategyobject.substrateclient.common.types.Lambda;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.pallet.GeneratedPalletFactory;
import com.strategyobject.substrateclient.pallet.PalletFactory;
import com.strategyobject.substrateclient.pallet.events.EventDescriptor;
import com.strategyobject.substrateclient.pallet.events.EventDescriptorReader;
import com.strategyobject.substrateclient.pallet.events.EventRegistry;
import com.strategyobject.substrateclient.rpc.GeneratedRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.api.section.State;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContext;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContextFactory;
import com.strategyobject.substrateclient.rpc.context.RpcEncoderContext;
import com.strategyobject.substrateclient.rpc.context.RpcEncoderContextFactory;
import com.strategyobject.substrateclient.rpc.metadata.ManualMetadataProvider;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.metadata.Pallet;
import com.strategyobject.substrateclient.rpc.metadata.PalletCollection;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DefaultModule extends AbstractModule {
    private static final String PREFIX = "com.strategyobject.substrateclient";

    private final @NonNull ProviderInterface providerInterface;

    private Consumer<ScaleReaderRegistry> configureScaleReaderRegistry = Lambda::noop;
    private Consumer<ScaleWriterRegistry> configureScaleWriterRegistry = Lambda::noop;
    private BiConsumer<RpcDecoderRegistry, RpcDecoderContextFactory> configureRpcDecoderRegistry = Lambda::noop;
    private BiConsumer<RpcEncoderRegistry, RpcEncoderContextFactory> configureRpcEncoderRegistry = Lambda::noop;
    private Consumer<EventRegistry> configureEventRegistry = Lambda::noop;


    public DefaultModule configureScaleReaderRegistry(Consumer<ScaleReaderRegistry> configure) {
        configureScaleReaderRegistry = configureScaleReaderRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureScaleWriterRegistry(Consumer<ScaleWriterRegistry> configure) {
        configureScaleWriterRegistry = configureScaleWriterRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureRpcDecoderRegistry(BiConsumer<RpcDecoderRegistry, RpcDecoderContextFactory> configure) {
        configureRpcDecoderRegistry = configureRpcDecoderRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureRpcEncoderRegistry(BiConsumer<RpcEncoderRegistry, RpcEncoderContextFactory> configure) {
        configureRpcEncoderRegistry = configureRpcEncoderRegistry.andThen(configure);
        return this;
    }

    public DefaultModule configureEventRegistry(Consumer<EventRegistry> configure) {
        configureEventRegistry = configureEventRegistry.andThen(configure);
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
                                    PalletFactory.class,
                                    MetadataProvider.class))
                    .asEagerSingleton();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
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

    @Provides
    @Singleton
    public MetadataProvider provideMetadata() {
        // TODO. Use provider based on real Metadata
        return new ManualMetadataProvider(
                SS58AddressFormat.SUBSTRATE_ACCOUNT,
                new PalletCollection(
                        new Pallet(0, "System"),
                        new Pallet(1, "Utility"),
                        new Pallet(2, "Babe"),
                        new Pallet(3, "Timestamp"),
                        new Pallet(4, "Authorship"),
                        new Pallet(5, "Indices"),
                        new Pallet(6, "Balances"),
                        new Pallet(7, "TransactionPayment"),
                        new Pallet(8, "Staking"),
                        new Pallet(9, "Session"),
                        new Pallet(10, "Democracy"),
                        new Pallet(11, "Council"),
                        new Pallet(12, "TechnicalCommittee"),
                        new Pallet(13, "Elections"),
                        new Pallet(14, "TechnicalMembership"),
                        new Pallet(15, "Grandpa"),
                        new Pallet(16, "Treasury"),
                        new Pallet(17, "Contracts"),
                        new Pallet(18, "Sudo"),
                        new Pallet(19, "ImOnline"),
                        new Pallet(20, "AuthorityDiscovery"),
                        new Pallet(21, "Offences"),
                        new Pallet(22, "Historical"),
                        new Pallet(23, "RandomnessCollectiveFlip"),
                        new Pallet(24, "Identity"),
                        new Pallet(25, "Society"),
                        new Pallet(26, "Recovery"),
                        new Pallet(27, "Vesting"),
                        new Pallet(28, "Scheduler"),
                        new Pallet(29, "Proxy"),
                        new Pallet(30, "Multisig"),
                        new Pallet(31, "Bounties"),
                        new Pallet(32, "Tips"),
                        new Pallet(33, "Assets"),
                        new Pallet(34, "Mmr"),
                        new Pallet(35, "Lottery")));
    }

    @Provides
    @Singleton
    public State provideState(RpcSectionFactory rpcSectionFactory) {
        return rpcSectionFactory.create(State.class);
    }
}
