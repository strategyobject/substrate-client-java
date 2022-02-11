package com.strategyobject.substrateclient.rpc.types;

import com.strategyobject.substrateclient.rpc.core.DecoderPair;
import com.strategyobject.substrateclient.rpc.core.annotations.AutoRegister;
import com.strategyobject.substrateclient.rpc.core.decoders.AbstractDecoder;
import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.val;

import java.math.BigInteger;

@AutoRegister(types = Number.class)
public class NumberDecoder extends AbstractDecoder<Number> {
    @Override
    protected Number decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
        val stringValue = value.asString();
        val number = new BigInteger(stringValue.substring(2), 16);

        return Number.of(number);
    }
}
