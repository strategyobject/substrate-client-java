package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.codegen.annotations.RpcInterface;

@RpcInterface(section = "empty")
public interface SectionWithIncorrectReturnOfMethod {
    @RpcCall(method = "doNothing")
    boolean doNothing(boolean a);
}
