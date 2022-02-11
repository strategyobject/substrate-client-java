package com.strategyobject.substrateclient.transport.coder;

import com.strategyobject.substrateclient.transport.RpcObject;
import lombok.Getter;

@Getter
public class JsonRpcResponseSingle extends JsonRpcContract {
    private final JsonRpcResponseBaseError error;
    private final RpcObject result;

    private JsonRpcResponseSingle(int id,
                                  String jsonrpc,
                                  JsonRpcResponseBaseError error,
                                  RpcObject result) {
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
    }

    public RpcObject getResult() {
        this.validate();
        return this.result;
    }

    public static JsonRpcResponseSingle from(JsonRpcResponse response) {
        return new JsonRpcResponseSingle(
                response.id,
                response.jsonrpc,
                response.error,
                response.result);
    }
}
