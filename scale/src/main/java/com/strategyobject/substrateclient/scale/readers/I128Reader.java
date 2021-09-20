package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class I128Reader implements ScaleReader<BigInteger> {
    @Override
    public BigInteger read(@NonNull InputStream stream) throws IOException {
        val bytes = StreamUtils.readBytes(16, stream, true);

        return new BigInteger(bytes);
    }
}
