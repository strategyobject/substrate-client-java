package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.scale.ScaleDispatch;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

@AutoRegister(types = com.strategyobject.substrateclient.rpc.api.SignedExtra.class)
@RequiredArgsConstructor
public class SignedExtraWriter<E extends Era> implements ScaleWriter<SignedExtra<E>> {
    private final @NonNull ScaleWriterRegistry registry;

    @Override
    @SuppressWarnings({"unchecked"})
    public void write(@NonNull SignedExtra<E> value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        val dispatcher = registry.resolve(ScaleDispatch.class);
        ((ScaleWriter<E>) dispatcher).write(value.getEra(), stream);
        ((ScaleWriter<Index>) registry.resolve(Index.class)).write(value.getNonce(), stream);
        ((ScaleWriter<BigInteger>) registry.resolve(ScaleType.CompactBigInteger.class)).write(value.getTip(), stream);
    }
}
