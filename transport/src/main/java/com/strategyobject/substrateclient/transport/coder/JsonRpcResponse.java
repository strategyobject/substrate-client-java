package com.strategyobject.substrateclient.transport.coder;

import lombok.Getter;

@Getter
public class JsonRpcResponse extends JsonRpcObject {
    static class SubscriptionParam {
        JsonRpcResponseBaseError error;
        Object result;
        String subscription;
    }

    // JsonRpcResponseSingle
    JsonRpcResponseBaseError error;
    Object result;

    // JsonRpcResponseSubscription
    String method;
    SubscriptionParam params;
}
