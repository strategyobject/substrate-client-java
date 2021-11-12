package com.strategyobject.substrateclient.rpc.core.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import lombok.val;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class MapDecoder extends AbstractDecoder<Map<?, ?>> {
    @Override
    @SuppressWarnings("unchecked")
    protected Map<?, ?> decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        val keyDecoder = decoders[0].getDecoderOrThrow();
        val valueDecoder = decoders[1].getDecoderOrThrow();

        return ((Map<String, ?>) value)
                .entrySet()
                .stream()
                .collect(toMap(e -> keyDecoder.decode(e.getKey()),
                        e -> valueDecoder.decode(e.getValue())));
    }

    @Override
    protected void checkArguments(Object value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 2);
        Preconditions.checkNotNull(decoders[0]);
        Preconditions.checkNotNull(decoders[1]);
    }
}
