package com.strategyobject.substrateclient.api.pallet.democracy;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

@AutoRegister(types = AccountVote.class)
public class AccountVoteReader implements ScaleReader<AccountVote> {

    private final ScaleReaderRegistry registry;

    public AccountVoteReader(ScaleReaderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public AccountVote read(@NonNull InputStream stream, ScaleReader<?>... readers) throws IOException {
        Preconditions.checkArgument(readers == null || readers.length == 0);

        val index = Streamer.readByte(stream);
        val standardReader  = registry.resolve(AccountVote.Standard.class);
        val splitReader = registry.resolve(AccountVote.Split.class);

        switch(index) {
            case 0:
                return (AccountVote) standardReader.read(stream);
            case 1:
                return (AccountVote) splitReader.read(stream);
        }

        throw new NoSuchElementException(String.format("AccountVote index %d is out of bounds.", index));
    }
}
