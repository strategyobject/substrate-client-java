package com.strategyobject.substrateclient.scale.readers;

import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class BoolReader implements ScaleReader<Boolean> {
    @Override
    public Boolean read(@NonNull InputStream stream) throws IOException {
        if (stream.read() == 0) {
            return false;
        }

        return true;
    }
}
