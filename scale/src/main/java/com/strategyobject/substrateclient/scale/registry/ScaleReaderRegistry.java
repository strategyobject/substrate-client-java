package com.strategyobject.substrateclient.scale.registry;

import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotations.AutoRegister;
import com.strategyobject.substrateclient.scale.readers.*;
import com.strategyobject.substrateclient.types.Result;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ScaleReaderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ScaleReaderRegistry.class);
    private static volatile ScaleReaderRegistry instance;

    public static ScaleReaderRegistry getInstance() {
        if (instance == null) {
            synchronized (ScaleReaderRegistry.class) {
                if (instance == null) {
                    instance = new ScaleReaderRegistry();
                }
            }
        }
        return instance;
    }

    private final Map<Class<?>, ScaleReader<?>> readers;

    private ScaleReaderRegistry() {
        readers = new ConcurrentHashMap<>(34);

        register(new BoolReader(), ScaleType.Bool.class, Boolean.class, boolean.class);
        register(new CompactBigIntegerReader(), ScaleType.CompactBigInteger.class);
        register(new CompactIntegerReader(), ScaleType.CompactInteger.class);
        register(new I8Reader(), ScaleType.I8.class, Byte.class, byte.class);
        register(new I16Reader(), ScaleType.I16.class, Short.class, short.class);
        register(new I32Reader(), ScaleType.I32.class, Integer.class, int.class);
        register(new I64Reader(), ScaleType.I64.class, Long.class, long.class);
        register(new I128Reader(), ScaleType.I128.class, BigInteger.class);
        register(new OptionBoolReader(), ScaleType.OptionBool.class);
        register(new OptionReader(), ScaleType.Option.class, Optional.class);
        register(new ResultReader(), ScaleType.Result.class, Result.class);
        register(new StringReader(), ScaleType.String.class, String.class);
        register(new U8Reader(), ScaleType.U8.class);
        register(new U16Reader(), ScaleType.U16.class);
        register(new U32Reader(), ScaleType.U32.class);
        register(new U64Reader(), ScaleType.U64.class);
        register(new U128Reader(), ScaleType.U128.class);
        register(new VecReader(), ScaleType.Vec.class, List.class);
        register(new VoidReader(), Void.class);

        autoRegistration();
    }

    private void autoRegistration() {
        Scanner.getSubTypesOf(ScaleReader.class).forEach(reader -> {
            val autoRegister = reader.getAnnotation(AutoRegister.class);
            if (autoRegister == null) {
                return;
            }

            try {
                val types = autoRegister.types();
                logger.info("Auto register reader {} for types: {}", reader, types);

                final ScaleReader<?> readerInstance = reader.newInstance();
                register(readerInstance, types);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Auto registration error", e);
            }
        });
    }

    public <T> void register(@NonNull ScaleReader<T> scaleReader, @NonNull Class<?>... clazz) {
        for (val type : clazz) {
            readers.put(type, scaleReader);
        }
    }

    public ScaleReader<?> resolve(@NonNull Class<?> clazz) throws ScaleReaderNotFoundException {
        val result = readers.get(clazz);
        if (result == null) {
            throw new ScaleReaderNotFoundException(clazz);
        }

        return result;
    }
}
