package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.common.streams.StreamUtils;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class OptionBoolReader implements ScaleReader<Optional<Boolean>> {
    @Override
    public Optional<Boolean> read(@NonNull InputStream stream) throws IOException {
        switch (StreamUtils.readByte(stream)) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(true);
            default:
                return Optional.of(false);
        }
    }
}
