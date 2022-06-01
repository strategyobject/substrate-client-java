package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;

public class ArrayDecoder extends AbstractDecoder<Object[]> {
    @Override
    protected Object[] decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        val nestedDecoder = decoders[0].getDecoderOrThrow();

        return value.asList()
                .stream()
                .map(nestedDecoder::decode)
                .toArray(Object[]::new);
    }

    @Override
    protected void checkArguments(RpcObject value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 1);
        Preconditions.checkNotNull(decoders[0]);
    }
}
