package com.strategyobject.substrateclient.api.pallet.system;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.readers.union.BaseUnionReader;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = Phase.class)
public class PhaseReader extends BaseUnionReader<Phase> {
    private final ScaleReaderRegistry registry;

    public PhaseReader(ScaleReaderRegistry registry) {
        super(3,
                x -> Phase.ofApplyExtrinsic((Long) x),
                x -> Phase.ofFinalization(),
                x -> Phase.ofInitialization());

        this.registry = registry;
    }

    @Override
    public Phase read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val u32Reader = registry.resolve(ScaleType.U32.class);
        val voidReader = registry.resolve(Void.class);
        return super.read(stream, u32Reader, voidReader, voidReader);
    }
}
