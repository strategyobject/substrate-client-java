package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.annotation.RpcDecoder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RpcDecoder
@NoArgsConstructor
@Getter
@Setter
public class RuntimeVersion {
  private Long specVersion;
  private Long transactionVersion;
}
