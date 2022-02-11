package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class IntDecoder extends AbstractDecoder<Integer> {
    @Override
    protected Integer decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().intValue();
    }
}
