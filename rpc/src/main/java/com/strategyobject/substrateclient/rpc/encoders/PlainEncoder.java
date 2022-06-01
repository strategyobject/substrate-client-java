package com.strategyobject.substrateclient.rpc.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;

public class PlainEncoder<T> implements RpcEncoder<T> {
    @Override
    public Object encode(T source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders == null || encoders.length == 0);

        return source;
    }
}
