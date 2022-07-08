package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.pallet.GeneratedPalletFactory;
import com.strategyobject.substrateclient.pallet.PalletFactory;
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

    private Consumer<ScaleReaderRegistry> configureScaleReaderRegistry = x -> x.registerAnnotatedFrom(PREFIX);
    private Consumer<ScaleWriterRegistry> configureScaleWriterRegistry = x -> x.registerAnnotatedFrom(PREFIX);
    private BiConsumer<RpcDecoderRegistry, RpcDecoderContextFactory> configureRpcDecoderRegistry =
            (registry, contextFactory) -> registry.registerAnnotatedFrom(contextFactory, PREFIX);
    private BiConsumer<RpcEncoderRegistry, RpcEncoderContextFactory> configureRpcEncoderRegistry =
            (registry, contextFactory) -> registry.registerAnnotatedFrom(contextFactory, PREFIX);

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
    public RpcDecoderRegistry provideRpcDecoderRegistry(MetadataProvider metadataProvider,
                                                        ScaleReaderRegistry scaleReaderRegistry) {
        val registry = new RpcDecoderRegistry();
        val context = new RpcDecoderContext(
                metadataProvider,
                registry,
                scaleReaderRegistry);

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

        configureRpcEncoderRegistry.accept(registry, () -> context);
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
