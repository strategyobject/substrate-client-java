package com.strategyobject.substrateclient.rpc.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BroadcastScaleFactory  implements JsonDeserializer<BroadcastScale> {
  @Override
  public BroadcastScale deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    return new BroadcastScale(json.getAsString());
  }
}
