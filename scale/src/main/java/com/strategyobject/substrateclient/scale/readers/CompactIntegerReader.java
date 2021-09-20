package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.CompactMode;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

public class CompactIntegerReader implements ScaleReader<Integer> {
    @Override
    public Integer read(@NonNull InputStream stream) throws IOException {
        return readInternal(stream);
    }

    static int readInternal(InputStream stream) throws IOException {
        val head = StreamUtils.readByte(stream);
        val mode = CompactMode.fromValue((byte) (head & 0b11));
        return parseCompactInteger(mode, head, stream);
    }

    static int parseCompactInteger(CompactMode mode,
                                   int head,
                                   InputStream stream) throws IOException {
        switch (mode) {
            case SINGLE:
                return head >> 2;
            case TWO:
                return (head >> 2) + (StreamUtils.readByte(stream) << 6);
            case FOUR:
                val bytes = StreamUtils.readBytes(3, stream);
                return (head >> 2) +
                        (Byte.toUnsignedInt(bytes[0]) << 6) +
                        (Byte.toUnsignedInt(bytes[1]) << (6 + 8)) +
                        (Byte.toUnsignedInt(bytes[2]) << (6 + 2 * 8));
            default:
                throw new UnsupportedOperationException("The number is in Big-integer mode.");
        }
    }
}
