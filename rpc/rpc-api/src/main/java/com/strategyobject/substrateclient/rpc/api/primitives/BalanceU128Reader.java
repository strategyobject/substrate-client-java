package com.strategyobject.substrateclient.rpc.api.primitives;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

@AutoRegister(types = Balance.class)
@RequiredArgsConstructor
public class BalanceU128Reader implements ScaleReader<Balance> {
    private final @NonNull ScaleReaderRegistry registry;

    @SuppressWarnings("unchecked")
    @Override
    public Balance read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val u128Reader = (ScaleReader<BigInteger>) registry.resolve(ScaleType.U128.class);
        return Balance.of(u128Reader.read(stream));
    }
}
