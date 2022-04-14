package com.strategyobject.substrateclient.rpc.sections;

import com.strategyobject.substrateclient.rpc.core.annotations.RpcCall;
import com.strategyobject.substrateclient.rpc.core.annotations.RpcInterface;

@RpcInterface("empty")
public interface SectionWithIncorrectReturnOfMethod {
    @RpcCall("doNothing")
    boolean doNothing(boolean a);
}
