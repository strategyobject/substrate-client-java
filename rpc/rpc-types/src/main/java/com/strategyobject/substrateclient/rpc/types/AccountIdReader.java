package com.strategyobject.substrateclient.rpc.types;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotations.AutoRegister;
import com.strategyobject.substrateclient.types.Size;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;

@AutoRegister(types = AccountId.class)
public class AccountIdReader implements ScaleReader<AccountId> {
    @Override
    public AccountId read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        return AccountId.fromBytes(Streamer.readBytes(Size.of32.getValue(), stream));
    }
}
