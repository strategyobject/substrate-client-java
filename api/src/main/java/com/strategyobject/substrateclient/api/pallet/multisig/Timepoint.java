package com.strategyobject.substrateclient.api.pallet.multisig;

import com.strategyobject.substrateclient.rpc.api.primitives.BlockNumber;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleReader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ScaleReader
public class Timepoint {
  private BlockNumber blockNumber;
  @Scale(ScaleType.U32.class)
  private Long index;
}
