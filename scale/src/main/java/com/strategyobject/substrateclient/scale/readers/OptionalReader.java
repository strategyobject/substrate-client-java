package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RequiredArgsConstructor
public class OptionalReader<T> implements ScaleReader<Optional<T>> {
    private final ScaleReader<T> nestedReader;

    @Override
    public Optional<T> read(@NonNull InputStream stream) throws IOException {
        val option = stream.read();
        if (option == 0) {
            return Optional.empty();
        }

        return Optional.of(nestedReader.read(stream));
    }
}
