package com.strategyobject.substrateclient.rpc.core.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.EncoderPair;
import com.strategyobject.substrateclient.rpc.core.RpcEncoder;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

public class ListEncoder implements RpcEncoder<List<?>> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object encode(List<?> source, EncoderPair<?>... encoders) {
        Preconditions.checkArgument(encoders != null && encoders.length == 1);

        if (source == null) {
            return null;
        }

        Preconditions.checkNotNull(encoders[0]);
        val nestedEncoder = (RpcEncoder) encoders[0].getEncoderOrThrow();

        return source.stream().map(nestedEncoder::encode).collect(Collectors.toList());
    }
}
