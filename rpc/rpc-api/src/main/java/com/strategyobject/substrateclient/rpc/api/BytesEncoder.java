package com.strategyobject.substrateclient.rpc.api;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.Bytes;
import com.strategyobject.substrateclient.common.utils.HexConverter;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.api.impl.Hash256;

@AutoRegister(
        types = {
                Bytes.class,
                Hash256.class,
                Hash.class,
                BlockHash.class
        })
public class BytesEncoder implements RpcEncoder<Bytes> {
    @Override
    public Object encode(Bytes source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return HexConverter.toHex(source.getData());
    }
}
