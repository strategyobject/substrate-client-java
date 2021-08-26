package com.strategyobject.substrateclient.rpc.provider.coder;

import lombok.Getter;

import java.util.List;

@Getter
public class JsonRpcRequest extends JsonRpcObject {
    protected String method;
    protected List<Object> params;

    public JsonRpcRequest(int id, String method, List<Object> params) {
        this.id = id;
        this.method = method;
        this.params = params;
    }
}
