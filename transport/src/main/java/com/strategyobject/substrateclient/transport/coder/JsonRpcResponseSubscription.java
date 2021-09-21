package com.strategyobject.substrateclient.transport.coder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.val;

@Getter
public class JsonRpcResponseSubscription extends JsonRpcObject {
    @Getter
    public static class SubscriptionParam {
        private final JsonRpcResponseBaseError error;
        private final Object result;
        private final String subscription;

        private SubscriptionParam(JsonRpcResponseBaseError error,
                                  Object result,
                                  String subscription) {
            this.error = error;
            this.result = result;
            this.subscription = subscription;
        }
    }

    private final String method;
    private final SubscriptionParam params;

    private JsonRpcResponseSubscription(int id,
                                        String jsonrpc,
                                        String method,
                                        SubscriptionParam params) {
        this.id = id;
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.params = params;
    }

    @Override
    void validate() {
        super.validate();
        Preconditions.checkState(
                !Strings.isNullOrEmpty(this.params.getSubscription()),
                "Invalid id field in decoded object");

        val error = this.params.getError();
        if (error != null) {
            error.throwRpcException();
        }
    }

    public Object getResult() {
        this.validate();
        return this.params.result;
    }

    public static JsonRpcResponseSubscription from(JsonRpcResponse response) {
        return new JsonRpcResponseSubscription(
                response.id,
                response.jsonrpc,
                response.method,
                new SubscriptionParam(
                        response.params.error,
                        response.params.result,
                        response.params.subscription
                ));
    }
}
