package com.strategyobject.substrateclient.rpc.codegen.sections;

import java.util.HashMap;
import java.util.Map;

class Constants {
    static final String CLASS_NAME_TEMPLATE = "%sImpl";
    static final String RPC_METHOD_NAME_TEMPLATE = "%s_%s";
    static final Map<String, Integer> EMPTY_TYPE_VAR_MAP = new HashMap<>();
    static final String PROVIDER_INTERFACE = "providerInterface";
    static final String SEND = "send";
    static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    static final String THEN_APPLY = "thenApply";
    static final String ACCEPT = "accept";
}
