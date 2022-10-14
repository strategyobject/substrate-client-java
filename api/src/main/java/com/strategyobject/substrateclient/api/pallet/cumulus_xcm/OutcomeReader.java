package com.strategyobject.substrateclient.api.pallet.cumulus_xcm;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.rpc.api.runtime.ArithmeticError;
import com.strategyobject.substrateclient.rpc.api.runtime.DispatchError;
import com.strategyobject.substrateclient.rpc.api.runtime.ModuleError;
import com.strategyobject.substrateclient.rpc.api.runtime.TokenError;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.readers.union.BaseUnionReader;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

@AutoRegister(types = Outcome.class)
public class OutcomeReader implements ScaleReader<Outcome> {

    private final ScaleReaderRegistry registry;

    public OutcomeReader(ScaleReaderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Outcome read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val index = Streamer.readByte(stream);
        val completeReader  = registry.resolve(Outcome.Complete.class);
        val incompleteReader = registry.resolve(Outcome.Incomplete.class);
        val errorReader = registry.resolve(Outcome.Error.class);

        switch(index) {
            case 0:
                return (Outcome) completeReader.read(stream);
            case 1:
                return (Outcome) incompleteReader.read(stream);
            case 2:
                return (Outcome) errorReader.read(stream);
        }

        throw new NoSuchElementException(String.format("Outcome index %d is out of bounds.", index));
    }
}

