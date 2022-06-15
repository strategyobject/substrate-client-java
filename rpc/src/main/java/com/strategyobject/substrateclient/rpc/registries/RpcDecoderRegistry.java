package com.strategyobject.substrateclient.rpc.registries;

import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.common.types.Array;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.decoders.*;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RpcDecoderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RpcDecoderRegistry.class);
    private static final String ROOT_PREFIX = "com.strategyobject.substrateclient";
    private static final Map<Class<?>, RpcDecoder<?>> decoders;

    private RpcDecoderRegistry() {
    }

    static {
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

        registerAnnotatedFrom(ROOT_PREFIX);
    }

    public static void registerAnnotatedFrom(String... prefixes) {
        Scanner.forPrefixes(prefixes)
                .getSubTypesOf(RpcDecoder.class).forEach(decoder -> {
                    val autoRegister = decoder.getAnnotation(AutoRegister.class);
                    if (autoRegister == null) {
                        return;
                    }

                    try {
                        val types = autoRegister.types();
                        logger.info("Auto register decoder {} for types: {}", decoder, types);

                        final RpcDecoder<?> instance = decoder.newInstance();
                        register(instance, types);
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error("Auto registration error", e);
                    }
                });
    }

    public static <T> void register(@NonNull RpcDecoder<T> decoder, @NonNull Class<?>... clazz) {
        for (val type : clazz) {
            decoders.put(type, decoder);
        }
    }

    public static RpcDecoder<?> resolve(@NonNull Class<?> clazz) {
        return decoders.get(clazz);
    }
}
