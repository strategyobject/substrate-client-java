package com.strategyobject.substrateclient.rpc.registries;

import com.strategyobject.substrateclient.common.reflection.ClassUtils;
import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.common.types.Array;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContext;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContextFactory;
import com.strategyobject.substrateclient.rpc.decoders.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RpcDecoderRegistry {
    private final Map<Class<?>, RpcDecoder<?>> decoders;

    public RpcDecoderRegistry() {
        decoders = new ConcurrentHashMap<>(128);
        register(new ListDecoder(), List.class);
        register(new MapDecoder(), Map.class);
        register(new VoidDecoder(), Void.class, void.class);
        register(new UnitDecoder(), Unit.class);
        register(new BooleanDecoder(), Boolean.class, boolean.class);
        register(new ByteDecoder(), Byte.class, byte.class);
        register(new DoubleDecoder(), Double.class, double.class);
        register(new FloatDecoder(), Float.class, float.class);
        register(new IntDecoder(), Integer.class, int.class);
        register(new LongDecoder(), Long.class, long.class);
        register(new ShortDecoder(), Short.class, short.class);
        register(new StringDecoder(), String.class);
        register(new ArrayDecoder(), Array.class);
    }

    public void registerAnnotatedFrom(RpcDecoderContextFactory rpcDecoderContextFactory, String... prefixes) {
        Scanner.forPrefixes(prefixes)
                .getSubTypesOf(RpcDecoder.class).forEach(decoder -> {
                    val autoRegister = decoder.getAnnotation(AutoRegister.class);
                    if (autoRegister == null) {
                        return;
                    }

                    try {
                        val types = autoRegister.types();
                        log.info("Auto register decoder {} for types: {}", decoder, types);

                        RpcDecoder<?> rpcDecoder;
                        if (ClassUtils.hasDefaultConstructor(decoder)) {
                            rpcDecoder = decoder.newInstance();
                        } else {
                            val ctor = decoder.getConstructor(RpcDecoderContext.class);
                            rpcDecoder = ctor.newInstance(rpcDecoderContextFactory.create());
                        }

                        register(rpcDecoder, types);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        log.error("Auto registration error", e);
                    }
                });
    }

    public <T> void register(@NonNull RpcDecoder<T> decoder, @NonNull Class<?>... clazz) {
        for (val type : clazz) {
            decoders.put(type, decoder);
        }
    }

    public RpcDecoder<?> resolve(@NonNull Class<?> clazz) {
        return decoders.get(clazz);
    }
}
