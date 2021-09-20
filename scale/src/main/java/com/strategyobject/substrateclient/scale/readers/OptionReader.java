package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RequiredArgsConstructor
public class OptionReader<T> implements ScaleReader<Optional<T>> {
    private final ScaleReader<T> nestedReader;

    @Override
    public Optional<T> read(@NonNull InputStream stream) throws IOException {
        return StreamUtils.readByte(stream) == 0
                ? Optional.empty()
                : Optional.of(nestedReader.read(stream));

    }
}
