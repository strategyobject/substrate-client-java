package com.strategyobject.substrateclient.transport.coder;

import com.strategyobject.substrateclient.transport.ws.RpcException;

class JsonRpcResponseBaseError {
    protected int code;
    protected String data;
    protected String message;

    void throwRpcException() {
        throw new RpcException(this.code, this.message, this.data);
    }
}
