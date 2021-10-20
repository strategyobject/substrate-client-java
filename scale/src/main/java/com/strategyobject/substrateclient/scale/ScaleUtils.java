package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.common.utils.HexConverter;
import lombok.NonNull;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ScaleUtils {
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
