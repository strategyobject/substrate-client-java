package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class OptionalBoolReader implements ScaleReader<Optional<Boolean>> {
    @Override
    public Optional<Boolean> read(@NonNull InputStream stream) throws IOException {
        val b = stream.read();

        if (b == 0) {
            return Optional.empty();
        }

        if (b == 1) {
            return Optional.of(true);
        }

        return Optional.of(false);
    }
}
