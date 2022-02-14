package com.strategyobject.substrateclient.transport.coder;

import lombok.Getter;

import java.util.List;

@Getter
public class JsonRpcRequest extends JsonRpcContract {
    protected final String method;
    protected final List<Object> params;

    public JsonRpcRequest(int id, String method, List<Object> params) {
        this.id = id;
        this.method = method;
        this.params = params;
    }
}
