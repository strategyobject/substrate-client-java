package com.strategyobject.substrateclient.rpc.decoders;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.transport.RpcObject;

import java.math.BigInteger;

public class BigIntegerDecoder extends AbstractDecoder<BigInteger> {
  @Override
  protected BigInteger decodeNonNull(RpcObject value, DecoderPair<?>[] decoders) {
    final Number numberValue = value.asNumber();
    if(numberValue instanceof BigInteger){
      return (BigInteger) numberValue;
    }else {
      return BigInteger.valueOf(numberValue.longValue());
    }
  }
}
