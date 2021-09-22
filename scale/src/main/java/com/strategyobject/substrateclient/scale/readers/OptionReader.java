package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RequiredArgsConstructor
public class OptionReader<T> implements ScaleReader<Optional<T>> {
    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> read(@NonNull InputStream stream, @NonNull ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers.length == 1);
        Preconditions.checkNotNull(readers[0]);

        val nestedReader = (ScaleReader<T>)readers[0];
        return StreamUtils.readByte(stream) == 0
                ? Optional.empty()
                : Optional.of(nestedReader.read(stream));

    }
}
