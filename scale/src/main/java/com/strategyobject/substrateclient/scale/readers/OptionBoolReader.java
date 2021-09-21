package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class OptionBoolReader implements ScaleReader<Optional<Boolean>> {
    @Override
    public Optional<Boolean> read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        switch (Streamer.readByte(stream)) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(true);
            default:
                return Optional.of(false);
        }
    }
}
