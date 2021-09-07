package com.strategyobject.substrateclient.transport.coder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.val;

import java.lang.reflect.Type;
import java.util.function.Function;

@Getter
public class JsonRpcResponseSubscription<T> extends JsonRpcObject {
    @Getter
    public static class SubscriptionParam<T> {
        private final JsonRpcResponseBaseError error;
        private final Function<Type, T> result;
        private final String subscription;

        private SubscriptionParam(JsonRpcResponseBaseError error,
                                  Function<Type, T> result,
                                  String subscription) {
            this.error = error;
            this.result = result;
            this.subscription = subscription;
        }
    }

    private final String method;
    private final SubscriptionParam<T> params;

    private JsonRpcResponseSubscription(int id,
                                        String jsonrpc,
                                        String method,
                                        SubscriptionParam<T> params) {
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

    public T getResult(Type resultType) {
        this.validate();
        return this.params.result.apply(resultType);
    }

    public static JsonRpcResponseSubscription<?> from(JsonRpcResponse response) {
        return new JsonRpcResponseSubscription<>(
                response.id,
                response.jsonrpc,
                response.method,
                new SubscriptionParam<>(
                        response.params.error,
                        type -> RpcCoder.decodeObject(response.params.result.toString(), type),
                        response.params.subscription
                ));
    }
}
