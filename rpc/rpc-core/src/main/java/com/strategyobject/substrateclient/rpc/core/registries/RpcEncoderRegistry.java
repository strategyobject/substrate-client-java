package com.strategyobject.substrateclient.rpc.core.registries;

import com.strategyobject.substrateclient.common.CommonType;
import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import com.strategyobject.substrateclient.rpc.core.encoders.ArrayEncoder;
import com.strategyobject.substrateclient.rpc.core.encoders.ListEncoder;
import com.strategyobject.substrateclient.rpc.core.encoders.MapEncoder;
import com.strategyobject.substrateclient.rpc.core.encoders.PlainEncoder;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RpcEncoderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RpcEncoderRegistry.class);
    private static final String ROOT_PREFIX = "com.strategyobject.substrateclient";
    private static volatile RpcEncoderRegistry instance;
    private final Map<Class<?>, RpcEncoder<?>> encoders;

    private RpcEncoderRegistry() {
        encoders = new ConcurrentHashMap<>(128);

        register(new PlainEncoder<>(),
                Void.class, String.class, Boolean.class, boolean.class, Byte.class, byte.class, Double.class, double.class,
                Float.class, float.class, Integer.class, int.class, Long.class, long.class, Short.class, short.class);
        register(new ListEncoder(), List.class);
        register(new MapEncoder(), Map.class);
        register(new ArrayEncoder(), CommonType.Array.class);

        registerAnnotatedFrom(ROOT_PREFIX);
    }

    public static RpcEncoderRegistry getInstance() {
        if (instance == null) {
            synchronized (RpcEncoderRegistry.class) {
                if (instance == null) {
                    instance = new RpcEncoderRegistry();
                }
            }
        }

        return instance;
    }

    public void registerAnnotatedFrom(String... prefixes) {
        Scanner.forPrefixes(prefixes)
                .getSubTypesOf(RpcEncoder.class).forEach(encoder -> {
                    val autoRegister = encoder.getAnnotation(AutoRegister.class);
                    if (autoRegister == null) {
                        return;
                    }

                    try {
                        val types = autoRegister.types();
                        logger.info("Auto register encoder {} for types: {}", encoder, types);

                        final RpcEncoder<?> instance = encoder.newInstance();
                        register(instance, types);
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error("Auto registration error", e);
                    }
                });
    }

    public <T> void register(@NonNull RpcEncoder<T> encoder, @NonNull Class<?>... clazz) {
        for (val type : clazz) {
            encoders.put(type, encoder);
        }
    }

    public RpcEncoder<?> resolve(@NonNull Class<?> clazz) {
        return encoders.get(clazz);
    }
}
