package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.CompactMode;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class CompactBigIntegerWriter implements ScaleWriter<BigInteger> {
    @Override
    public void write(BigInteger value, OutputStream stream) throws IOException {
        val mode = CompactMode.fromNumber(value);
        if (mode != CompactMode.BIG_INTEGER) {
            CompactIntegerWriter.writeValue(value.intValueExact(), stream, mode);
            return;
        }

        val bytes = value.toByteArray();
        val leadingZero = bytes[0] == 0;
        val len = leadingZero ? bytes.length - 1 : bytes.length;
        stream.write(((len - 4) << 2) + mode.getValue());
        StreamUtils.writeBytes(bytes, leadingZero ? 1 : 0, stream, true);
    }
}
