package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.annotation.RpcCall;
import com.strategyobject.substrateclient.rpc.annotation.RpcInterface;

@RpcInterface("empty")
public interface SectionWithIncorrectReturnOfMethod {
    @RpcCall("doNothing")
    boolean doNothing(boolean a);
}
