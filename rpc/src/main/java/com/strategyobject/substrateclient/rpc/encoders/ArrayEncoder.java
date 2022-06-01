package com.strategyobject.substrateclient.rpc.encoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.EncoderPair;
import com.strategyobject.substrateclient.rpc.RpcEncoder;
import lombok.val;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ArrayEncoder implements RpcEncoder<Object[]> {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object encode(Object[] source, EncoderPair<?>... encoders) {
        if (source == null) {
            return null;
        }

        Preconditions.checkArgument(encoders != null && encoders.length == 1);
        Preconditions.checkNotNull(encoders[0]);
        val nestedEncoder = (RpcEncoder) encoders[0].getEncoderOrThrow();

        return Arrays.stream(source).map(nestedEncoder::encode).collect(Collectors.toList());
    }
}
