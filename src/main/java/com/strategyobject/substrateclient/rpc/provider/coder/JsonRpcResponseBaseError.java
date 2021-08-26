package com.strategyobject.substrateclient.rpc.provider.coder;

import com.strategyobject.substrateclient.rpc.provider.ws.RpcException;

class JsonRpcResponseBaseError {
    protected int code;
    protected String data;
    protected String message;

    void throwRpcException() {
        throw new RpcException(this.code, this.message, this.data);
    }
}
