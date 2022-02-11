package com.strategyobject.substrateclient.rpc.core.decoders;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

public class ShortDecoder extends AbstractDecoder<Short> {
    @Override
    protected Short decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        return value.asNumber().shortValue();
    }
}
