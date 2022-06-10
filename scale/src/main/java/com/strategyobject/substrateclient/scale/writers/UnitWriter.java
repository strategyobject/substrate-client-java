package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.OutputStream;

public class UnitWriter implements ScaleWriter<Unit> {
    @Override
    public void write(@NonNull Unit value, @NonNull OutputStream stream, ScaleWriter<?>... writers) {
        Preconditions.checkArgument(writers == null || writers.length == 0);
    }
}

