package com.strategyobject.substrateclient.rpc;

import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.rpc.registries.RpcEncoderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import com.strategyobject.substrateclient.transport.ProviderInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class GeneratedRpcSectionFactory implements RpcSectionFactory, AutoCloseable {
    private static final String CLASS_NAME_TEMPLATE = "%sImpl";

    private final ProviderInterface provider;
    private final RpcEncoderRegistry rpcEncoderRegistry;
    private final ScaleWriterRegistry scaleWriterRegistry;
    private final RpcDecoderRegistry rpcDecoderRegistry;
    private final ScaleReaderRegistry scaleReaderRegistry;

    @Override
    public <T> T create(@NonNull Class<T> interfaceClass) {
        if (interfaceClass.getDeclaredAnnotationsByType(RpcInterface.class).length == 0) {
            throw new IllegalArgumentException(
                    String.format("`%s` can't be constructed because isn't annotated with `@%s`.",
                            interfaceClass.getSimpleName(),
                            RpcInterface.class.getSimpleName()));
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(String.format(CLASS_NAME_TEMPLATE, interfaceClass.getCanonicalName()));
            val ctor = clazz.getConstructor(
                    ProviderInterface.class,
                    RpcEncoderRegistry.class,
                    ScaleWriterRegistry.class,
                    RpcDecoderRegistry.class,
                    ScaleReaderRegistry.class);

            return interfaceClass.cast(
                    ctor.newInstance(provider,
                            rpcEncoderRegistry,
                            scaleWriterRegistry,
                            rpcDecoderRegistry,
                            scaleReaderRegistry));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (provider instanceof AutoCloseable) {
            ((AutoCloseable) provider).close();
        }
    }
}
