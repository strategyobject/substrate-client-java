package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.rpc.CachingRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.GeneratedRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.RpcSectionFactory;
import com.strategyobject.substrateclient.rpc.metadata.ManualMetadataProvider;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.metadata.Pallet;
import com.strategyobject.substrateclient.rpc.metadata.PalletCollection;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;

public class RpcModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new MetadataModule());
        install(new RpcSectionModule());
    }

    public static class RpcSectionModule extends AbstractModule {
        @Provides
        @Singleton
        public RpcSectionFactory provideRpcSectionFactory(ProviderInterface providerInterface,
                                                      RpcEncoderRegistry rpcEncoderRegistry,
                                                      ScaleWriterRegistry scaleWriterRegistry,
                                                      RpcDecoderRegistry rpcDecoderRegistry,
                                                      ScaleReaderRegistry scaleReaderRegistry) {
            return new CachingRpcSectionFactory(new GeneratedRpcSectionFactory(providerInterface,
                    rpcEncoderRegistry,
                    scaleWriterRegistry,
                    rpcDecoderRegistry,
                    scaleReaderRegistry));
        }
    }

    public static class MetadataModule extends AbstractModule {
        @Provides
        @Singleton
        public MetadataProvider provideMetadata() {
            // TODO. Use provider based on real Metadata
            return new ManualMetadataProvider(SS58AddressFormat.SUBSTRATE_ACCOUNT,
                    new PalletCollection(new Pallet(0, "System"),
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
    }
}
