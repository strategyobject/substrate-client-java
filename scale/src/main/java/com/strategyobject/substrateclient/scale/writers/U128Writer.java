package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class U128Writer implements ScaleWriter<BigInteger> {
    @Override
    public void write(@NonNull BigInteger value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);
        Preconditions.checkArgument(
                value.compareTo(BigInteger.valueOf(0)) >= 0 && value.compareTo(BigInteger.ZERO.setBit(128)) < 0,
                "Only values in range 0..2^128-1 are supported");

        val bytes = value.toByteArray();
        //leading zero byte
        if (bytes.length > 16) {
            Streamer.writeBytes(bytes, 1, stream, true);
        } else {
            Streamer.writeBytes(bytes, stream, true);
            Streamer.writeBytes(new byte[16 - bytes.length], stream, true);
        }
    }
}
