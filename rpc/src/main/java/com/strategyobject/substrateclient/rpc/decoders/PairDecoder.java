package com.strategyobject.substrateclient.rpc.decoders;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.types.tuple.Pair;
import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;

public class PairDecoder extends AbstractDecoder<Pair<?, ?>> {
    @Override
    protected Pair<?, ?> decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        val firstDecoder = decoders[0].getDecoderOrThrow();
        val secondDecoder = decoders[1].getDecoderOrThrow();

        val tuple = value.asList();

        return Pair.of(
                firstDecoder.decode(tuple.get(0)),
                secondDecoder.decode(tuple.get(1))
        );
    }

    @Override
    protected void checkArguments(RpcObject value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 2);
        Preconditions.checkNotNull(decoders[0]);
        Preconditions.checkNotNull(decoders[1]);
    }
}
