package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

public class ListDecoder extends AbstractDecoder<List<?>> {
    @Override
    protected List<?> decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        val nestedDecoder = decoders[0].getDecoderOrThrow();

        return value.asList()
                .stream()
                .map(nestedDecoder::decode)
                .collect(Collectors.toList());
    }

    @Override
    protected void checkArguments(RpcObject value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 1);
        Preconditions.checkNotNull(decoders[0]);
    }
}
