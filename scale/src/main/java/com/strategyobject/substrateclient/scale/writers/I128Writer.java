package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class I128Writer implements ScaleWriter<BigInteger> {
    @Override
    public void write(BigInteger value, OutputStream stream) throws IOException {
        val bytes = value.toByteArray();
        Preconditions.checkArgument(bytes.length <= 16,
                "Value exceeded 128 bits.");

        StreamUtils.writeBytes(bytes, stream, true);
        StreamUtils.writeBytes(new byte[16 - bytes.length], stream);
    }
}
