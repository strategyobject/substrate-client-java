package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.common.types.Size;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
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
