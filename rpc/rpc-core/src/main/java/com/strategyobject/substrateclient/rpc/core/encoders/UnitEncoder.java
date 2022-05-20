package com.strategyobject.substrateclient.rpc.core.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import com.strategyobject.substrateclient.types.Unit;

public class UnitEncoder implements RpcEncoder<Unit> {

    @Override
    public Object encode(Unit source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return null;
    }
}
