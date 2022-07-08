package com.strategyobject.substrateclient.rpc.context;

import com.strategyobject.substrateclient.rpc.metadata.MetadataProvider;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RpcEncoderContext {
    private final MetadataProvider metadataProvider;
    private final RpcEncoderRegistry rpcEncoderRegistry;
    private final ScaleWriterRegistry scaleWriterRegistry;
}
