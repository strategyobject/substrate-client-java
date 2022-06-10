package com.strategyobject.substrateclient.scale.registries;

import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.common.types.Array;
import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.common.types.union.*;
import com.strategyobject.substrateclient.scale.ScaleSelfWritable;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.writers.*;
import com.strategyobject.substrateclient.scale.writers.union.*;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ScaleWriterRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ScaleWriterRegistry.class);
    private static final String ROOT_PREFIX = "com.strategyobject.substrateclient";
    private static volatile ScaleWriterRegistry instance;
    private final Map<Class<?>, ScaleWriter<?>> writers;

    private ScaleWriterRegistry() {
        writers = new ConcurrentHashMap<>(128);

        register(new BoolWriter(), ScaleType.Bool.class, Boolean.class, boolean.class);
        register(new CompactBigIntegerWriter(), ScaleType.CompactBigInteger.class);
        register(new CompactIntegerWriter(), ScaleType.CompactInteger.class);
        register(new I8Writer(), ScaleType.I8.class, Byte.class, byte.class);
        register(new I16Writer(), ScaleType.I16.class, Short.class, short.class);
        register(new I32Writer(), ScaleType.I32.class, Integer.class, int.class);
        register(new I64Writer(), ScaleType.I64.class, Long.class, long.class);
        register(new I128Writer(), ScaleType.I128.class, BigInteger.class);
        register(new OptionBoolWriter(), ScaleType.OptionBool.class);
        register(new OptionWriter(), ScaleType.Option.class, Optional.class);
        register(new ResultWriter(), ScaleType.Result.class, Result.class);
        register(new StringWriter(), ScaleType.String.class, String.class);
        register(new U8Writer(), ScaleType.U8.class);
        register(new U16Writer(), ScaleType.U16.class);
        register(new U32Writer(), ScaleType.U32.class);
        register(new U64Writer(), ScaleType.U64.class);
        register(new U128Writer(), ScaleType.U128.class);
        register(new Union1Writer(), Union1.class, ScaleType.Union1.class);
        register(new Union2Writer(), Union2.class, ScaleType.Union2.class);
        register(new Union3Writer(), Union3.class, ScaleType.Union3.class);
        register(new Union4Writer(), Union4.class, ScaleType.Union4.class);
        register(new Union5Writer(), Union5.class, ScaleType.Union5.class);
        register(new Union6Writer(), Union6.class, ScaleType.Union6.class);
        register(new Union7Writer(), Union7.class, ScaleType.Union7.class);
        register(new Union8Writer(), Union8.class, ScaleType.Union8.class);
        register(new Union9Writer(), Union9.class, ScaleType.Union9.class);
        register(new Union10Writer(), Union10.class, ScaleType.Union10.class);
        register(new Union11Writer(), Union11.class, ScaleType.Union11.class);
        register(new Union12Writer(), Union12.class, ScaleType.Union12.class);
        register(new VecWriter(), ScaleType.Vec.class, List.class);
        register(new ArrayWriter(), Array.class);
        register(new BooleanArrayWriter(), boolean[].class);
        register(new ByteArrayWriter(), byte[].class);
        register(new ShortArrayWriter(), short[].class);
        register(new IntArrayWriter(), int[].class);
        register(new LongArrayWriter(), long[].class);
        register(new VoidWriter(), Void.class, void.class);
        register(new UnitWriter(), Unit.class);
        register(new SelfWriter(), ScaleSelfWritable.class);

        registerAnnotatedFrom(ROOT_PREFIX);
    }

    public static ScaleWriterRegistry getInstance() {
        if (instance == null) {
            synchronized (ScaleWriterRegistry.class) {
                if (instance == null) {
                    instance = new ScaleWriterRegistry();
                }
            }
        }
        return instance;
    }

    public void registerAnnotatedFrom(String... prefixes) {
        Scanner.forPrefixes(prefixes)
                .getSubTypesOf(ScaleWriter.class).forEach(writer -> {
                    val autoRegister = writer.getAnnotation(AutoRegister.class);
                    if (autoRegister == null) {
                        return;
                    }

                    try {
                        val types = autoRegister.types();
                        logger.info("Auto register writer {} for types: {}", writer, types);

                        final ScaleWriter<?> writerInstance = writer.newInstance();
                        register(writerInstance, types);
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error("Auto registration error", e);
                    }
                });
    }

    public <T> void register(@NonNull ScaleWriter<T> scaleWriter, @NonNull Class<?>... clazz) {
        for (val type : clazz) {
            writers.put(type, scaleWriter);
        }
    }

    public ScaleWriter<?> resolve(@NonNull Class<?> clazz) {
        return writers.get(clazz);
    }
}
