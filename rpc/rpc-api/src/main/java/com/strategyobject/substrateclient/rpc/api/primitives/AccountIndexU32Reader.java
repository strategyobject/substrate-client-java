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

@AutoRegister(types = AccountIndex.class)
@RequiredArgsConstructor
public class AccountIndexU32Reader implements ScaleReader<AccountIndex> {
    private final @NonNull ScaleReaderRegistry registry;

    @SuppressWarnings("unchecked")
    @Override
    public AccountIndex read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val u32Reader = (ScaleReader<Long>) registry.resolve(ScaleType.U32.class);
        return AccountIndex.of(u32Reader.read(stream));
    }
}
