package com.strategyobject.substrateclient.scale.registries;

import com.strategyobject.substrateclient.common.reflection.ClassUtils;
import com.strategyobject.substrateclient.common.reflection.Scanner;
import com.strategyobject.substrateclient.common.types.Array;
import com.strategyobject.substrateclient.common.types.AutoRegistry;
import com.strategyobject.substrateclient.common.types.Result;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.common.types.union.*;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.readers.*;
import com.strategyobject.substrateclient.scale.readers.union.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ScaleReaderRegistry implements AutoRegistry {
    private final Map<Class<?>, ScaleReader<?>> readers;

    public ScaleReaderRegistry() {
        readers = new ConcurrentHashMap<>(128);

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
        register(new Union1Reader(), Union1.class, ScaleType.Union1.class);
        register(new Union2Reader(), Union2.class, ScaleType.Union2.class);
        register(new Union3Reader(), Union3.class, ScaleType.Union3.class);
        register(new Union4Reader(), Union4.class, ScaleType.Union4.class);
        register(new Union5Reader(), Union5.class, ScaleType.Union5.class);
        register(new Union6Reader(), Union6.class, ScaleType.Union6.class);
        register(new Union7Reader(), Union7.class, ScaleType.Union7.class);
        register(new Union8Reader(), Union8.class, ScaleType.Union8.class);
        register(new Union9Reader(), Union9.class, ScaleType.Union9.class);
        register(new Union10Reader(), Union10.class, ScaleType.Union10.class);
        register(new Union11Reader(), Union11.class, ScaleType.Union11.class);
        register(new Union12Reader(), Union12.class, ScaleType.Union12.class);
        register(new VecReader(), ScaleType.Vec.class, List.class);
        register(new ArrayReader(), Array.class);
        register(new BooleanArrayReader(), boolean[].class);
        register(new ByteArrayReader(), byte[].class);
        register(new ShortArrayReader(), short[].class);
        register(new IntArrayReader(), int[].class);
        register(new LongArrayReader(), long[].class);
        register(new VoidReader(), Void.class, void.class);
        register(new UnitReader(), Unit.class);
    }

    public void registerAnnotatedFrom(String... prefixes) {
        Scanner.forPrefixes(prefixes)
                .getSubTypesOf(ScaleReader.class).forEach(reader -> {
                    val autoRegister = reader.getAnnotation(AutoRegister.class);
                    if (autoRegister == null) {
                        return;
                    }

                    try {
                        val types = autoRegister.types();
                        log.info("Auto register reader {} for types: {}", reader, types);


                        ScaleReader<?> readerInstance;
                        if (ClassUtils.hasDefaultConstructor(reader)) {
                            readerInstance = reader.newInstance();
                        } else {
                            val ctor = reader.getConstructor(ScaleReaderRegistry.class);
                            readerInstance = ctor.newInstance(this);
                        }

                        register(readerInstance, types);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        log.error("Auto registration error", e);
                    }
                });
    }

    public <T> void register(@NonNull ScaleReader<T> scaleReader, @NonNull Class<?>... clazz) {
        for (val type : clazz) {
            readers.put(type, scaleReader);
        }
    }

    public ScaleReader<?> resolve(@NonNull Class<?> clazz) {
        return readers.get(clazz);
    }
}
