package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class U128Reader implements ScaleReader<BigInteger> {
    @Override
    public BigInteger read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);
        
        val bytes = Streamer.readBytes(16, stream, true);

        return new BigInteger(1, bytes);
    }
}
