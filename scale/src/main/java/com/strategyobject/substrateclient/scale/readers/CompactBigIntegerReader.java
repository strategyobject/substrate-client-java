package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.CompactMode;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class CompactBigIntegerReader implements ScaleReader<BigInteger> {
    @Override
    public BigInteger read(@NonNull InputStream stream) throws IOException {
        val head = StreamUtils.readByte(stream);
        val mode = CompactMode.fromValue((byte) (head & 0b11));
        if (mode != CompactMode.BIG_INTEGER) {
            return BigInteger.valueOf(
                    CompactIntegerReader.parseCompactInteger(mode, head, stream));
        }

        val len = (head >> 2) + 4;
        val value = StreamUtils.readBytes(len, stream, true);
        return new BigInteger(1, value);
    }
}
