package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;

@RpcInterface(section = "empty")
public interface SectionWithIncorrectReturnOfMethod {
    @RpcCall(method = "doNothing")
    boolean doNothing(boolean a);
}
