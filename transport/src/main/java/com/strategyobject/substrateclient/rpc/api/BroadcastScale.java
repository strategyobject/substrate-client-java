package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.annotation.Scale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
@AllArgsConstructor
public class BroadcastScale {
  @Scale
  private String value;
}
