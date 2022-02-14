package com.strategyobject.substrateclient.transport.coder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strategyobject.substrateclient.transport.RpcObject;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcCoder {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(RpcObject.class, new RpcObjectDeserializer())
            .create();

    private final AtomicInteger id = new AtomicInteger(0);

    public JsonRpcRequest encodeObject(String method, List<Object> params) {
        return new JsonRpcRequest(this.id.incrementAndGet(), method, params);
    }

    public static String encodeJson(JsonRpcRequest jsonRpcRequest) {
        return GSON.toJson(jsonRpcRequest);
    }

    public static JsonRpcResponse decodeJson(String json) {
        return GSON.fromJson(json, JsonRpcResponse.class);
    }
}
