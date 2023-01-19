package com.strategyobject.substrateclient.rpc.api.weights;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@ScaleReader
public class Weight {
  @Scale(ScaleType.CompactBigInteger.class)
  private BigInteger refTime;

  @Scale(ScaleType.CompactBigInteger.class)
  private BigInteger proofSize;
}
