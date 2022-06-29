package com.strategyobject.substrateclient.pallet;

import com.strategyobject.substrateclient.rpc.GeneratedRpcSectionFactory;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;

public class TestsHelper {
    public static final ScaleReaderRegistry SCALE_READER_REGISTRY = new ScaleReaderRegistry() {{
        registerAnnotatedFrom("com.strategyobject.substrateclient");
    }};

    public static final ScaleWriterRegistry SCALE_WRITER_REGISTRY = new ScaleWriterRegistry() {{
        registerAnnotatedFrom("com.strategyobject.substrateclient");
    }};

    public static final RpcEncoderRegistry RPC_ENCODER_REGISTRY = new RpcEncoderRegistry(SCALE_WRITER_REGISTRY) {{
        registerAnnotatedFrom("com.strategyobject.substrateclient");
    }};

    public static final RpcDecoderRegistry RPC_DECODER_REGISTRY = new RpcDecoderRegistry(SCALE_READER_REGISTRY) {{
        registerAnnotatedFrom("com.strategyobject.substrateclient");
    }};

    public static GeneratedRpcSectionFactory createSectionFactory(ProviderInterface provider) {
        return new GeneratedRpcSectionFactory(
                provider,
                RPC_ENCODER_REGISTRY,
                SCALE_WRITER_REGISTRY,
                RPC_DECODER_REGISTRY,
                SCALE_READER_REGISTRY);
    }
}

