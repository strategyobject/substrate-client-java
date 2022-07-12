package com.strategyobject.substrateclient.rpc.context;

import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RpcDecoderContext {
    private final MetadataProvider metadataProvider;
    private final RpcDecoderRegistry rpcDecoderRegistry;
    private final ScaleReaderRegistry scaleReaderRegistry;
}
