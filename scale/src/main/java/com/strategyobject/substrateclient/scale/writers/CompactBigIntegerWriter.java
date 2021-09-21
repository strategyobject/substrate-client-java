package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.CompactMode;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class CompactBigIntegerWriter implements ScaleWriter<BigInteger> {
    @Override
    public void write(@NonNull BigInteger value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        val mode = CompactMode.fromNumber(value);
        if (mode != CompactMode.BIG_INTEGER) {
            CompactIntegerWriter.writeValue(value.intValueExact(), stream, mode);
            return;
        }

        val bytes = value.toByteArray();
        val leadingZero = bytes[0] == 0;
        val len = leadingZero ? bytes.length - 1 : bytes.length;
        stream.write(((len - 4) << 2) + mode.getValue());
        Streamer.writeBytes(bytes, leadingZero ? 1 : 0, stream, true);
    }
}
