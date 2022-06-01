package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

public class MapDecoder extends AbstractDecoder<Map<?, ?>> {
    @Override
    protected Map<?, ?> decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        val keyDecoder = decoders[0].getDecoderOrThrow();
        val valueDecoder = decoders[1].getDecoderOrThrow();

        return value.asMap()
                .entrySet()
                .stream()
                .collect(
                        HashMap::new,
                        (map, x) -> map.put(
                                keyDecoder.decode(RpcObject.of(x.getKey())),
                                valueDecoder.decode(x.getValue())),
                        HashMap::putAll);
    }

    @Override
    protected void checkArguments(RpcObject value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 2);
        Preconditions.checkNotNull(decoders[0]);
        Preconditions.checkNotNull(decoders[1]);
    }
}
