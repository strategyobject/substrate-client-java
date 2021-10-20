package com.strategyobject.substrateclient.rpc.core.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import lombok.val;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class MapEncoder implements RpcEncoder<Map<?, ?>> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object encode(Map<?, ?> source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders != null && encoders.length == 2);
        if (source == null) {
            return null;
        }

        Preconditions.checkNotNull(encoders[0]);
        Preconditions.checkNotNull(encoders[1]);
        val keyEncoder = (RpcEncoder) encoders[0].getEncoderOrThrow();
        val valueEncoder = (RpcEncoder) encoders[1].getEncoderOrThrow();

        return source.entrySet()
                .stream()
                .collect(toMap(e -> keyEncoder.encode(e.getKey()),
                        e -> valueEncoder.encode(e.getValue())));
    }
}
