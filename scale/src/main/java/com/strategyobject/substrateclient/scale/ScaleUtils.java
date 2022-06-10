package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ScaleUtils {

    @SuppressWarnings("unchecked")
    public static <T> T fromBytes(byte @NonNull [] bytes, Class<T> clazz) {
        val stream = new ByteArrayInputStream(bytes);
        val reader = (ScaleReader<T>) ScaleReaderRegistry.getInstance().resolve(clazz);
        try {
            return reader.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> byte[] toBytes(T value, Class<T> clazz) {
        val stream = new ByteArrayOutputStream();
        val writer = (ScaleWriter<T>) ScaleWriterRegistry.getInstance().resolve(clazz);
        try {
            writer.write(value, stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stream.toByteArray();
    }

    public static <T extends ScaleSelfWritable<T>> byte[] toBytes(ScaleSelfWritable<T> value) {
        val stream = new ByteArrayOutputStream();
        try {
            value.write(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stream.toByteArray();
    }

    public static <T> String toHexString(@NonNull T value, @NonNull ScaleWriter<T> writer) {
        val stream = new ByteArrayOutputStream();

        try {
            writer.write(value, stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return HexConverter.toHex(stream.toByteArray());
    }

    public static <T> T fromHexString(@NonNull String hex, @NonNull ScaleReader<T> reader) {
        val stream = new ByteArrayInputStream(HexConverter.toBytes(hex));

        try {
            return reader.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ScaleUtils() {
    }
}
