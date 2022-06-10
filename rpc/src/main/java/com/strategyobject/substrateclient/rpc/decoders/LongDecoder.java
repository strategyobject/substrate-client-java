package com.strategyobject.substrateclient.rpc.decoders;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class LongDecoder extends AbstractDecoder<Long> {
    @Override
    protected Long decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().longValue();
    }
}
