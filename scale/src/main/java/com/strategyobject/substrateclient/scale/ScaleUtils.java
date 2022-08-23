package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.common.inject.Injection;
import com.strategyobject.substrateclient.common.inject.Injector;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ScaleUtils {

    public static <T> T fromBytes(byte @NonNull [] bytes, @NonNull ScaleReader<T> reader) {
        val stream = new ByteArrayInputStream(bytes);
        try {
            return reader.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> byte[] toBytes(T value, @NonNull ScaleWriter<T> writer) {
        val stream = new ByteArrayOutputStream();
        try {
            writer.write(value, stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stream.toByteArray();
    }

    @SuppressWarnings({"unchecked"})
    public static <T> byte[] toBytes(T value, @NonNull ScaleWriterRegistry registry, @NonNull Object clazz) {
        return new Injector<>(
                clazz instanceof Injection ?
                        (Injection<Class<?>>) clazz :
                        Injection.of((Class<?>) clazz),
                ScaleWriter.class,
                registry::resolve,
                x -> toBytes(value, (ScaleWriter<T>) x))
                .process();
    }

    public static <T> T fromHexString(@NonNull String hex, @NonNull ScaleReader<T> reader) {
        return fromBytes(HexConverter.toBytes(hex), reader);
    }

    public static <T> String toHexString(T value, @NonNull ScaleWriter<T> writer) {
        return HexConverter.toHex(toBytes(value, writer));
    }

    private ScaleUtils() {
    }
}
