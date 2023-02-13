package com.strategyobject.substrateclient.rpc.api.runtime;

import com.strategyobject.substrateclient.scale.annotation.ScaleReader;

@ScaleReader
public enum TransactionalError {
  LIMIT_REACHED,
  NO_LAYER
}
