package com.strategyobject.substrateclient.transport.coder;

import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.Getter;

@Getter
public class JsonRpcResponse extends JsonRpcContract {
    static class SubscriptionParam {
        JsonRpcResponseBaseError error;
        RpcObject result;
        String subscription;
    }

    // JsonRpcResponseSingle
    JsonRpcResponseBaseError error;
    RpcObject result;

    // JsonRpcResponseSubscription
    String method;
    SubscriptionParam params;
}
