package com.strategyobject.substrateclient.rpc.core.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

public class ListDecoder extends AbstractDecoder<List<?>> {
    @Override
    protected List<?> decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        val nestedDecoder = decoders[0].getDecoderOrThrow();

        return ((List<?>) value).stream().map(nestedDecoder::decode).collect(Collectors.toList());
    }

    @Override
    protected void checkArguments(Object value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 1);
        Preconditions.checkNotNull(decoders[0]);
    }
}
