package com.strategyobject.substrateclient.rpc.api.runtime;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.readers.union.BaseUnionReader;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = DispatchError.class)
public class DispatchErrorReader extends BaseUnionReader<DispatchError> {
    private final ScaleReaderRegistry registry;

    public DispatchErrorReader(ScaleReaderRegistry registry) {
        super(8,
                x -> DispatchError.ofOther(),
                x -> DispatchError.ofCannotLookup(),
                x -> DispatchError.ofBadOrigin(),
                x -> DispatchError.ofModule((ModuleError) x),
                x -> DispatchError.ofConsumerRemaining(),
                x -> DispatchError.ofNoProviders(),
                x -> DispatchError.ofToken((TokenError) x),
                x -> DispatchError.ofArithmetic((ArithmeticError) x));

        this.registry = registry;
    }

    @Override
    public DispatchError read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val voidReader = registry.resolve(Void.class);
        val moduleErrorReader = registry.resolve(ModuleError.class);
        val tokenErrorReader = registry.resolve(TokenError.class);
        val arithmeticErrorReader = registry.resolve(ArithmeticError.class);
        return super.read(stream,
                voidReader,
                voidReader,
                voidReader,
                moduleErrorReader,
                voidReader,
                voidReader,
                tokenErrorReader,
                arithmeticErrorReader);
    }
}
