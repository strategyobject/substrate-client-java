package com.strategyobject.substrateclient.rpc.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.Unit;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;

public class UnitEncoder implements RpcEncoder<Unit> {

    @Override
    public Object encode(Unit source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return null;
    }
}
