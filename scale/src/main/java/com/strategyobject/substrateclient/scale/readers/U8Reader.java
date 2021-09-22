package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class U8Reader implements ScaleReader<Integer> {
    @Override
    public Integer read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        return StreamUtils.readByte(stream);
    }
}
