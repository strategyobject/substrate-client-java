package com.strategyobject.substrateclient.rpc.provider.coder;

import com.google.gson.JsonElement;
import lombok.Getter;

@Getter
public class JsonRpcResponse extends JsonRpcObject {
    static class SubscriptionParam {
        JsonRpcResponseBaseError error;
        JsonElement result;
        String subscription;
    }

    // JsonRpcResponseSingle
    JsonRpcResponseBaseError error;
    JsonElement result;

    // JsonRpcResponseSubscription
    String method;
    SubscriptionParam params;
}
