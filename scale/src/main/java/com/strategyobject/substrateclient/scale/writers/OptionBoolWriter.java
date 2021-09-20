package com.strategyobject.substrateclient.scale.writers;

import com.strategyobject.substrateclient.scale.ScaleWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class OptionBoolWriter implements ScaleWriter<Optional<Boolean>> {
    @Override
    public void write(Optional<Boolean> value, OutputStream stream) throws IOException {
        if (!value.isPresent()) {
            stream.write(0);
        } else if (value.get()) {
            stream.write(1);
        } else {
            stream.write(2);
        }
    }
}
