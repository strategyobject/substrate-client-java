package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;

public class UnitDecoder implements RpcDecoder<Unit> {
    @Override
    public Unit decode(RpcObject value, DecoderPair<?>... decoders) {
        Preconditions.checkArgument(decoders == null || decoders.length == 0);

        return Unit.get();
    }
}
