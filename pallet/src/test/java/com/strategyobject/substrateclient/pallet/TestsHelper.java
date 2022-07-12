package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.crypto.ss58.SS58AddressFormat;
import com.strategyobject.substrateclient.rpc.GeneratedRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContext;
import com.strategyobject.substrateclient.rpc.context.RpcEncoderContext;
import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestsHelper {
    public static final MetadataProvider METADATA_PROVIDER = mock(MetadataProvider.class);

    static {
        when(METADATA_PROVIDER.getSS58AddressFormat()).thenReturn(SS58AddressFormat.SUBSTRATE_ACCOUNT);
    }

    public static final ScaleReaderRegistry SCALE_READER_REGISTRY = new ScaleReaderRegistry() {{
        registerAnnotatedFrom("com.strategyobject.substrateclient");
    }};

    public static final ScaleWriterRegistry SCALE_WRITER_REGISTRY = new ScaleWriterRegistry() {{
        registerAnnotatedFrom("com.strategyobject.substrateclient");
    }};

    public static final RpcEncoderRegistry RPC_ENCODER_REGISTRY = new RpcEncoderRegistry();

    static {
        RPC_ENCODER_REGISTRY.registerAnnotatedFrom(
                () -> new RpcEncoderContext(METADATA_PROVIDER, RPC_ENCODER_REGISTRY, SCALE_WRITER_REGISTRY),
                "com.strategyobject.substrateclient");
    }

    public static final RpcDecoderRegistry RPC_DECODER_REGISTRY = new RpcDecoderRegistry();

    static {
        RPC_DECODER_REGISTRY.registerAnnotatedFrom(
                () -> new RpcDecoderContext(METADATA_PROVIDER, RPC_DECODER_REGISTRY, SCALE_READER_REGISTRY),
                "com.strategyobject.substrateclient");
    }

    public static GeneratedRpcSectionFactory createSectionFactory(ProviderInterface provider) {
        return new GeneratedRpcSectionFactory(
                provider,
                RPC_ENCODER_REGISTRY,
                SCALE_WRITER_REGISTRY,
                RPC_DECODER_REGISTRY,
                SCALE_READER_REGISTRY);
    }
}

