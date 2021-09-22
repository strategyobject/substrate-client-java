package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class I128Writer implements ScaleWriter<BigInteger> {
    @Override
    public void write(@NonNull BigInteger value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        val bytes = value.toByteArray();
        Preconditions.checkArgument(bytes.length <= 16,
                "Value exceeded 128 bits.");

        StreamUtils.writeBytes(bytes, stream, true);
        StreamUtils.writeBytes(new byte[16 - bytes.length], stream);
    }
}
