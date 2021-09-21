package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import lombok.NonNull;

import java.io.InputStream;

public class VoidReader implements ScaleReader<Void> {
    @Override
    public Void read(@NonNull InputStream stream, ScaleReader<?>... readers) {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        return null;
    }
}
