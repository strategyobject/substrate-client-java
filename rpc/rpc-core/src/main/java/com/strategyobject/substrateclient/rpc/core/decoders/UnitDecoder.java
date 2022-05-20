package com.strategyobject.substrateclient.rpc.core.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;
import com.strategyobject.substrateclient.types.Unit;

public class UnitDecoder implements RpcDecoder<Unit> {
    @Override
    public Unit decode(RpcObject value, DecoderPair<?>... decoders) {
        Preconditions.checkArgument(decoders == null || decoders.length == 0);

        return Unit.get();
    }
}
