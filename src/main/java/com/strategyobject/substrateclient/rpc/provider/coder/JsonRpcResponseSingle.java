package com.strategyobject.substrateclient.rpc.provider.coder;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.function.Function;

@Getter
public class JsonRpcResponseSingle<T> extends JsonRpcObject {
    private final JsonRpcResponseBaseError error;
    private final Function<Type, T> result;

    private JsonRpcResponseSingle(int id,
                                  String jsonrpc,
                                  JsonRpcResponseBaseError error,
                                  Function<Type, T> result) {
        this.id = id;
        this.jsonrpc = jsonrpc;
        this.error = error;
        this.result = result;
    }

    @Override
    void validate() {
        super.validate();
        if (error != null) {
            error.throwRpcException();
        }

        Preconditions.checkNotNull(this.result, "No result found in JsonRpc response");
    }

    public T getResult(Type resultType) {
        this.validate();
        return this.result.apply(resultType);
    }

    public static JsonRpcResponseSingle<?> from(JsonRpcResponse response) {
        return new JsonRpcResponseSingle<>(
                response.id,
                response.jsonrpc,
                response.error,
                type -> RpcCoder.decodeObject(response.result.toString(), type));
    }
}
