package com.strategyobject.substrateclient.rpc.core.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;

public class PlainEncoder<T> implements RpcEncoder<T> {
    @Override
    public Object encode(T source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return source;
    }
}
