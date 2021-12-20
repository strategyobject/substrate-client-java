package com.strategyobject.substrateclient.rpc.types;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import com.strategyobject.substrateclient.rpc.core.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.types.tuples.Pair;
import lombok.val;

import java.util.List;

@AutoRegister(types = Pair.class)
public class PairDecoder extends AbstractDecoder<Pair<?, ?>> {
    @Override
    protected Pair<?, ?> decodeNonNull(Object value, DecoderPair<?>[] decoders) {
        val firstDecoder = decoders[0].getDecoderOrThrow();
        val secondDecoder = decoders[1].getDecoderOrThrow();

        val tuple = (List<?>) value;

        return Pair.of(
                firstDecoder.decode(tuple.get(0)),
                secondDecoder.decode(tuple.get(1))
        );
    }

    @Override
    protected void checkArguments(Object value, DecoderPair<?>[] decoders) {
        Preconditions.checkArgument(decoders != null && decoders.length == 2);
        Preconditions.checkNotNull(decoders[0]);
        Preconditions.checkNotNull(decoders[1]);
    }
}
