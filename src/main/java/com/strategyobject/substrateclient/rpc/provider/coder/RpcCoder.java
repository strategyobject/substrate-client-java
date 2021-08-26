package com.strategyobject.substrateclient.rpc.provider.coder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcCoder {
    private static final Gson GSON = new Gson();

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

    public static <T> T decodeObject(String json, Type objectType) {
        return GSON.fromJson(json, objectType);
    }

    public static <T> Type getResultType() {
        return new TypeToken<T>() {
        }.getType();
    }
}
