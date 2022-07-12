package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.context.RpcEncoderContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AutoRegister(types = AccountId.class)
public class AccountIdEncoder implements RpcEncoder<AccountId> {
    private final @NonNull RpcEncoderContext rpcEncoderContext;

    @Override
    public Object encode(AccountId source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return SS58Codec.encode(
                source.getBytes(),
                rpcEncoderContext.getMetadataProvider().getSS58AddressFormat());
    }
}
