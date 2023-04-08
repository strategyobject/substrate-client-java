package com.strategyobject.substrateclient.api;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
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
        @Override
        protected void configure() {
            try {
                bind(RpcSectionFactory.class)
                        .toConstructor(
                                GeneratedRpcSectionFactory.class.getConstructor(
                                        ProviderInterface.class,
                                        RpcEncoderRegistry.class,
                                        ScaleWriterRegistry.class,
                                        RpcDecoderRegistry.class,
                                        ScaleReaderRegistry.class))
                        .asEagerSingleton();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class MetadataModule extends AbstractModule {
        @Provides
        @Singleton
        public MetadataProvider provideMetadata() {
            // TODO. Use provider based on real Metadata
            return new ManualMetadataProvider(
                    SS58AddressFormat.SUBSTRATE_ACCOUNT,
                    new PalletCollection(
                            new Pallet(0, "System"),
                            new Pallet(1, "ParachainSystem"),
                            new Pallet(2, "Timestamp"),
                            new Pallet(3, "ParachainInfo"),
                            new Pallet(4, "Sudo"),
                            new Pallet(5, "Preimage"),
                            new Pallet(6, "Democracy"),
                            new Pallet(8, "Scheduler"),
                            new Pallet(9, "Utility"),
                            new Pallet(10, "Balances"),
                            new Pallet(11, "TransactionPayment"),
                            new Pallet(12, "Council"),
                            new Pallet(13, "TechnicalCommittee"),
                            new Pallet(20, "Authorship"),
                            new Pallet(21, "CollatorSelection"),
                            new Pallet(22, "Session"),
                            new Pallet(23, "Aura"),
                            new Pallet(24, "AuraExt"),
                            new Pallet(30, "XcmpQueue"),
                            new Pallet(31, "PolkadotXcm"),
                            new Pallet(32, "DmpQueue"),
                            new Pallet(40, "Vesting"),
                            new Pallet(60, "Msa"),
                            new Pallet(61, "Messages"),
                            new Pallet(62, "Schemas"),
                            new Pallet(64, "Capacity"),
                            new Pallet(65, "FrequencyTxPayment"),
                            new Pallet(66, "Handles")
                    ));

        }
    }
}
