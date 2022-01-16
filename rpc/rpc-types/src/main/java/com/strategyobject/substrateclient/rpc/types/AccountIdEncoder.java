package com.strategyobject.substrateclient.rpc.types;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.crypto.ss58.SS58Codec;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.MetadataRegistry;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;

@AutoRegister(types = AccountId.class)
public class AccountIdEncoder implements RpcEncoder<AccountId> {
    @Override
    public Object encode(AccountId source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return SS58Codec.encode(source.getData(), MetadataRegistry.getInstance().getSS58AddressFormat());
    }
}
