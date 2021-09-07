package com.strategyobject.substrateclient.transport.coder;

import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public abstract class JsonRpcObject {
    private static final String JSONRPC = "2.0";

    protected int id;
    protected String jsonrpc = JSONRPC;

    void validate() {
        Preconditions.checkState(JSONRPC.equals(this.jsonrpc), "Invalid jsonrpc field in decoded object");
    }
}
