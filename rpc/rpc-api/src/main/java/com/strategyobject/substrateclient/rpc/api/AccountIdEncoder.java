package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;

@AutoRegister(types = AccountId.class)
public class AccountIdEncoder implements RpcEncoder<AccountId> {
    @Override
    public Object encode(AccountId source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return source.toString();
    }
}
