package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.DecoderPair;
import com.strategyobject.substrateclient.rpc.RpcDecoder;
import com.strategyobject.substrateclient.rpc.RpcRegistryHelper;
import com.strategyobject.substrateclient.rpc.annotation.AutoRegister;
import com.strategyobject.substrateclient.rpc.context.RpcDecoderContext;
import com.strategyobject.substrateclient.rpc.registries.RpcDecoderRegistry;
import com.strategyobject.substrateclient.scale.ScaleReader;
import com.strategyobject.substrateclient.scale.ScaleRegistryHelper;
import com.strategyobject.substrateclient.scale.registries.ScaleReaderRegistry;
import com.strategyobject.substrateclient.transport.RpcList;
import com.strategyobject.substrateclient.transport.RpcObject;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@AutoRegister(
    types = {com.strategyobject.substrateclient.rpc.api.ExtrinsicStatus.Broadcast.class}
)
public class BroadcastDecoder implements RpcDecoder<ExtrinsicStatus.Broadcast> {

  @Override
  @SuppressWarnings({"unchecked"})
  public ExtrinsicStatus.Broadcast decode(RpcObject value, DecoderPair<?>... decoders) {
    if (value.isNull()) { return null; };
    if (decoders != null && decoders.length > 0) throw new IllegalArgumentException();
    Map<String, RpcObject> sourceMap = value.asMap();
    ExtrinsicStatus.Broadcast result = new ExtrinsicStatus.Broadcast();
    /*
    RpcList broadcastList = (RpcList) sourceMap.get("broadcast");
    List<String> values = new LinkedList<>();
    for(RpcObject element : broadcastList.asList()){
      values.add(element.asString());
    }


    result.setBroadcast(values);

     */
    return result;
  }
}
