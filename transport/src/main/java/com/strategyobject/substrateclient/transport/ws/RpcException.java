package com.strategyobject.substrateclient.transport.ws;

import lombok.Getter;

@Getter
public class RpcException extends RuntimeException {
    private final int code;
    private final String message;
    private final String data;

    public RpcException(int code, String message, String data) {
        super("RPC error " + code + ": " + message + (data == null ? "" : " (" + data + ")"));
        this.code = code;
        this.message = message;
        this.data = data;
    }
}