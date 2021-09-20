package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class U8Reader implements ScaleReader<Integer> {
    @Override
    public Integer read(@NonNull InputStream stream) throws IOException {
        return StreamUtils.readByte(stream);
    }
}
