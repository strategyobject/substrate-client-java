package com.strategyobject.substrateclient.scale.readers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.types.Unit;
import lombok.NonNull;

import java.io.InputStream;

public class UnitReader implements ScaleReader<Unit> {
    @Override
    public Unit read(@NonNull InputStream stream, ScaleReader<?>... readers) {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        return Unit.get();
    }
}
