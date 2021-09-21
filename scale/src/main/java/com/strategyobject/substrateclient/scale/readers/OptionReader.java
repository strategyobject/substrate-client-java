package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RequiredArgsConstructor
public class OptionReader implements ScaleReader<Optional<?>> {
    @Override
    public Optional<?> read(@NonNull InputStream stream, @NonNull ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == 1);
        Preconditions.checkNotNull(readers[0]);

        return Streamer.readByte(stream) == 0
                ? Optional.empty()
                : Optional.of(readers[0].read(stream));

    }
}
